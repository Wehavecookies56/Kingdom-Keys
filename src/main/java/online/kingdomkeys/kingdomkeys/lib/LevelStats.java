package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

/** Mainly here just to reduce the size of {@link online.kingdomkeys.kingdomkeys.capability.PlayerCapabilities} */
public class LevelStats {

    public static void applyStatsForLevel(int level, PlayerEntity player, IPlayerCapabilities cap) {
    	if(cap.getSoAState() != SoAState.COMPLETE) {
    		return;
    	}
    	
    	Level levelData = ModLevels.registry.getValue(new ResourceLocation(KingdomKeys.MODID+":"+ cap.getChosen().toString().toLowerCase()));
		
    	if (levelData.getStr(level) > 0) {
			cap.addStrength(levelData.getStr(level));
		}
		
		if (levelData.getMag(level) > 0) {
			cap.addMagic(levelData.getMag(level));
		}
		
		if (levelData.getDef(level) > 0) {
			cap.addDefense(levelData.getDef(level));
		}
		
		if (levelData.getAP(level) > 0) {
			cap.addMaxAP(levelData.getAP(level));
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
					Ability a = ModAbilities.registry.getValue(new ResourceLocation(ability));
					if (a != null) {
						cap.addAbility(ability, true);
					}
				}
			}
		}
		
		if (levelData.getShotlocks(level).length > 0) {
			for (String shotlock : levelData.getShotlocks(level)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.getValue(new ResourceLocation(shotlock));
					if (a != null) {
						cap.addShotlockToList(shotlock, true);
					}
				}
			}
		}
    }

}
