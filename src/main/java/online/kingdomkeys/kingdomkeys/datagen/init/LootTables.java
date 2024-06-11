package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.List;
import java.util.Set;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.EntryGroup;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseLootTables;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class LootTables extends BaseLootTables {

	public LootTables(DataGenerator generator, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> pSubProviders) {
		super(generator.getPackOutput(), pRequiredTables, pSubProviders, generator);
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
        
        standardBlockLoot(ModBlocks.rodStone.get());
        standardBlockLoot(ModBlocks.rodSand.get());
        standardBlockLoot(ModBlocks.rodCrackedStone.get());

		standardBlockLoot(ModBlocks.airstepTarget.get());
        
        lootTables.put(ModBlocks.prizeBlox.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(AlternativesEntry.alternatives(
					LootItem.lootTableItem(ModBlocks.prizeBlox.get().asItem()).when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))),
					EntryGroup.list(
						LootItem.lootTableItem(ModItems.fireSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.blizzardSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.waterSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.thunderSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.cureSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.aeroSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.magnetSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.gravitySpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.reflectSpell.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.stopSpell.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.betwixt_shard.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.sinister_shard.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.stormy_shard.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.writhing_shard.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.pulsing_shard.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.betwixt_stone.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.sinister_stone.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.stormy_stone.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.writhing_stone.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.pulsing_stone.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.fluorite.get()).setWeight(4),
						LootItem.lootTableItem(ModItems.damascus.get()).setWeight(3),
						LootItem.lootTableItem(ModItems.adamantite.get()).setWeight(2),
						LootItem.lootTableItem(ModItems.electrum.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.orichalcum.get()).setWeight(1)
					)
				))));
        
        lootTables.put(ModBlocks.rarePrizeBlox.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(AlternativesEntry.alternatives(
					LootItem.lootTableItem(ModBlocks.rarePrizeBlox.get().asItem()).when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))),
					EntryGroup.list(
						LootItem.lootTableItem(ModItems.valorOrb.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.wisdomOrb.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.masterOrb.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.finalOrb.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.limitOrb.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.betwixt_gem.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.sinister_gem.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.stormy_gem.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.writhing_gem.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.pulsing_gem.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.betwixt_crystal.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.sinister_crystal.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.stormy_crystal.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.writhing_crystal.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.pulsing_crystal.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.orichalcum.get()).setWeight(3),
						LootItem.lootTableItem(ModItems.orichalcumplus.get()).setWeight(2),
						LootItem.lootTableItem(ModItems.manifest_illusion.get()).setWeight(3),
						LootItem.lootTableItem(ModItems.lost_illusion.get()).setWeight(2),
						
						LootItem.lootTableItem(ModItems.illusory_crystal.get()).setWeight(1),
						LootItem.lootTableItem(ModItems.evanescent_crystal.get()).setWeight(1),

						LootItem.lootTableItem(ModItems.electrum.get()).setWeight(2))
					)
				)));
        
    }

	private void ores() {
		addOreLootTable(ModBlocks.betwixtOre.get(), ModItems.betwixt_crystal.get(), ModItems.betwixt_gem.get(), ModItems.betwixt_stone.get(), ModItems.betwixt_shard.get());
		addOreLootTable(ModBlocks.betwixtOreD.get(), ModItems.betwixt_crystal.get(), ModItems.betwixt_gem.get(), ModItems.betwixt_stone.get(), ModItems.betwixt_shard.get());
		addOreLootTable(ModBlocks.betwixtOreE.get(), ModItems.betwixt_crystal.get(), ModItems.betwixt_gem.get(), ModItems.betwixt_stone.get(), ModItems.betwixt_shard.get());
		addOreLootTable(ModBlocks.blazingOre.get(), ModItems.blazing_crystal.get(), ModItems.blazing_gem.get(), ModItems.blazing_stone.get(), ModItems.blazing_shard.get());
		addOreLootTable(ModBlocks.blazingOreN.get(), ModItems.blazing_crystal.get(), ModItems.blazing_gem.get(), ModItems.blazing_stone.get(), ModItems.blazing_shard.get());
		addOreLootTable(ModBlocks.blazingOreD.get(), ModItems.blazing_crystal.get(), ModItems.blazing_gem.get(), ModItems.blazing_stone.get(), ModItems.blazing_shard.get());
		addOreLootTable(ModBlocks.frostOre.get(), ModItems.frost_crystal.get(), ModItems.frost_gem.get(), ModItems.frost_stone.get(), ModItems.frost_shard.get());
		addOreLootTable(ModBlocks.frostOreD.get(), ModItems.frost_crystal.get(), ModItems.frost_gem.get(), ModItems.frost_stone.get(), ModItems.frost_shard.get());
		addOreLootTable(ModBlocks.hungryOre.get(), ModItems.hungry_crystal.get(), ModItems.hungry_gem.get(), ModItems.hungry_stone.get(), ModItems.hungry_shard.get());
		addOreLootTable(ModBlocks.lightningOre.get(), ModItems.lightning_crystal.get(), ModItems.lightning_gem.get(), ModItems.lightning_stone.get(), ModItems.lightning_shard.get());
		addOreLootTable(ModBlocks.lucidOre.get(), ModItems.lucid_crystal.get(), ModItems.lucid_gem.get(), ModItems.lucid_stone.get(), ModItems.lucid_shard.get());
		addOreLootTable(ModBlocks.pulsingOre.get(), ModItems.pulsing_crystal.get(), ModItems.pulsing_gem.get(), ModItems.pulsing_stone.get(), ModItems.pulsing_shard.get());
		addOreLootTable(ModBlocks.pulsingOreE.get(), ModItems.pulsing_crystal.get(), ModItems.pulsing_gem.get(), ModItems.pulsing_stone.get(), ModItems.pulsing_shard.get());
		addOreLootTable(ModBlocks.pulsingOreD.get(), ModItems.pulsing_crystal.get(), ModItems.pulsing_gem.get(), ModItems.pulsing_stone.get(), ModItems.pulsing_shard.get());
		addOreLootTable(ModBlocks.remembranceOre.get(), ModItems.remembrance_crystal.get(), ModItems.remembrance_gem.get(), ModItems.remembrance_stone.get(), ModItems.remembrance_shard.get());
		addOreLootTable(ModBlocks.sinisterOre.get(), ModItems.sinister_crystal.get(), ModItems.sinister_gem.get(), ModItems.sinister_stone.get(), ModItems.sinister_shard.get());
		addOreLootTable(ModBlocks.sinisterOreD.get(), ModItems.sinister_crystal.get(), ModItems.sinister_gem.get(), ModItems.sinister_stone.get(), ModItems.sinister_shard.get());
		addOreLootTable(ModBlocks.soothingOre.get(), ModItems.soothing_crystal.get(), ModItems.soothing_gem.get(), ModItems.soothing_stone.get(), ModItems.soothing_shard.get());
		addOreLootTable(ModBlocks.soothingOreD.get(), ModItems.soothing_crystal.get(), ModItems.soothing_gem.get(), ModItems.soothing_stone.get(), ModItems.soothing_shard.get());
		addOreLootTable(ModBlocks.stormyOre.get(), ModItems.stormy_crystal.get(), ModItems.stormy_gem.get(), ModItems.stormy_stone.get(), ModItems.stormy_shard.get());
		addOreLootTable(ModBlocks.stormyOreD.get(), ModItems.stormy_crystal.get(), ModItems.stormy_gem.get(), ModItems.stormy_stone.get(), ModItems.stormy_shard.get());
		addOreLootTable(ModBlocks.tranquilityOre.get(), ModItems.tranquility_crystal.get(), ModItems.tranquility_gem.get(), ModItems.tranquility_stone.get(), ModItems.tranquility_shard.get());
		addOreLootTable(ModBlocks.twilightOre.get(), ModItems.twilight_crystal.get(), ModItems.twilight_gem.get(), ModItems.twilight_stone.get(), ModItems.twilight_shard.get());
		addOreLootTable(ModBlocks.twilightOreN.get(), ModItems.twilight_crystal.get(), ModItems.twilight_gem.get(), ModItems.twilight_stone.get(), ModItems.twilight_shard.get());
		addOreLootTable(ModBlocks.twilightOreD.get(), ModItems.twilight_crystal.get(), ModItems.twilight_gem.get(), ModItems.twilight_stone.get(), ModItems.twilight_shard.get());
		addOreLootTable(ModBlocks.wellspringOre.get(), ModItems.wellspring_crystal.get(), ModItems.wellspring_gem.get(), ModItems.wellspring_stone.get(), ModItems.wellspring_shard.get());
		addOreLootTable(ModBlocks.wellspringOreN.get(), ModItems.wellspring_crystal.get(), ModItems.wellspring_gem.get(), ModItems.wellspring_stone.get(), ModItems.wellspring_shard.get());
		addOreLootTable(ModBlocks.writhingOre.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
		addOreLootTable(ModBlocks.writhingOreE.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
		addOreLootTable(ModBlocks.writhingOreN.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
		addOreLootTable(ModBlocks.writhingOreD.get(), ModItems.writhing_crystal.get(), ModItems.writhing_gem.get(), ModItems.writhing_stone.get(), ModItems.writhing_shard.get());
	}
    
	private void addOreLootTable(Block block, Item crystal, Item gem, Item stone, Item shard) {
		 lootTables.put(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
			.add(AlternativesEntry.alternatives(
					LootItem.lootTableItem(block.asItem()).when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))),
					EntryGroup.list(
						LootItem.lootTableItem(crystal).setWeight(1).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)),
						LootItem.lootTableItem(gem).setWeight(2).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)),
						LootItem.lootTableItem(stone).setWeight(3).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)),
						LootItem.lootTableItem(shard).setWeight(4).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
					)
			))));
	}

	void standardBlockLoot(Block block){
        lootTables.put(block, createStandardTable(block.getDescriptionId(), block));
    }
}