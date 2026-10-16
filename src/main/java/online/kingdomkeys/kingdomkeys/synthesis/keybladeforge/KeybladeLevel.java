package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats;

import java.util.ArrayList;
import java.util.List;
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
    //The abilities gained when upgrading to this level, in the order the file lists them
    private List<ResourceLocation> abilities = new ArrayList<>();

    public KeybladeLevel() { }

    public KeybladeLevel(KeybladeLevelBuilder keybladeLevelBuilder)
    {
        this.abilities = new ArrayList<>(keybladeLevelBuilder.abilities);
        this.magic = keybladeLevelBuilder.magic;
        this.strength = keybladeLevelBuilder.strength;
        this.materials = keybladeLevelBuilder.materials;
    }

    public void setAbility(ResourceLocation ability) {
        this.abilities = new ArrayList<>();
        if (ability != null)
            this.abilities.add(ability);
    }

    public void setAbilities(List<ResourceLocation> abilities) {
        this.abilities = new ArrayList<>(abilities);
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

	/** The first of this level's abilities, or null if it grants none */
	public ResourceLocation getAbility() {
		return abilities.isEmpty() ? null : abilities.getFirst();
	}

	public List<ResourceLocation> getAbilities() {
		return abilities;
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
        setAbility(ability);
    }

    @Override
    public String toString() {
        return String.format("KeybladeLevel[strength:%d, magic:%d, materials[%d]]", strength, magic, materials.size());
    }

    public static class KeybladeLevelBuilder{
        private int strength;
        private int magic;
        private Map<Item, Integer> materials;
        private final List<ResourceLocation> abilities = new ArrayList<>();

        public KeybladeLevelBuilder() { }

		public KeybladeLevelBuilder withStats(int str, int mag) {
			this.strength = str;
			this.magic = mag;
			return this;
		}

		public KeybladeLevelBuilder withAbility(ResourceLocation ability) {
			if (ability != null)
				this.abilities.add(ability);
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

