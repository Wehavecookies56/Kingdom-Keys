package online.kingdomkeys.kingdomkeys.ability;

import net.minecraft.resources.ResourceLocation;

public class Ability {

	public static enum AbilityType{
		ACTION, GROWTH, SUPPORT, WEAPON, ACCESSORY
	}
	
    String name;
    int apCost;
    AbilityType type;
    int order;
    String translationKey;

    public Ability(String registryName, int apCost, AbilityType type, int order) {
    	this.name = registryName;
    	this.apCost = apCost;
    	this.type = type;
    	this.order = order;
        translationKey = "ability." + new ResourceLocation(registryName).getPath() + ".name";
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
		return name;
	}

	public ResourceLocation getRegistryName() {
		return new ResourceLocation(name);
	}
}