package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    int cost;

    public Magic(String registryName, int cost) {
    	this.name = registryName;
    	this.cost = cost;
        setRegistryName(registryName);
    }
    
    public int getCost() {
    	return cost;
    }
    
    public abstract void onUse(PlayerEntity player);

}