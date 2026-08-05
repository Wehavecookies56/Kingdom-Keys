package online.kingdomkeys.kingdomkeys.world.dimension.worldmap;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldMapChunkGenerator extends ChunkGenerator {

	private final BiomeSource biomeSource;

	public WorldMapChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource);
		this.biomeSource = biomeSource;
	}

	public static final MapCodec<WorldMapChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(inst -> inst.biomeSource)).apply(instance, instance.stable(WorldMapChunkGenerator::new)));

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	private static final int REGION_CHUNKS = 8;
	private static final float CLUSTER_CHANCE = 0.55F;
	private static final int CLUSTER_SPREAD = 2;
	private static final int ROCKS_PER_CHUNK = 3;
	private static final int MIN_ROCK_RADIUS = 1;
	private static final int MAX_ROCK_RADIUS = 4;
	private static final int CLUSTER_HEIGHT = 40;
	private static final int MIN_Y = 48;
	private static final int MAX_Y = 320;
	private static final float METEOR_CHANCE = 0.6F;

	@Override
	public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk, GenerationStep.Carving step) {
	}

	// Deliberately does NOT call super. The default runs the biome's placed features, and the void biome carries minecraft:void_start_platform - that was the stone square sitting under the origin.
	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
		ChunkPos chunkPos = chunk.getPos();
		int regionX = Math.floorDiv(chunkPos.x, REGION_CHUNKS);
		int regionZ = Math.floorDiv(chunkPos.z, REGION_CHUNKS);

		RandomSource regionRandom = RandomSource.create(level.getSeed() ^ (regionX * 341873128712L + regionZ * 132897987541L));
		if (regionRandom.nextFloat() > CLUSTER_CHANCE) {
			return;
		}

		int centreChunkX = regionX * REGION_CHUNKS + regionRandom.nextInt(REGION_CHUNKS);
		int centreChunkZ = regionZ * REGION_CHUNKS + regionRandom.nextInt(REGION_CHUNKS);
		int centreY = MIN_Y + regionRandom.nextInt(MAX_Y - MIN_Y);

		if (Math.abs(chunkPos.x - centreChunkX) > CLUSTER_SPREAD || Math.abs(chunkPos.z - centreChunkZ) > CLUSTER_SPREAD) {
			return;
		}

		RandomSource chunkRandom = RandomSource.create(level.getSeed() ^ (chunkPos.x * 4987142L + chunkPos.z * 5947611L));
		int rocks = 1 + chunkRandom.nextInt(ROCKS_PER_CHUNK);

		for (int i = 0; i < rocks; i++) {
			int radius = MIN_ROCK_RADIUS + chunkRandom.nextInt(MAX_ROCK_RADIUS - MIN_ROCK_RADIUS + 1);
			int x = chunkPos.getMinBlockX() + radius + chunkRandom.nextInt(16 - radius * 2);
			int z = chunkPos.getMinBlockZ() + radius + chunkRandom.nextInt(16 - radius * 2);
			int y = centreY - CLUSTER_HEIGHT / 2 + chunkRandom.nextInt(CLUSTER_HEIGHT);

			placeRock(level, chunkRandom, new BlockPos(x, y, z), radius);
		}
	}

	private void placeRock(WorldGenLevel level, RandomSource random, BlockPos centre, int radius) {
		BlockState meteor = ModBlocks.gummiMeteor.get().defaultBlockState();
		BlockState basalt = Blocks.BASALT.defaultBlockState();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					// The jitter on the edge stops every rock coming out as a perfect ball.
					if (x * x + y * y + z * z > radius * radius + random.nextFloat() * 0.8F) {
						continue;
					}

					pos.set(centre.getX() + x, centre.getY() + y, centre.getZ() + z);
					if (!level.isOutsideBuildHeight(pos)) {
						level.setBlock(pos, random.nextFloat() < METEOR_CHANCE ? meteor : basalt, 2);
					}
				}
			}
		}
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
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
	}
}
