package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.advancements.criterion.ItemPredicate;
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
                .addEntry(ItemLootEntry.builder(ModItems.brightGem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazingGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityGem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilightGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilGem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormyGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceGem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucidGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerGem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.darkGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseGem.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frostGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyGem.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazingCrystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilightCrystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormyCrystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucidCrystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.darkCrystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frostCrystal.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyCrystal.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.orichalcum.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.orichalcumPlus.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.manifestIllusion.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.lostIllusion.get()).weight(1))
        ));
        lootTables.put(ModBlocks.prizeBlox.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.valorOrb.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.wisdomOrb.get())
                        .weight(1)).addEntry(ItemLootEntry.builder(ModItems.masterOrb.get()).weight(1)).addEntry(ItemLootEntry
                        .builder(ModItems.finalOrb.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.limitOrb.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazingStone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilightStone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormyStone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucidStone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.darkStone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frostStone.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyStone.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.brightShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.blazingShard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.twilightShard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.stormyShard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.lucidShard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.darkShard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.frostShard.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyShard.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.orichalcum.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.orichalcumPlus.get()).weight(1)).addEntry(ItemLootEntry.builder(ModItems.manifestIllusion.get()).weight(1))
                .addEntry(ItemLootEntry.builder(ModItems.lostIllusion.get()).weight(1))
        ));
    }
    private void ores(){
        lootTables.put(ModBlocks.blazingOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.blazingOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.blazingStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.lucidOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lucidStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.lightningOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.lightningStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.denseOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.denseStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.darkOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.darkOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.darkOreE.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.darkStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.energyOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.frostOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.frostStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.serenityOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.serenityStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.twilightOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.twilightOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.twilightStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.energyOreN.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.energyStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.brightOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.brightStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.powerOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.powerOreE.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.powerStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.stormyOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.stormyStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.tranquilOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.tranquilStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );

        lootTables.put(ModBlocks.remembranceOre.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceCrystal.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceGem.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceShard.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f)))).addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(ModItems.remembranceStone.get()).weight(1).acceptFunction(SetCount.builder(new RandomValueRange(1,3)))
                        .acceptCondition(RandomChanceWithLooting.builder(.25f, .10f))))
        );
    }

    void standardBlockLoot(Block block){
        lootTables.put(block, createStandardTable(block.getTranslationKey(), block));
    }
}