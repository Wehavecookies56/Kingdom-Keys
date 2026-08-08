package online.kingdomkeys.kingdomkeys.world.dimension.prebuilt;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Rebuilds a hand-built world one chunk at a time from the pieces produced by /kingdomkeys export.
 *
 * Each piece covers exactly one chunk, so a chunk only ever places its own blocks: no cascading generation
 * and no single enormous placement to stall the server. Columns the exporter found empty have no file at
 * all and are left as void.
 *
 * Everything is data driven, so a new world needs no code: only a dimension json pointing at a different
 * folder. See {@link online.kingdomkeys.kingdomkeys.command.ExportWorldCommand} for the file format.
 */
public class PrebuiltWorldChunkGenerator extends ChunkGenerator {

	private final BiomeSource biomeSource;
	/** Folder under data/&lt;namespace&gt;/kk_worlds/ holding manifest.nbt and the &lt;x&gt;_&lt;z&gt;.nbt pieces */
	private final ResourceLocation world;
	/** Where the export's lowest corner goes. X and Z want to be multiples of 16 so pieces land on chunks. */
	private final BlockPos origin;

	// Read once and kept for the life of the dimension. Worldgen is threaded, so palette is written last and read first: seeing it non-null means manifestMinY is already set.
	private volatile int manifestMinY;
	private volatile BlockState[] palette;

	public PrebuiltWorldChunkGenerator(BiomeSource biomeSource, ResourceLocation world, BlockPos origin) {
		super(biomeSource);
		this.biomeSource = biomeSource;
		this.world = world;
		this.origin = origin;
	}

	public static final MapCodec<PrebuiltWorldChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
					ResourceLocation.CODEC.fieldOf("world").forGetter(generator -> generator.world),
					BlockPos.CODEC.optionalFieldOf("origin", BlockPos.ZERO).forGetter(generator -> generator.origin)
			).apply(instance, instance.stable(PrebuiltWorldChunkGenerator::new)));

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}
	// Deliberately does NOT call super: the default runs the biome's placed features, and the void biome carries minecraft:void_start_platform, which would drop a stone square under the origin.
	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
		BlockState[] states = palette(level);
		if (states == null) {
			return;
		}

		ChunkPos chunkPos = chunk.getPos();

		// Pieces are numbered from the export's anchor and may sit on either side of it, since a world can be
		// exported in several passes covering districts far apart.
		int pieceX = chunkPos.x - SectionPos.blockToSectionCoord(origin.getX());
		int pieceZ = chunkPos.z - SectionPos.blockToSectionCoord(origin.getZ());

		CompoundTag piece = read(level, pieceX + "_" + pieceZ + ".nbt");
		if (piece == null) {
			return;
		}

		// The piece knows where it sat in the world it was built in; the origin says where that export's
		// floor lives now, so the difference carries the piece across.
		int baseY = origin.getY() + piece.getInt("min_y") - manifestMinY;
		BlockPos base = new BlockPos(chunkPos.getMinBlockX(), baseY, chunkPos.getMinBlockZ());

		placeBlocks(level, piece, states, base);
		placeBlockEntities(level, piece, base);
		placeEntities(level, piece, base);
	}

	private void placeBlocks(WorldGenLevel level, CompoundTag piece, BlockState[] states, BlockPos base) {
		int height = piece.getInt("height");
		int[] local = piece.getIntArray("palette");
		byte[] packed = piece.getByteArray("blocks");
		int[] wide = piece.getIntArray("blocks_int");
		boolean isPacked = packed.length > 0;

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int y = 0; y < height; y++) {
			for (int z = 0; z < 16; z++) {
				for (int x = 0; x < 16; x++) {
					int cell = (y * 16 + z) * 16 + x;
					int index = isPacked ? packed[cell] & 0xFF : wide[cell];

					// Index 0 is always air, which is simply never written
					if (index <= 0 || index >= local.length) {
						continue;
					}

					int global = local[index];
					if (global < 0 || global >= states.length) {
						continue;
					}

					pos.set(base.getX() + x, base.getY() + y, base.getZ() + z);
					level.setBlock(pos, states[global], Block.UPDATE_CLIENTS);
				}
			}
		}
	}

	private void placeBlockEntities(WorldGenLevel level, CompoundTag piece, BlockPos base) {
		ListTag list = piece.getList("block_entities", Tag.TAG_COMPOUND);

		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = list.getCompound(i);
			BlockPos at = base.offset(entry.getInt("x"), entry.getInt("y"), entry.getInt("z"));

			// The block was placed just above, so its entity already exists and only needs its contents
			BlockEntity blockEntity = level.getBlockEntity(at);
			if (blockEntity != null) {
				blockEntity.loadWithComponents(entry.getCompound("nbt"), level.registryAccess());
				blockEntity.setChanged();
			}
		}
	}

	private void placeEntities(WorldGenLevel level, CompoundTag piece, BlockPos base) {
		ListTag list = piece.getList("entities", Tag.TAG_COMPOUND);

		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = list.getCompound(i);
			ListTag at = entry.getList("pos", Tag.TAG_DOUBLE);

			double x = base.getX() + at.getDouble(0);
			double y = base.getY() + at.getDouble(1);
			double z = base.getZ() + at.getDouble(2);

			try {
				EntityType.create(entry.getCompound("nbt"), level.getLevel()).ifPresent(entity -> {
					entity.moveTo(x, y, z, entity.getYRot(), entity.getXRot());
					level.addFreshEntity(entity);
				});
			} catch (Exception e) {
				KingdomKeys.LOGGER.warn("Could not place an entity from world {}", world, e);
			}
		}
	}

	private BlockState[] palette(WorldGenLevel level) {
		BlockState[] cached = palette;
		if (cached != null) {
			return cached;
		}

		CompoundTag manifest = read(level, "manifest.nbt");
		if (manifest == null) {
			KingdomKeys.LOGGER.error("No manifest for prebuilt world {}, nothing will generate", world);
			return null;
		}

		ListTag states = manifest.getList("palette", Tag.TAG_COMPOUND);
		BlockState[] read = new BlockState[states.size()];

		for (int i = 0; i < states.size(); i++) {
			read[i] = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), states.getCompound(i));
		}

		manifestMinY = manifest.getInt("min_y");
		palette = read;
		return read;
	}

	private CompoundTag read(WorldGenLevel level, String file) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(world.getNamespace(), "kk_worlds/" + world.getPath() + "/" + file);
		Optional<Resource> resource = level.getLevel().getServer().getResourceManager().getResource(id);

		if (resource.isEmpty()) {
			return null;
		}

		try {
			return NbtIo.readCompressed(resource.get().open(), NbtAccounter.unlimitedHeap());
		} catch (Exception e) {
			KingdomKeys.LOGGER.error("Could not read {}", id, e);
			return null;
		}
	}

	@Override
	public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion level) {
	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
		return origin.getY();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
	}
}
