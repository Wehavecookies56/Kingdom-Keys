package online.kingdomkeys.kingdomkeys.datagen.init;

import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.advancements.*;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * All of the mod's advancements, previously static JSON under data/kingdomkeys/advancement, now generated
 * from code so the whole tree lives in one place and is easier to extend/reorder.
 *
 * Tree shape (parent -> children), same overall progression as before plus the additions requested:
 *
 * root
 *  |- to_soa
 *  |   `- soa ("choice" - now fires on the actual SoA choice being made, not just leaving the dimension)
 *  |       |- get_stick
 *  |       |- first_level_up -> fifty_level_up -> hundred_level_up
 *  |       |- obtain_drive -> obtain_all_drive_forms
 *  |       |- obtain_keychain -> obtain_kiblade -> summon_keyblade -> upgrade_keyblade -> max_keyblade_level -> dual_wield_oblivion_oathkeeper
 *  |       |- obtain_magic
 *  |       |- obtain_org
 *  |       |- obtain_projector -> obtain_recipe
 *  |       |- get_pauldron
 *  |       |- obtain_winner_stick
 *  |       |- munny_millionaire
 *  |       `- open_menu
 *  |- to_rod
 *  |- play_music_disc
 *  |- reach_castle_oblivion
 *  `- craft_wehavecookies56_skull -> craft_abelatox_skull -> both_dev_skulls
 */
public class AdvancementsGen implements AdvancementProvider.AdvancementGenerator {

	private static final ResourceKey<Level> REALM_OF_DARKNESS = ResourceKey.create(Registries.DIMENSION, KingdomKeys.rl("realm_of_darkness"));

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
		AdvancementHolder root = Advancement.Builder.advancement()
				.display(new ItemStack(ModItems.kingdomKey.get()), Component.translatable("advancements.kingdomkeys.root"), Component.translatable("advancements.kingdomkeys.root.desc"), KingdomKeys.rl("textures/block/rod_sand.png"), AdvancementType.TASK, true, true, false)
				.addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
				.rewards(AdvancementRewards.Builder.experience(0)
						.addLootTable(ResourceKey.create(Registries.LOOT_TABLE, KingdomKeys.rl("grant_book_on_first_join"))))
				.save(saver, KingdomKeys.rl("root"), existingFileHelper);

		AdvancementHolder toSoa = Advancement.Builder.advancement()
				.parent(root)
				.display(new ItemStack(ModBlocks.pedestal.get()), Component.translatable("advancements.kingdomkeys.to_soa"), Component.translatable("advancements.kingdomkeys.to_soa.desc"), null, AdvancementType.TASK, true, false, false)
				.addCriterion("m_key", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModDimensions.DIVE_TO_THE_HEART))
				.save(saver, KingdomKeys.rl("to_soa"), existingFileHelper);

		AdvancementHolder soa = Advancement.Builder.advancement()
				.parent(toSoa)
				.display(new ItemStack(ModItems.dreamSword.get()), Component.translatable("advancements.kingdomkeys.choice"), Component.translatable("advancements.kingdomkeys.choice.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("choice_made", ModAdvancements.CHOICE_MADE.get().createCriterion(new KKChoiceTrigger.TriggerInstance(Optional.empty())))
				.rewards(AdvancementRewards.Builder.experience(0)
						.addLootTable(ResourceKey.create(Registries.LOOT_TABLE, KingdomKeys.rl("grant_wooden_keyblade"))))
				.save(saver, KingdomKeys.rl("soa"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.woodenStick.get()), Component.translatable("advancements.kingdomkeys.get_stick"), Component.translatable("advancements.kingdomkeys.get_stick.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("get_stick", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.woodenStick.get()))
				.save(saver, KingdomKeys.rl("get_stick"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.struggleSword.get()), Component.translatable("advancements.kingdomkeys.get_struggle_weapon"), Component.translatable("advancements.kingdomkeys.get_struggle_weapon.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("struggle_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.struggleSword.get()))
				.addCriterion("struggle_hammer", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.struggleHammer.get()))
				.addCriterion("struggle_wand", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.struggleWand.get()))
				.requirements(new AdvancementRequirements(List.of(
						List.of("struggle_sword","struggle_hammer","struggle_wand")
				)))
				.rewards(AdvancementRewards.Builder.experience(80))
				.save(saver, KingdomKeys.rl("get_struggle_weapon"), existingFileHelper);

		AdvancementHolder firstLevelUp = Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.kingdomKey.get()), Component.translatable("advancements.kingdomkeys.levelup1"), Component.translatable("advancements.kingdomkeys.levelup1.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("levelup", ModAdvancements.levelUp.holder().get().createCriterion(new KKLevelUpTrigger.TriggerInstance(Optional.empty(), 2)))
				.save(saver, KingdomKeys.rl("first_level_up"), existingFileHelper);

		AdvancementHolder fiftyLevelUp = Advancement.Builder.advancement()
				.parent(firstLevelUp)
				.display(new ItemStack(ModItems.incompleteKiblade.get()), Component.translatable("advancements.kingdomkeys.levelup50"), Component.translatable("advancements.kingdomkeys.levelup50.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("levelup", ModAdvancements.levelUp.holder().get().createCriterion(new KKLevelUpTrigger.TriggerInstance(Optional.empty(), 50)))
				.rewards(AdvancementRewards.Builder.experience(50))
				.save(saver, KingdomKeys.rl("fifty_level_up"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(fiftyLevelUp)
				.display(new ItemStack(ModItems.kiblade.get()), Component.translatable("advancements.kingdomkeys.levelup100"), Component.translatable("advancements.kingdomkeys.levelup100.desc"), null, AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("levelup", ModAdvancements.levelUp.holder().get().createCriterion(new KKLevelUpTrigger.TriggerInstance(Optional.empty(), 100)))
				.rewards(AdvancementRewards.Builder.experience(180))
				.save(saver, KingdomKeys.rl("hundred_level_up"), existingFileHelper);

		AdvancementHolder obtainDrive = Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.valorOrb.get()), Component.translatable("advancements.kingdomkeys.obtain_drive"), Component.translatable("advancements.kingdomkeys.obtain_drive.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("obtain_drive", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.DRIVES).build()))
				.rewards(AdvancementRewards.Builder.experience(10))
				.save(saver, KingdomKeys.rl("obtain_drive"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(obtainDrive)
				.display(new ItemStack(ModItems.finalOrb.get()), Component.translatable("advancements.kingdomkeys.obtain_all_drive_forms"), Component.translatable("advancements.kingdomkeys.obtain_all_drive_forms.desc"), null, AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("valor", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.valorOrb.get()))
				.addCriterion("wisdom", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.wisdomOrb.get()))
				.addCriterion("limit", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.limitOrb.get()))
				.addCriterion("master", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.masterOrb.get()))
				.addCriterion("finale", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.finalOrb.get()))
				.requirements(new AdvancementRequirements(List.of(
						List.of("valor"), List.of("wisdom"), List.of("limit"), List.of("master"), List.of("finale")
				)))
				.rewards(AdvancementRewards.Builder.experience(150))
				.save(saver, KingdomKeys.rl("obtain_all_drive_forms"), existingFileHelper);

		AdvancementHolder obtainKeychain = Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.kingdomKeyChain.get()), Component.translatable("advancements.kingdomkeys.obtain_keychain"), Component.translatable("advancements.kingdomkeys.obtain_keychain.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("obtain_keychain", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.KEYCHAINS).build()))
				.rewards(AdvancementRewards.Builder.experience(30))
				.save(saver, KingdomKeys.rl("obtain_keychain"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(obtainKeychain)
				.display(new ItemStack(ModItems.kiblade.get()), Component.translatable("advancements.kingdomkeys.obtain_kiblade"), Component.translatable("advancements.kingdomkeys.obtain_kiblade.desc"), null, AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("obtain_kiblade", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.kibladeChain.get()))
				.rewards(AdvancementRewards.Builder.experience(400))
				.save(saver, KingdomKeys.rl("obtain_kiblade"), existingFileHelper);

		AdvancementHolder summonKeyblade = Advancement.Builder.advancement()
				.parent(obtainKeychain)
				.display(new ItemStack(ModItems.kingdomKey.get()), Component.translatable("advancements.kingdomkeys.summon_keyblade"), Component.translatable("advancements.kingdomkeys.summon_keyblade.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("summon_keyblade", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.KEYBLADES).build()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(saver, KingdomKeys.rl("summon_keyblade"), existingFileHelper);

		AdvancementHolder upgradeKeyblade = Advancement.Builder.advancement()
				.parent(summonKeyblade)
				.display(new ItemStack(ModItems.kingdomKeyD.get()), Component.translatable("advancements.kingdomkeys.upgrade_keyblade"), Component.translatable("advancements.kingdomkeys.upgrade_keyblade.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("upgrade_keyblade", ModAdvancements.KEYBLADE_LEVEL.get().createCriterion(new KKKeybladeLevelTrigger.TriggerInstance(Optional.empty(), 2)))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(saver, KingdomKeys.rl("upgrade_keyblade"), existingFileHelper);

		AdvancementHolder maxKeybladeLevel = Advancement.Builder.advancement()
				.parent(upgradeKeyblade)
				.display(new ItemStack(ModItems.oathkeeper.get()), Component.translatable("advancements.kingdomkeys.max_keyblade_level"), Component.translatable("advancements.kingdomkeys.max_keyblade_level.desc"), null, AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("max_keyblade_level", ModAdvancements.KEYBLADE_LEVEL.get().createCriterion(new KKKeybladeLevelTrigger.TriggerInstance(Optional.empty(), 10)))
				.rewards(AdvancementRewards.Builder.experience(300))
				.save(saver, KingdomKeys.rl("max_keyblade_level"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(maxKeybladeLevel)
				.display(new ItemStack(ModItems.oblivion.get()), Component.translatable("advancements.kingdomkeys.dual_wield_oblivion_oathkeeper"), Component.translatable("advancements.kingdomkeys.dual_wield_oblivion_oathkeeper.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("dual_wield", ModAdvancements.DUAL_WIELD.get().createCriterion(new KKDualWieldTrigger.TriggerInstance(Optional.empty(), ModItems.oblivion.get(), ModItems.oathkeeper.get())))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(saver, KingdomKeys.rl("dual_wield_oblivion_oathkeeper"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.fireSpell.get()), Component.translatable("advancements.kingdomkeys.obtain_magic"), Component.translatable("advancements.kingdomkeys.obtain_magic.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("obtain_magic", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.MAGICS).build()))
				.rewards(AdvancementRewards.Builder.experience(10))
				.save(saver, KingdomKeys.rl("obtain_magic"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.organizationRobe_Helmet.get()), Component.translatable("advancements.kingdomkeys.obtain_org"), Component.translatable("advancements.kingdomkeys.obtain_org.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("org_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Helmet.get()))
				.addCriterion("xemnas_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.xemnas_Helmet.get()))
				.addCriterion("anti_helmet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.antiCoat_Helmet.get()))
				.addCriterion("org_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Chestplate.get()))
				.addCriterion("xemnas_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.xemnas_Chestplate.get()))
				.addCriterion("anti_chestplate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.antiCoat_Chestplate.get()))
				.addCriterion("org_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Leggings.get()))
				.addCriterion("xemnas_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.xemnas_Leggings.get()))
				.addCriterion("anti_leggings", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.antiCoat_Leggings.get()))
				.addCriterion("org_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.organizationRobe_Boots.get()))
				.addCriterion("xemnas_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.xemnas_Boots.get()))
				.addCriterion("anti_boots", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.antiCoat_Boots.get()))
				.requirements(new AdvancementRequirements(List.of(
						List.of("org_helmet", "xemnas_helmet", "anti_helmet"),
						List.of("org_chestplate", "xemnas_chestplate", "anti_chestplate"),
						List.of("org_leggings", "xemnas_leggings", "anti_leggings"),
						List.of("org_boots", "xemnas_boots", "anti_boots")
				)))
				.rewards(AdvancementRewards.Builder.experience(80))
				.save(saver, KingdomKeys.rl("obtain_org"), existingFileHelper);

		AdvancementHolder obtainProjector = Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModBlocks.moogleProjector.get()), Component.translatable("advancements.kingdomkeys.obtain_projector"), Component.translatable("advancements.kingdomkeys.obtain_projector.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("obtain_projector", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.moogleProjector.get()))
				.rewards(AdvancementRewards.Builder.experience(20))
				.save(saver, KingdomKeys.rl("obtain_projector"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(obtainProjector)
				.display(new ItemStack(ModItems.recipeD.get()), Component.translatable("advancements.kingdomkeys.obtain_recipe"), Component.translatable("advancements.kingdomkeys.obtain_recipe.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("obtain_recipe", InventoryChangeTrigger.TriggerInstance.hasItems(
						ModItems.recipe.get(), ModItems.recipeD.get(), ModItems.recipeC.get(), ModItems.recipeB.get(),
						ModItems.recipeA.get(), ModItems.recipeS.get(), ModItems.recipeSS.get(), ModItems.recipeSSS.get()))
				.rewards(AdvancementRewards.Builder.experience(25))
				.save(saver, KingdomKeys.rl("obtain_recipe"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(Items.LEATHER_CHESTPLATE), Component.translatable("advancements.kingdomkeys.get_pauldron"), Component.translatable("advancements.kingdomkeys.get_pauldron.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("get_pauldron", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.PAULDRONS).build()))
				.rewards(AdvancementRewards.Builder.experience(15))
				.save(saver, KingdomKeys.rl("get_pauldron"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.iceCream.get()), Component.translatable("advancements.kingdomkeys.obtain_winner_stick"), Component.translatable("advancements.kingdomkeys.obtain_winner_stick.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("obtain_winner_stick", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.winnerStick.get()))
				.rewards(AdvancementRewards.Builder.experience(250))
				.save(saver, KingdomKeys.rl("obtain_winner_stick"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(Items.EMERALD), Component.translatable("advancements.kingdomkeys.munny_millionaire"), Component.translatable("advancements.kingdomkeys.munny_millionaire.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("munny_millionaire", ModAdvancements.MUNNY_REACHED.get().createCriterion(new KKMunnyTrigger.TriggerInstance(Optional.empty(), 1000000)))
				.rewards(AdvancementRewards.Builder.experience(100))
				.save(saver, KingdomKeys.rl("munny_millionaire"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(soa)
				.display(new ItemStack(ModItems.kingdomKey.get()), Component.translatable("advancements.kingdomkeys.open_menu"), Component.translatable("advancements.kingdomkeys.open_menu.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("open_menu", ModAdvancements.OPEN_MENU.get().createCriterion(new KKOpenMenuTrigger.TriggerInstance(Optional.empty())))
				.rewards(AdvancementRewards.Builder.experience(5))
				.save(saver, KingdomKeys.rl("open_menu"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(root)
				.display(new ItemStack(ModBlocks.rodCrackedStone.get()), Component.translatable("advancements.kingdomkeys.to_rod"), Component.translatable("advancements.kingdomkeys.to_rod.desc"), null, AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("to_rod", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(REALM_OF_DARKNESS))
				.rewards(AdvancementRewards.Builder.experience(60))
				.save(saver, KingdomKeys.rl("to_rod"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(root)
				.display(new ItemStack(Items.ENDER_PEARL), Component.translatable("advancements.kingdomkeys.reach_castle_oblivion"), Component.translatable("advancements.kingdomkeys.reach_castle_oblivion.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("reach_castle_oblivion", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(ModDimensions.CASTLE_OBLIVION))
				.rewards(AdvancementRewards.Builder.experience(60))
				.save(saver, KingdomKeys.rl("reach_castle_oblivion"), existingFileHelper);

		Advancement.Builder.advancement()
				.parent(root)
				.display(new ItemStack(Items.MUSIC_DISC_CAT), Component.translatable("advancements.kingdomkeys.play_music_disc"), Component.translatable("advancements.kingdomkeys.play_music_disc.desc"), null, AdvancementType.GOAL, true, false, false)
				.addCriterion("play_music_disc", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ModTags.MUSIC_DISCS).build()))
				.rewards(AdvancementRewards.Builder.experience(10))
				.save(saver, KingdomKeys.rl("play_music_disc"), existingFileHelper);

		AdvancementHolder previous = root;

		for (Constants.DevRecipe dev : Constants.devRecipes) {
			ItemStack head = new ItemStack(Items.PLAYER_HEAD);
			head.set(DataComponents.PROFILE, new ResolvableProfile(Optional.of(dev.name()), Optional.empty(), new PropertyMap()));

			String id = "craft_" + dev.name().toLowerCase() + "_skull";

			previous = Advancement.Builder.advancement()
					.parent(previous)
					.display(head, Component.translatable("advancements.kingdomkeys." + id), Component.translatable("advancements.kingdomkeys." + id + ".desc"), null, AdvancementType.GOAL, true, false, true)
					.addCriterion("crafted", ModAdvancements.CRAFT_PROFILE_HEAD.get().createCriterion(new KKCraftProfileHeadTrigger.TriggerInstance(Optional.empty(), dev.name())))
					.rewards(AdvancementRewards.Builder.experience(50))
					.save(saver, KingdomKeys.rl(id), existingFileHelper);
		}

		// All of them
		Advancement.Builder allHeads = Advancement.Builder.advancement().parent(previous).display(new ItemStack(Items.PLAYER_HEAD), Component.translatable("advancements.kingdomkeys.all_dev_skulls"), Component.translatable("advancements.kingdomkeys.all_dev_skulls.desc"), null, AdvancementType.CHALLENGE, true, true, false);

		List<List<String>> requirements = new ArrayList<>();

		for (Constants.DevRecipe dev : Constants.devRecipes) {
			String criterion = dev.name().toLowerCase();
			allHeads.addCriterion(criterion, ModAdvancements.CRAFT_PROFILE_HEAD.get().createCriterion(new KKCraftProfileHeadTrigger.TriggerInstance(Optional.empty(), dev.name())));
			requirements.add(List.of(criterion));
		}

		allHeads.requirements(new AdvancementRequirements(requirements))
				.rewards(AdvancementRewards.Builder.experience(200))
				.save(saver, KingdomKeys.rl("all_dev_skulls"), existingFileHelper);
	}
}
