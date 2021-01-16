package online.kingdomkeys.kingdomkeys.world.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

import java.util.Arrays;
import java.util.List;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, KingdomKeys.MODID);

    public static final RegistryObject<Feature<BloxOreFeatureConfig>> BLOX = FEATURES.register("blox", () -> new BloxOreFeature(BloxOreFeatureConfig.CODEC));

    public static final IRuleTestType<?> OVERWORLD_GROUND =  IRuleTestType.func_237129_a_("multiple_block_match", MultipleBlockMatchRuleTest.CODEC);

    public static ConfiguredFeature<?, ?>
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


    private static ConfiguredFeature<?, ?> addBloxOreFeature(ResourceLocation registryName, List<BlockState> blocks, int size, int height, int count) {
        ConfiguredFeature<?, ?> feature = ModFeatures.BLOX.get().withConfiguration(new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.OVERWORLD, blocks, size)).range(height).square().func_242731_b(count);
        return register(registryName, feature);
    }

    private static ConfiguredFeature<?, ?> addEndBloxOreFeature(ResourceLocation registryName, List<BlockState> blocks, int size, int height, int count) {
        ConfiguredFeature<?, ?> feature = ModFeatures.BLOX.get().withConfiguration(new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.END, blocks, size)).range(height).square().func_242731_b(count);
        return register(registryName, feature);
    }

    private static ConfiguredFeature<?, ?> addOreFeature(ResourceLocation registryName, RuleTest fillerBlock, Block block, int size, int height, int count) {
        ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(fillerBlock, block.getDefaultState(), size)).range(height).square().func_242731_b(count);
        return register(registryName, feature);
    }

    private static ConfiguredFeature<?, ?> addEndOre(ResourceLocation registryName, Block block, int size, int height, int count) {
        final RuleTest END = new BlockMatchRuleTest(Blocks.END_STONE);
        return addOreFeature(registryName, END, block, size, height, count);
    }

    private static ConfiguredFeature<?, ?> addNetherOre(ResourceLocation registryName, Block block, int size, int height, int count) {
        return addOreFeature(registryName, OreFeatureConfig.FillerBlockType.NETHERRACK, block, size, height, count);
    }

    private static ConfiguredFeature<?, ?> addOverworldOre(ResourceLocation registryName, Block block, int size, int height, int count) {
        return addOreFeature(registryName, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, block, size, height, count);
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(ResourceLocation key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
    }

    public static void registerConfiguredFeatures() {
        final List<BlockState> BLOX_LIST = Arrays.asList(ModBlocks.normalBlox.get().getDefaultState(), ModBlocks.hardBlox.get().getDefaultState(), ModBlocks.metalBlox.get().getDefaultState(), ModBlocks.dangerBlox.get().getDefaultState());
        final List<BlockState> PRIZE_BLOX_LIST = Arrays.asList(ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.rarePrizeBlox.get().getDefaultState(), ModBlocks.rarePrizeBlox.get().getDefaultState(), ModBlocks.dangerBlox.get().getDefaultState(), ModBlocks.blastBlox.get().getDefaultState());

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
