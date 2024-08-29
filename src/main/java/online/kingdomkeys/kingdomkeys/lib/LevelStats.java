package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

/** Mainly here just to reduce the size of {@link PlayerData} */
public class LevelStats {

    public static void applyStatsForLevel(int level, Player player, PlayerData cap) {
    	if(cap.getSoAState() != SoAState.COMPLETE) {
    		return;
    	}
    	
    	Level levelData = ModLevels.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, cap.getChosen().toString().toLowerCase()));
		
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
		
		if (levelData.getAbilities(level).length > 0) {
			for (String ability : levelData.getAbilities(level)) {
				if (ability != null) {
					Ability a = ModAbilities.registry.get(ResourceLocation.parse(ability));
					if (a != null) {
						cap.addAbility(ability, true);
					}
				}
			}
		}
		
		if (levelData.getShotlocks(level).length > 0) {
			for (String shotlock : levelData.getShotlocks(level)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.get(ResourceLocation.parse(shotlock));
					if (a != null) {
						cap.addShotlockToList(shotlock, true);
					}
				}
			}
		}
		
		if (levelData.getSpells(level).length > 0) {
			for (String magic : levelData.getSpells(level)) {
				if (magic != null) {
					Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
					if (magicInstance != null) {
						if (cap != null && cap.getMagicsMap() != null) {
							if (!cap.getMagicsMap().containsKey(magic)) {
								cap.setMagicLevel(ResourceLocation.parse(magic), cap.getMagicLevel(ResourceLocation.parse(magic)), true);
							} else {
								cap.setMagicLevel(ResourceLocation.parse(magic), cap.getMagicLevel(ResourceLocation.parse(magic))+1, true);
							}
						}
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
    }

}
