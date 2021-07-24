package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public abstract class Magic extends ForgeRegistryEntry<Magic> {

    String name;
    boolean hasTargetSelector;
    int order;
    int maxLevel;
    String translationKey;
    boolean hasRC;
    
	private MagicData data;	

    public Magic(String registryName, boolean hasToSelect, int maxLevel, boolean hasRC, int order) {
    	this.name = registryName;
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
    
    public int getCost(int lvl) {
    	return data.getCost(lvl);
    }
    
    public float getDamageMult(int lvl) {
    	return data.getDmgMult(lvl);
    }
    
	public int getUsesToGM(int lvl) {
		return data.getUsesToGM(lvl);
	}
    
    public boolean getHasToSelect() {
    	return hasTargetSelector;
    }
    
    public boolean hasRC() {
    	return hasRC;
    }
    
    public MagicData getMagicData() {
    	return data;
    }
    
    public void setMagicData(MagicData data) {
    	this.data = data;
    }
   
    protected void magicUse(Player player, Player caster, int level) {

    }
    
    /**
     * If player and caster are different it means the magic was casted from a target selector to another player in the party
     * @param player
     * @param caster
     */
    public final void onUse(Player player, Player caster, int level) {
    	IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
    	if(hasRC() && ModConfigs.magicUsesTimer != 1) {
			int maxLevel = casterData.getMagicLevel(name);
	    	if(level > maxLevel){ 
				casterData.setMagicUses(name, 0);
			} else {
				casterData.addMagicUses(name, 1);
				casterData.remMP(getCost(level));
			}
    	} else {
			casterData.remMP(getCost(level));
    	}
		casterData.setMagicCooldownTicks(data.getCooldown(level));

    	magicUse(player, caster, level);
    	caster.swing(InteractionHand.MAIN_HAND, true);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayer) caster);
    }

	public int getOrder() {
		return order;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

}