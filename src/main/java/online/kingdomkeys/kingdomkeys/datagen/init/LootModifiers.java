package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.loot.DiscGenModifier;
import online.kingdomkeys.kingdomkeys.loot.FortuneBonusModifier;
import online.kingdomkeys.kingdomkeys.loot.GummiBlueprintModifier;

import java.util.concurrent.CompletableFuture;

public class LootModifiers extends GlobalLootModifierProvider {

	public LootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, KingdomKeys.MODID);
	}

	@Override
	protected void start() {
		add("fortune_bonus", new FortuneBonusModifier(new LootItemCondition[0]), new ICondition[0]);

		add("disc_gen", new DiscGenModifier(new LootItemCondition[]{
				inTable("chests/simple_dungeon"),
				LootItemRandomChanceCondition.randomChance(0.5F).build()
		}), new ICondition[0]);

		blueprint("highwind_end_city", "kingdomkeys:highwind", "chests/end_city_treasure", 0.25F);
		blueprint("highwind_stronghold", "kingdomkeys:highwind", "chests/stronghold_library", 0.15F);
	}

	private void blueprint(String name, String ship, String table, float chance) {
		add(name, new GummiBlueprintModifier(new LootItemCondition[]{
				inTable(table),
				LootItemRandomChanceCondition.randomChance(chance).build()
		}, ResourceLocation.parse(ship)), new ICondition[0]);
	}

	private static LootItemCondition inTable(String table) {
		return LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace(table)).build();
	}
}
