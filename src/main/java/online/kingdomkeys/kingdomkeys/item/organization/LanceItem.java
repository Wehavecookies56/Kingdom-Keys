package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class LanceItem extends OrgWeaponItem implements IOrgWeapon {

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.XALDIN;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
     }

    public int getUseDuration(ItemStack stack) {
        return 72000;
     }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getHeldItem(handIn);
   
        player.setActiveHand(handIn);
        
        return ActionResult.resultConsume(itemstack);

    }
    
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
    	if(entityLiving instanceof PlayerEntity) {
    		int ticks = getUseDuration(stack) - timeLeft;
    		if(ticks >= 10) {
	    		PlayerEntity player = (PlayerEntity)entityLiving;
	    		float dmgMult = Math.min(ticks, 30) / 20F;
		    	LanceEntity entity = new LanceEntity(worldIn, player, this.getRegistryName().getPath(), DamageCalculation.getOrgStrengthDamage(player, stack) * dmgMult);
		    	switch(this.getRegistryName().getPath()) {
		    	case Strings.lindworm:
		    		entity.setRotationPoint(0);
		    		break;
		    	default:
		    		entity.setRotationPoint(2);	
		    	}
		    	player.world.addEntity(entity);

				player.world.playSound(player, player.getPosition(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1F, 1F);
				entity.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0, (1F + (dmgMult * 2)), 0);
				
				if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == this) {
					player.swingArm(Hand.MAIN_HAND);
				} else if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == this) {
					player.swingArm(Hand.OFF_HAND);
				}
    		}
    	}
    }

}
