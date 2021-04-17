package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

public class KKPotionItem extends Item implements IItemCategory {
	
	public static enum PotionType {
		HP, MP, HPMP, DRIVE, FOCUS
	}
	
	PotionType type;
	double amount;
	boolean percentage;
	
    public KKPotionItem(Item.Properties properties, PotionType type, double amount, boolean perc) {
        super(properties);
		this.type = type;
		this.amount = amount;
		this.percentage = perc;
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
    	switch(type) {
    	case HP:
        	float hpAmount = (float) (percentage ? player.getMaxHealth() * amount / 100 : amount);
    		player.heal(hpAmount);
    		break;
    	case MP:
    		break;
    	case HPMP:
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
       	tooltip.add(new TranslationTextComponent("Restores: "+amount+ (percentage ? "% of your "+type.toString() : " "+type.toString())));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.CONSUMABLE;
	}
}
