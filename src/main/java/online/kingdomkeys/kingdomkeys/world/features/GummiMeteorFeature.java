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
        BlockPos surfacePos = new BlockPos(origin.getX(), surfaceY, origin.getZ());
        BlockState surfaceBlock = level.getBlockState(surfacePos);

        if (surfaceBlock.getFluidState().isSource()) {
            surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX(), origin.getZ());
        }

        BlockPos center = new BlockPos(origin.getX(), surfaceY, origin.getZ());

        int craterRadius = 6 + random.nextInt(5); // 6–10 bloques
        int craterDepth = 3 + random.nextInt(3);
        int meteorRadius = 2 + random.nextInt(2); // 2 o 3 bloques

        // === 1️⃣ Crear cráter ===
        for (int x = -craterRadius; x <= craterRadius; x++) {
            for (int z = -craterRadius; z <= craterRadius; z++) {
                double dist = Math.sqrt(x * x + z * z);
                if (dist <= craterRadius) {
                    int localDepth = (int) (craterDepth * (1 - dist / craterRadius));
                    for (int y = 0; y <= localDepth; y++) {
                        BlockPos pos = center.offset(x, -y, z);

                        // Romper cualquier bloque que esté en el alcance
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

                        // Pared del borde del cráter
                        if (y == localDepth && dist > craterRadius - 2) {
                            BlockState rimBlock = random.nextFloat() < 0.7f
                                    ? Blocks.BASALT.defaultBlockState()
                                    : Blocks.AIR.defaultBlockState();

                            // Colocamos bloque del borde y lo expandimos hacia abajo
                            BlockPos rimPos = pos;
                            for (int down = 0; down < 3; down++) {
                                BlockPos below = rimPos.below();
                                if (level.getBlockState(below).isAir()) {
                                    level.setBlock(below, rimBlock, 2);
                                    rimPos = below;
                                } else {
                                    break;
                                }
                            }
                            // También ponemos el bloque en la posición original del borde
                            level.setBlock(pos, rimBlock, 2);
                        }
                    }
                }
            }
        }



        // === 2️⃣ Crear meteorito esférico ===
        BlockPos meteorCenter = center.below(craterDepth / 2 + meteorRadius / 2);
        for (int x = -meteorRadius; x <= meteorRadius; x++) {
            for (int y = -meteorRadius; y <= meteorRadius; y++) {
                for (int z = -meteorRadius; z <= meteorRadius; z++) {
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    if (dist <= meteorRadius + random.nextFloat() * 0.3) { // forma orgánica
                        BlockPos pos = meteorCenter.offset(x, y, z);
                        BlockState meteorMat;

                        meteorMat = ModBlocks.gummiMeteor.get().defaultBlockState();

                        level.setBlock(pos, meteorMat, 2);
                    }
                }
            }
        }

        // === 3️⃣ Fragmentos alrededor del cráter ===
        int fragCount = 6 + random.nextInt(5);
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

       /* if (random.nextFloat() < 0.3f) {
            BlockPos lavaSpot = center.offset(random.nextIntBetweenInclusive(-2, 2), -1, random.nextIntBetweenInclusive(-2, 2));
            level.setBlock(lavaSpot, Blocks.LAVA.defaultBlockState(), 2);
        }*/
        if (random.nextFloat() < 0.4f) {
            BlockPos fireSpot = center.offset(random.nextIntBetweenInclusive(-craterRadius, craterRadius),
                    0,
                    random.nextIntBetweenInclusive(-craterRadius, craterRadius));
            if (level.getBlockState(fireSpot.below()).isSolid())
                level.setBlock(fireSpot, Blocks.FIRE.defaultBlockState(), 2);
        }

        return true;
    }
}