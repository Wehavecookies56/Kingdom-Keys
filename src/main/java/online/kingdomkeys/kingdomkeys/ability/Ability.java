package online.kingdomkeys.kingdomkeys.ability;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class Ability {

	public static enum AbilityType{
		ACTION, GROWTH, SUPPORT, WEAPON, ACCESSORY
	}
	
    ResourceLocation name;
    int apCost;
    AbilityType type;
    int order;
    String translationKey;

    public Ability(ResourceLocation registryName, int apCost, AbilityType type, int order) {
    	this.name = registryName;
    	this.apCost = apCost;
    	this.type = type;
    	this.order = order;
        translationKey = "ability." + registryName.getPath() + ".name";
    }

    public String getTranslationKey() {
    	return translationKey;
	}

	public String getTranslationKey(int level) {
		if (level > 0) {
			return translationKey.replace(".name", "_" + level + ".name");
		} else {
			return getTranslationKey();
		}
	}

	public String getDescTranslationKey() {
		return translationKey.replace(".name", ".desc");
	}
    
    public int getAPCost() {
    	return apCost;
    }    

	public int getOrder() {
		return order;
	}
	
	public AbilityType getType() {
		return type;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

	public int compareTo(Ability other) {
		int typeOrder = type.compareTo(other.getType());
		return typeOrder == 0 ? Integer.compare(order, other.getOrder()) : typeOrder;
	}
}