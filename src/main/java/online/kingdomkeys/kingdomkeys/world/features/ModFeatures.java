package online.kingdomkeys.kingdomkeys.world.features;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, KingdomKeys.MODID);

    public static final RegistryObject<Feature<BloxOreFeatureConfig>> BLOX = FEATURES.register("blox", () -> new BloxOreFeature(BloxOreFeatureConfig.CODEC));

    public static OreConfig
            //Nether
            TWILIGHT_ORE_NETHER_CONFIG = new OreConfig("twilight_ore_nether", new OreConfig.Values(true, 10, 0, 100, 8)),
            WELLSPRING_ORE_NETHER_CONFIG = new OreConfig("wellspring_ore_nether", new OreConfig.Values(true, 10, 0, 100, 8)),
            WRITHING_ORE_NETHER_CONFIG = new OreConfig("writhing_ore_nether", new OreConfig.Values(true, 10, 0, 100, 8)),
            BLAZING_ORE_NETHER_CONFIG = new OreConfig("blazing_ore_nether", new OreConfig.Values(true, 10, 0, 100, 8)),
            //End
            WRITHING_ORE_END_CONFIG = new OreConfig("writhing_ore_end", new OreConfig.Values(true, 6, 0, 200, 8)),
            PULSING_ORE_END_CONFIG = new OreConfig("pulsing_ore_end", new OreConfig.Values(true, 6, 0, 200, 8)),
            BLOX_CLUSTER_END_CONFIG = new OreConfig("blox_cluster_end", new OreConfig.Values(true, 6, 0, 216, 8)),
            PRIZE_BLOX_CLUSTER_END_CONFIG = new OreConfig("prize_blox_cluster_end", new OreConfig.Values(true, 2, 0, 216, 8)),
            //Overworld
            BLOX_CLUSTER_CONFIG = new OreConfig("blox_cluster", new OreConfig.Values(true, 10, 0, 256, 10)),
            PRIZE_BLOX_CLUSTER_CONFIG = new OreConfig("prize_blox_cluster", new OreConfig.Values(true, 6, 0, 256, 4)),
            BETWIXT_ORE_CONFIG = new OreConfig("betwixt_ore", new OreConfig.Values(true, 4, 0, 20, 7)),
            SINISTER_ORE_CONFIG = new OreConfig("sinister_ore", new OreConfig.Values(true, 4, 0, 20, 7)),
            STORMY_ORE_CONFIG = new OreConfig("stormy_ore", new OreConfig.Values(true, 4, 0, 20, 7)),
            WRITHING_ORE_CONFIG = new OreConfig("writhing_ore", new OreConfig.Values(true, 4, 0, 20, 7)),
            HUNGRY_ORE_CONFIG = new OreConfig("hungry_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            LIGHTNING_ORE_CONFIG = new OreConfig("lightning_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            LUCID_ORE_CONFIG = new OreConfig("lucid_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            REMEMBRANCE_ORE_CONFIG = new OreConfig("remembrance_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            SOOTHING_ORE_CONFIG = new OreConfig("soothing_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            TRANQUILITY_ORE_CONFIG = new OreConfig("tranquility_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            TWILIGHT_ORE_CONFIG = new OreConfig("twilight_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            WELLSPRING_ORE_CONFIG = new OreConfig("wellspring_ore", new OreConfig.Values(true, 4, 0, 100, 7)),
            BLAZING_ORE_WARM_CONFIG = new OreConfig("blazing_ore_warm", new OreConfig.Values(true, 4, 0, 100, 7)),
            FROST_ORE_COLD_CONFIG = new OreConfig("frost_ore_cold", new OreConfig.Values(true, 4, 0, 100, 7)),
            PULSING_ORE_COLD_CONFIG = new OreConfig("pulsing_ore_cold", new OreConfig.Values(true, 4, 0, 20, 7)),
            FROST_ORE_COLDER_CONFIG = new OreConfig("frost_ore_colder", new OreConfig.Values(true, 4, 0, 20, 7)),
            PULSING_ORE_WET_CONFIG = new OreConfig("pulsing_ore_wet", new OreConfig.Values(true, 4, 0, 20, 7)),
            STORMY_ORE_WET_CONFIG = new OreConfig("stormy_ore_wet", new OreConfig.Values(true, 4, 0, 100, 7)),
    		BLAZING_ORE_DEEPSLATE_CONFIG = new OreConfig("blazing_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            BETWIXT_ORE_DEEPSLATE_CONFIG = new OreConfig("betwixt_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            FROST_ORE_DEEPSLATE_CONFIG = new OreConfig("frost_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            PULSING_ORE_DEEPSLATE_CONFIG = new OreConfig("pulsing_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            SINISTER_ORE_DEEPSLATE_CONFIG = new OreConfig("sinister_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            SOOTHING_ORE_DEEPSLATE_CONFIG = new OreConfig("soothing_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            STORMY_ORE_DEEPSLATE_CONFIG = new OreConfig("stormy_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            TWILIGHT_ORE_DEEPSLATE_CONFIG = new OreConfig("twilight_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7)),
            WRITHING_ORE_DEEPSLATE_CONFIG = new OreConfig("writhing_ore_deepslate", new OreConfig.Values(true, 4, -64, 0, 7))
    ;

    public static Holder<PlacedFeature>
            //Nether
            TWILIGHT_ORE_NETHER,
            WELLSPRING_ORE_NETHER,
            WRITHING_ORE_NETHER,
            BLAZING_ORE_NETHER,
            //End
            WRITHING_ORE_END,
            PULSING_ORE_END,
            BLOX_CLUSTER_END,
            PRIZE_BLOX_CLUSTER_END,
            //Overworld
            BLOX_CLUSTER,
            PRIZE_BLOX_CLUSTER,
            BETWIXT_ORE,
            SINISTER_ORE,
            STORMY_ORE,
            WRITHING_ORE,
            HUNGRY_ORE,
            LIGHTNING_ORE,
            LUCID_ORE,
            REMEMBRANCE_ORE,
            SOOTHING_ORE,
            TRANQUILITY_ORE,
            TWILIGHT_ORE,
            WELLSPRING_ORE,
            BLAZING_ORE_WARM,
            FROST_ORE_COLD,
            PULSING_ORE_COLD,
            FROST_ORE_COLDER,
            PULSING_ORE_WET,
            STORMY_ORE_WET,
            BLAZING_ORE_DEEPSLATE,
            BETWIXT_ORE_DEEPSLATE,
            FROST_ORE_DEEPSLATE,
            PULSING_ORE_DEEPSLATE,
            SINISTER_ORE_DEEPSLATE,
            SOOTHING_ORE_DEEPSLATE,
            STORMY_ORE_DEEPSLATE,
            TWILIGHT_ORE_DEEPSLATE,
            WRITHING_ORE_DEEPSLATE
            ;
    


    /* gonna remove soon anyway
    private static Holder<PlacedFeature> addBloxOreFeature(ResourceLocation registryName, List<BlockState> blocks, OreConfig config) {
        Holder<ConfiguredFeature<BloxOreFeatureConfig, ?>> feature = registerCF(registryName, new ConfiguredFeature<>(ModFeatures.BLOX.get(), new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.OVERWORLD, blocks, config.values.veinSize())));
        return registerPF(registryName, feature, CountPlacement.of(config.values.count()), InSquarePlacement.spread(), BiomeFilter.biome(), HeightRangePlacement.triangle(VerticalAnchor.absolute(config.values.minHeight()), VerticalAnchor.absolute((config.values.maxHeight() - config.values.minHeight())/2)));
}

    private static Holder<PlacedFeature> addEndBloxOreFeature(ResourceLocation registryName, List<BlockState> blocks, OreConfig config) {
        Holder<ConfiguredFeature<BloxOreFeatureConfig, ?>> feature = registerCF(registryName, new ConfiguredFeature<>(ModFeatures.BLOX.get(), new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.END, blocks, config.values.veinSize())));
        return registerPF(registryName, feature, CountPlacement.of(config.values.count()), InSquarePlacement.spread(), BiomeFilter.biome(), HeightRangePlacement.triangle(VerticalAnchor.absolute(config.values.minHeight()), VerticalAnchor.absolute((config.values.maxHeight() - config.values.minHeight())/2)));
    }
     */

    private static Holder<PlacedFeature> addOreFeature(ResourceLocation registryName, RuleTest fillerBlock, Block block, OreConfig config) {
        Holder<ConfiguredFeature<OreConfiguration, ?>> feature = registerCF(registryName, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(fillerBlock, block.defaultBlockState(), config.values.veinSize())));
        return registerPF(registryName, feature, CountPlacement.of(config.values.count()), InSquarePlacement.spread(), BiomeFilter.biome(), HeightRangePlacement.triangle(VerticalAnchor.absolute(config.values.minHeight()), VerticalAnchor.absolute((config.values.maxHeight() - config.values.minHeight())/2)));
    }

    private static Holder<PlacedFeature> addEndOre(ResourceLocation registryName, Block block, OreConfig config) {
        final RuleTest END = new BlockMatchTest(Blocks.END_STONE);
        return addOreFeature(registryName, END, block, config);
    }

    private static Holder<PlacedFeature> addNetherOre(ResourceLocation registryName, Block block, OreConfig config) {
        return addOreFeature(registryName, OreFeatures.NETHERRACK, block, config);
    }

    private static Holder<PlacedFeature> addOverworldOre(ResourceLocation registryName, Block block, OreConfig config) {
        return addOreFeature(registryName, OreFeatures.NATURAL_STONE, block, config);
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> registerCF(ResourceLocation key, ConfiguredFeature<FC, ?> configuredFeature) {
        return FeatureUtils.register(key.toString(), configuredFeature.feature(), configuredFeature.config());
    }
    private static Holder<PlacedFeature> registerPF(ResourceLocation key, Holder<? extends ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(key.toString(), Holder.hackyErase(configuredFeature), placementModifiers);
    }

    public static void registerConfiguredFeatures() {
        final List<BlockState> BLOX_LIST = Arrays.asList(ModBlocks.normalBlox.get().defaultBlockState(), ModBlocks.hardBlox.get().defaultBlockState(), ModBlocks.metalBlox.get().defaultBlockState(), ModBlocks.dangerBlox.get().defaultBlockState());
        final List<BlockState> PRIZE_BLOX_LIST = Arrays.asList(ModBlocks.prizeBlox.get().defaultBlockState(), ModBlocks.prizeBlox.get().defaultBlockState(), ModBlocks.prizeBlox.get().defaultBlockState(), ModBlocks.prizeBlox.get().defaultBlockState(), ModBlocks.rarePrizeBlox.get().defaultBlockState(), ModBlocks.rarePrizeBlox.get().defaultBlockState(), ModBlocks.dangerBlox.get().defaultBlockState(), ModBlocks.blastBlox.get().defaultBlockState());

        TWILIGHT_ORE_NETHER_CONFIG.setFromConfig(ModConfigs.twilightOreNetherGen);
        WELLSPRING_ORE_NETHER_CONFIG.setFromConfig(ModConfigs.wellspringOreNetherGen);
        WRITHING_ORE_NETHER_CONFIG.setFromConfig(ModConfigs.writhingOreNetherGen);
        BLAZING_ORE_NETHER_CONFIG.setFromConfig(ModConfigs.blazingOreNetherGen);

        WRITHING_ORE_END_CONFIG.setFromConfig(ModConfigs.writhingOreEndGen);
        PULSING_ORE_END_CONFIG.setFromConfig(ModConfigs.pulsingOreEndGen);
        BLOX_CLUSTER_END_CONFIG.setFromConfig(ModConfigs.bloxClusterEndGen);
        PRIZE_BLOX_CLUSTER_END_CONFIG.setFromConfig(ModConfigs.prizeBloxClusterEndGen);

        BLOX_CLUSTER_CONFIG.setFromConfig(ModConfigs.bloxClusterGen);
        PRIZE_BLOX_CLUSTER_CONFIG.setFromConfig(ModConfigs.prizeBloxClusterGen);
        BETWIXT_ORE_CONFIG.setFromConfig(ModConfigs.betwixtOreGen);
        SINISTER_ORE_CONFIG.setFromConfig(ModConfigs.sinisterOreGen);
        STORMY_ORE_CONFIG.setFromConfig(ModConfigs.stormyOreGen);
        WRITHING_ORE_CONFIG.setFromConfig(ModConfigs.writhingOreGen);
        HUNGRY_ORE_CONFIG.setFromConfig(ModConfigs.hungryOreGen);
        LIGHTNING_ORE_CONFIG.setFromConfig(ModConfigs.lightningOreGen);
        LUCID_ORE_CONFIG.setFromConfig(ModConfigs.lucidOreGen);
        REMEMBRANCE_ORE_CONFIG.setFromConfig(ModConfigs.remembranceOreGen);
        SOOTHING_ORE_CONFIG.setFromConfig(ModConfigs.soothingOreGen);
        TRANQUILITY_ORE_CONFIG.setFromConfig(ModConfigs.tranquilityOreGen);
        TWILIGHT_ORE_CONFIG.setFromConfig(ModConfigs.twilightOreGen);
        WELLSPRING_ORE_CONFIG.setFromConfig(ModConfigs.wellspringOreGen);
        BLAZING_ORE_WARM_CONFIG.setFromConfig(ModConfigs.blazingOreWarmGen);
        FROST_ORE_COLD_CONFIG.setFromConfig(ModConfigs.frostOreColdGen);
        PULSING_ORE_COLD_CONFIG.setFromConfig(ModConfigs.pulsingOreColdGen);
        FROST_ORE_COLDER_CONFIG.setFromConfig(ModConfigs.frostOreColderGen);
        PULSING_ORE_WET_CONFIG.setFromConfig(ModConfigs.pulsingOreWetGen);
        STORMY_ORE_WET_CONFIG.setFromConfig(ModConfigs.stormyOreWetGen);
        
        BLAZING_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.blazingOreDeepslateGen);
        BETWIXT_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.betwixtOreDeepslateGen);
        FROST_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.frostOreDeepslateGen);
        PULSING_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.pulsingOreDeepslateGen);
        SINISTER_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.sinisterOreDeepslateGen);
        SOOTHING_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.soothingOreDeepslateGen);
        STORMY_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.stormyOreDeepslateGen);
        TWILIGHT_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.twilightOreDeepslateGen);
        WRITHING_ORE_DEEPSLATE_CONFIG.setFromConfig(ModConfigs.writhingOreDeepslateGen);

        TWILIGHT_ORE_NETHER = addNetherOre(rl("twilight_ore_nether"), ModBlocks.twilightOreN.get(), TWILIGHT_ORE_NETHER_CONFIG);
        WELLSPRING_ORE_NETHER = addNetherOre(rl("wellspring_ore_nether"), ModBlocks.wellspringOreN.get(), WELLSPRING_ORE_NETHER_CONFIG);
        WRITHING_ORE_NETHER = addNetherOre(rl("writhing_ore_nether"), ModBlocks.writhingOreN.get(), WRITHING_ORE_NETHER_CONFIG);
        BLAZING_ORE_NETHER = addNetherOre(rl("blazing_ore_nether"), ModBlocks.blazingOreN.get(), BLAZING_ORE_NETHER_CONFIG);

        WRITHING_ORE_END = addEndOre(rl("writhing_ore_end"), ModBlocks.writhingOreE.get(), WRITHING_ORE_END_CONFIG);
        PULSING_ORE_END = addEndOre(rl("pulsing_ore_end"), ModBlocks.pulsingOreE.get(), PULSING_ORE_END_CONFIG);
        //BLOX_CLUSTER_END = addEndBloxOreFeature(rl("blox_cluster_end"), BLOX_LIST, BLOX_CLUSTER_END_CONFIG);
        //PRIZE_BLOX_CLUSTER_END = addEndBloxOreFeature(rl("prize_blox_cluster_end"), PRIZE_BLOX_LIST, PRIZE_BLOX_CLUSTER_END_CONFIG);

        //BLOX_CLUSTER = addBloxOreFeature(rl("blox_cluster"), BLOX_LIST, BLOX_CLUSTER_CONFIG);
        //PRIZE_BLOX_CLUSTER = addBloxOreFeature(rl("prize_blox_cluster"), PRIZE_BLOX_LIST, PRIZE_BLOX_CLUSTER_CONFIG);
        BETWIXT_ORE = addOverworldOre(rl("betwixt_ore"), ModBlocks.betwixtOre.get(), BETWIXT_ORE_CONFIG);
        SINISTER_ORE = addOverworldOre(rl("sinister_ore"), ModBlocks.sinisterOre.get(), SINISTER_ORE_CONFIG);
        STORMY_ORE = addOverworldOre(rl("stormy_ore"), ModBlocks.stormyOre.get(), STORMY_ORE_CONFIG);
        WRITHING_ORE = addOverworldOre(rl("writhing_ore"), ModBlocks.writhingOre.get(), WRITHING_ORE_CONFIG);
        HUNGRY_ORE = addOverworldOre(rl("hungry_ore"), ModBlocks.hungryOre.get(), HUNGRY_ORE_CONFIG);
        LIGHTNING_ORE = addOverworldOre(rl("lightning_ore"), ModBlocks.lightningOre.get(), LIGHTNING_ORE_CONFIG);
        LUCID_ORE = addOverworldOre(rl("lucid_ore"), ModBlocks.lucidOre.get(), LUCID_ORE_CONFIG);
        REMEMBRANCE_ORE = addOverworldOre(rl("remembrance_ore"), ModBlocks.remembranceOre.get(), REMEMBRANCE_ORE_CONFIG);
        SOOTHING_ORE = addOverworldOre(rl("soothing_ore"), ModBlocks.soothingOre.get(), SOOTHING_ORE_CONFIG);
        TRANQUILITY_ORE = addOverworldOre(rl("tranquility_ore"), ModBlocks.tranquilityOre.get(), TRANQUILITY_ORE_CONFIG);
        TWILIGHT_ORE = addOverworldOre(rl("twilight_ore"), ModBlocks.twilightOre.get(), TWILIGHT_ORE_CONFIG);
        WELLSPRING_ORE = addOverworldOre(rl("wellspring_ore"), ModBlocks.wellspringOre.get(), WELLSPRING_ORE_CONFIG);
        BLAZING_ORE_WARM = addOverworldOre(rl("blazing_ore_warm"), ModBlocks.blazingOre.get(), BLAZING_ORE_WARM_CONFIG);
        FROST_ORE_COLD = addOverworldOre(rl("frost_ore_cold"), ModBlocks.frostOre.get(), FROST_ORE_COLD_CONFIG);
        PULSING_ORE_COLD = addOverworldOre(rl("pulsing_ore_cold"), ModBlocks.pulsingOre.get(), PULSING_ORE_COLD_CONFIG);
        FROST_ORE_COLDER = addOverworldOre(rl("frost_ore_colder"), ModBlocks.frostOre.get(), FROST_ORE_COLDER_CONFIG);
        PULSING_ORE_WET = addOverworldOre(rl("pulsing_ore_wet"), ModBlocks.pulsingOre.get(), PULSING_ORE_WET_CONFIG);
        STORMY_ORE_WET = addOverworldOre(rl("stormy_ore_wet"), ModBlocks.stormyOre.get(), STORMY_ORE_WET_CONFIG);
        
        BLAZING_ORE_DEEPSLATE = addOverworldOre(rl("blazing_ore_deepslate"), ModBlocks.blazingOreD.get(), BLAZING_ORE_DEEPSLATE_CONFIG);
        BETWIXT_ORE_DEEPSLATE = addOverworldOre(rl("betwixt_ore_deepslate"), ModBlocks.betwixtOreD.get(), BETWIXT_ORE_DEEPSLATE_CONFIG);
        FROST_ORE_DEEPSLATE = addOverworldOre(rl("frost_ore_deepslate"), ModBlocks.frostOreD.get(), FROST_ORE_DEEPSLATE_CONFIG);
        PULSING_ORE_DEEPSLATE = addOverworldOre(rl("pulsing_ore_deepslate"), ModBlocks.pulsingOreD.get(), PULSING_ORE_DEEPSLATE_CONFIG);
        SINISTER_ORE_DEEPSLATE = addOverworldOre(rl("sinister_ore_deepslate"), ModBlocks.sinisterOreD.get(), SINISTER_ORE_DEEPSLATE_CONFIG);
        SOOTHING_ORE_DEEPSLATE = addOverworldOre(rl("soothing_ore_deepslate"), ModBlocks.soothingOreD.get(), SOOTHING_ORE_DEEPSLATE_CONFIG);
        STORMY_ORE_DEEPSLATE = addOverworldOre(rl("stormy_ore_deepslate"), ModBlocks.stormyOreD.get(), STORMY_ORE_DEEPSLATE_CONFIG);
        TWILIGHT_ORE_DEEPSLATE = addOverworldOre(rl("twilight_ore_deepslate"), ModBlocks.twilightOreD.get(), TWILIGHT_ORE_DEEPSLATE_CONFIG);
        WRITHING_ORE_DEEPSLATE = addOverworldOre(rl("writhing_ore_deepslate"), ModBlocks.writhingOreD.get(), WRITHING_ORE_DEEPSLATE_CONFIG);
    }

    private static ResourceLocation rl(String string) {
        return new ResourceLocation(KingdomKeys.MODID, string);
    }

}
