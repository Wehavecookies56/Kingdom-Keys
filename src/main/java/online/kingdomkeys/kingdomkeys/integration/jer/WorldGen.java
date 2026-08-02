package online.kingdomkeys.kingdomkeys.integration.jer;

import jeresources.api.IWorldGenRegistry;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.BiomeRestriction;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

// Feeds JER the ore distributions. Its own scanner only recognises a subset of world gen, so the
// mod's ores have to be declared here or they show up in JEI with no distribution graph at all.
//
// The numbers below have to be kept in step with data/kingdomkeys/worldgen/placed_feature - this is
// a description of the world gen for the tooltip, not the world gen itself.
public class WorldGen {

    IWorldGenRegistry registry;

    public WorldGen(IWorldGenRegistry registry) {
        this.registry = registry;
    }

    // The mod ships this one itself, see data/c/tags/worldgen/biome/wet_cold.json
    private static final TagKey<Biome> WET_COLD = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", "wet_cold"));

    private static Restriction
            overworld = new Restriction(createBiomeRestriction(BiomeTags.IS_OVERWORLD), DimensionRestriction.OVERWORLD),
            nether = new Restriction(createBiomeRestriction(BiomeTags.IS_NETHER), DimensionRestriction.NETHER),
            end = new Restriction(createBiomeRestriction(BiomeTags.IS_END), DimensionRestriction.END),
            hot = new Restriction(createBiomeRestriction(Tags.Biomes.IS_HOT_OVERWORLD), DimensionRestriction.OVERWORLD),
            cold = new Restriction(createBiomeRestriction(Tags.Biomes.IS_COLD_OVERWORLD), DimensionRestriction.OVERWORLD),
            wet = new Restriction(createBiomeRestriction(Tags.Biomes.IS_WET_OVERWORLD), DimensionRestriction.OVERWORLD),
            wetCold = new Restriction(createBiomeRestriction(WET_COLD));

