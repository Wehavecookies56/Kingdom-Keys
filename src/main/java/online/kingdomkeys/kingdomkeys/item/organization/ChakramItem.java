package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ChakramItem extends OrgWeaponItem implements IOrgWeapon {
   
	int reloadTicks = 30;

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.AXEL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
		if (!player.getHeldItem(handIn).getTag().getBoolean("shot")) {
	    	ChakramEntity entity = new ChakramEntity(worldIn, player, this.getRegistryName().getPath(), DamageCalculation.getOrgStrengthDamage(player, player.getHeldItem(handIn)));
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
			entity.shoot(player, player.rotationPitch, player.rotationYaw, 0, 2.5F, 0);
			player.swingArm(handIn);
			player.getHeldItem(handIn).getTag().putBoolean("shot", true);
		}
    	return super.onItemRightClick(worldIn, player, handIn);
    }

    @Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof PlayerEntity) {
			if (!itemStack.hasTag()) {
				itemStack.setTag(new CompoundNBT());
				itemStack.getTag().putInt("reload", reloadTicks);
				itemStack.getTag().putBoolean("shot", false);
			}

			if(itemStack.getTag().getBoolean("shot")) {
				if (itemStack.getTag().getInt("reload") > 0) {
					itemStack.getTag().putInt("reload", itemStack.getTag().getInt("reload") - 1);
				} else {
	            	//world.playSound(player, player.getPosition(), ModSounds.arrowgunReload.get(), SoundCategory.PLAYERS, 1F, 1F);
					itemStack.getTag().putInt("reload", reloadTicks);
					itemStack.getTag().putBoolean("shot", false);
				}
			}

		}
	}
	
}
