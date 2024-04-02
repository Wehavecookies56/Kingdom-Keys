package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKPotionItem extends Item implements IItemCategory {
	
	public static enum PotionType {
		HP, MP, HPMP, DRIVE, FOCUS
	}
	
	PotionType type;
	double amount;
	boolean percentage;
	boolean all;
	
    public KKPotionItem(Item.Properties properties, PotionType type, double amount, boolean perc, boolean all) {
        super(properties);
		this.type = type;
		this.amount = amount;
		this.percentage = perc;
		this.all = all;
    }

    public void potionEffect (Player player) {
    	IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		//Utils.reviveFromKO(player);

		switch(type) {
    	case HP:
        	float hpAmount = (float) (percentage ? player.getMaxHealth() * amount / 100 : amount);
        	hpAmount += hpAmount * playerData.getNumberOfAbilitiesEquipped(Strings.itemBoost) / 2;
        	player.heal(hpAmount);
    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
			Utils.reviveFromKO(player);

    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
    			Party party = worldData.getPartyFromMember(player.getUUID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUUID())) {
    						Player target = player.level().getPlayerByUUID(m.getUUID());
    						if(target.distanceTo(player) < ModConfigs.partyRangeLimit) {
	    			        	hpAmount = (float) (percentage ? target.getMaxHealth() * amount / 100 : amount);
	    			        	hpAmount += hpAmount * playerData.getNumberOfAbilitiesEquipped(Strings.itemBoost) / 2;
	    						target.heal(hpAmount);
	    						Utils.reviveFromKO(target);

	    			    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    						}
    					}
    				}
    			}
    		}
    		break;
    	case MP:
        	float mpAmount = (float) (percentage ? playerData.getMaxMP() * amount / 100 : amount);
    		playerData.addMP(mpAmount);
    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
    			Party party = worldData.getPartyFromMember(player.getUUID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUUID())) {
    						Player target = player.level().getPlayerByUUID(m.getUUID());
    						IPlayerCapabilities targetData = ModCapabilities.getPlayer(target);
    						if(target.distanceTo(player) < ModConfigs.partyRangeLimit) {
	    						mpAmount = (float) (percentage ? targetData.getMaxMP() * amount / 100 : amount);
	    			        	targetData.addMP(mpAmount);
	    			    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    						}
    			    		PacketHandler.syncToAllAround(target, targetData);
    					}
    				}
    			}
    		}
    		break;
    	case HPMP:
    		mpAmount = (float) (percentage ? playerData.getMaxMP() * amount / 100 : amount);
    		hpAmount = (float) (percentage ? player.getMaxHealth() * amount / 100 : amount);
        	hpAmount += hpAmount * playerData.getNumberOfAbilitiesEquipped(Strings.itemBoost) / 2;
        	
    		playerData.addMP(mpAmount);
			Utils.reviveFromKO(player);
    		player.heal(hpAmount);
    		player.level().playSound(null, player.blockPosition(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
    			Party party = worldData.getPartyFromMember(player.getUUID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUUID())) {
    						Player target = player.level().getPlayerByUUID(m.getUUID());
    						IPlayerCapabilities targetData = ModCapabilities.getPlayer(target);
    						if(target.distanceTo(player) < ModConfigs.partyRangeLimit) {
	    						mpAmount = (float) (percentage ? targetData.getMaxMP() * amount / 100 : amount);
	    						hpAmount = (float) (percentage ? target.getMaxHealth() * amount / 100 : amount);
	    			        	hpAmount += hpAmount * playerData.getNumberOfAbilitiesEquipped(Strings.itemBoost) / 2;
	    			        	Utils.reviveFromKO(target);
	    			        	targetData.addMP(mpAmount);
	    						target.heal(hpAmount);
	    			    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    						}
    			    		PacketHandler.syncToAllAround(target, targetData);
    					}
    				}
    			}
    		}
    		break;
    	case DRIVE:
    		float dpAmount = (float) (percentage ? playerData.getMaxDP() * amount / 100 : amount);
    		playerData.addDP(dpAmount);
    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
    			Party party = worldData.getPartyFromMember(player.getUUID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUUID())) {
    						Player target = player.level().getPlayerByUUID(m.getUUID());
    						IPlayerCapabilities targetData = ModCapabilities.getPlayer(target);
    						if(target.distanceTo(player) < ModConfigs.partyRangeLimit) {
	    						dpAmount = (float) (percentage ? targetData.getMaxDP() * amount / 100 : amount);
	    			        	targetData.addDP(dpAmount);
	    			    		player.level().playSound(null, target.blockPosition(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    						}
    			    		PacketHandler.syncToAllAround(target, targetData);
    					}
    				}
    			}
    		}
    		break;
    	case FOCUS:
    		float focusAmount = (float) (percentage ? playerData.getMaxFocus() * amount / 100 : amount);
    		playerData.addFocus(focusAmount);
    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
    			Party party = worldData.getPartyFromMember(player.getUUID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUUID())) {
    						Player target = player.level().getPlayerByUUID(m.getUUID());
    						IPlayerCapabilities targetData = ModCapabilities.getPlayer(target);
    						if(target.distanceTo(player) < ModConfigs.partyRangeLimit) {
	    						focusAmount = (float) (percentage ? targetData.getMaxFocus() * amount / 100 : amount);
	    			        	targetData.addFocus(focusAmount);
	    			    		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.potion.get(), SoundSource.PLAYERS, 1, 1);
    						}
    			    		PacketHandler.syncToAllAround(target, targetData);
    					}
    				}
    			}
    		}
    		break;
    	}
		PacketHandler.syncToAllAround(player, playerData);

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    	String sType = Utils.translateToLocal("potion.desc."+type.toString().toLowerCase());
    	String beginning = Utils.translateToLocal("potion.desc.beginning", (int)amount, percentage ? "%":"", sType);
    	String end = Utils.translateToLocal(all ? "potion.desc.toall" : "potion.desc.toone");
		tooltip.add(Component.translatable(beginning + end));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.CONSUMABLE;
	}

	public boolean isGlobal() {
		return all;
	}
}
