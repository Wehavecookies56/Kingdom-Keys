package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.FireRingCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeCoreEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EtherealBladeItem extends OrgWeaponItem implements IOrgWeapon {
   
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.XEMNAS;
    }
    	
    @Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		/*if (!player.isSneaking()) {
			world.playSound(player, player.getPosition(), ModSounds.savespawn.get(), SoundCategory.PLAYERS, 1F, 1F);
			ItemStack stack = player.getHeldItem(hand);
			float damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, 1, this)) / 2 * 0.15F;
			LaserDomeCoreEntity core = new LaserDomeCoreEntity(world, player, player, damage, 0);
			core.setPosition(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			world.addEntity(core);
		}*/

		return super.onItemRightClick(world, player, hand);
	}
}
