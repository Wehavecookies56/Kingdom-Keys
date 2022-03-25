package online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.MosaicStainedGlassBlock;
import online.kingdomkeys.kingdomkeys.block.SoAPlatformCoreBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DiveToTheHeartChunkGenerator extends ChunkGenerator {

    public DiveToTheHeartChunkGenerator(Registry<StructureSet> structureSetRegistry, Registry<Biome> registry) {
        super(structureSetRegistry, Optional.empty(), new DiveToTheHeartBiomeProvider(registry));
    }

    public static void registerChunkGenerator() {
		Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart_generator"), DiveToTheHeartChunkGenerator.CODEC);
	}

	public static final Codec<DiveToTheHeartChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(DiveToTheHeartChunkGenerator::getStructureSetRegistry),
                    RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(DiveToTheHeartChunkGenerator::getBiomeRegistry)
            ).apply(instance, DiveToTheHeartChunkGenerator::new));

    public Registry<Biome> getBiomeRegistry() {
        return ((DiveToTheHeartBiomeProvider)biomeSource).getBiomeRegistry();
    }

    public Registry<StructureSet> getStructureSetRegistry() {
        return structureSets;
    }

    private static final BlockPos SPAWN_POS = new BlockPos(0, 25, 0);
    private static final ChunkPos SPAWN_CHUNK_POS = new ChunkPos(SPAWN_POS);

    //x
    int width = 17;
    //y
    int height = 25;
    //z
    int depth = 17;

    String topOfPlatform =
			"00000000000000000" +
			"00000000300000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00400000000000500" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000" +
            "00000000000000000";

    String structureTop =
            "00000111111100000" +
            "00011111111111000" +
            "00111111111111100" +
            "01111111111111110" +
            "01111111111111110" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111211111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "01111111111111110" +
            "01111111111111110" +
            "00111111111111100" +
            "00011111111111000" +
            "00000111111100000";

    String structureMiddle =
            "00000111111100000" +
            "00011000000011000" +
            "00100000000000100" +
            "01000000000000010" +
            "01000000000000010" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "10000000000000001" +
            "01000000000000010" +
            "01000000000000010" +
            "00100000000000100" +
            "00011000000011000" +
            "00000111111100000";

    String structureBottom =
            "00000111111100000" +
            "00011111111111000" +
            "00111111111111100" +
            "01111111111111110" +
            "01111111111111110" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "11111111111111111" +
            "01111111111111110" +
            "01111111111111110" +
            "00111111111111100" +
            "00011111111111000" +
            "00000111111100000";

    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new DiveToTheHeartChunkGenerator(getStructureSetRegistry(), getBiomeRegistry());
    }

    @Override
    public Climate.Sampler climateSampler() {
        return new Climate.Sampler(DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), DensityFunctions.constant(0.0), Collections.emptyList());
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, BiomeManager pBiomeManager, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) { }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureFeatureManager pStructureFeatureManager, ChunkAccess pChunk) { }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) { }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, StructureFeatureManager manager, ChunkAccess chunkIn) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        if (distance(chunkIn.getPos().x, chunkIn.getPos().z, SPAWN_CHUNK_POS.x, SPAWN_CHUNK_POS.z) < 1) {
            int startZ = chunkIn.getPos().getMinBlockZ() - (depth/2);
            int startX = chunkIn.getPos().getMinBlockX() - (width/2);

            for (int y = 0; y < height; ++y) {
                for (int z = startZ; z <= chunkIn.getPos().getMinBlockZ() + depth/2; ++z) {
                    for (int x = startX; x <= chunkIn.getPos().getMinBlockX() + width/2; ++x) {
                        blockpos$mutable.set(x, SPAWN_POS.getY() - y, z);
                        int strucX = x - startX;
                        int strucZ = z - startZ;
                        if (y == 0) {
                            stateToPlace(topOfPlatform.charAt(strucX + strucZ * width), chunkIn.getWorldForge(), blockpos$mutable);
                        } else if (y == 1) {
                            stateToPlace(structureTop.charAt(strucX + strucZ * width), chunkIn.getWorldForge(), blockpos$mutable);
                        } else if (y == height - 1) {
                            stateToPlace(structureBottom.charAt(strucX + strucZ * width), chunkIn.getWorldForge(), blockpos$mutable);
                        } else {
                            stateToPlace(structureMiddle.charAt(strucX + strucZ * width), chunkIn.getWorldForge(), blockpos$mutable);
                        }
                    }
                }
            }
        }
        return CompletableFuture.completedFuture(chunkIn);
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
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pLevel) {
        return null;
    }

    @Override
    public void addDebugScreenInfo(List<String> p_208054_, BlockPos p_208055_) {

    }

    /**
     * Returns the Manhattan distance between the two points.
     */
    private static int distance(int firstX, int firstZ, int secondX, int secondZ) {
        return Math.max(Math.abs(firstX - secondX), Math.abs(firstZ - secondZ));
    }

    private void stateToPlace(char c, LevelAccessor world, BlockPos.MutableBlockPos pos) {
        switch (c) {
            case '0':
                return;
            case '1':
                world.setBlock(pos, ModBlocks.mosaic_stained_glass.get().defaultBlockState().setValue(MosaicStainedGlassBlock.STRUCTURE, true), 2);
                break;
            case '2':
                world.setBlock(pos, ModBlocks.station_of_awakening_core.get().defaultBlockState().setValue(SoAPlatformCoreBlock.STRUCTURE, true), 2);
                ((SoAPlatformTileEntity) world.getBlockEntity(pos)).setMultiblockFormed(true);
                break;
            case '3':
                createPedestal(world, pos, new ItemStack(ModItems.dreamSword.get()));
                break;
            case '4':
                createPedestal(world, pos, new ItemStack(ModItems.dreamShield.get()));
                break;
            case '5':
                createPedestal(world, pos, new ItemStack(ModItems.dreamStaff.get()));
                break;
        }
    }

    private void createPedestal(LevelAccessor world, BlockPos.MutableBlockPos pos, ItemStack toDisplay) {
        world.setBlock(pos, ModBlocks.pedestal.get().defaultBlockState(), 2);
        PedestalTileEntity te = ((PedestalTileEntity) world.getBlockEntity(pos));
        te.setStationOfAwakeningMarker(true);
        te.setDisplayStack(toDisplay);
    }
    
}