    private static OreConfig
            BETWIXT_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
            BETWIXT_ORE_END_CONFIG = new OreConfig(10, 8, 0, 200, end),
            BLAZING_ORE_HOT_CONFIG = new OreConfig(4, 7, -64, 100, hot),
            BLAZING_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether),
            // The overworld blox clusters place on the WORLD_SURFACE heightmap rather than in a depth band, so the height here is just "roughly sea level" to give the graph something to draw.
            // Their count is a weighted list averaging about 0.42 a chunk, which an int can't carry - 1 is the closest this can get without claiming they're ten times commoner than they are.
            BLOX_CLUSTER_CONFIG = new OreConfig(10, 1, 64, 64, overworld),
            BLOX_CLUSTER_END_CONFIG = new OreConfig(6, 8, 0, 200, end),
            FROST_ORE_COLD_CONFIG = new OreConfig(4, 7, -64, 100, cold),
            HUNGRY_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            LIGHTNING_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            LUCID_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            // Same story as the blox clusters: surface placed, and a weighted count averaging ~0.14.
            PRIZE_BLOX_CLUSTER_CONFIG = new OreConfig(6, 1, 64, 64, overworld),
            PRIZE_BLOX_CLUSTER_END_CONFIG = new OreConfig(4, 6, 0, 200, end),
            PULSING_ORE_WET_COLD_CONFIG = new OreConfig(10, 7, -64, 20, wetCold),
            PULSING_ORE_END_CONFIG = new OreConfig(10, 8, 0, 200, end),
            REMEMBRANCE_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            SINISTER_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
            SOOTHING_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            STORMY_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
            STORMY_ORE_WET_CONFIG = new OreConfig(4, 7, -64, 100, wet),
            TRANQUILITY_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            TWILIGHT_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            TWILIGHT_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether),
            WELLSPRING_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
            WELLSPRING_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether),
            WRITHING_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
            WRITHING_ORE_END_CONFIG = new OreConfig(10, 8, 0, 200, end),
            WRITHING_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether)
    ;

    public void setup() {
        final List<Block> BLOX_LIST = Arrays.asList(ModBlocks.normalBlox.get(), ModBlocks.hardBlox.get(), ModBlocks.metalBlox.get(), ModBlocks.dangerBlox.get());
        final List<Block> PRIZE_BLOX_LIST = Arrays.asList(ModBlocks.prizeBlox.get(), ModBlocks.rarePrizeBlox.get(), ModBlocks.dangerBlox.get(), ModBlocks.blastBlox.get());

        registerSynthOre(ModBlocks.twilightOreN, TWILIGHT_ORE_NETHER_CONFIG, ModItems.twilight_shard, ModItems.twilight_stone, ModItems.twilight_gem, ModItems.twilight_crystal);
        registerSynthOre(ModBlocks.wellspringOreN, WELLSPRING_ORE_NETHER_CONFIG, ModItems.wellspring_shard, ModItems.wellspring_stone, ModItems.wellspring_gem, ModItems.wellspring_crystal);
        registerSynthOre(ModBlocks.writhingOreN, WRITHING_ORE_NETHER_CONFIG, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
        registerSynthOre(ModBlocks.blazingOreN, BLAZING_ORE_NETHER_CONFIG, ModItems.blazing_shard, ModItems.blazing_stone, ModItems.blazing_gem, ModItems.blazing_crystal);

        registerSynthOre(ModBlocks.betwixtOreE, BETWIXT_ORE_END_CONFIG, ModItems.betwixt_shard, ModItems.betwixt_stone, ModItems.betwixt_gem, ModItems.betwixt_crystal);
        registerSynthOre(ModBlocks.writhingOreE, WRITHING_ORE_END_CONFIG, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
        registerSynthOre(ModBlocks.pulsingOreE, PULSING_ORE_END_CONFIG, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
        registerBlox(BLOX_LIST, BLOX_CLUSTER_END_CONFIG);
        registerBlox(PRIZE_BLOX_LIST, PRIZE_BLOX_CLUSTER_END_CONFIG);

        registerBlox(BLOX_LIST, BLOX_CLUSTER_CONFIG);
        registerBlox(PRIZE_BLOX_LIST, PRIZE_BLOX_CLUSTER_CONFIG);
        registerSynthOre(ModBlocks.betwixtOre, BETWIXT_ORE_CONFIG, ModItems.betwixt_shard, ModItems.betwixt_stone, ModItems.betwixt_gem, ModItems.betwixt_crystal);
        registerSynthOre(ModBlocks.sinisterOre, SINISTER_ORE_CONFIG, ModItems.sinister_shard, ModItems.sinister_stone, ModItems.sinister_gem, ModItems.sinister_crystal);
        registerSynthOre(ModBlocks.stormyOre, STORMY_ORE_CONFIG, ModItems.stormy_shard, ModItems.stormy_stone, ModItems.stormy_gem, ModItems.stormy_crystal);
        registerSynthOre(ModBlocks.writhingOre, WRITHING_ORE_CONFIG, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
        registerSynthOre(ModBlocks.hungryOre, HUNGRY_ORE_CONFIG, ModItems.hungry_shard, ModItems.hungry_stone, ModItems.hungry_gem, ModItems.hungry_crystal);
        registerSynthOre(ModBlocks.lightningOre, LIGHTNING_ORE_CONFIG, ModItems.lightning_shard, ModItems.lightning_stone, ModItems.lightning_gem, ModItems.lightning_crystal);
        registerSynthOre(ModBlocks.lucidOre, LUCID_ORE_CONFIG, ModItems.lucid_shard, ModItems.lucid_stone, ModItems.lucid_gem, ModItems.lucid_crystal);
        registerSynthOre(ModBlocks.remembranceOre, REMEMBRANCE_ORE_CONFIG, ModItems.remembrance_shard, ModItems.remembrance_stone, ModItems.remembrance_gem, ModItems.remembrance_crystal);
        registerSynthOre(ModBlocks.soothingOre, SOOTHING_ORE_CONFIG, ModItems.soothing_shard, ModItems.soothing_stone, ModItems.soothing_gem, ModItems.soothing_crystal);
        registerSynthOre(ModBlocks.tranquilityOre, TRANQUILITY_ORE_CONFIG, ModItems.tranquility_shard, ModItems.tranquility_stone, ModItems.tranquility_gem, ModItems.tranquility_crystal);
        registerSynthOre(ModBlocks.twilightOre, TWILIGHT_ORE_CONFIG, ModItems.twilight_shard, ModItems.twilight_stone, ModItems.twilight_gem, ModItems.twilight_crystal);
        registerSynthOre(ModBlocks.wellspringOre, WELLSPRING_ORE_CONFIG, ModItems.wellspring_shard, ModItems.wellspring_stone, ModItems.wellspring_gem, ModItems.wellspring_crystal);
        registerSynthOre(ModBlocks.blazingOre, BLAZING_ORE_HOT_CONFIG, ModItems.blazing_shard, ModItems.blazing_stone, ModItems.blazing_gem, ModItems.blazing_crystal);
        registerSynthOre(ModBlocks.frostOre, FROST_ORE_COLD_CONFIG, ModItems.frost_shard, ModItems.frost_stone, ModItems.frost_gem, ModItems.frost_crystal);
        registerSynthOre(ModBlocks.pulsingOre, PULSING_ORE_WET_COLD_CONFIG, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
        registerSynthOre(ModBlocks.stormyOre, STORMY_ORE_WET_CONFIG, ModItems.stormy_shard, ModItems.stormy_stone, ModItems.stormy_gem, ModItems.stormy_crystal);
    }

    private LootDrop[] createOreDrops(Supplier<Item> shard, Supplier<Item> stone, Supplier<Item> gem, Supplier<Item> crystal) {
        return new LootDrop[]{ createWithFortune(shard.get(), 40), createWithFortune(stone.get(), 30), createWithFortune(gem.get(), 20), createWithFortune(crystal.get(), 10) };
    }

    //Hard coded min and max due to loot table function not working it seems
    private LootDrop createWithFortune(Item item, float chance) {
        LootDrop drop = new LootDrop(new ItemStack(item));
        drop.addConditional(Conditional.affectedByFortune);
        drop.chance = chance/100F;
        drop.minDrop = 0;
        drop.maxDrop = 4;
        return drop;
    }

    private LootDrop loot(Supplier<Item> item, int weight, int totalDrops) {
        return new LootDrop(item.get(), 0, 1, (100F / totalDrops) * weight);
    }

    //Can only display 8 drops
    private LootDrop[] createPrizeBloxDrops() {
        int drops = 31;
        return new LootDrop[] {
                loot(ModItems.fireSpell, 1, drops),
                loot(ModItems.blizzardSpell, 1, drops),
                loot(ModItems.waterSpell, 1, drops),
                loot(ModItems.thunderSpell, 1, drops),
                loot(ModItems.cureSpell, 1, drops),
                loot(ModItems.aeroSpell, 1, drops),
                loot(ModItems.magnetSpell, 1, drops),
                loot(ModItems.gravitySpell, 1, drops)
        };
    }

    //Can only display 8 drops
    private LootDrop[] createRarePrizeBloxDrops() {
        int drops = 27;
        return new LootDrop[] {
                loot(ModItems.valorOrb, 1, drops),
                loot(ModItems.wisdomOrb, 1, drops),
                loot(ModItems.masterOrb, 1, drops),
                loot(ModItems.finalOrb, 1, drops),
                loot(ModItems.limitOrb, 1, drops),
                loot(ModItems.orichalcum, 3, drops),
                loot(ModItems.orichalcumplus, 2, drops),
                loot(ModItems.manifest_illusion, 3, drops)
        };
    }

    private void registerSynthOre(Supplier<Block> block, OreConfig config, Supplier<Item> shard, Supplier<Item> stone, Supplier<Item> gem, Supplier<Item> crystal) {
        registry.register(
                new ItemStack(block.get()),
                new DistributionSquare(config.veinSize(), config.count(), config.minHeight(), config.maxHeight()),
                config.restriction(),
                true,
                createOreDrops(shard, stone, gem, crystal)
        );
    }

    private void registerBlox(List<Block> blocks, OreConfig config) {
        blocks.forEach(block -> {
            LootDrop[] drops = new LootDrop[] { new LootDrop(new ItemStack(block)) };
            boolean silkTouch = false;
            if (block == ModBlocks.prizeBlox.get()) {
                silkTouch = true;
                drops = createPrizeBloxDrops();
            }
            if (block == ModBlocks.rarePrizeBlox.get()) {
                silkTouch = true;
                drops = createRarePrizeBloxDrops();
            }
            registry.register(
                    new ItemStack(block),
                    new DistributionTriangular(config.veinSize(), config.count(), (config.maxHeight() - config.minHeight()) / 2, config.maxHeight() - config.minHeight()),
                    config.restriction(),
                    silkTouch,
                    drops
            );
        });
    }

    // Built once and reused. Every restriction above needs a biome lookup, and creating one of these
    // rebuilds the whole set of vanilla worldgen registries - doing it per call paid for that seven
    // times over during startup.
    private static HolderLookup.Provider biomeLookup;

    private static BiomeRestriction createBiomeRestriction(TagKey<Biome> tag) {
        if (biomeLookup == null) {
            biomeLookup = VanillaRegistries.createLookup();
        }

        List<Biome> biomes = new ArrayList<>();
        biomeLookup.lookupOrThrow(Registries.BIOME).listElements().forEach(biomeEntry -> {
            if (biomeEntry.is(tag)) {
                biomes.add(biomeEntry.value());
            }
        });

        if (!biomes.isEmpty()) {
            Biome first = biomes.remove(0);
            return new BiomeRestriction(Restriction.Type.WHITELIST, first, biomes.toArray(new Biome[0]));
        }
        return new BiomeRestriction();
    }

    public record OreConfig(int veinSize, int count, int minHeight, int maxHeight, Restriction restriction){}

}
