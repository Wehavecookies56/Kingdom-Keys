package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.ApplyBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.fml.RegistryObject;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseLootTables;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class LootTables extends BaseLootTables {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        blox();
        ores();
    }
    
    private void blox() {
        standardBlockLoot(ModBlocks.normalBlox.get());
        standardBlockLoot(ModBlocks.hardBlox.get());
        standardBlockLoot(ModBlocks.metalBlox.get());
        standardBlockLoot(ModBlocks.bounceBlox.get());
        standardBlockLoot(ModBlocks.dangerBlox.get());
        standardBlockLoot(ModBlocks.blastBlox.get());
        standardBlockLoot(ModBlocks.ghostBlox.get());
        standardBlockLoot(ModBlocks.magnetBlox.get());
        standardBlockLoot(ModBlocks.orgPortal.get());
        standardBlockLoot(ModBlocks.moogleProjector.get());
        standardBlockLoot(ModBlocks.mosaic_stained_glass.get());
        standardBlockLoot(ModBlocks.station_of_awakening_core.get());
        standardBlockLoot(ModBlocks.pedestal.get());        
        standardBlockLoot(ModBlocks.savepoint.get());
        standardBlockLoot(ModBlocks.magicalChest.get());
        
        lootTables.put(ModBlocks.prizeBlox.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
        		.addEntry(ItemLootEntry.builder(ModItems.fireSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.blizzardSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.waterSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.thunderSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.cureSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.aeroSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.magnetSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.gravitySpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.reflectSpell.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.stopSpell.get()).weight(1))
	            
	            .addEntry(ItemLootEntry.builder(ModItems.betwixt_shard.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.sinister_shard.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.stormy_shard.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.writhing_shard.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.pulsing_shard.get()).weight(1))
	            
	            .addEntry(ItemLootEntry.builder(ModItems.betwixt_stone.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.sinister_stone.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.stormy_stone.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.writhing_stone.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.pulsing_stone.get()).weight(1))

	            .addEntry(ItemLootEntry.builder(ModItems.fluorite.get()).weight(4))
	            .addEntry(ItemLootEntry.builder(ModItems.damascus.get()).weight(3))
	            .addEntry(ItemLootEntry.builder(ModItems.adamantite.get()).weight(2))
	            .addEntry(ItemLootEntry.builder(ModItems.electrum.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.orichalcum.get()).weight(1))
        ));
        
        lootTables.put(ModBlocks.rarePrizeBlox.get(), LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
	    		.addEntry(ItemLootEntry.builder(ModItems.valorOrb.get()).weight(1))
	    		.addEntry(ItemLootEntry.builder(ModItems.wisdomOrb.get()).weight(1))
	    		.addEntry(ItemLootEntry.builder(ModItems.masterOrb.get()).weight(1))
	    		.addEntry(ItemLootEntry.builder(ModItems.finalOrb.get()).weight(1))
	    		.addEntry(ItemLootEntry.builder(ModItems.limitOrb.get()).weight(1))
	    		
	    		.addEntry(ItemLootEntry.builder(ModItems.betwixt_gem.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.sinister_gem.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.stormy_gem.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.writhing_gem.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.pulsing_gem.get()).weight(1))
	            
	            .addEntry(ItemLootEntry.builder(ModItems.betwixt_crystal.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.sinister_crystal.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.stormy_crystal.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.writhing_crystal.get()).weight(1))
	            .addEntry(ItemLootEntry.builder(ModItems.pulsing_crystal.get()).weight(1))
	            
	            .addEntry(ItemLootEntry.builder(ModItems.orichalcum.get()).weight(3))
	            .addEntry(ItemLootEntry.builder(ModItems.orichalcumplus.get()).weight(2))
	            .addEntry(ItemLootEntry.builder(ModItems.manifest_illusion.get()).weight(3))
	            .addEntry(ItemLootEntry.builder(ModItems.lost_illusion.get()).weight(2))
	            
	            .addEntry(ItemLootEntry.builder(ModItems.electrum.get()).weight(2))
        ));
        
    }

	private void ores() {
		addOreLootTable(ModBlocks.betwixtOre.get(), ModItems.betwixt_crystal.get(), ModItems.betwixt_gem.get(), ModItems.betwixt_stone.get(), ModItems.betwixt_shard.get());
		addOreLootTable(ModBlocks.blazingOre.get(), ModItems.blazing_crystal.get(), ModItems.blazing_gem.get(), ModItems.blazing_stone.get(), ModItems.blazing_shard.get());
		addOreLootTable(ModBlocks.blazingOreN.get(), ModItems.blazing_crystal.get(), ModItems.blazing_gem.get(), ModItems.blazing_stone.get(), ModItems.blazing_shard.get());
		addOreLootTable(ModBlocks.frostOre.get(), ModItems.frost_crystal.get(), ModItems.frost_gem.get(), ModItems.frost_stone.get(), ModItems.frost_shard.get());
		addOreLootTable(ModBlocks.hungryOre.get(), ModItems.hungry_crystal.get(), ModItems.hungry_gem.get(), ModItems.hungry_stone.get(), ModItems.hungry_shard.get());
		addOreLootTable(ModBlocks.lightningOre.get(), ModItems.lightning_crystal.get(), ModItems.lightning_gem.get(), ModItems.lightning_stone.get(), ModItems.lightning_shard.get());
		addOreLootTable(ModBlocks.lucidOre.get(), ModItems.lucid_crystal.get(), ModItems.lucid_gem.get(), ModItems.lucid_stone.get(), ModItems.lucid_shard.get());
		addOreLootTable(ModBlocks.pulsingOre.get(), ModItems.pulsing_crystal.get(), ModItems.pulsing_gem.get(), ModItems.pulsing_stone.get(), ModItems.pulsing_shard.get());
		addOreLootTable(ModBlocks.pulsingOreE.get(), ModItems.pulsing_crystal.get(), ModItems.pulsing_gem.get(), ModItems.pulsing_stone.get(), ModItems.pulsing_shard.get());
		addOreLootTable(ModBlocks.remembranceOre.get(), ModItems.remembrance_crystal.get(), ModItems.remembrance_gem.get(), ModItems.remembrance_stone.get(), ModItems.remembrance_shard.get());
		addOreLootTable(ModBlocks.sinisterOre.get(), ModItems.sinister_crystal.get(), ModItems.sinister_gem.get(), ModItems.sinister_stone.get(), ModItems.sinister_shard.get());
		addOreLootTable(ModBlocks.soothingOre.get(), ModItems.soothing_crystal.get(), ModItems.soothing_gem.get(), ModItems.soothing_stone.get(), ModItems.soothing_shard.get());
		addOreLootTable(ModBlocks.stormyOre.get(), ModItems.stormy_crystal.get(), ModItems.stormy_gem.get(), ModItems.stormy_stone.get(), ModItems.stormy_shard.get());
		addOreLootTable(ModBlocks.tranquilityOre.get(), ModItems.tranquility_crystal.get(), ModItems.tranquility_gem.get(), ModItems.tranquility_stone.get(), ModItems.tranquility_shard.get());
		addOreLootTable(ModBlocks.twilightOre.get(), ModItems.twilight_crystal.get(), ModItems.twilight_gem.get(), ModItems.twilight_stone.get(), ModItems.twilight_shard.get());
		addOreLootTable(ModBlocks.twilightOreN.get(), ModItems.twilight_crystal.get(), ModItems.twilight_gem.get(), ModItems.twilight_stone.get(), ModItems.twilight_shard.get());
		addOreLootTable(ModBlocks.wellspringOre.get(), ModItems.wellspring_crystal.get(), ModItems.wellspring_gem.get(), ModItems.wellspring_stone.get(), ModItems.wellspring_shard.get());
		addOreLootTable(ModBlocks.wellspringOreN.get(), ModItems.wellspring_crystal.get(), ModItems.wellspring_gem.get(), ModItems.wellspring_stone.get(), ModItems.wellspring_shard.get());
		addOreLootTable(ModBlocks.writhingOre.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
		addOreLootTable(ModBlocks.writhingOreE.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
		addOreLootTable(ModBlocks.writhingOreN.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
	}
    
	private void addOreLootTable(Block block, Item crystal, Item gem, Item stone, Item shard) {
		 lootTables.put(block, LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
	                .addEntry(ItemLootEntry.builder(crystal).weight(1).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))
	                .addEntry(ItemLootEntry.builder(gem).weight(2).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))
	                .addEntry(ItemLootEntry.builder(stone).weight(3).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)))
	                .addEntry(ItemLootEntry.builder(shard).weight(4).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE))))
	        );
		
	}

	void standardBlockLoot(Block block){
        lootTables.put(block, createStandardTable(block.getTranslationKey(), block));
    }
}