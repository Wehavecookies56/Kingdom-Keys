package online.kingdomkeys.kingdomkeys;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class Ability extends ForgeRegistryEntry<Ability> {

	public static enum Type{
		ACTION, GROWTH, SUPPORT
	}
	
    String name;
    int apCost;
    Type type;
    int order;

    public Ability(String registryName, int apCost, Type type, int order) {
    	this.name = registryName;
    	this.apCost = apCost;
    	this.type = type;
    	this.order = order;
        setRegistryName(registryName);
    }
    
    public int getAPCost() {
    	return apCost;
    }    

	public int getOrder() {
		return order;
	}
	
	public Type getType() {
		return type;
	}

}