package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    int cost;
    boolean hasTargetSelector;
    int order;
    int maxLevel;
    String translationKey;
    boolean hasRC;

    public Magic(String registryName, int cost, boolean hasToSelect, int maxLevel, boolean hasRC, int order) {
    	this.name = registryName;
    	this.cost = cost;
    	this.hasTargetSelector = hasToSelect;
    	this.order = order;
    	this.maxLevel = maxLevel - 1;
    	this.hasRC = hasRC;
        setRegistryName(registryName);
        translationKey = "magic." + new ResourceLocation(registryName).getPath() + ".name";
    }

    public String getTranslationKey() {
        return getTranslationKey(0);
    }
    
    public String getTranslationKey(int level) {
        return translationKey.replace(".name", level+".name");
    }
    
    public int getCost() {
    	return cost;
    }
    
    public boolean getHasToSelect() {
    	return hasTargetSelector;
    }
    
    public boolean hasRC() {
    	return hasRC;
    }
    
   
    protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {

    }
    
    /**
     * If player and caster are different it means the magic was casted from a target selector to another player in the party
     * @param player
     * @param caster
     */
    public final void onUse(PlayerEntity player, PlayerEntity caster, int level) {
    	IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
    	if(hasRC()) {
			int maxLevel = casterData.getMagicLevel(name);
	    	if(level > maxLevel){ 
				casterData.setMagicUses(name, 0);
			} else {
				casterData.addMagicUses(name, 1);
			}
    	}
    	PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity) caster);
    	magicUse(player, caster, level);
    }

	public int getOrder() {
		return order;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

}