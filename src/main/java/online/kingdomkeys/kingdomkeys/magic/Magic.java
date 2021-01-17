package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    int cost;
    boolean hasToSelect;
    int order;

    String translationKey;

    public Magic(String registryName, int cost, boolean hasToSelect, int order) {
    	this.name = registryName;
    	this.cost = cost;
    	this.hasToSelect = hasToSelect;
    	this.order = order;
        setRegistryName(registryName);
        translationKey = "magic." + new ResourceLocation(registryName).getPath() + ".name";
    }

    public String getTranslationKey() {
        return translationKey;
    }
    
    public int getCost() {
    	return cost;
    }
    
    public boolean getHasToSelect() {
    	return hasToSelect;
    }
    
    /**
     * If player and caster are different it means the magic was casted from a target selector to another player in the party
     * @param player
     * @param caster
     */
    public abstract void onUse(PlayerEntity player, PlayerEntity caster);

	public int getOrder() {
		return order;
	}

}