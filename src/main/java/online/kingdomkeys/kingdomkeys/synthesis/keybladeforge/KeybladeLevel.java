package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats;

import java.util.Map;

/**
 * Class to contain keyblade level information
 */
public class KeybladeLevel {

    //The stats when upgraded to this level
    private int strength;
    private int magic;
    //The materials required to upgrade to this level, key is the material, value is the quantity
    private Map<Item, Integer> materials;
    //private Map<Material, Integer> materialsList;
    //The ability gained when upgrading to this level
    
    //TODO ability system and potentially multiple abilities here
    private ResourceLocation ability;

    public KeybladeLevel() { }

    public KeybladeLevel(KeybladeLevelBuilder keybladeLevelBuilder)
    {
        if(keybladeLevelBuilder.ability != null)
            this.ability = keybladeLevelBuilder.ability;
        this.magic = keybladeLevelBuilder.magic;
        this.strength = keybladeLevelBuilder.strength;
        this.materials = keybladeLevelBuilder.materials;
    }

    public void setAbility(ResourceLocation ability) {
        this.ability = ability;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setMaterials(Map<Item, Integer> materials) {
        this.materials = materials;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

	public ResourceLocation getAbility() {
		return this.ability;
	}

    public int getMagic() {
        return magic;
    }

    public int getStrength() {
        return strength;
    }

    public Map<Item, Integer> getMaterialList() {
    	return materials;
    }

    public KeybladeLevel(int strength, int magic, Map<Item, Integer> materials, ResourceLocation ability) {
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
        private Map<Item, Integer> materials;
        private ResourceLocation ability;

        public KeybladeLevelBuilder() { }

		public KeybladeLevelBuilder withStats(int str, int mag) {
			this.strength = str;
			this.magic = mag;
			return this;
		}

		public KeybladeLevelBuilder withAbility(ResourceLocation ability) {
			this.ability = ability;
			return this;
		}

		public KeybladeLevelBuilder withMaterials(KeybladeStats.Recipe recipe) {
			this.materials = recipe.asMap();
			return this;
		}

		public KeybladeLevel build() {
			return new KeybladeLevel(this);
		}
	}
}

