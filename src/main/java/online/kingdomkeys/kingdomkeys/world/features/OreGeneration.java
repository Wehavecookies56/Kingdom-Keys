package online.kingdomkeys.kingdomkeys.world.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

import java.util.Arrays;
import java.util.List;

public class OreGeneration {

	//local variable to reduce parameters
	private static BiomeGenerationSettingsBuilder settingsBuilder;

	private static void addBloxOreFeature(List<BlockState> blocks, int size, int height, int count) {
		settingsBuilder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.BLOX.get().withConfiguration(new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.OVERWORLD, blocks, size)).range(height).square().func_242731_b(count));
	}

	private static void addEndBloxOreFeature(List<BlockState> blocks, int size, int height, int count) {
		settingsBuilder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.BLOX.get().withConfiguration(new BloxOreFeatureConfig(BloxOreFeatureConfig.FillerBlockType.END, blocks, size)).range(height).square().func_242731_b(count));
	}

	private static void addOreFeature(RuleTest fillerBlock, Block block, int size, int height, int count) {
		settingsBuilder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(fillerBlock, block.getDefaultState(), size)).range(height).square().func_242731_b(count));
	}

	private static void addEndOre(Block block, int size, int height, int count) {
		final RuleTest END = new BlockMatchRuleTest(Blocks.END_STONE);
		addOreFeature(END, block, size, height, count);
	}

	private static void addNetherOre(Block block, int size, int height, int count) {
		addOreFeature(FillerBlockType.NETHERRACK, block, size, height, count);
	}

	private static void addOverworldOre(Block block, int size, int height, int count) {
		addOreFeature(FillerBlockType.BASE_STONE_OVERWORLD, block, size, height, count);
	}

	public static void generateOre(BiomeLoadingEvent event) {
		settingsBuilder = event.getGeneration();
		final List<BlockState> BLOX_LIST = Arrays.asList(ModBlocks.normalBlox.get().getDefaultState(), ModBlocks.hardBlox.get().getDefaultState(), ModBlocks.metalBlox.get().getDefaultState(), ModBlocks.dangerBlox.get().getDefaultState());
		final List<BlockState> PRIZE_BLOX_LIST = Arrays.asList(ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.prizeBlox.get().getDefaultState(), ModBlocks.rarePrizeBlox.get().getDefaultState(), ModBlocks.rarePrizeBlox.get().getDefaultState(), ModBlocks.dangerBlox.get().getDefaultState(), ModBlocks.blastBlox.get().getDefaultState());

		//Nether
		if (event.getCategory().equals(Biome.Category.NETHER)) {
			addNetherOre(ModBlocks.twilightOreN.get(), 10, 100, 8);
			addNetherOre(ModBlocks.wellspringOreN.get(), 10, 100, 8);
			addNetherOre(ModBlocks.writhingOreN.get(), 10, 100, 8);
			addNetherOre(ModBlocks.blazingOreN.get(), 10, 100, 8);
		}
		//End
		else if (event.getCategory().equals(Biome.Category.THEEND)) {
			addEndOre(ModBlocks.writhingOreE.get(), 6, 200, 8);
			addEndOre(ModBlocks.pulsingOreE.get(), 6, 200, 8);
			addEndBloxOreFeature(BLOX_LIST, 6, 216, 8);
			addEndBloxOreFeature(PRIZE_BLOX_LIST, 2, 216, 8);
		}
		//Overworld
		else {
			addBloxOreFeature(BLOX_LIST, 10, 256, 10);
			addBloxOreFeature(PRIZE_BLOX_LIST, 6, 256, 4);

			addOverworldOre(ModBlocks.betwixtOre.get(), 4, 20, 7);
			addOverworldOre(ModBlocks.sinisterOre.get(), 4, 20, 7);
			addOverworldOre(ModBlocks.stormyOre.get(), 4, 20, 7);
			addOverworldOre(ModBlocks.writhingOre.get(), 4, 20, 7);

			addOverworldOre(ModBlocks.hungryOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.lightningOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.lucidOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.remembranceOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.soothingOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.tranquilityOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.twilightOre.get(), 4, 100, 7);
			addOverworldOre(ModBlocks.wellspringOre.get(), 4, 100, 7);

			if (event.getClimate().temperature >= 1.0) {
				addOverworldOre(ModBlocks.blazingOre.get(), 4, 100, 7);
			} else if (event.getClimate().temperature <= 0.3) {
				addOverworldOre(ModBlocks.frostOre.get(), 4, 100, 7);
				addOverworldOre(ModBlocks.pulsingOre.get(), 4, 20, 7);
			} else if (event.getClimate().temperature <= 0.0) {
				addOverworldOre(ModBlocks.frostOre.get(), 4, 20, 7);
			}

			if (event.getClimate().downfall > 0.5) {
				addOverworldOre(ModBlocks.pulsingOre.get(), 4, 20, 7);
				addOverworldOre(ModBlocks.stormyOre.get(), 4, 100, 7);
			}
		}
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, KingdomKeys.MODID + ":" + key, configuredFeature);
	}
}
