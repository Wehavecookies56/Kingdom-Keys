package online.kingdomkeys.kingdomkeys.world.dimension.prebuilt;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.*;
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

import java.util.Arrays;
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

	/** What every piece and the manifest are written as */
	private static final String EXTENSION = ".nbt";

	private final BiomeSource biomeSource;
	/** Folder under data/&lt;namespace&gt;/kk_worlds/ holding manifest.nbt and the &lt;x&gt;_&lt;z&gt;.nbt pieces */
	private final ResourceLocation world;
	/** Where the export's lowest corner goes. X and Z want to be multiples of 16 so pieces land on chunks. */
	private final BlockPos origin;
	/** Poured around the build instead of leaving void. Absent means the world floats in nothing, as it used to. */
	private final Optional<SeaSettings> sea;

	// Read once and kept for the life of the dimension. Worldgen is threaded, so palette is written last and read first: seeing it non-null means manifestMinY is already set.
	private volatile int manifestMinY;
	private volatile BlockState[] palette;

	public PrebuiltWorldChunkGenerator(BiomeSource biomeSource, ResourceLocation world, BlockPos origin, Optional<SeaSettings> sea) {
		super(biomeSource);
		this.biomeSource = biomeSource;
		this.world = world;
		this.origin = origin;
		this.sea = sea;
	}

	public static final MapCodec<PrebuiltWorldChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
					ResourceLocation.CODEC.fieldOf("world").forGetter(generator -> generator.world),
					BlockPos.CODEC.optionalFieldOf("origin", BlockPos.ZERO).forGetter(generator -> generator.origin),
					SeaSettings.CODEC.optionalFieldOf("sea").forGetter(generator -> generator.sea)
			).apply(instance, instance.stable(PrebuiltWorldChunkGenerator::new)));

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}
	// Deliberately does NOT call super: the default runs the biome's placed features, which would scatter ores, kelp and whatever else the biome carries through a world that was built by hand.
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

		CompoundTag piece = read(level, pieceX + "_" + pieceZ + EXTENSION);
		if (piece == null) {
			// If the chunk is null fill it with whatever is specified
			fill(level, chunk, null);
			return;
		}

		// The piece knows where it sat in the world it was built in; the origin says where that export's
		// floor lives now, so the difference carries the piece across.
		int baseY = origin.getY() + piece.getInt("min_y") - manifestMinY;
		BlockPos base = new BlockPos(chunkPos.getMinBlockX(), baseY, chunkPos.getMinBlockZ());

		fill(level, chunk, placeBlocks(level, piece, states, base));
		placeBlockEntities(level, piece, base);
		placeEntities(level, piece, base);
	}

	/** @return the lowest Y the build reaches in each of the chunk's 256 columns, indexed z * 16 + x */
	private int[] placeBlocks(WorldGenLevel level, CompoundTag piece, BlockState[] states, BlockPos base) {
		int height = piece.getInt("height");
		int[] local = piece.getIntArray("palette");
		byte[] packed = piece.getByteArray("blocks");
		int[] wide = piece.getIntArray("blocks_int");
		boolean isPacked = packed.length > 0;

		int[] bottoms = new int[256];
		Arrays.fill(bottoms, Integer.MAX_VALUE);

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

					// Climbing, so the first hit in a column is its underside
					if (bottoms[z * 16 + x] == Integer.MAX_VALUE) {
						bottoms[z * 16 + x] = base.getY() + y;
					}
				}
			}
		}

		return bottoms;
	}

	/** Chunks the export covers, as packed piece coordinates. Listed once and kept. */
	private volatile LongSet covered;

	/** Chunks around the build that stay as flat as it is */
	private static final int FLAT_CHUNKS = 1;

	/** Chunks to get all of its relief back */
	private static final int FALLOFF_CHUNKS = 7;

	/** Margin of flatness */
	private float relief(WorldGenLevel level, int pieceX, int pieceZ) {
		LongSet pieces = covered(level);

		if (pieces.isEmpty()) {
			return 1F;
		}

		for (int ring = 0; ring <= FALLOFF_CHUNKS; ring++) {
			if (ringTouchesBuild(pieces, pieceX, pieceZ, ring)) {
				return Mth.clamp((ring - FLAT_CHUNKS) / (float) (FALLOFF_CHUNKS - FLAT_CHUNKS), 0F, 1F);
			}
		}

		return 1F;
	}

	/** Whether any exported chunk sits exactly this many chunks away, measured as a square ring */
	private static boolean ringTouchesBuild(LongSet pieces, int pieceX, int pieceZ, int ring) {
		if (ring == 0) {
			return pieces.contains(ChunkPos.asLong(pieceX, pieceZ));
		}

		for (int offset = -ring; offset <= ring; offset++) {
			if (pieces.contains(ChunkPos.asLong(pieceX + offset, pieceZ - ring))
					|| pieces.contains(ChunkPos.asLong(pieceX + offset, pieceZ + ring))
					|| pieces.contains(ChunkPos.asLong(pieceX - ring, pieceZ + offset))
					|| pieces.contains(ChunkPos.asLong(pieceX + ring, pieceZ + offset))) {
				return true;
			}
		}

		return false;
	}

	/** Reads the piece names once, which is cheaper than opening pieces to ask whether they exist */
	private LongSet covered(WorldGenLevel level) {
		LongSet known = covered;

		if (known != null) {
			return known;
		}

		LongSet found = new LongOpenHashSet();
		String folder = "kk_worlds/" + world.getPath();

		level.getLevel().getServer().getResourceManager().listResources(folder, path -> path.getPath().endsWith(EXTENSION)).keySet().forEach(path -> {
			String name = path.getPath().substring(path.getPath().lastIndexOf('/') + 1, path.getPath().length() - EXTENSION.length());
			int split = name.indexOf('_', name.startsWith("-") ? 1 : 0);

			if (split <= 0) {
				return; // the manifest, and anything else that is not a piece
			}

			try {
				found.add(ChunkPos.asLong(Integer.parseInt(name.substring(0, split)), Integer.parseInt(name.substring(split + 1))));
			} catch (NumberFormatException notAPiece) {
			}
		});

		covered = found;
		return found;
	}

	private void fill(WorldGenLevel level, ChunkAccess chunk, int[] bottoms) {
		SeaSettings settings = sea.orElse(null);

		if (settings == null) {
			return;
		}

		ChunkPos chunkPos = chunk.getPos();
		float relief = relief(level, chunkPos.x - SectionPos.blockToSectionCoord(origin.getX()), chunkPos.z - SectionPos.blockToSectionCoord(origin.getZ()));
		int bottom = chunk.getMinBuildHeight();
		int surface = Math.min(settings.level(), chunk.getMaxBuildHeight());

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				int worldX = chunkPos.getMinBlockX() + x;
				int worldZ = chunkPos.getMinBlockZ() + z;

				// The first Y that belongs to the build, and so the first one nothing may be written at
				int limit = bottoms == null ? surface : Math.min(surface, bottoms[z * 16 + x]);

				if (limit <= bottom) {
					continue;
				}

				int solid = limit - 1 <= settings.floor() ? limit - 1 : settings.floorAt(worldX, worldZ, relief);

				// Straight at the chunk rather than through the region: every one of these is inside it, and
				// there are thousands per chunk, so the lookup the region would do each time is worth losing
				for (int y = bottom; y <= solid; y++) {
					pos.set(worldX, y, worldZ);
					chunk.setBlockState(pos, settings.bed(worldX, y, worldZ, bottom, solid), false);
				}

				for (int y = solid + 1; y < limit; y++) {
					pos.set(worldX, y, worldZ);
					chunk.setBlockState(pos, settings.water(), false);
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

		CompoundTag manifest = read(level, "manifest" + EXTENSION);
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
		return sea.map(SeaSettings::level).orElse(0);
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
