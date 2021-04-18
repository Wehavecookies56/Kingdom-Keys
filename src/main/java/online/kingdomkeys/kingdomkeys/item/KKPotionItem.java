package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

   /* @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    	IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerIn);
        if (playerData.getAlignment() != Utils.OrgMember.NONE) {
        	if(Utils.isWearingOrgRobes(playerIn)) {
        		playerIn.sendStatusMessage(new TranslationTextComponent("gui.proofofheart.unequip"), true);
        	} else {
        		if(worldIn.isRemote) {
					if(Utils.findSummoned(playerIn.inventory, playerData.getEquippedWeapon(), true) > -1)
						PacketHandler.sendToServer(new CSSummonKeyblade(true, playerData.getAlignment()));
        		}
        		playerIn.sendStatusMessage(new TranslationTextComponent("gui.proofofheart.leftorg"), true);

        		if(playerIn.getHeldItemMainhand() != null && playerIn.getHeldItemMainhand().getItem() == this) {
        			playerIn.getHeldItemMainhand().shrink(1);
    	            playerData.setAlignment(Utils.OrgMember.NONE);
    	            return super.onItemRightClick(worldIn, playerIn, handIn);
        		}
        		
        		if(playerIn.getHeldItemOffhand() != null && playerIn.getHeldItemOffhand().getItem() == this) {
        			playerIn.getHeldItemOffhand().shrink(1);
    	            playerData.setAlignment(Utils.OrgMember.NONE);
    	            return super.onItemRightClick(worldIn, playerIn, handIn);
        		}
        		
        	}
        } else {
    		playerIn.sendStatusMessage(new TranslationTextComponent("gui.proofofheart.notinorg"), true);
        }    	
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }*/
    
    public void potionEffect (PlayerEntity player) {
    	IPlayerCapabilities playerData;
		switch(type) {
    	case HP:
        	float hpAmount = (float) (percentage ? player.getMaxHealth() * amount / 100 : amount);
    		player.heal(hpAmount);
    		player.world.playSound(null, player.getPosition(), ModSounds.potion.get(), SoundCategory.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
    			Party party = worldData.getPartyFromMember(player.getUniqueID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUniqueID())) {
    						PlayerEntity target = player.world.getPlayerByUuid(m.getUUID());
    						if(target.getDistance(player) < ModConfigs.partyRangeLimit) {
	    			        	hpAmount = (float) (percentage ? target.getMaxHealth() * amount / 100 : amount);
	    						target.heal(hpAmount);
	    			    		player.world.playSound(null, target.getPosition(), ModSounds.potion.get(), SoundCategory.PLAYERS, 1, 1);
    						}
    					}
    				}
    			}
    		}
    		break;
    	case MP:
        	playerData = ModCapabilities.getPlayer(player);
    		float mpAmount = (float) (percentage ? playerData.getMaxMP() * amount / 100 : amount);
    		playerData.addMP(mpAmount);
    		player.world.playSound(null, player.getPosition(), ModSounds.potion.get(), SoundCategory.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
    			Party party = worldData.getPartyFromMember(player.getUniqueID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUniqueID())) {
    						PlayerEntity target = player.world.getPlayerByUuid(m.getUUID());
    						IPlayerCapabilities targetData = ModCapabilities.getPlayer(target);
    						if(target.getDistance(player) < ModConfigs.partyRangeLimit) {
	    						mpAmount = (float) (percentage ? targetData.getMaxMP() * amount / 100 : amount);
	    			        	targetData.addMP(mpAmount);
	    			    		player.world.playSound(null, target.getPosition(), ModSounds.potion.get(), SoundCategory.PLAYERS, 1, 1);
    						}
    			    		PacketHandler.syncToAllAround(target, targetData);
    					}
    				}
    			}
    		}
    		PacketHandler.syncToAllAround(player, playerData);
    		break;
    	case HPMP:
    		playerData = ModCapabilities.getPlayer(player);
    		mpAmount = (float) (percentage ? playerData.getMaxMP() * amount / 100 : amount);
    		hpAmount = (float) (percentage ? player.getMaxHealth() * amount / 100 : amount);
    		playerData.addMP(mpAmount);
    		player.heal(hpAmount);
    		player.world.playSound(null, player.getPosition(), ModSounds.potion.get(), SoundCategory.PLAYERS, 1, 1);
    		if(all) {
    			//Heal the rest of the party
    			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
    			Party party = worldData.getPartyFromMember(player.getUniqueID());
    			if(party != null) {
    				for(Member m : party.getMembers()) {
    					if(!m.getUUID().equals(player.getUniqueID())) {
    						PlayerEntity target = player.world.getPlayerByUuid(m.getUUID());
    						IPlayerCapabilities targetData = ModCapabilities.getPlayer(target);
    						if(target.getDistance(player) < ModConfigs.partyRangeLimit) {
	    						mpAmount = (float) (percentage ? targetData.getMaxMP() * amount / 100 : amount);
	    						hpAmount = (float) (percentage ? target.getMaxHealth() * amount / 100 : amount);
	    			        	targetData.addMP(mpAmount);
	    						target.heal(hpAmount);
	    			    		player.world.playSound(null, target.getPosition(), ModSounds.potion.get(), SoundCategory.PLAYERS, 1, 1);
    						}
    			    		PacketHandler.syncToAllAround(target, targetData);
    					}
    				}
    			}
    		}
    		PacketHandler.syncToAllAround(player, playerData);
    		break;
    	case DRIVE:
    		break;
    	case FOCUS:
    		break;
    	}
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	String sType = Utils.translateToLocal("potion.desc."+type.toString().toLowerCase());
    	String beginning = Utils.translateToLocal("potion.desc.beginning", (int)amount, percentage ? "%":"", sType);
    	String end = Utils.translateToLocal(all ? "potion.desc.toall" : "potion.desc.toone");
		tooltip.add(new TranslationTextComponent(beginning + end));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.CONSUMABLE;
	}

	public boolean isGlobal() {
		return all;
	}
}
