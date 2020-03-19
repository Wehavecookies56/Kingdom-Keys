package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class LootTables extends BaseLootTables {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(ModBlocks.rarePrizeBlox, createStandardTable("rare_prize_blox", ModBlocks.prizeBlox));
    }
}