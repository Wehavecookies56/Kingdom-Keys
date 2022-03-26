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

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, KingdomKeys.MODID);

    public static final RegistryObject<Feature<BloxOreFeatureConfig>> BLOX = FEATURES.register("blox", () -> new BloxOreFeature(BloxOreFeatureConfig.CODEC));

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
            STORMY_ORE_WET;


    private static Holder<PlacedFeature> addBloxOreFeature(ResourceLocation registryName, List<BlockState> blocks, int size, int height, int count) {
        Holder<ConfiguredFeature<BloxOreFeatureConfig, ?>> feature = registerCF(registryName, new ConfiguredFeature<>(ModFeatures.BLOX.get(), new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.OVERWORLD, blocks, size)));
        return registerPF(registryName, feature, CountPlacement.of(count), InSquarePlacement.spread(), BiomeFilter.biome(), HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(height)));
}

    private static Holder<PlacedFeature> addEndBloxOreFeature(ResourceLocation registryName, List<BlockState> blocks, int size, int height, int count) {
        Holder<ConfiguredFeature<BloxOreFeatureConfig, ?>> feature = registerCF(registryName, new ConfiguredFeature<>(ModFeatures.BLOX.get(), new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.END, blocks, size)));
        return registerPF(registryName, feature, CountPlacement.of(count), InSquarePlacement.spread(), BiomeFilter.biome(), HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(height)));    }

    private static Holder<PlacedFeature> addOreFeature(ResourceLocation registryName, RuleTest fillerBlock, Block block, int size, int height, int count) {
        Holder<ConfiguredFeature<OreConfiguration, ?>> feature = registerCF(registryName, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(fillerBlock, block.defaultBlockState(), size)));
        return registerPF(registryName, feature, CountPlacement.of(count), InSquarePlacement.spread(), BiomeFilter.biome(), HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(height)));
    }

    private static Holder<PlacedFeature> addEndOre(ResourceLocation registryName, Block block, int size, int height, int count) {
        final RuleTest END = new BlockMatchTest(Blocks.END_STONE);
        return addOreFeature(registryName, END, block, size, height, count);
    }

    private static Holder<PlacedFeature> addNetherOre(ResourceLocation registryName, Block block, int size, int height, int count) {
        return addOreFeature(registryName, OreFeatures.NETHERRACK, block, size, height, count);
    }

    private static Holder<PlacedFeature> addOverworldOre(ResourceLocation registryName, Block block, int size, int height, int count) {
        return addOreFeature(registryName, OreFeatures.NATURAL_STONE, block, size, height, count);
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

        TWILIGHT_ORE_NETHER = addNetherOre(rl("twilight_ore_nether"), ModBlocks.twilightOreN.get(), 10, 100, 8);
        WELLSPRING_ORE_NETHER = addNetherOre(rl("wellspring_ore_nether"), ModBlocks.wellspringOreN.get(), 10, 100, 8);
        WRITHING_ORE_NETHER = addNetherOre(rl("writhing_ore_nether"), ModBlocks.writhingOreN.get(), 10, 100, 8);
        BLAZING_ORE_NETHER = addNetherOre(rl("blazing_ore_nether"), ModBlocks.blazingOreN.get(), 10, 100, 8);

        WRITHING_ORE_END = addEndOre(rl("writhing_ore_end"), ModBlocks.writhingOreE.get(), 6, 200, 8);
        PULSING_ORE_END = addEndOre(rl("pulsing_ore_end"), ModBlocks.pulsingOreE.get(), 6, 200, 8);
        BLOX_CLUSTER_END = addEndBloxOreFeature(rl("blox_cluster_end"), BLOX_LIST, 6, 216, 8);
        PRIZE_BLOX_CLUSTER_END = addEndBloxOreFeature(rl("prize_blox_cluster_end"), PRIZE_BLOX_LIST, 2, 216, 8);

        BLOX_CLUSTER = addBloxOreFeature(rl("blox_cluster"), BLOX_LIST, 10, 256, 10);
        PRIZE_BLOX_CLUSTER = addBloxOreFeature(rl("prize_blox_cluster"), PRIZE_BLOX_LIST, 6, 256, 4);
        BETWIXT_ORE = addOverworldOre(rl("betwixt_ore"), ModBlocks.betwixtOre.get(), 4, 20, 7);
        SINISTER_ORE = addOverworldOre(rl("sinister_ore"), ModBlocks.sinisterOre.get(), 4, 20, 7);
        STORMY_ORE = addOverworldOre(rl("stormy_ore"), ModBlocks.stormyOre.get(), 4, 20, 7);
        WRITHING_ORE = addOverworldOre(rl("writhing_ore"), ModBlocks.writhingOre.get(), 4, 20, 7);
        HUNGRY_ORE = addOverworldOre(rl("hungry_ore"), ModBlocks.hungryOre.get(), 4, 100, 7);
        LIGHTNING_ORE = addOverworldOre(rl("lightning_ore"), ModBlocks.lightningOre.get(), 4, 100, 7);
        LUCID_ORE = addOverworldOre(rl("lucid_ore"), ModBlocks.lucidOre.get(), 4, 100, 7);
        REMEMBRANCE_ORE = addOverworldOre(rl("remembrance_ore"), ModBlocks.remembranceOre.get(), 4, 100, 7);
        SOOTHING_ORE = addOverworldOre(rl("soothing_ore"), ModBlocks.soothingOre.get(), 4, 100, 7);
        TRANQUILITY_ORE = addOverworldOre(rl("tranquility_ore"), ModBlocks.tranquilityOre.get(), 4, 100, 7);
        TWILIGHT_ORE = addOverworldOre(rl("twilight_ore"), ModBlocks.twilightOre.get(), 4, 100, 7);
        WELLSPRING_ORE = addOverworldOre(rl("wellspring_ore"), ModBlocks.wellspringOre.get(), 4, 100, 7);
        BLAZING_ORE_WARM = addOverworldOre(rl("blazing_ore_warm"), ModBlocks.blazingOre.get(), 4, 100, 7);
        FROST_ORE_COLD = addOverworldOre(rl("frost_ore_cold"), ModBlocks.frostOre.get(), 4, 100, 7);
        PULSING_ORE_COLD = addOverworldOre(rl("pulsing_ore_cold"), ModBlocks.pulsingOre.get(), 4, 20, 7);
        FROST_ORE_COLDER = addOverworldOre(rl("forst_ore_colder"), ModBlocks.frostOre.get(), 4, 20, 7);
        PULSING_ORE_WET = addOverworldOre(rl("pulsing_ore_wet"), ModBlocks.pulsingOre.get(), 4, 20, 7);
        STORMY_ORE_WET = addOverworldOre(rl("stormy_ore_wet"), ModBlocks.stormyOre.get(), 4, 100, 7);
    }

    private static ResourceLocation rl(String string) {
        return new ResourceLocation(KingdomKeys.MODID, string);
    }

    public static void register() {}

}
