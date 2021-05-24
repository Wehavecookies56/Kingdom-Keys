package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import java.util.Map;

import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

/**
 * Class to contain keyblade level information
 */
public class KeybladeLevel {

    //The stats when upgraded to this level
    private int strength;
    private int magic;
    //The materials required to upgrade to this level, key is the material, value is the quantity
    private Map<Material, Integer> materials;
    //private Map<Material, Integer> materialsList;
    //The ability gained when upgrading to this level
    
    //TODO ability system and potentially multiple abilities here
    String ability;

    public KeybladeLevel() { }

    public KeybladeLevel(KeybladeLevelBuilder keybladeLevelBuilder)
    {
        if(keybladeLevelBuilder.ability != null)
            this.ability = keybladeLevelBuilder.ability;
        this.magic = keybladeLevelBuilder.magic;
        this.strength = keybladeLevelBuilder.strength;
        this.materials = keybladeLevelBuilder.materials;
    }


    public void setAbility(String ability) {
        this.ability = ability;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setMaterials(Map<Material, Integer> materials) {
        this.materials = materials;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public String getAbility() { return this.ability; }

    public int getMagic() {
        return magic;
    }

    public int getStrength() {
        return strength;
    }

    public Map<Material, Integer> getMaterialList() {
    	return materials;
    }

    public KeybladeLevel(int strength, int magic, Map<Material, Integer> materials, String... abilities) {
        this.strength = strength;
        this.magic = magic;
        this.materials = materials;
        this.ability = ability;
    }

    @Override
    public String toString() {
        return String.format("KeybladeLevel[strength:%d, magic:%d, materials[%d]]", strength, magic, materials.size());
    }

    public static class KeybladeLevelBuilder{
        private int strength;
        private int magic;
        private Map<Material, Integer> materials;
        private String ability;

        public KeybladeLevelBuilder() { }

        public KeybladeLevelBuilder withStats(int str, int mag)
        {
            this.strength = str;
            this.magic = mag;
            return this;
        }

        public KeybladeLevelBuilder withAbilties(String... abilities)
        {
            this.ability = abilities[0];
            return this;
        }

        public KeybladeLevelBuilder withMaterials(KeybladeStats.Recipe recipe)
        {
            this.materials = recipe.asMap();
            return this;
        }

        public KeybladeLevel build()
        {
            return new KeybladeLevel(this);
        }
    }
}

