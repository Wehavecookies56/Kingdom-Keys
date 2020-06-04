package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    int cost;
    int order;

    public Magic(String registryName, int cost, int order) {
    	this.name = registryName;
    	this.cost = cost;
    	this.order = order;
        setRegistryName(registryName);
    }
    
    public int getCost() {
    	return cost;
    }
    
    public abstract void onUse(PlayerEntity player);

	public int getOrder() {
		return order;
	}

}