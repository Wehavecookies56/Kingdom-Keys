package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.prebuilt.PrebuiltWorldChunkGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Development tool. Slices a hand-built area into one file per chunk column, so a world can be authored in
 * creative and then rebuilt chunk by chunk by {@link PrebuiltWorldChunkGenerator}.
 *
 * /kingdomkeys export &lt;name&gt; &lt;from&gt; &lt;to&gt; [entities]
 *
 * The format is deliberately not the vanilla structure one. A structure template spends 36 bytes per block
 * on a "pos" list plus a "state" int, and repeats its entire block palette in every single file, which on a
 * world sized export runs to tens of megabytes. Here the palette lives once in the manifest and each piece
 * is a flat array of palette indices, which gzip squeezes hard because empty space becomes long runs of
 * zeroes.
 *
 * Layout, all NBT, written to &lt;game dir&gt;/kingdomkeys/exports/&lt;name&gt;/:
 *
 * manifest.nbt
 *   min_y              : int, the Y the anchor chunk was exported from
 *   anchor_x, anchor_z : int, chunk coords every piece name is measured from
 *   palette            : list of block states, index 0 always air
 *
 * Running the command again on the same name merges: the anchor and the existing palette are reused, so a
 * second export of a distant district lands at the right offset and the empty space between them costs
 * nothing, because it simply has no files.
 *
 * &lt;x&gt;_&lt;z&gt;.nbt
 *   min_y, height   : int, the piece's own vertical slice, trimmed to what it actually contains
 *   palette         : int array, local index -&gt; manifest index
 *   blocks          : byte array of local indices, ordered y then z then x
 *   blocks_int      : int array instead, only when a chunk somehow holds over 256 distinct states
 *   block_entities  : list of {x, y, z relative, nbt}
 *   entities        : list of {pos relative doubles, nbt}
 */
public class ExportWorldCommand extends BaseCommand {

	// Past this a byte per cell can't address the local palette any more and the piece falls back to ints
	private static final int MAX_BYTE_PALETTE = 256;

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("export").requires(source -> source.hasPermission(2));
		builder.then(Commands.argument("name", StringArgumentType.word())
				.then(Commands.argument("from", BlockPosArgument.blockPos())
						.then(Commands.argument("to", BlockPosArgument.blockPos())
								// Paintings, item frames and armor stands are usually part of a build, so they
								// are kept unless asked otherwise
								.executes(context -> export(context, true))
								.then(Commands.argument("entities", BoolArgumentType.bool())
										.executes(context -> export(context, BoolArgumentType.getBool(context, "entities")))))));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int export(CommandContext<CommandSourceStack> context, boolean withEntities) {
		ServerLevel level = context.getSource().getLevel();
		String name = StringArgumentType.getString(context, "name");
		BlockPos from = BlockPosArgument.getBlockPos(context, "from");
		BlockPos to = BlockPosArgument.getBlockPos(context, "to");

		// The horizontal bounds grow out to whole chunks so every piece lines up with a real chunk both
		// here and in the dimension it will be rebuilt in.
		int minChunkX = SectionPos.blockToSectionCoord(Math.min(from.getX(), to.getX()));
		int maxChunkX = SectionPos.blockToSectionCoord(Math.max(from.getX(), to.getX()));
		int minChunkZ = SectionPos.blockToSectionCoord(Math.min(from.getZ(), to.getZ()));
		int maxChunkZ = SectionPos.blockToSectionCoord(Math.max(from.getZ(), to.getZ()));

		int minY = Math.max(level.getMinBuildHeight(), Math.min(from.getY(), to.getY()));
		int maxY = Math.min(level.getMaxBuildHeight() - 1, Math.max(from.getY(), to.getY()));

		int chunksX = maxChunkX - minChunkX + 1;
		int chunksZ = maxChunkZ - minChunkZ + 1;

		Path directory = context.getSource().getServer().getServerDirectory().resolve("kingdomkeys").resolve("exports").resolve(name);

		// Shared by every piece, which is the whole point: written once at the end instead of once per file.
		// Kept as a parallel list and map so an earlier export's indices survive untouched even if one of its
		// blocks no longer exists in the registry.
		List<CompoundTag> paletteTags = new ArrayList<>();
		Map<BlockState, Integer> paletteIndex = new LinkedHashMap<>();

		CompoundTag existing = readManifest(directory);
		boolean merging = existing != null;

		// The anchor is what lets a second export of a far away district land in the right place: pieces are
		// numbered from it rather than from each export's own corner, so the gap between districts simply
		// has no files. It is fixed by the first export and never moves, otherwise every earlier piece would
		// shift underneath us.
		int anchorChunkX;
		int anchorChunkZ;
		int anchorY;

		if (merging) {
			loadPalette(level, existing, paletteTags, paletteIndex);
			anchorChunkX = existing.getInt("anchor_x");
			anchorChunkZ = existing.getInt("anchor_z");
			anchorY = existing.getInt("min_y");
		} else {
			paletteTags.add(NbtUtils.writeBlockState(Blocks.AIR.defaultBlockState()));
			paletteIndex.put(Blocks.AIR.defaultBlockState(), 0);
			anchorChunkX = minChunkX;
			anchorChunkZ = minChunkZ;
			anchorY = minY;
		}

		int written = 0;
		long bytes = 0;

		try {
			Files.createDirectories(directory);

			for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
				for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
					BlockPos corner = new BlockPos(SectionPos.sectionToBlockCoord(chunkX), minY, SectionPos.sectionToBlockCoord(chunkZ));
					CompoundTag piece = buildPiece(level, corner, maxY, paletteTags, paletteIndex, withEntities);

					if (piece == null) {
						continue;
					}

					Path file = directory.resolve((chunkX - anchorChunkX) + "_" + (chunkZ - anchorChunkZ) + ".nbt");
					NbtIo.writeCompressed(piece, file);
					bytes += Files.size(file);
					written++;
				}
			}

