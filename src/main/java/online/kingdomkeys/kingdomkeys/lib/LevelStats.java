package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

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

        for (ResourceLocation shotlock : levelData.getShotlocks(level)) {
            if (shotlock != null) {
                Shotlock a = ModShotlocks.registry.get(shotlock);
                if (a != null) {
                    Item shotlockItem = ModItems.getShotlockItem(shotlock);
                    if (shotlockItem != null) {
                        ItemStack stack = new ItemStack(shotlockItem);
                        if (!player.getInventory().add(stack)) {
                            player.drop(stack, false);
                        }
                        cap.notifyShotlockUnlocked(shotlock);
                    } else {
                        KingdomKeys.LOGGER.warn("No ShotlockItem registered for shotlock {}, player won't receive an item for it", shotlock);
                    }
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
