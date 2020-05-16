package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
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
        // blox
        standardBlockLoot(ModBlocks.normalBlox);
        standardBlockLoot(ModBlocks.hardBlox);
        standardBlockLoot(ModBlocks.metalBlox);
        standardBlockLoot(ModBlocks.bounceBlox);
        standardBlockLoot(ModBlocks.dangerBlox);
        standardBlockLoot(ModBlocks.blastBlox);
        standardBlockLoot(ModBlocks.ghostBlox);
        lootTables.put(ModBlocks.rarePrizeBlox, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(Blocks.STONE).weight(1)).addEntry(ItemLootEntry.builder(Blocks.SAND)
                .weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3))))));


        ores();
        structures();
    }
    private void structures(){
        lootTables.put(Blocks.CHEST, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(Blocks.STONE).weight(1)).addEntry(ItemLootEntry.builder(Blocks.SAND)
                        .weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3))))));

    }


    private void ores(){
        lootTables.put(ModBlocks.blazingOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.blazingOreN, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.lucidOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.lightningOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.denseOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.darkOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.darkOreN, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.darkOreE, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.energyOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.frostOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.serenityOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.twilightOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.twilightOreN, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.energyOreN, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.brightOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.powerOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.powerOreE, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.stormyOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.tranquilOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.remembranceOre, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceCrystal).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3))).acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceGem).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceShard).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceStone).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );
    }

    void standardBlockLoot(Block block){
        lootTables.put(block, createStandardTable(block.getTranslationKey(), block));
    }
}