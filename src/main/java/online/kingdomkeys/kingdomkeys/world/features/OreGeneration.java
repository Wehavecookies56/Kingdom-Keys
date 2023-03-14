package online.kingdomkeys.kingdomkeys.world.features;

import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class OreGeneration {

	//local variable to reduce parameters
	private static BiomeGenerationSettingsBuilder settingsBuilder;

	public static void addOre(Holder<PlacedFeature> feature) {
		settingsBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
	}

	/** //TODO Ore Generation all done in JSON now pretty much
	 *
	public static void generateOre(BiomeLoadingEvent event) {
		settingsBuilder = event.getGeneration();

		if (event.getName() != null) {
			if (BiomeDictionary.hasAnyType(ResourceKey.create(Registry.BIOME_REGISTRY, event.getName()))) {
				Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(ResourceKey.create(Registry.BIOME_REGISTRY, event.getName()));

				//Nether
				if (types.contains(BiomeDictionary.Type.NETHER)) {
					if (ModConfigs.oreGen) {
						if (ModFeatures.TWILIGHT_ORE_NETHER_CONFIG.values.enabled())
							addOre(ModFeatures.TWILIGHT_ORE_NETHER);
						if (ModFeatures.WELLSPRING_ORE_NETHER_CONFIG.values.enabled())
							addOre(ModFeatures.WELLSPRING_ORE_NETHER);
						if (ModFeatures.WRITHING_ORE_NETHER_CONFIG.values.enabled())
							addOre(ModFeatures.WRITHING_ORE_NETHER);
						if (ModFeatures.BLAZING_ORE_NETHER_CONFIG.values.enabled())
							addOre(ModFeatures.BLAZING_ORE_NETHER);
					}
				}
				//End
				else if (types.contains(BiomeDictionary.Type.END)) {
					if (ModConfigs.oreGen) {
						if (ModFeatures.WRITHING_ORE_END_CONFIG.values.enabled())
							addOre(ModFeatures.WRITHING_ORE_END);
						if (ModFeatures.PULSING_ORE_END_CONFIG.values.enabled())
							addOre(ModFeatures.PULSING_ORE_END);
					}
					if (ModConfigs.bloxGen) {
						if (ModFeatures.BLOX_CLUSTER_END_CONFIG.values.enabled())
							addOre(ModFeatures.BLOX_CLUSTER_END);
						if (ModFeatures.PRIZE_BLOX_CLUSTER_END_CONFIG.values.enabled())
							addOre(ModFeatures.PRIZE_BLOX_CLUSTER_END);
					}
				}
				//Overworld
				else if (types.contains(BiomeDictionary.Type.OVERWORLD)) {
					if (ModConfigs.bloxGen) {
						if (ModFeatures.BLOX_CLUSTER_CONFIG.values.enabled())
							addOre(ModFeatures.BLOX_CLUSTER);
						if (ModFeatures.PRIZE_BLOX_CLUSTER_CONFIG.values.enabled())
							addOre(ModFeatures.PRIZE_BLOX_CLUSTER);
					}
					if (ModConfigs.oreGen) {
						if (ModFeatures.BETWIXT_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.BETWIXT_ORE);
						if (ModFeatures.BETWIXT_ORE_DEEPSLATE_CONFIG.values.enabled())
							addOre(ModFeatures.BETWIXT_ORE_DEEPSLATE);

						if (ModFeatures.SINISTER_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.SINISTER_ORE);
						if (ModFeatures.SINISTER_ORE_DEEPSLATE_CONFIG.values.enabled())
							addOre(ModFeatures.SINISTER_ORE_DEEPSLATE);

						if (ModFeatures.STORMY_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.STORMY_ORE);
						if (ModFeatures.STORMY_ORE_DEEPSLATE_CONFIG.values.enabled())
							addOre(ModFeatures.STORMY_ORE_DEEPSLATE);

						if (ModFeatures.WRITHING_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.WRITHING_ORE);
						if (ModFeatures.WRITHING_ORE_DEEPSLATE_CONFIG.values.enabled())
							addOre(ModFeatures.WRITHING_ORE_DEEPSLATE);

						if (ModFeatures.HUNGRY_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.HUNGRY_ORE);

						if (ModFeatures.LIGHTNING_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.LIGHTNING_ORE);

						if (ModFeatures.LUCID_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.LUCID_ORE);

						if (ModFeatures.REMEMBRANCE_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.REMEMBRANCE_ORE);
						if (ModFeatures.SOOTHING_ORE_CONFIG.values.enabled())

							addOre(ModFeatures.SOOTHING_ORE);
						if (ModFeatures.SOOTHING_ORE_DEEPSLATE_CONFIG.values.enabled())
							addOre(ModFeatures.SOOTHING_ORE_DEEPSLATE);

						if (ModFeatures.TRANQUILITY_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.TRANQUILITY_ORE);
						if (ModFeatures.TWILIGHT_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.TWILIGHT_ORE);
						if (ModFeatures.TWILIGHT_ORE_DEEPSLATE_CONFIG.values.enabled())
							addOre(ModFeatures.TWILIGHT_ORE_DEEPSLATE);

						if (ModFeatures.WELLSPRING_ORE_CONFIG.values.enabled())
							addOre(ModFeatures.WELLSPRING_ORE);

						if (types.contains(BiomeDictionary.Type.HOT)) {
							if (ModFeatures.BLAZING_ORE_WARM_CONFIG.values.enabled())
								addOre(ModFeatures.BLAZING_ORE_WARM);
							if (ModFeatures.BLAZING_ORE_DEEPSLATE_CONFIG.values.enabled())
								addOre(ModFeatures.BLAZING_ORE_DEEPSLATE);
						}
						if (types.contains(BiomeDictionary.Type.COLD)) {
							if (ModFeatures.FROST_ORE_COLD_CONFIG.values.enabled())
								addOre(ModFeatures.FROST_ORE_COLD);
							if (ModFeatures.FROST_ORE_DEEPSLATE_CONFIG.values.enabled())
								addOre(ModFeatures.FROST_ORE_DEEPSLATE);
							if (ModFeatures.PULSING_ORE_COLD_CONFIG.values.enabled())
								addOre(ModFeatures.PULSING_ORE_COLD);

							if (types.contains(BiomeDictionary.Type.SNOWY)) {
								if (ModFeatures.FROST_ORE_COLDER_CONFIG.values.enabled())
									addOre(ModFeatures.FROST_ORE_COLDER);
							}

							if (types.contains(BiomeDictionary.Type.WET)) {
								if (ModFeatures.PULSING_ORE_WET_CONFIG.values.enabled())
									addOre(ModFeatures.PULSING_ORE_WET);
								if (ModFeatures.PULSING_ORE_DEEPSLATE_CONFIG.values.enabled())
									addOre(ModFeatures.PULSING_ORE_DEEPSLATE);

								if (ModFeatures.STORMY_ORE_WET_CONFIG.values.enabled())
									addOre(ModFeatures.STORMY_ORE_WET);
							}
						}
					}
				}
			}
		}
	}
	 **/
}
