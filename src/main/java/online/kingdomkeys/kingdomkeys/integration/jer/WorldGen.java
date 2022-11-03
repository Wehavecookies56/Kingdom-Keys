package online.kingdomkeys.kingdomkeys.integration.jer;

import jeresources.api.IWorldGenRegistry;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.BiomeRestriction;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.api.util.BiomeHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;
import online.kingdomkeys.kingdomkeys.world.features.OreConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class WorldGen {

    IWorldGenRegistry registry;

    public WorldGen(IWorldGenRegistry registry) {
        this.registry = registry;
    }

    Restriction warm = new Restriction(createClimateBiomeRestriction(1.0F, ClimateType.TEMPERATURE, (a, b) -> b >= a), DimensionRestriction.OVERWORLD);
    Restriction cold = new Restriction(createClimateBiomeRestriction(0.3F, ClimateType.TEMPERATURE, (a, b) -> b <= a), DimensionRestriction.OVERWORLD);
    Restriction colder = new Restriction(createClimateBiomeRestriction(0.0F, ClimateType.TEMPERATURE, (a, b) -> b <= a), DimensionRestriction.OVERWORLD);
    Restriction wet = new Restriction(createClimateBiomeRestriction(0.5F, ClimateType.DOWNFALL, (a, b) -> b > a), DimensionRestriction.OVERWORLD);

    public void setup() {
        final List<Block> BLOX_LIST = Arrays.asList(ModBlocks.normalBlox.get(), ModBlocks.hardBlox.get(), ModBlocks.metalBlox.get(), ModBlocks.dangerBlox.get());
        final List<Block> PRIZE_BLOX_LIST = Arrays.asList(ModBlocks.prizeBlox.get(), ModBlocks.rarePrizeBlox.get(), ModBlocks.dangerBlox.get(), ModBlocks.blastBlox.get());

        registerSynthOre(ModBlocks.twilightOreN, ModFeatures.TWILIGHT_ORE_NETHER_CONFIG, Restriction.NETHER, ModItems.twilight_shard, ModItems.twilight_stone, ModItems.twilight_gem, ModItems.twilight_crystal);
        registerSynthOre(ModBlocks.wellspringOreN, ModFeatures.WELLSPRING_ORE_NETHER_CONFIG, Restriction.NETHER, ModItems.wellspring_shard, ModItems.wellspring_stone, ModItems.wellspring_gem, ModItems.wellspring_crystal);
        registerSynthOre(ModBlocks.writhingOreN, ModFeatures.WRITHING_ORE_NETHER_CONFIG, Restriction.NETHER, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
        registerSynthOre(ModBlocks.blazingOreN, ModFeatures.BLAZING_ORE_NETHER_CONFIG, Restriction.NETHER, ModItems.blazing_shard, ModItems.blazing_stone, ModItems.blazing_gem, ModItems.blazing_crystal);

        registerSynthOre(ModBlocks.writhingOreE, ModFeatures.WRITHING_ORE_END_CONFIG, Restriction.END, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
        registerSynthOre(ModBlocks.pulsingOreE, ModFeatures.PULSING_ORE_END_CONFIG, Restriction.END, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
        registerBlox(BLOX_LIST, ModFeatures.BLOX_CLUSTER_END_CONFIG, Restriction.END);
        registerBlox(PRIZE_BLOX_LIST, ModFeatures.PRIZE_BLOX_CLUSTER_END_CONFIG, Restriction.END);

        registerBlox(BLOX_LIST, ModFeatures.BLOX_CLUSTER_CONFIG, Restriction.OVERWORLD);
        registerBlox(PRIZE_BLOX_LIST, ModFeatures.PRIZE_BLOX_CLUSTER_CONFIG, Restriction.OVERWORLD);
        registerSynthOre(ModBlocks.betwixtOre, ModFeatures.BETWIXT_ORE_CONFIG, Restriction.OVERWORLD, ModItems.betwixt_shard, ModItems.betwixt_stone, ModItems.betwixt_gem, ModItems.betwixt_crystal);
        registerSynthOre(ModBlocks.sinisterOre, ModFeatures.SINISTER_ORE_CONFIG, Restriction.OVERWORLD, ModItems.sinister_shard, ModItems.sinister_stone, ModItems.sinister_gem, ModItems.sinister_crystal);
        registerSynthOre(ModBlocks.stormyOre, ModFeatures.STORMY_ORE_CONFIG, Restriction.OVERWORLD, ModItems.stormy_shard, ModItems.stormy_stone, ModItems.stormy_gem, ModItems.stormy_crystal);
        registerSynthOre(ModBlocks.writhingOre, ModFeatures.WRITHING_ORE_CONFIG, Restriction.OVERWORLD, ModItems.writhing_shard, ModItems.writhing_stone, ModItems.writhing_gem, ModItems.writhing_crystal);
        registerSynthOre(ModBlocks.hungryOre, ModFeatures.HUNGRY_ORE_CONFIG, Restriction.OVERWORLD, ModItems.hungry_shard, ModItems.hungry_stone, ModItems.hungry_gem, ModItems.hungry_crystal);
        registerSynthOre(ModBlocks.lightningOre, ModFeatures.LIGHTNING_ORE_CONFIG, Restriction.OVERWORLD, ModItems.lightning_shard, ModItems.lightning_stone, ModItems.lightning_gem, ModItems.lightning_crystal);
        registerSynthOre(ModBlocks.lucidOre, ModFeatures.LUCID_ORE_CONFIG, Restriction.OVERWORLD, ModItems.lucid_shard, ModItems.lucid_stone, ModItems.lucid_gem, ModItems.lucid_crystal);
        registerSynthOre(ModBlocks.remembranceOre, ModFeatures.REMEMBRANCE_ORE_CONFIG, Restriction.OVERWORLD, ModItems.remembrance_shard, ModItems.remembrance_stone, ModItems.remembrance_gem, ModItems.remembrance_crystal);
        registerSynthOre(ModBlocks.soothingOre, ModFeatures.SOOTHING_ORE_CONFIG, Restriction.OVERWORLD, ModItems.soothing_shard, ModItems.soothing_stone, ModItems.soothing_gem, ModItems.soothing_crystal);
        registerSynthOre(ModBlocks.tranquilityOre, ModFeatures.TRANQUILITY_ORE_CONFIG, Restriction.OVERWORLD, ModItems.tranquility_shard, ModItems.tranquility_stone, ModItems.tranquility_gem, ModItems.tranquility_crystal);
        registerSynthOre(ModBlocks.twilightOre, ModFeatures.TWILIGHT_ORE_CONFIG, Restriction.OVERWORLD, ModItems.twilight_shard, ModItems.twilight_stone, ModItems.twilight_gem, ModItems.twilight_crystal);
        registerSynthOre(ModBlocks.wellspringOre, ModFeatures.WELLSPRING_ORE_CONFIG, Restriction.OVERWORLD, ModItems.wellspring_shard, ModItems.wellspring_stone, ModItems.wellspring_gem, ModItems.wellspring_crystal);
        registerSynthOre(ModBlocks.blazingOre, ModFeatures.BLAZING_ORE_WARM_CONFIG, warm, ModItems.blazing_shard, ModItems.blazing_stone, ModItems.blazing_gem, ModItems.blazing_crystal);
        registerSynthOre(ModBlocks.frostOre, ModFeatures.FROST_ORE_COLD_CONFIG, cold, ModItems.frost_shard, ModItems.frost_stone, ModItems.frost_gem, ModItems.frost_crystal);
        registerSynthOre(ModBlocks.pulsingOre, ModFeatures.PULSING_ORE_COLD_CONFIG, cold, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
        registerSynthOre(ModBlocks.frostOre, ModFeatures.FROST_ORE_COLDER_CONFIG, colder, ModItems.frost_shard, ModItems.frost_stone, ModItems.frost_gem, ModItems.frost_crystal);
        registerSynthOre(ModBlocks.pulsingOre, ModFeatures.PULSING_ORE_WET_CONFIG, wet, ModItems.pulsing_shard, ModItems.pulsing_stone, ModItems.pulsing_gem, ModItems.pulsing_crystal);
        registerSynthOre(ModBlocks.stormyOre, ModFeatures.STORMY_ORE_WET_CONFIG, wet, ModItems.stormy_shard, ModItems.stormy_stone, ModItems.stormy_gem, ModItems.stormy_crystal);
    }

    private LootDrop[] createOreDrops(RegistryObject<Item> shard, RegistryObject<Item> stone, RegistryObject<Item> gem, RegistryObject<Item> crystal) {
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

    private LootDrop loot(RegistryObject<Item> item, int weight, int totalDrops) {
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
                //loot(ModItems.reflectSpell, 1, drops),
                //loot(ModItems.stopSpell, 1, drops),
                //loot(ModItems.betwixt_shard, 1, drops),
                //loot(ModItems.sinister_shard, 1, drops),
                //loot(ModItems.stormy_shard, 1, drops),
                //loot(ModItems.writhing_shard, 1, drops),
                //loot(ModItems.pulsing_shard, 1, drops),
                //loot(ModItems.betwixt_stone, 1, drops),
                //loot(ModItems.sinister_stone, 1, drops),
                //loot(ModItems.stormy_stone, 1, drops),
                //loot(ModItems.writhing_stone, 1, drops),
                //loot(ModItems.pulsing_stone, 1, drops),
                //loot(ModItems.fluorite, 4, drops),
                //loot(ModItems.damascus, 3, drops),
                //loot(ModItems.adamantite, 2, drops),
                //loot(ModItems.electrum, 1, drops)
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
                //loot(ModItems.betwixt_gem, 1, drops),
                //loot(ModItems.sinister_gem, 1, drops),
                //loot(ModItems.stormy_gem, 1, drops)
                //loot(ModItems.writhing_gem, 1, drops),
                //loot(ModItems.pulsing_gem, 1, drops),
                //loot(ModItems.betwixt_crystal, 1, drops),
                //loot(ModItems.sinister_crystal, 1, drops),
                //loot(ModItems.stormy_crystal, 1, drops),
                //loot(ModItems.writhing_crystal, 1, drops),
                //loot(ModItems.pulsing_crystal, 1, drops),
                loot(ModItems.orichalcum, 3, drops),
                loot(ModItems.orichalcumplus, 2, drops),
                loot(ModItems.manifest_illusion, 3, drops)
                //loot(ModItems.lost_illusion, 2, drops),
                //loot(ModItems.electrum, 2, drops)
        };
    }

    enum ClimateType {
        DOWNFALL, TEMPERATURE
    }

    private void registerSynthOre(RegistryObject<Block> block, OreConfig config, Restriction restriction, RegistryObject<Item> shard, RegistryObject<Item> stone, RegistryObject<Item> gem, RegistryObject<Item> crystal) {
        if (config.values.enabled()) {
            registry.register(
                    new ItemStack(block.get()),
                    new DistributionTriangular(config.values.veinSize(), config.values.count(), (config.values.maxHeight() - config.values.minHeight()) / 2, config.values.maxHeight() - config.values.minHeight()),
                    restriction,
                    true,
                    createOreDrops(shard, stone, gem, crystal)
            );
        }
    }

    private void registerBlox(List<Block> blocks, OreConfig config, Restriction restriction) {
        if (config.values.enabled()) {
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
                        new DistributionTriangular(config.values.veinSize(), config.values.count(), (config.values.maxHeight() - config.values.minHeight()) / 2, config.values.maxHeight() - config.values.minHeight()),
                        restriction,
                        silkTouch,
                        drops
                );
            });
        }
    }

    private BiomeRestriction createClimateBiomeRestriction(float input, ClimateType type, BiFunction<Float, Float, Boolean> comparison) {
        List<Biome> biomes = BiomeHelper.getAllBiomes();
        Biome biome1 = null;
        List<Biome> allowedBiomes = new ArrayList<>();
        for (Biome biome : biomes) {
            float typeInput = 0;
            switch (type) {
                case DOWNFALL -> typeInput = biome.getDownfall();
                case TEMPERATURE -> typeInput = biome.getBaseTemperature();
            }
            if (comparison.apply(input, typeInput)) {
                if (biome1 == null) biome1 = biome;
                else allowedBiomes.add(biome);
            }
        }
        return new BiomeRestriction(Restriction.Type.WHITELIST, biome1, allowedBiomes.toArray(new Biome[0]));
    }

}
