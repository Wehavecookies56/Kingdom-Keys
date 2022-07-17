package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

/** Mainly here just to reduce the size of {@link online.kingdomkeys.kingdomkeys.capability.PlayerCapabilities} */
public class LevelStats {

    public static void applyStatsForLevel(int level, Player player, IPlayerCapabilities cap) {
    	if(cap.getSoAState() != SoAState.COMPLETE) {
    		return;
    	}
    	
    	Level levelData = ModLevels.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID+":"+ cap.getChosen().toString().toLowerCase()));
		
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
					Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(ability));
					if (a != null) {
						cap.addAbility(ability, true);
					}
				}
			}
		}
		
		if (levelData.getShotlocks(level).length > 0) {
			for (String shotlock : levelData.getShotlocks(level)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlock));
					if (a != null) {
						cap.addShotlockToList(shotlock, true);
					}
				}
			}
		}
		
		if (levelData.getSpells(level).length > 0) {
			for (String magic : levelData.getSpells(level)) {
				if (magic != null) {
					Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));
					if (magicInstance != null) {
						if (cap != null && cap.getMagicsMap() != null) {
							if (!cap.getMagicsMap().containsKey(magic)) {
								cap.setMagicLevel(magic, cap.getMagicLevel(magic), true);
							} else {
								cap.setMagicLevel(magic, cap.getMagicLevel(magic)+1, true);
							}
						}
					}
				}
			}
		}
    }

}
