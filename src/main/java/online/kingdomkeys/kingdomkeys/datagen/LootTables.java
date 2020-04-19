package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.SetCount;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;

public class LootTables extends BaseLootTables {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        // blox
        lootTables.put(ModBlocks.normalBlox, createStandardTable("normal_blox", ModBlocks.normalBlox));
        lootTables.put(ModBlocks.hardBlox, createStandardTable("hard_blox", ModBlocks.hardBlox));
        lootTables.put(ModBlocks.metalBlox, createStandardTable("metal_blox", ModBlocks.metalBlox));
        lootTables.put(ModBlocks.bounceBlox, createStandardTable("bounce_blox", ModBlocks.bounceBlox));
        lootTables.put(ModBlocks.dangerBlox, createStandardTable("danger_blox", ModBlocks.dangerBlox));
        lootTables.put(ModBlocks.blastBlox, createStandardTable("blast_blox", ModBlocks.blastBlox));
        lootTables.put(ModBlocks.ghostBlox, createStandardTable("ghost_blox", ModBlocks.ghostBlox));
        lootTables.put(ModBlocks.rarePrizeBlox, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(Blocks.STONE).weight(1)).addEntry(ItemLootEntry.builder(Blocks.SAND)
                .weight(1).acceptFunction(SetCount.builder(new RandomValueRange(0,4))))));

        // ore
    }
}