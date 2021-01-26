package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ChakramItem extends OrgWeaponItem implements IOrgWeapon {
  
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.AXEL;
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
		    	ChakramEntity entity = new ChakramEntity(worldIn, player, this.getRegistryName().getPath(), DamageCalculation.getOrgStrengthDamage(player, stack) * dmgMult);
		    	switch(this.getRegistryName().getPath()) {
		    	case Strings.eternalFlames:
		    	case Strings.prometheus:
		    	case Strings.volcanics:
		    		entity.setRotationPoint(0);
		    		break;
		    	default:
		    		entity.setRotationPoint(2);	
		    	}
				player.world.addEntity(entity);
				player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
				entity.shoot(player, player.rotationPitch, player.rotationYaw, 0, (1F + (dmgMult * 2)), 0);
    		}
    	}
    }
	
}
