package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class KeybladeData {

    //The keychain that summons the keyblade this is for, if null the upgrade levels are ignored and only base stats are used
    @Nullable Item keychain;
    //The level 0 stats
    int baseStrength, baseMagic;
    //List of upgrades for the keyblade
    @Nullable List<KeybladeLevel> levels;
    //Description for the tooltip of the keyblade
    String description;

    public KeybladeData() {

    }

    public KeybladeData(@Nullable Item keychain, @Nullable List<KeybladeLevel> levels, String description, int baseStrength, int baseMagic) {
        this.keychain = keychain;
        this.levels = levels;
        this.description = description;
        this.baseStrength = baseStrength;
        this.baseMagic = baseMagic;
    }

    //Returns the base strength if level is 0
    public int getStrength(int level) {
        return level == 0 ? baseStrength : levels.get(level-1).getStrength();
    }

    //Returns the base magic if level is 0
    public int getMagic(int level) {
        return level == 0 ? baseMagic : levels.get(level-1).getMagic();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeychain(Item keychain) {
        this.keychain = keychain;
    }

    public void setLevels(List<KeybladeLevel> levels) {
        this.levels = levels;
    }

    public KeybladeLevel getLevelData(int level) {
        return levels.get(level);
    }

    public void setBaseMagic(int baseMagic) {
        this.baseMagic = baseMagic;
    }

    public void setBaseStrength(int baseStrength) {
        this.baseStrength = baseStrength;
    }
}
