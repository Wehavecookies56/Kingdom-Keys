package online.kingdomkeys.kingdomkeys;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreGen {

    public static void  generateOre() {
        List<Biome> wetBiomes = new ArrayList<Biome>(BiomeDictionary.getBiomes(BiomeDictionary.Type.WET));
        for (Biome biome : ForgeRegistries.BIOMES) {
            if(biome != Biomes.NETHER || biome != Biomes.THE_END) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.lightningOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.blazingOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.brightOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.darkOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 20))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.denseOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 20))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.lucidOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.twilightOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.tranquilOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.energyOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.remembranceOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.serenityOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.stormyOre.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 20))));
                if (biome.getTempCategory() == Biome.TempCategory.COLD) {
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                            (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.frostOre.getDefaultState(), 10))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 100))));
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                            (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.powerOre.getDefaultState(), 10))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 20))));
                }
                else if (wetBiomes.contains(biome))
                    biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                            (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.powerOre.getDefaultState(), 10))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 20))));
            }
            else if(biome == Biomes.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.twilightOreN.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 5, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.energyOreN.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(5, 5, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.darkOreN.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 5, 0, 100))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.blazingOreN.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 5, 0, 100))));
            }
            else if (biome == Biomes.NETHER) {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.darkOreE.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 200))));
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration
                        (new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, ModBlocks.powerOreE.getDefaultState(), 10))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 200))));
            }
        }
    }
}
