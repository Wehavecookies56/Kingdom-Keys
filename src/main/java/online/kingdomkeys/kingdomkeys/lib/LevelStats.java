package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.entity.player.PlayerEntity;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;

/** Mainly here just to reduce the size of {@link online.kingdomkeys.kingdomkeys.capability.PlayerCapabilities} */
public class LevelStats {

    public static void applyStatsForLevel(int level, PlayerEntity player, IPlayerCapabilities cap) {
        switch (level) {
            case 2:
                cap.addDefense(1);
                cap.addAbility(Strings.scan, true);
                break;
            case 3:
                cap.addStrength(1);
                break;
            case 4:
                cap.addDefense(1);
                cap.addMaxMP(4);
                break;
            case 5:
                cap.addStrength(1);
                cap.addMaxHP(5);
              //  cap.addMaxMP(5);
                // ABILITIES.unlockAbility(ModAbilities.guard);
                break;
            case 6:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 7:
                cap.addStrength(1);
                break;
            case 8:
                cap.addMagic(1);
                cap.addMaxMP(4);
                break;
            case 9:
                cap.addStrength(1);
                break;
            case 10:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addMaxHP(5);
              //  cap.addMaxMP(5);
                // ABILITIES.unlockAbility(ModAbilities.mpHaste);
                break;
            case 11:
                cap.addStrength(1);
                break;
            case 12:
                cap.addMagic(1);
                cap.addAbility(Strings.mpHaste, true);
                cap.addMaxMP(4);
                break;
            case 13:
                cap.addStrength(1);
                break;
            case 14:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 15:
                cap.addStrength(1);
                cap.addMaxHP(5);
             //   cap.addMaxMP(5);
                cap.addAbility(Strings.damageDrive, true);
                // ABILITIES.unlockAbility(ModAbilities.formBoost);
                break;
            case 16:
                cap.addMagic(1);
                cap.addAbility(Strings.mpRage, true);
                cap.addMaxMP(4);
                break;
            case 17:
                cap.addStrength(1);
                break;
            case 18:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 19:
                cap.addStrength(1);
                break;
            case 20: //Up to lvl 20 every 5 levels u get twice the max mp (to make it match with the 20 extra to have 120)
                cap.addMagic(1);
                cap.addMaxHP(5);
                cap.addMaxMP(4);
                // ABILITIES.unlockAbility(ModAbilities.mpHastera);
                break;
            case 21:
                cap.addStrength(1);
                break;
            case 22:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addAbility(Strings.formBoost, true);
                break;
            case 23:
                cap.addStrength(1);
                break;
            case 24:
                cap.addMagic(1);
                break;
            case 25:
                cap.addStrength(1);
                cap.addMaxHP(5);
                cap.addAbility(Strings.driveBoost, true);
                break;
            case 26:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 27:
                cap.addStrength(1);
                cap.addMagic(1);
                break;
            case 28:
                cap.addMagic(1);
                break;
            case 29:
                cap.addStrength(1);
                break;
            case 30:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addMaxHP(5);
                break;
            case 31:
                cap.addStrength(1);
                break;
            case 32:
                cap.addStrength(1);
                cap.addMagic(1);
                break;
            case 33:
                cap.addStrength(1);
                // ABILITIES.unlockAbility(ModAbilities.driveConverter);
                break;
            case 34:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addAbility(Strings.mpHastera, true);
                break;
            case 35:
                cap.addStrength(1);
                cap.addMaxHP(5);
                break;
            case 36:
                cap.addMagic(1);
                break;
            case 37:
                cap.addStrength(1);
                break;
            case 38:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 39:
                cap.addStrength(1);
                break;
            case 40:
                cap.addMagic(1);
                cap.addMaxHP(5);
                break;
            case 41:
                cap.addStrength(1);
                break;
            case 42:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 43:
                cap.addStrength(1);
                cap.addMagic(1);
                break;
            case 44:
                cap.addMagic(1);
                break;
            case 45:
                cap.addStrength(1);
                cap.addMaxHP(5);
                break;
            case 46:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 47:
                cap.addStrength(1);
                break;
            case 48:
                cap.addStrength(1);
                cap.addMagic(1);
                // ABILITIES.unlockAbility(ModAbilities.sonicBlade);
                break;
            case 49:
                cap.addStrength(1);
                break;
            case 50:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addMaxHP(5);
                // ABILITIES.unlockAbility(ModAbilities.mpHastega);
                break;
            case 51:
                cap.addStrength(1);
                break;
            case 52:
                cap.addMagic(1);
                break;
            case 53:
                cap.addStrength(1);
                break;
            case 54:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 55:
                cap.addStrength(1);
                cap.addMaxHP(5);
                // ABILITIES.unlockAbility(ModAbilities.strikeRaid);
                break;
            case 56:
                cap.addMagic(1);
                break;
            case 57:
                cap.addStrength(1);
                break;
            case 58:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 59:
                cap.addStrength(1);
                break;
            case 60:
                cap.addMagic(1);
                cap.addMaxHP(5);
                break;
            case 61:
                cap.addStrength(1);
                break;
            case 62:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 63:
                cap.addStrength(1);
                break;
            case 64:
                cap.addMagic(1);
                break;
            case 65:
                cap.addStrength(1);
                cap.addMaxHP(5);
                break;
            case 66:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 67:
                cap.addStrength(1);
                break;
            case 68:
                cap.addMagic(1);
                break;
            case 69:
                cap.addStrength(1);
                break;
            case 70:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addMaxHP(5);
                break;
            case 71:
                cap.addStrength(1);
                break;
            case 72:
                cap.addMagic(1);
                break;
            case 73:
                cap.addStrength(1);
                break;
            case 74:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 75:
                cap.addStrength(1);
                cap.addMaxHP(5);
                break;
            case 76:
                cap.addMagic(1);
                break;
            case 77:
                cap.addStrength(1);
                break;
            case 78:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 79:
                cap.addStrength(1);
                break;
            case 80:
                cap.addMagic(1);
                cap.addMaxHP(5);
                break;
            case 81:
                cap.addStrength(1);
                break;
            case 82:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 83:
                cap.addStrength(1);
                break;
            case 84:
                cap.addMagic(1);
                break;
            case 85:
                cap.addStrength(1);
                cap.addMaxHP(5);
                break;
            case 86:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 87:
                cap.addStrength(1);
                break;
            case 88:
                cap.addMagic(1);
                break;
            case 89:
                cap.addStrength(1);
                break;
            case 90:
                cap.addMagic(1);
                cap.addDefense(1);
                cap.addMaxHP(5);
                break;
            case 91:
                cap.addStrength(1);
                break;
            case 92:
                cap.addMagic(1);
                break;
            case 93:
                cap.addStrength(1);
                break;
            case 94:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 95:
                cap.addStrength(1);
                cap.addMaxHP(5);
                break;
            case 96:
                cap.addMagic(1);
                break;
            case 97:
                cap.addStrength(1);
                break;
            case 98:
                cap.addMagic(1);
                cap.addDefense(1);
                break;
            case 99:
                cap.addStrength(1);
                break;
            case 100:
                cap.addStrength(10);
                cap.addDefense(10);
                cap.addMagic(10);
                cap.addMaxHP(5);
                break;
        }

        if (level % 4 == 0) {
            player.setHealth(cap.getMaxHP());
            player.getFoodStats().addStats(20, 0);
            cap.addMaxMP(4);
            cap.setMP(cap.getMaxMP());
        }

        if (level % 2 == 0) {
            cap.addMaxAP(1);
        }
        
    }

}
