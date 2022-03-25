package online.kingdomkeys.kingdomkeys.ability;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Ability extends ForgeRegistryEntry<Ability> {

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
    
    public int getAPCost() {
    	return apCost;
    }    

	public int getOrder() {
		return order;
	}
	
	public AbilityType getType() {
		return type;
	}

}