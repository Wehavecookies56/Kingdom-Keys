package online.kingdomkeys.kingdomkeys.world.features;

import java.util.BitSet;
import java.util.function.Function;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * Copy of {@link net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration} modified to use {@link BloxOreFeatureConfig} otherwise it is identical
 */
public class BloxOreFeature extends Feature<BloxOreFeatureConfig> {
    public BloxOreFeature(Codec<BloxOreFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    public boolean place(FeaturePlaceContext<BloxOreFeatureConfig> pContext) {
        RandomSource rand = pContext.random();
        BloxOreFeatureConfig config = pContext.config();
        BlockPos pos = pContext.origin();
        WorldGenLevel worldgenlevel = pContext.level();
        float f = rand.nextFloat() * (float)Math.PI;
        float f1 = (float)config.size / 8.0F;
        int i = Mth.ceil(((float)config.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double d0 = (double)(pos.getX() + Mth.sin(f) * f1);
        double d1 = (double)(pos.getX() - Mth.sin(f) * f1);
        double d2 = (double)(pos.getZ() + Mth.cos(f) * f1);
        double d3 = (double)(pos.getZ() - Mth.cos(f) * f1);
        int j = 2;
        double d4 = (double)(pos.getY() + rand.nextInt(3) - 2);
        double d5 = (double)(pos.getY() + rand.nextInt(3) - 2);
        int k = pos.getX() - Mth.ceil(f1) - i;
        int l = pos.getY() - 2 - i;
        int i1 = pos.getZ() - Mth.ceil(f1) - i;
        int j1 = 2 * (Mth.ceil(f1) + i);
        int k1 = 2 * (2 + i);

        for(int l1 = k; l1 <= k + j1; ++l1) {
            for(int i2 = i1; i2 <= i1 + j1; ++i2) {
                if (l <= worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, l1, i2)) {
                    return this.doPlace(worldgenlevel, rand, config, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);
                }
            }
        }

        return false;
    }

    protected boolean doPlace(WorldGenLevel worldIn, RandomSource random, BloxOreFeatureConfig config, double pMinX, double pMaxX, double pMinZ, double pMaxZ, double pMinY, double pMaxY, int pX, int pY, int pZ, int pWidth, int pHeight) {
        int i = 0;
        BitSet bitset = new BitSet(pWidth * pHeight * pWidth);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        int j = config.size;
        double[] adouble = new double[j * 4];

        for(int k = 0; k < config.size; ++k) {
            float f = (float)k / (float)j;
            double d0 = Mth.lerp((double)f, pMinX, pMaxX);
            double d1 = Mth.lerp((double)f, pMinY, pMaxY);
            double d2 = Mth.lerp((double)f, pMinZ, pMaxZ);
            double d3 = random.nextDouble() * (double)j / 16.0D;
            double d4 = ((double)(Mth.sin((float)Math.PI * f) + 1.0F) * d3 + 1.0D) / 2.0D;
            adouble[k * 4 + 0] = d0;
            adouble[k * 4 + 1] = d1;
            adouble[k * 4 + 2] = d2;
            adouble[k * 4 + 3] = d4;
        }

        for(int l3 = 0; l3 < j - 1; ++l3) {
            if (!(adouble[l3 * 4 + 3] <= 0.0D)) {
                for(int i4 = l3 + 1; i4 < j; ++i4) {
                    if (!(adouble[i4 * 4 + 3] <= 0.0D)) {
                        double d8 = adouble[l3 * 4 + 0] - adouble[i4 * 4 + 0];
                        double d10 = adouble[l3 * 4 + 1] - adouble[i4 * 4 + 1];
                        double d12 = adouble[l3 * 4 + 2] - adouble[i4 * 4 + 2];
                        double d14 = adouble[l3 * 4 + 3] - adouble[i4 * 4 + 3];
                        if (d14 * d14 > d8 * d8 + d10 * d10 + d12 * d12) {
                            if (d14 > 0.0D) {
                                adouble[i4 * 4 + 3] = -1.0D;
                            } else {
                                adouble[l3 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        BulkSectionAccess bulksectionaccess = new BulkSectionAccess(worldIn);

        try {
            for(int j4 = 0; j4 < j; ++j4) {
                double d9 = adouble[j4 * 4 + 3];
                if (!(d9 < 0.0D)) {
                    double d11 = adouble[j4 * 4 + 0];
                    double d13 = adouble[j4 * 4 + 1];
                    double d15 = adouble[j4 * 4 + 2];
                    int k4 = Math.max(Mth.floor(d11 - d9), pX);
                    int l = Math.max(Mth.floor(d13 - d9), pY);
                    int i1 = Math.max(Mth.floor(d15 - d9), pZ);
                    int j1 = Math.max(Mth.floor(d11 + d9), k4);
                    int k1 = Math.max(Mth.floor(d13 + d9), l);
                    int l1 = Math.max(Mth.floor(d15 + d9), i1);

                    for(int i2 = k4; i2 <= j1; ++i2) {
                        double d5 = ((double)i2 + 0.5D - d11) / d9;
                        if (d5 * d5 < 1.0D) {
                            for(int j2 = l; j2 <= k1; ++j2) {
                                double d6 = ((double)j2 + 0.5D - d13) / d9;
                                if (d5 * d5 + d6 * d6 < 1.0D) {
                                    for(int k2 = i1; k2 <= l1; ++k2) {
                                        double d7 = ((double)k2 + 0.5D - d15) / d9;
                                        if (d5 * d5 + d6 * d6 + d7 * d7 < 1.0D && !worldIn.isOutsideBuildHeight(j2)) {
                                            int l2 = i2 - pX + (j2 - pY) * pWidth + (k2 - pZ) * pWidth * pHeight;
                                            if (!bitset.get(l2)) {
                                                bitset.set(l2);
                                                blockpos$mutable.set(i2, j2, k2);
                                                if (worldIn.ensureCanWrite(blockpos$mutable)) {
                                                    LevelChunkSection levelchunksection = bulksectionaccess.getSection(blockpos$mutable);
                                                    if (levelchunksection != null) {
                                                        int i3 = SectionPos.sectionRelative(i2);
                                                        int j3 = SectionPos.sectionRelative(j2);
                                                        int k3 = SectionPos.sectionRelative(k2);
                                                        BlockState blockstate = levelchunksection.getBlockState(i3, j3, k3);

                                                        for(BloxOreFeatureConfig.TargetBlockState targetBlockStates : config.targetStates) {
                                                            if (canPlaceOre(blockstate, bulksectionaccess::getBlockState, random, config, targetBlockStates, blockpos$mutable)) {
                                                                levelchunksection.setBlockState(i3, j3, k3, targetBlockStates.getState(), false);
                                                                ++i;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable1) {
            try {
                bulksectionaccess.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }

            throw throwable1;
        }

        bulksectionaccess.close();
        return i > 0;
    }

    public static boolean canPlaceOre(BlockState pState, Function<BlockPos, BlockState> pAdjacentStateAccessor, RandomSource pRandom, BloxOreFeatureConfig pConfig, BloxOreFeatureConfig.TargetBlockState pTargetState, BlockPos.MutableBlockPos pMutablePos) {
        if (!pTargetState.target.test(pState, pRandom)) {
            return false;
        } else if (shouldSkipAirCheck(pRandom, pConfig.discardChanceOnAirExposure)) {
            return true;
        } else {
            return !isAdjacentToAir(pAdjacentStateAccessor, pMutablePos);
        }
    }

    protected static boolean shouldSkipAirCheck(RandomSource pRandom, float pChance) {
        if (pChance <= 0.0F) {
            return true;
        } else if (pChance >= 1.0F) {
            return false;
        } else {
            return pRandom.nextFloat() >= pChance;
        }
    }
}