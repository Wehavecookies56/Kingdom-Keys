package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKLivingMotionsEnum;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import yesman.epicfight.api.client.forgeevent.UpdatePlayerMotionEvent;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

public abstract class Magic {

    ResourceLocation name;
    boolean hasTargetSelector;
    int order;
    int maxLevel;
    String translationKey;
    String gmAbility;
    
	private MagicData data;	

    public Magic(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility, int order) {
    	this.name = registryName;
    	this.hasTargetSelector = hasToSelect;
    	this.order = order;
    	this.maxLevel = maxLevel - 1;
    	this.gmAbility = gmAbility;
        translationKey = "magic." + registryName.getNamespace() + "." + registryName.getPath() + ".name";
    }

    public String getTranslationKey() {
        return getTranslationKey(0);
    }
    
    public String getTranslationKey(int level) {
        return translationKey.replace(".name", level+".name");
    }
    
    public double getCost(int lvl, Player player) {
    	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
    	double cost = data.getCost(lvl);
    	if(cost != 300)
    		cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
    	return Math.max(1, cost);
    }
    
    public float getDamageMult(int lvl) {
    	return data.getDmgMult(lvl);
    }
    
    public boolean getHasToSelect() {
    	return hasTargetSelector;
    }
    
    public Ability getGMAbility() {
    	if(gmAbility == null)
    		return null;
    	return ModAbilities.registry.get().getValue(new ResourceLocation(gmAbility));
    }
    
    public MagicData getMagicData() {
    	return data;
    }
    
    public void setMagicData(MagicData data) {
    	this.data = data;
    }
   
    protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {

    }
    
    /**
     * If player and caster are different it means the magic was casted from a target selector to another player in the party
     * @param player
     * @param caster
     */
    public final void onUse(Player player, Player caster, int level) {
    	IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
    	float fullMPBlastMult = casterData.isAbilityEquipped(Strings.fullMPBlast) && casterData.getMP() >= casterData.getMaxMP() ? 1.5F: 1F;
    	
    	//if(hasRC()) {// If the magic has a Grand Magic and the timer is not 1 (GM is not disabled in the config)
		int maxLevel = casterData.getMagicLevel(name);
    	if(level > maxLevel){ // Grand Magic, set GM variable to 0 and not consume MP
			casterData.setMagicUses(name, 0);
		} else { // If it's not using a grand magic add a point and remove MP
			casterData.addMagicUses(name, 1);
			casterData.remMP(getCost(level, player));

			if(getMagicData() != null) { //If the magic exists and has data and has Grand Magic
				if(getRCProb(casterData)) {// If the actual uses is equals or above the required
					//If player has max level magic (and doesnt have GM) don't give RC
					if(!(getGMAbility() == null && level == getMaxLevel())) {
						casterData.addReactionCommand(KingdomKeys.MODID + ":" +getRegistryName().getPath(), caster);
					} else {
						//System.out.println(level+" "+getMaxLevel()+" disabled RC");
					}
					casterData.setMagicUses(name, 0);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayer)caster);
				}				
			}			
		}
    	int cd = (int)(data.getCooldown(level) * (1 - casterData.getNumberOfAbilitiesEquipped(Strings.endlessMagic) * 0.2));
		casterData.setMagicCooldownTicks(Math.max(cd,5));
		
		if(casterData.isAbilityEquipped(Strings.wizardsRuse)) { //Wizard's Ruse has a chance to heal the player based on the amount of stacked abilities and amount healed based on the cost of the ability
			double num = player.level.random.nextDouble();
			if(num < (0.25+(0.125*(casterData.getNumberOfAbilitiesEquipped(Strings.wizardsRuse)-1)))){
				caster.heal((int) getCost(level, player)/2);
			}
		}
		
    	magicUse(player, caster, level, fullMPBlastMult);
    	caster.swing(InteractionHand.MAIN_HAND, true);
		//TODO magic efm
		//MinecraftForge.EVENT_BUS.post(new UpdatePlayerMotionEvent.BaseLayer((LocalPlayerPatch)
		//		player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null), KKLivingMotionsEnum.SPELL));
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayer) caster);
    }

	private boolean getRCProb(IPlayerCapabilities casterData) {
		int prob = casterData.getNumberOfAbilitiesEquipped(Strings.grandMagicHaste) * 10;

		if(gmAbility != null && casterData.isAbilityEquipped(gmAbility) && casterData.getMagicLevel(getRegistryName()) == getMaxLevel()) {
			prob += casterData.getNumberOfAbilitiesEquipped(gmAbility) * 10;
		}
		prob += (casterData.getMagicUses(name)-1)*5;
		//System.out.println(prob);
		double num = Math.random()*100;
		return num <= prob;
	}

	public int getOrder() {
		return order;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

}