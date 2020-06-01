package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.SilkTouchEnchantment;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.SetCount;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class LootTables extends BaseLootTables {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        bloxs();
        ores();
    }
    private void bloxs()
    {
        standardBlockLoot(ModBlocks.normalBlox.get());
        standardBlockLoot(ModBlocks.hardBlox.get());
        standardBlockLoot(ModBlocks.metalBlox.get());
        standardBlockLoot(ModBlocks.bounceBlox.get());
        standardBlockLoot(ModBlocks.dangerBlox.get());
        standardBlockLoot(ModBlocks.blastBlox.get());
        standardBlockLoot(ModBlocks.ghostBlox.get());
        standardBlockLoot(ModBlocks.magnetBlox.get());
        lootTables.put(ModBlocks.rarePrizeBlox.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.valorOrb.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.wisdomOrb.get())
                        .weight(1)).addEntry(ItemLootEntry.builder(ModItems.masterOrb.get()).weight(1)).addEntry(ItemLootEntry
                        .builder(ModItems.finalOrb.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.limitOrb.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_gem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazing_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_gem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilight_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_gem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormy_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_gem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucid_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_gem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.writhing_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_gem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frost_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_gem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazing_crystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilight_crystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormy_crystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucid_crystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.writhing_crystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frost_crystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_crystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.orichalcum.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.orichalcumplus.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.manifest_illusion.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.lost_illusion.get()).weight(1))
        ));
        lootTables.put(ModBlocks.prizeBlox.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.valorOrb.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.wisdomOrb.get())
                        .weight(1)).addEntry(ItemLootEntry.builder(ModItems.masterOrb.get()).weight(1)).addEntry(ItemLootEntry
                        .builder(ModItems.finalOrb.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.limitOrb.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazing_stone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilight_stone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormy_stone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucid_stone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.writhing_stone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frost_stone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_stone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.soothing_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazing_shard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilight_shard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormy_shard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucid_shard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.writhing_shard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frost_shard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_shard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.orichalcum.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.orichalcumplus.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.manifest_illusion.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.lost_illusion.get()).weight(1))
        ));
    }
    private void ores(){
        lootTables.put(ModBlocks.blazingOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
                //.addLootPool(ItemLootEntry.builder(ModBlocks.blazingOre.get()).acceptCondition(Silk))
        );

        lootTables.put(ModBlocks.blazingOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.lucidOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucid_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucid_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucid_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucid_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.lightningOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightning_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightning_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightning_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightning_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.betwixtOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.betwixt_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.writhingOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.writhingOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.writhingOreE.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.writhing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.wellspringOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.frostOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frost_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frost_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frost_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frost_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.hungryOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.hungry_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.twilightOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.twilightOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilight_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.wellspringOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.wellspring_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.soothingOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.soothing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.pulsingOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.pulsingOreE.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.pulsing_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.stormyOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormy_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormy_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormy_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormy_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.tranquilityOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquility_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.remembranceOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_crystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_gem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_shard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembrance_stone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );
    }

    void standardBlockLoot(Block block){
        lootTables.put(block, createStandardTable(block.getTranslationKey(), block));
    }
}