			writeManifest(directory, anchorY, anchorChunkX, anchorChunkZ, paletteTags);
		} catch (IOException e) {
			KingdomKeys.LOGGER.error("Failed to export world {}", name, e);
			context.getSource().sendFailure(Component.literal("Export failed, see the log: " + e.getMessage()));
			return 0;
		}

		int total = written;
		int skipped = chunksX * chunksZ - written;
		int states = paletteTags.size();
		long kilobytes = bytes / 1024;
		String mode = merging ? "Merged " : "Exported ";
		context.getSource().sendSuccess(() -> Component.literal(mode + total + " pieces (" + skipped + " empty, " + states + " block states, " + kilobytes + " KB) into kingdomkeys/exports/" + name), true);
		return 1;
	}

	private static CompoundTag readManifest(Path directory) {
		Path file = directory.resolve("manifest.nbt");
		if (!Files.isRegularFile(file)) {
			return null;
		}

		try {
			return NbtIo.readCompressed(file, NbtAccounter.unlimitedHeap());
		} catch (IOException e) {
			KingdomKeys.LOGGER.error("Could not read the existing manifest at {}, refusing to guess", file, e);
			throw new IllegalStateException("Existing manifest could not be read", e);
		}
	}

	/** Rebuilds the lookup without renumbering: an old index has to keep meaning what it meant */
	private static void loadPalette(ServerLevel level, CompoundTag manifest, List<CompoundTag> paletteTags, Map<BlockState, Integer> paletteIndex) {
		ListTag states = manifest.getList("palette", Tag.TAG_COMPOUND);

		for (int i = 0; i < states.size(); i++) {
			CompoundTag tag = states.getCompound(i);
			paletteTags.add(tag);
			// putIfAbsent, so a state that no longer parses and falls back to air can't steal index 0
			paletteIndex.putIfAbsent(NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag), i);
		}
	}

	/**
	 * Reads one chunk column into a piece tag. The column is read once into a full height buffer and then
	 * trimmed, so a piece holding a single layer of road doesn't carry three hundred layers of nothing.
	 *
	 * @return null when the column holds nothing worth a file
	 */
	private static CompoundTag buildPiece(ServerLevel level, BlockPos corner, int maxY, List<CompoundTag> paletteTags, Map<BlockState, Integer> paletteIndex, boolean withEntities) {
		// The heightmap already knows where the highest block in each column is, so an export in a 384 tall
		// world doesn't read hundreds of empty layers per chunk.
		ChunkAccess chunk = level.getChunk(corner);
		int top = level.getMinBuildHeight();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				top = Math.max(top, chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z));
			}
		}

		int ceiling = Math.min(maxY, top);
		if (ceiling < corner.getY()) {
			return null;
		}

		int span = ceiling - corner.getY() + 1;
		int[] buffer = new int[16 * span * 16];

		// Local palette, so the per cell index stays inside a byte even though the shared palette may hold
		// thousands of states across the whole world.
		Map<Integer, Integer> local = new LinkedHashMap<>();
		local.put(0, 0);

		ListTag blockEntities = new ListTag();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		int lowest = Integer.MAX_VALUE;
		int highest = Integer.MIN_VALUE;

		for (int y = 0; y < span; y++) {
			for (int z = 0; z < 16; z++) {
				for (int x = 0; x < 16; x++) {
					pos.set(corner.getX() + x, corner.getY() + y, corner.getZ() + z);
					BlockState state = level.getBlockState(pos);

					if (state.isAir()) {
						continue;
					}

					lowest = Math.min(lowest, y);
					highest = Math.max(highest, y);

					Integer global = paletteIndex.get(state);
					if (global == null) {
						global = paletteTags.size();
						paletteTags.add(NbtUtils.writeBlockState(state));
						paletteIndex.put(state, global);
					}

					int index = global;
					buffer[index(x, y, z)] = local.computeIfAbsent(index, key -> local.size());

					BlockEntity blockEntity = level.getBlockEntity(pos);
					if (blockEntity != null) {
						CompoundTag entry = new CompoundTag();
						entry.putInt("x", x);
						entry.putInt("y", y);
						entry.putInt("z", z);
						entry.put("nbt", blockEntity.saveWithId(level.registryAccess()));
						blockEntities.add(entry);
					}
				}
			}
		}

		if (lowest > highest) {
			return null;
		}

		int height = highest - lowest + 1;
		int[] cells = new int[16 * height * 16];
		System.arraycopy(buffer, index(0, lowest, 0), cells, 0, cells.length);

		// Block entity heights were recorded against the untrimmed column
		for (int i = 0; i < blockEntities.size(); i++) {
			CompoundTag entry = blockEntities.getCompound(i);
			entry.putInt("y", entry.getInt("y") - lowest);
		}

		CompoundTag piece = new CompoundTag();
		piece.putInt("min_y", corner.getY() + lowest);
		piece.putInt("height", height);
		piece.putIntArray("palette", local.keySet().stream().mapToInt(Integer::intValue).toArray());

		if (local.size() <= MAX_BYTE_PALETTE) {
			byte[] packed = new byte[cells.length];
			for (int i = 0; i < cells.length; i++) {
				packed[i] = (byte) cells[i];
			}
			piece.putByteArray("blocks", packed);
		} else {
			piece.putIntArray("blocks_int", cells);
		}

		if (!blockEntities.isEmpty()) {
			piece.put("block_entities", blockEntities);
		}

		if (withEntities) {
			ListTag entities = collectEntities(level, corner, ceiling, corner.getY() + lowest);
			if (!entities.isEmpty()) {
				piece.put("entities", entities);
			}
		}

		return piece;
	}

	private static ListTag collectEntities(ServerLevel level, BlockPos corner, int maxY, int pieceMinY) {
		AABB box = new AABB(corner.getX(), corner.getY(), corner.getZ(), corner.getX() + 16, maxY + 1, corner.getZ() + 16);
		List<Entity> found = level.getEntities((Entity) null, box, entity -> !(entity instanceof Player));
		ListTag entities = new ListTag();

		for (Entity entity : found) {
			CompoundTag nbt = new CompoundTag();
			if (!entity.save(nbt)) {
				continue;
			}

			// Dropped so a regenerated world doesn't spawn two entities claiming the same identity
			nbt.remove("UUID");

			ListTag at = new ListTag();
			at.add(DoubleTag.valueOf(entity.getX() - corner.getX()));
			at.add(DoubleTag.valueOf(entity.getY() - pieceMinY));
			at.add(DoubleTag.valueOf(entity.getZ() - corner.getZ()));

			CompoundTag entry = new CompoundTag();
			entry.put("pos", at);
			entry.put("nbt", nbt);
			entities.add(entry);
		}

		return entities;
	}

	/** Order is y, then z, then x, so a flat layer of blocks becomes one long run for the compressor */
	private static int index(int x, int y, int z) {
		return (y * 16 + z) * 16 + x;
	}

	private static void writeManifest(Path directory, int anchorY, int anchorChunkX, int anchorChunkZ, List<CompoundTag> paletteTags) throws IOException {
		ListTag states = new ListTag();
		states.addAll(paletteTags);

		CompoundTag manifest = new CompoundTag();
		manifest.putInt("min_y", anchorY);
		manifest.putInt("anchor_x", anchorChunkX);
		manifest.putInt("anchor_z", anchorChunkZ);
		manifest.put("palette", states);

		NbtIo.writeCompressed(manifest, directory.resolve("manifest.nbt"));
	}
}
