package online.kingdomkeys.kingdomkeys.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

/**
 * Copy of {@link net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration} modified to use {@link BloxOreFeatureConfig} otherwise it is identical
 */
public class GummiMeteorFeature extends Feature<NoneFeatureConfiguration> {
    public GummiMeteorFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, origin.getX(), origin.getZ());
        BlockPos center = new BlockPos(origin.getX(), surfaceY, origin.getZ());

        BlockState surfaceBlock = level.getBlockState(center.below());
        if (surfaceBlock.getFluidState().isSource()) {
            return false;
        }

        int craterRadius = 6 + random.nextInt(5);
        int craterDepth = 3 + random.nextInt(3);
        int meteorRadius = 2 + random.nextInt(2);

        spawnCrater(level, random, center, craterRadius, craterDepth, meteorRadius);

        return true;
    }

    private void spawnCrater(WorldGenLevel level, RandomSource random, BlockPos center, int craterRadius, int craterDepth, int meteorRadius) {
        //Add main meteor core
        BlockPos meteorCenter = center.below(craterDepth / 2 + meteorRadius / 2);
        for (int x = -meteorRadius; x <= meteorRadius; x++) {
            for (int y = -meteorRadius; y <= meteorRadius; y++) {
                for (int z = -meteorRadius; z <= meteorRadius; z++) {
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    if (dist <= meteorRadius + random.nextFloat() * 0.3) {
                        BlockPos pos = meteorCenter.offset(x, y, z);
                        BlockState meteorMat = random.nextFloat() < 0.6F ? ModBlocks.gummiMeteor.get().defaultBlockState() : Blocks.BASALT.defaultBlockState();
                        level.setBlock(pos, meteorMat, 2);
                    }
                }
            }
        }

        //Add random meteor fragments
        int fragCount = 4 + random.nextInt(5);
        for (int i = 0; i < fragCount; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double dist = craterRadius + random.nextDouble() * 3.0;
            int x = (int) (Math.cos(angle) * dist);
            int z = (int) (Math.sin(angle) * dist);
            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, center.getX() + x, center.getZ() + z);

            BlockPos fragPos = new BlockPos(center.getX() + x, y, center.getZ() + z);
            BlockState fragment = ModBlocks.gummiMeteor.get().defaultBlockState();
            level.setBlock(fragPos, fragment, 2);
        }

        //Add random fire
        if (random.nextFloat() < 0.4f) {
            BlockPos fireSpot = center.offset(random.nextIntBetweenInclusive(-craterRadius, craterRadius), 0, random.nextIntBetweenInclusive(-craterRadius, craterRadius));
            if (level.getBlockState(fireSpot.below()).isSolid())
                level.setBlock(fireSpot, Blocks.FIRE.defaultBlockState(), 2);
        }

        //Clear the rest of blocks
        for (int x = -craterRadius; x <= craterRadius; x++) {
            for (int y = -craterDepth; y <= 0; y++) {
                for (int z = -craterRadius; z <= craterRadius; z++) {
                    double distXZ = Math.sqrt(x * x + z * z);
                    double dist3D = Math.sqrt(x*x + y*y + z*z);

                    if (distXZ <= craterRadius) {
                        BlockPos pos = center.offset(x, y, z);

                        if (dist3D > craterRadius - 1.5 && dist3D <= craterRadius) {
                            BlockState rimBlock = random.nextFloat() < 0.7f ? Blocks.BASALT.defaultBlockState() : Blocks.AIR.defaultBlockState();
                            if(level.getBlockState(pos) != Blocks.AIR.defaultBlockState())
                                level.setBlock(pos, rimBlock, 2);

                            for (int down = 0; down < craterDepth * 2; down++) {
                                BlockPos below = pos.below(down);
                                if (!level.getBlockState(below).isAir()) {
                                    level.setBlock(below, rimBlock, 2);
                                } else {
                                    break;
                                }
                            }
                        }

                        //Finish clearing blocks on top of the meteor
                        for(int i=0;i<20;i++) {
                            pos = pos.above(i);
                            if (level.getBlockState(pos) != Blocks.BASALT.defaultBlockState() && level.getBlockState(pos) != ModBlocks.gummiMeteor.get().defaultBlockState()) {
                                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }
    }
}