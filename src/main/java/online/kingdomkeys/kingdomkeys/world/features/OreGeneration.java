package online.kingdomkeys.kingdomkeys.world.features;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class OreGeneration {

	//local variable to reduce parameters
	private static BiomeGenerationSettingsBuilder settingsBuilder;

	public static void addOre(ConfiguredFeature<?, ?> feature) {
		settingsBuilder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature);
	}

	public static void generateOre(BiomeLoadingEvent event) {
		settingsBuilder = event.getGeneration();

		//Nether
		if (event.getCategory().equals(Biome.Category.NETHER)) {
			if (ModConfigs.oreGen) {
				addOre(ModFeatures.TWILIGHT_ORE_NETHER);
				addOre(ModFeatures.WELLSPRING_ORE_NETHER);
				addOre(ModFeatures.WRITHING_ORE_NETHER);
				addOre(ModFeatures.BLAZING_ORE_NETHER);
			}
		}
		//End
		else if (event.getCategory().equals(Biome.Category.THEEND)) {
			if (ModConfigs.oreGen) {
				addOre(ModFeatures.WRITHING_ORE_END);
				addOre(ModFeatures.PULSING_ORE_END);
			}
			if (ModConfigs.bloxGen) {
				addOre(ModFeatures.BLOX_CLUSTER_END);
				addOre(ModFeatures.PRIZE_BLOX_CLUSTER_END);
			}
		}
		//Overworld
		else {
			if (ModConfigs.bloxGen) {
				addOre(ModFeatures.BLOX_CLUSTER);
				addOre(ModFeatures.PRIZE_BLOX_CLUSTER);
			}
			if (ModConfigs.oreGen) {
				addOre(ModFeatures.BETWIXT_ORE);
				addOre(ModFeatures.SINISTER_ORE);
				addOre(ModFeatures.STORMY_ORE);
				addOre(ModFeatures.WRITHING_ORE);

				addOre(ModFeatures.HUNGRY_ORE);
				addOre(ModFeatures.LIGHTNING_ORE);
				addOre(ModFeatures.LUCID_ORE);
				addOre(ModFeatures.REMEMBRANCE_ORE);
				addOre(ModFeatures.SOOTHING_ORE);
				addOre(ModFeatures.TRANQUILITY_ORE);
				addOre(ModFeatures.TWILIGHT_ORE);
				addOre(ModFeatures.WELLSPRING_ORE);

				if (event.getClimate().temperature >= 1.0) {
					addOre(ModFeatures.BLAZING_ORE_WARM);
				} else if (event.getClimate().temperature <= 0.3) {
					addOre(ModFeatures.FROST_ORE_COLD);
					addOre(ModFeatures.PULSING_ORE_COLD);
				} else if (event.getClimate().temperature <= 0.0) {
					addOre(ModFeatures.FROST_ORE_COLDER);
				}

				if (event.getClimate().downfall > 0.5) {
					addOre(ModFeatures.PULSING_ORE_WET);
					addOre(ModFeatures.STORMY_ORE_WET);
				}
			}
		}
	}
}
