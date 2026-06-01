package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;

public abstract class Magic {

    ResourceLocation name;
    boolean hasTargetSelector;
    int maxLevel;
    String translationKey;
    String gmAbility;
    
	private MagicData data;	

    public Magic(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
    	this.name = registryName;
    	this.hasTargetSelector = hasToSelect;
    	this.maxLevel = maxLevel - 1;
    	this.gmAbility = gmAbility;
        translationKey = "magic." + registryName.getNamespace() + "." + registryName.getPath() + ".name";
    }

    public int getCasttimeTicks(int level) {
    	return data.getCasttime(level);
    }
    
    public String getTranslationKey() {
        return getTranslationKey(0);
    }
    
    public String getTranslationKey(int level) {
        return translationKey.replace(".name", level+".name");
    }
    
    public double getCost(int lvl, Player player) {
    	PlayerData playerData = PlayerData.get(player);
    	double cost = data.getCost(lvl);
    	if(cost != 300)
    		cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
    	return Math.max(1, cost);
    }
    
    public float getDamageMult(int lvl) {
    	return data.getDmgMult(lvl);
    }

	public float getDamageMultMax(int lvl) {
		return data.getDmgMultMax(lvl);
	}

	public float getRealDamageMult(int lvl, Player player) {
		if (getMaxLocalLevel(lvl) <= 1) {
			return getDamageMult(lvl);
		}

		PlayerData playerData = PlayerData.get(player);
		int localLevel = Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName().toString(), lvl);

		float t = (float)(localLevel - 1) / (getMaxLocalLevel(lvl) - 1);
		float base = getDamageMult(lvl);
		float max = getDamageMultMax(lvl);

		float dmg = base + (max - base) * t;
		//System.out.println("localLevel=" + localLevel + " maxLevel=" + getMaxExpLevel(lvl) + " base=" + base + " max=" + max + " dmg=" + dmg);
		return dmg;
	}
    
    public boolean getHasToSelect() {
    	return hasTargetSelector;
    }
    
    public boolean getMagicLockOn(int lvl) {
    	return data.getMagicLockOn(lvl);
    }

	public int getMaxExp(int lvl) {
		return data.getMaxExp(lvl);
	}

	public int getMaxLocalLevel(int lvl) {
		return data.getMaxLocalLevel(lvl);
	}
    
    public Ability getGMAbility() {
    	if(gmAbility == null)
    		return null;
    	return ModAbilities.registry.get(ResourceLocation.parse(gmAbility));
    }
    
    public MagicData getMagicData() {
    	return data;
    }
    
    public void setMagicData(MagicData data) {
    	this.data = data;
    }

    public void magicUse(LivingEntity player, LivingEntity caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {

    }

	public int getMagicLocalLevel(Player player, int lvl) {
		PlayerData playerData = PlayerData.get(player);
		return Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName().toString(), lvl);
	}

    /**
     * If player and caster are different it means the magic was casted from a target selector to another player in the party
     * @param player
     * @param caster
     */
    public final void onUse(LivingEntity player, Player caster, int level, LivingEntity lockOnEntity) {
    	PlayerData casterData = PlayerData.get(caster);
	    float fullMPBlastMult = 1F;

	    if (casterData.isAbilityEquipped(Strings.fullMPBlast) && casterData.getMP() >= casterData.getMaxMP()) {
		    int stacks = casterData.getNumberOfAbilitiesEquipped(Strings.fullMPBlast);
		    fullMPBlastMult = (float) (2F - Math.pow(0.5F, stacks));
	    }
    	//if(hasRC()) {// If the magic has a Grand Magic and the timer is not 1 (GM is not disabled in the config)
		//int maxLevel = casterData.getMagicLevel(name);
    	if(level > maxLevel){ // Grand Magic, set GM variable to 0 and not consume MP
			casterData.setMagicUses(name, 0);
		} else { // If it's not using a grand magic add a point and remove MP
			casterData.addMagicUses(name, 1);
			casterData.remMP(getCost(level, caster));

			if(getMagicData() != null) { //If the magic exists and has data and has Grand Magic
				if(getRCProb(casterData, level)) {// If the actual uses is equals or above the required
					//If player has max level magic (and doesnt have GM) don't give RC
					if(!(getGMAbility() == null && level == getMaxLevel())) {
						ReactionCommand reactionCommand = ModReactionCommands.registry.get(ResourceLocation.parse(getRegistryName().toString()));
						if(reactionCommand != null) {
							int duration = (int) (reactionCommand.getDuration() + reactionCommand.getDuration() * (casterData.getNumberOfAbilitiesEquipped(Strings.grandMagicExtender) * 0.25F));
							casterData.addReactionCommand(getRegistryName().toString(), caster, duration);
						}
					} else {

					}
					casterData.setMagicUses(name, 0);
					PacketHandler.sendTo(new SCSyncPlayerData(caster), (ServerPlayer)caster);
				}				
			}			
		}
    	int cd = (int)(data.getCooldown(level) * (1 - casterData.getNumberOfAbilitiesEquipped(Strings.endlessMagic) * 0.2));
		casterData.setMagicCooldownTicks(Math.max(cd,5));
		
		if(casterData.isAbilityEquipped(Strings.wizardsRuse)) { //Wizard's Ruse has a chance to heal the player based on the amount of stacked abilities and amount healed based on the cost of the ability
			double num = player.level().random.nextDouble();
			if(num < (0.25+(0.125*(casterData.getNumberOfAbilitiesEquipped(Strings.wizardsRuse)-1)))){
				caster.heal((int) getCost(level, caster)/2);
			}
		}
		
		if(!casterData.isAbilityEquipped(Strings.magicLockOn)) {
			lockOnEntity = null;
		}
		
		playMagicCastSound(player,caster,level);
		Utils.castMagic cast = new Utils.castMagic(player, caster, level, fullMPBlastMult, lockOnEntity, this);
		casterData.setCastedMagic(cast);

		//MinecraftForge.EVENT_BUS.post(new UpdatePlayerMotionEvent.BaseLayer((LocalPlayerPatch) player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null), KKLivingMotionsEnum.SPELL));
		PacketHandler.sendTo(new SCSyncPlayerData(caster), (ServerPlayer) caster);
    }

    public abstract void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity);

	public void playMagicCastSound2(LivingEntity player, Player caster, int level) {
		playMagicCastSound(player,caster,level);
	}

    protected abstract void playMagicCastSound(LivingEntity player, Player caster, int level);

	private boolean getRCProb(PlayerData casterData, int level) {
		int prob = casterData.getNumberOfAbilitiesEquipped(Strings.grandMagicHaste) * 10;

		if(gmAbility != null && casterData.isAbilityEquipped(gmAbility) && level == getMaxLevel()) {
			prob += casterData.getNumberOfAbilitiesEquipped(gmAbility) * 10;
		}
		prob += (casterData.getMagicUses(name)-1)*5;
		double num = Math.random()*100;
		return num <= prob;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

}