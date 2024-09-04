//package online.kingdomkeys.kingdomkeys.integration.jer;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import jeresources.api.IWorldGenRegistry;
//import jeresources.api.conditionals.Conditional;
//import jeresources.api.distributions.DistributionSquare;
//import jeresources.api.distributions.DistributionTriangular;
//import jeresources.api.drop.LootDrop;
//import jeresources.api.restrictions.BiomeRestriction;
//import jeresources.api.restrictions.DimensionRestriction;
//import jeresources.api.restrictions.Restriction;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.data.registries.VanillaRegistries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.BiomeTags;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.common.Tags;
//import net.minecraftforge.registries.RegistryObject;
//import online.kingdomkeys.kingdomkeys.block.ModBlocks;
//import online.kingdomkeys.kingdomkeys.item.ModItems;
//
//public class WorldGen {
//
//    IWorldGenRegistry registry;
//
//    public WorldGen(IWorldGenRegistry registry) {
//        this.registry = registry;
//    }
//
//    private static Restriction
//            overworld = new Restriction(createBiomeRestriction(BiomeTags.IS_OVERWORLD), DimensionRestriction.OVERWORLD),
//            nether = new Restriction(createBiomeRestriction(BiomeTags.IS_NETHER), DimensionRestriction.NETHER),
//            end = new Restriction(createBiomeRestriction(BiomeTags.IS_END), DimensionRestriction.END),
//            hot = new Restriction(createBiomeRestriction(Tags.Biomes.IS_HOT_OVERWORLD), DimensionRestriction.OVERWORLD),
//            cold = new Restriction(createBiomeRestriction(Tags.Biomes.IS_COLD_OVERWORLD), DimensionRestriction.OVERWORLD),
//            wet = new Restriction(createBiomeRestriction(Tags.Biomes.IS_WET_OVERWORLD), DimensionRestriction.OVERWORLD),
//            wetCold = new Restriction(createBiomeRestriction(TagKey.create(Registries.BIOME, new ResourceLocation("forge", "wet_cold"))));
//    private static OreConfig
//            BETWIXT_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
//            BLAZING_ORE_HOT_CONFIG = new OreConfig(4, 7, -64, 100, hot),
//            BLAZING_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether),
//            BLOX_CLUSTER_CONFIG = new OreConfig(10, 4, 64, 64, overworld),
//            BLOX_CLUSTER_END_CONFIG = new OreConfig(6, 8, 0, 200, end),
//            FROST_ORE_COLD_CONFIG = new OreConfig(4, 7, -64, 100, cold),
//            HUNGRY_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            LIGHTNING_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            LUCID_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            PRIZE_BLOX_CLUSTER_CONFIG = new OreConfig(4, 2, 64, 64, overworld),
//            PRIZE_BLOX_CLUSTER_END_CONFIG = new OreConfig(4, 6, 0, 200, end),
//            PULSING_ORE_WET_COLD_CONFIG = new OreConfig(10, 7, -64, 20, wetCold),
//            PULSING_ORE_END_CONFIG = new OreConfig(10, 8, 0, 200, end),
//            REMEMBRANCE_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            SINISTER_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
//            SOOTHING_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            STORMY_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
//            STORMY_ORE_WET_CONFIG = new OreConfig(4, 7, -64, 20, wet),
//            TRANQUILITY_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            TWILIGHT_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            TWILIGHT_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether),
//            WELLSPRING_ORE_CONFIG = new OreConfig(4, 7, -64, 100, overworld),
//            WELLSPRING_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether),
//            WRITHING_ORE_CONFIG = new OreConfig(4, 7, -64, 20, overworld),
//            WRITHING_ORE_END_CONFIG = new OreConfig(10, 8, 0, 200, end),
//            WRITHING_ORE_NETHER_CONFIG = new OreConfig(10, 8, 0, 100, nether)
//    ;
//
//    public void setup() {
//        final List<Block> BLOX_LIST = Arrays.asList(ModBlocks.normalBlox.get(), ModBlocks.hardBlox.get(), ModBlocks.metalBlox.get(), ModBlocks.dangerBlox.get());
//        final List<Block> PRIZE_BLOX_LIST = Arrays.asList(ModBlocks.prizeBlox.get(), ModBlocks.rarePrizeBlox.get(), ModBlocks.dangerBlox.get(), ModBlocks.blastBlox.get());
//
//        registerSynthOre(ModBlocks.twilightOreN, TWILIGHT_ORE_NETHER_CONFIG, ModItems.twilight_shard, ModItems.twilight_stone, ModItems.twilight_gem, ModItems.twilight_crystal);
//        registerSynthOre(ModBlocks.wellspringOreN, WELLSPRING_ORE_NETHER_CONFIG, ModItems.wellspring_shard, ModItems.wellspring_stone, ModItems.wellspring_gem, ModItems.wellspring_crystal);
//        registerSynthOre(ModBlocks.writhingOreN, WRITHING_ORE_NETHER_CONFIG, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
//        registerSynthOre(ModBlocks.blazingOreN, BLAZING_ORE_NETHER_CONFIG, ModItems.blazing_shard, ModItems.blazing_stone, ModItems.blazing_gem, ModItems.blazing_crystal);
//
//        registerSynthOre(ModBlocks.writhingOreE, WRITHING_ORE_END_CONFIG,ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
//        registerSynthOre(ModBlocks.pulsingOreE, PULSING_ORE_END_CONFIG, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
//        registerBlox(BLOX_LIST, BLOX_CLUSTER_END_CONFIG);
//        registerBlox(PRIZE_BLOX_LIST, PRIZE_BLOX_CLUSTER_END_CONFIG);
//
//        registerBlox(BLOX_LIST, BLOX_CLUSTER_CONFIG);
//        registerBlox(PRIZE_BLOX_LIST, PRIZE_BLOX_CLUSTER_CONFIG);
//        registerSynthOre(ModBlocks.betwixtOre, BETWIXT_ORE_CONFIG, ModItems.betwixt_shard, ModItems.betwixt_stone, ModItems.betwixt_gem, ModItems.betwixt_crystal);
//        registerSynthOre(ModBlocks.sinisterOre, SINISTER_ORE_CONFIG, ModItems.sinister_shard, ModItems.sinister_stone, ModItems.sinister_gem, ModItems.sinister_crystal);
//        registerSynthOre(ModBlocks.stormyOre, STORMY_ORE_CONFIG, ModItems.stormy_shard, ModItems.stormy_stone, ModItems.stormy_gem, ModItems.stormy_crystal);
//        registerSynthOre(ModBlocks.writhingOre, WRITHING_ORE_CONFIG, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
//        registerSynthOre(ModBlocks.hungryOre, HUNGRY_ORE_CONFIG, ModItems.hungry_shard, ModItems.hungry_stone, ModItems.hungry_gem, ModItems.hungry_crystal);
//        registerSynthOre(ModBlocks.lightningOre, LIGHTNING_ORE_CONFIG, ModItems.lightning_shard, ModItems.lightning_stone, ModItems.lightning_gem, ModItems.lightning_crystal);
//        registerSynthOre(ModBlocks.lucidOre, LUCID_ORE_CONFIG, ModItems.lucid_shard, ModItems.lucid_stone, ModItems.lucid_gem, ModItems.lucid_crystal);
//        registerSynthOre(ModBlocks.remembranceOre, REMEMBRANCE_ORE_CONFIG, ModItems.remembrance_shard, ModItems.remembrance_stone, ModItems.remembrance_gem, ModItems.remembrance_crystal);
//        registerSynthOre(ModBlocks.soothingOre, SOOTHING_ORE_CONFIG, ModItems.soothing_shard, ModItems.soothing_stone, ModItems.soothing_gem, ModItems.soothing_crystal);
//        registerSynthOre(ModBlocks.tranquilityOre, TRANQUILITY_ORE_CONFIG, ModItems.tranquility_shard, ModItems.tranquility_stone, ModItems.tranquility_gem, ModItems.tranquility_crystal);
//        registerSynthOre(ModBlocks.twilightOre, TWILIGHT_ORE_CONFIG, ModItems.twilight_shard, ModItems.twilight_stone, ModItems.twilight_gem, ModItems.twilight_crystal);
//        registerSynthOre(ModBlocks.wellspringOre, WELLSPRING_ORE_CONFIG, ModItems.wellspring_shard, ModItems.wellspring_stone, ModItems.wellspring_gem, ModItems.wellspring_crystal);
//        registerSynthOre(ModBlocks.blazingOre, BLAZING_ORE_HOT_CONFIG, ModItems.blazing_shard, ModItems.blazing_stone, ModItems.blazing_gem, ModItems.blazing_crystal);
//        registerSynthOre(ModBlocks.frostOre, FROST_ORE_COLD_CONFIG, ModItems.frost_shard, ModItems.frost_stone, ModItems.frost_gem, ModItems.frost_crystal);
//        registerSynthOre(ModBlocks.pulsingOre, PULSING_ORE_WET_COLD_CONFIG, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
//        registerSynthOre(ModBlocks.stormyOre, STORMY_ORE_WET_CONFIG, ModItems.stormy_shard, ModItems.stormy_stone, ModItems.stormy_gem, ModItems.stormy_crystal);
//    }
//
//    private LootDrop[] createOreDrops(RegistryObject<Item> shard, RegistryObject<Item> stone, RegistryObject<Item> gem, RegistryObject<Item> crystal) {
//        return new LootDrop[]{ createWithFortune(shard.get(), 40), createWithFortune(stone.get(), 30), createWithFortune(gem.get(), 20), createWithFortune(crystal.get(), 10) };
//    }
//
//    //Hard coded min and max due to loot table function not working it seems
//    private LootDrop createWithFortune(Item item, float chance) {
//        LootDrop drop = new LootDrop(new ItemStack(item));
//        drop.addConditional(Conditional.affectedByFortune);
//        drop.chance = chance/100F;
//        drop.minDrop = 0;
//        drop.maxDrop = 4;
//        return drop;
//    }
//
//    private LootDrop loot(RegistryObject<Item> item, int weight, int totalDrops) {
//        return new LootDrop(item.get(), 0, 1, (100F / totalDrops) * weight);
//    }
//
//    //Can only display 8 drops
//    private LootDrop[] createPrizeBloxDrops() {
//        int drops = 31;
//        return new LootDrop[] {
//                loot(ModItems.fireSpell, 1, drops),
//                loot(ModItems.blizzardSpell, 1, drops),
//                loot(ModItems.waterSpell, 1, drops),
//                loot(ModItems.thunderSpell, 1, drops),
//                loot(ModItems.cureSpell, 1, drops),
//                loot(ModItems.aeroSpell, 1, drops),
//                loot(ModItems.magnetSpell, 1, drops),
//                loot(ModItems.gravitySpell, 1, drops)
//                //loot(ModItems.reflectSpell, 1, drops),
//                //loot(ModItems.stopSpell, 1, drops),
//                //loot(ModItems.betwixt_shard, 1, drops),
//                //loot(ModItems.sinister_shard, 1, drops),
//                //loot(ModItems.stormy_shard, 1, drops),
//                //loot(ModItems.writhing_shard, 1, drops),
//                //loot(ModItems.pulsing_shard, 1, drops),
//                //loot(ModItems.betwixt_stone, 1, drops),
//                //loot(ModItems.sinister_stone, 1, drops),
//                //loot(ModItems.stormy_stone, 1, drops),
//                //loot(ModItems.writhing_stone, 1, drops),
//                //loot(ModItems.pulsing_stone, 1, drops),
//                //loot(ModItems.fluorite, 4, drops),
//                //loot(ModItems.damascus, 3, drops),
//                //loot(ModItems.adamantite, 2, drops),
//                //loot(ModItems.electrum, 1, drops)
//        };
//    }
//
//    //Can only display 8 drops
//    private LootDrop[] createRarePrizeBloxDrops() {
//        int drops = 27;
//        return new LootDrop[] {
//                loot(ModItems.valorOrb, 1, drops),
//                loot(ModItems.wisdomOrb, 1, drops),
//                loot(ModItems.masterOrb, 1, drops),
//                loot(ModItems.finalOrb, 1, drops),
//                loot(ModItems.limitOrb, 1, drops),
//                //loot(ModItems.betwixt_gem, 1, drops),
//                //loot(ModItems.sinister_gem, 1, drops),
//                //loot(ModItems.stormy_gem, 1, drops)
//                //loot(ModItems.writhing_gem, 1, drops),
//                //loot(ModItems.pulsing_gem, 1, drops),
//                //loot(ModItems.betwixt_crystal, 1, drops),
//                //loot(ModItems.sinister_crystal, 1, drops),
//                //loot(ModItems.stormy_crystal, 1, drops),
//                //loot(ModItems.writhing_crystal, 1, drops),
//                //loot(ModItems.pulsing_crystal, 1, drops),
//                loot(ModItems.orichalcum, 3, drops),
//                loot(ModItems.orichalcumplus, 2, drops),
//                loot(ModItems.manifest_illusion, 3, drops)
//                //loot(ModItems.lost_illusion, 2, drops),
//                //loot(ModItems.electrum, 2, drops)
//        };
//    }
//
//    enum ClimateType {
//        DOWNFALL, TEMPERATURE
//    }
//
//    private void registerSynthOre(RegistryObject<Block> block, OreConfig config, RegistryObject<Item> shard, RegistryObject<Item> stone, RegistryObject<Item> gem, RegistryObject<Item> crystal) {
//        registry.register(
//                new ItemStack(block.get()),
//                new DistributionSquare(config.veinSize(), config.count(), config.minHeight(), config.maxHeight()),
//                config.restriction(),
//                true,
//                createOreDrops(shard, stone, gem, crystal)
//        );
//    }
//
//    private void registerBlox(List<Block> blocks, OreConfig config) {
//        blocks.forEach(block -> {
//            LootDrop[] drops = new LootDrop[] { new LootDrop(new ItemStack(block)) };
//            boolean silkTouch = false;
//            if (block == ModBlocks.prizeBlox.get()) {
//                silkTouch = true;
//                drops = createPrizeBloxDrops();
//            }
//            if (block == ModBlocks.rarePrizeBlox.get()) {
//                silkTouch = true;
//                drops = createRarePrizeBloxDrops();
//            }
//            registry.register(
//                    new ItemStack(block),
//                    new DistributionTriangular(config.veinSize(), config.count(), (config.maxHeight() - config.minHeight()) / 2, config.maxHeight() - config.minHeight()),
//                    config.restriction(),
//                    silkTouch,
//                    drops
//            );
//        });
//    }
//
//    private static BiomeRestriction createBiomeRestriction(TagKey<Biome> tag) {
//        List<Biome> biomes = new ArrayList<>();
//        VanillaRegistries.createLookup().lookupOrThrow(Registries.BIOME).listElements().forEach((biome_entry) -> {
//            if (biome_entry.is(tag)) {
//                biomes.add(biome_entry.value());
//            }
//        });
//        if (!biomes.isEmpty()) {
//            Biome biome1 = biomes.get(0);
//            biomes.remove(0);
//            return new BiomeRestriction(Restriction.Type.WHITELIST, biome1, biomes.toArray(new Biome[0]));
//        } else {
//            return new BiomeRestriction();
//        }
//    }
//
//    public record OreConfig(int veinSize, int count, int minHeight, int maxHeight, Restriction restriction){}
//
//}
