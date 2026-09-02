package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class LevelStats {

	// Finds and marks items as given
	public static void seekGrantedItems(PlayerData data) {
		if (data.areLevelItemsSought()) {
			return;
		}

		// Nothing has been earned yet, so an empty record is already ok
		if (data.getSoAState() != SoAState.COMPLETE) {
			data.setLevelItemsSought(true);
			return;
		}

		Level levelData = ModLevels.registry.get(KingdomKeys.rl(data.getChosen().getSerializedName()));
		if (levelData == null || levelData.getLevelingData() == null) {
			KingdomKeys.LOGGER.warn("Could not seek the level reward record for {}, leveling data was not available", data.getChosen().getSerializedName());
			return;
		}

		for (int lvl = 2; lvl <= data.getLevel(); lvl++) {
			for (ItemStack stack : levelData.getItems(lvl)) {
				if (stack != null && !stack.isEmpty()) {
					data.addGrantedLevelItem(PlayerData.levelItemKey(lvl, stack));
				}
			}
		}

		data.setLevelItemsSought(true);
	}

	public static void applyStatsForLevel(int level, Player player, PlayerData cap) {
		if(cap.getSoAState() != SoAState.COMPLETE) {
			return;
		}

		Level levelData = ModLevels.registry.get(KingdomKeys.rl(cap.getChosen().getSerializedName()));

		if (levelData == null) {
			KingdomKeys.LOGGER.error("Failed to get level from registry location {}, this should never happen", KingdomKeys.rl(cap.getChosen().getSerializedName()));
			return;
		}

		if (levelData.getLevelingData() == null) {
			KingdomKeys.LOGGER.error("Failed to get leveling data from registry location {}, this means the data was not loaded from the json correctly", KingdomKeys.rl(cap.getChosen().getSerializedName()));
			return;
		}

		if (levelData.getStr(level) > 0) {
			cap.addStrength(levelData.getStr(level));
		}

		if (levelData.getMag(level) > 0) {
			cap.addMagic(levelData.getMag(level));
		}

		if (levelData.getDef(level) > 0) {
			cap.addDefense(levelData.getDef(level));
		}

		if (levelData.getMaxAP(level) > 0) {
			cap.addMaxAP(levelData.getMaxAP(level));
		}

		if (levelData.getMaxHp(level) > 0) {
			cap.addMaxHP(levelData.getMaxHp(level));
		}

		if (levelData.getMaxMp(level) > 0) {
			cap.addMaxMP(levelData.getMaxMp(level));
		}

		for (ResourceLocation ability : levelData.getAbilities(level)) {
			if (ability != null) {
				Ability a = ModAbilities.registry.get(ability);
				if (a != null) {
					cap.addAbility(ability, true);
				}
			}
		}

		for (ItemStack stack : levelData.getItems(level)) {
			if (stack != null && !stack.isEmpty()) {
				String key = PlayerData.levelItemKey(level, stack);
				if (cap.hasGrantedLevelItem(key)) {
					continue;
				}

				cap.addGrantedLevelItem(key);

				ItemStack toGive = stack.copy();
				Utils.giveItems((ServerPlayer) player, true,  toGive);
				String itemName = toGive.getHoverName().getString();
				cap.getMessages().add("I_" + itemName + (toGive.getCount() > 1 ? " x" + toGive.getCount() : ""));
			}
		}

		if (levelData.getMaxAccessories(level) > 0) {
			cap.addMaxAccessories(levelData.getMaxAccessories(level));
		}
		if (levelData.getMaxArmors(level) > 0) {
			cap.addMaxArmors(levelData.getMaxArmors(level));
		}
		if (levelData.getMaxMagics(level) > 0) {
			cap.addMaxMagics(levelData.getMaxMagics(level));
		}
		if (levelData.getMaxItems(level) > 0) {
			cap.addMaxItems(levelData.getMaxItems(level));
		}
	}

}