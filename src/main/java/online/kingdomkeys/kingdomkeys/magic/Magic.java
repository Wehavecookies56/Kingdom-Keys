package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    int cost;
    boolean hasToSelect;
    int order;

    public Magic(String registryName, int cost, boolean hasToSelect, int order) {
    	this.name = registryName;
    	this.cost = cost;
    	this.hasToSelect = hasToSelect;
    	this.order = order;
        setRegistryName(registryName);
    }
    
    public int getCost() {
    	return cost;
    }
    
    public boolean getHasToSelect() {
    	return hasToSelect;
    }
    
    public abstract void onUse(PlayerEntity player);

	public int getOrder() {
		return order;
	}

}