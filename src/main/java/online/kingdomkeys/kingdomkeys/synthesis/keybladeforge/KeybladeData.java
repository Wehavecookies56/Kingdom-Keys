package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class KeybladeData {

    //The keychain that summons the keyblade this is for, if null the upgrade levels are ignored and only base stats are used
    @Nullable
	public KeychainItem keychain;
    //The level 0 stats
    int baseStrength, baseMagic;
    String baseAbility;
    //List of upgrades for the keyblade
    @Nullable List<KeybladeLevel> levels;
    //Description for the tooltip of the keyblade
    String description;

    public KeybladeData() {

    }

    public KeybladeData(@Nullable KeychainItem keychain, @Nullable List<KeybladeLevel> levels, String description, int baseStrength, int baseMagic) {
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
    
    public String getAbility(int level) {
        return levels.get(level-1).getAbility();
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeychain(Item keychain) {
    	if(keychain instanceof KeychainItem) {
    		this.keychain = (KeychainItem) keychain;
    	} else {
    		this.keychain = null;
    	}
    }
    
    public void setBaseAbility(String ability) {
    	this.baseAbility = ability;
    }
    
    public String getBaseAbility() {
    	return baseAbility;
    }

    public KeychainItem getKeychain() {
    	return keychain;
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
    
    /*public String materialsToString(KeybladeLevel level) {
    	String mat = "";
    	for(Entry<Material, Integer> m : level.getMaterialList().entrySet()) {
    		mat += m.getKey()+"?"+m.getValue();
    	}
    	return mat;
    }
    
    public String levelsToString(){
    	String levels = ""; //Get a string from the levels
    	for(int i = 0;i<this.levels.size();i++) {
    		KeybladeLevel level = this.levels.get(i);
    		levels+= ";"+level.getAbility()+";"+level.getStrength()+";"+level.getMagic()+";"+materialsToString(level);
    	}
    	return levels;
    }
    
    @Override
    public String toString() {
    	String s = keychain.getRegistryName()+","+ levelsToString()+","+baseStrength+","+baseMagic+","+description;
    	return super.toString();
    }*/
}
