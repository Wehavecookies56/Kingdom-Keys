package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ItemGrant;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class LevelStats {

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

		for (ItemGrant grant : levelData.getItems(level)) {
			if (grant != null) {
				Item item = BuiltInRegistries.ITEM.get(grant.item());
				if (item != null && item != Items.AIR) {
					ItemStack stack = new ItemStack(item, grant.amount());
					Utils.giveItems((ServerPlayer) player, new ItemStack(item, grant.amount()));
					String itemName = stack.getHoverName().getString();
					cap.getMessages().add("I_" + itemName + (grant.amount() > 1 ? " x" + grant.amount() : ""));
				} else {
					KingdomKeys.LOGGER.warn("No item registered for {}, player won't receive it", grant.item());
				}
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
	}

}