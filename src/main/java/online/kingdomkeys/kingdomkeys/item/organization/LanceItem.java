package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class LanceItem extends OrgSwordItem implements IOrgWeapon {

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.XALDIN;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
     }

   /* public int getUseDuration(ItemStack stack) {
        return 72000;
     }*/
    
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Level level = player.level;
		int slot = hand == InteractionHand.OFF_HAND ? player.getInventory().getContainerSize() - 1 : player.getInventory().selected;
		if (!level.isClientSide && stack != null) {
			//player.setItemInHand(hand, ItemStack.EMPTY);
			player.getCooldowns().addCooldown(this, 10);
			LanceEntity entity = new LanceEntity(level);
			
			switch(ForgeRegistries.ITEMS.getKey(this).getPath()) {
	    	case Strings.lindworm:
	    		entity.setRotationPoint(0);
	    		break;
	    	default:
	    		entity.setRotationPoint(2);	
	    	}
			
			entity.setData(DamageCalculation.getOrgStrengthDamage(player, stack), player.getUUID(), slot, stack);
			entity.setPos(player.position().x, player.getEyePosition().y, player.position().z);

			entity.getEntityData().set(KKThrowableEntity.ITEMSTACK, stack);

			entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0F);
			level.addFreshEntity(entity);
			return super.use(world, player, hand);	

		}
		if(level.isClientSide()) {
			player.swing(slot == 40 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
		}
		
		return InteractionResultHolder.consume(stack);
	}

    
    /*@Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
        ItemStack itemstack = player.getItemInHand(handIn);
        player.startUsingItem(handIn);        
        return InteractionResultHolder.consume(itemstack);
    }
    
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
    	if(entityLiving instanceof Player) {
    		int ticks = getUseDuration(stack) - timeLeft;
    		if(ticks >= 10) {
	    		Player player = (Player)entityLiving;
	    		float dmgMult = Math.min(ticks, 30) / 20F;
		    	LanceEntity entity = new LanceEntity(worldIn, player, ForgeRegistries.ITEMS.getKey(this).getPath(), DamageCalculation.getOrgStrengthDamage(player, stack) * dmgMult);
		    	switch(ForgeRegistries.ITEMS.getKey(this).getPath()) {
		    	case Strings.lindworm:
		    		entity.setRotationPoint(0);
		    		break;
		    	default:
		    		entity.setRotationPoint(2);	
		    	}
		    	player.level.addFreshEntity(entity);

				player.level.playSound(player, player.blockPosition(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1F, 1F);
				entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, (1F + (dmgMult * 2)), 0);
				
				if(player.getMainHandItem() != null && player.getMainHandItem().getItem() == this) {
					player.swing(InteractionHand.MAIN_HAND);
				} else if(player.getOffhandItem() != null && player.getOffhandItem().getItem() == this) {
					player.swing(InteractionHand.OFF_HAND);
				}
    		}
    	}
    }*/

}
