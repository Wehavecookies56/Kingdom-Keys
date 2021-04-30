package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    int cost;
    boolean hasToSelect;
    int order;
    int maxLevel;
    String translationKey;

    public Magic(String registryName, int cost, boolean hasToSelect, int maxLevel, int order) {
    	this.name = registryName;
    	this.cost = cost;
    	this.hasToSelect = hasToSelect;
    	this.order = order;
    	this.maxLevel = maxLevel;
        setRegistryName(registryName);
        translationKey = "magic." + new ResourceLocation(registryName).getPath() + ".name";
    }

    public String getTranslationKey() {
        return getTranslationKey(1);
    }
    
    public String getTranslationKey(int level) {
        return translationKey.replace(".name", level+".name");
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
    public abstract void onUse(PlayerEntity player, PlayerEntity caster, int level);

	public int getOrder() {
		return order;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

}