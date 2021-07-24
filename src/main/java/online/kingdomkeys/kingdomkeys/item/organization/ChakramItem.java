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
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
		ItemStack itemstack = player.getItemInHand(handIn);
		player.startUsingItem(handIn);
		return InteractionResultHolder.consume(itemstack);
	}

	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			int ticks = getUseDuration(stack) - timeLeft;
			if (ticks >= 10) {
				Player player = (Player) entityLiving;
				float dmgMult = Math.min(ticks, 30) / 20F;
				ChakramEntity entity = new ChakramEntity(worldIn, player, this.getRegistryName().getPath(), DamageCalculation.getOrgStrengthDamage(player, stack) * dmgMult);
				switch (this.getRegistryName().getPath()) {
				case Strings.eternalFlames:
				case Strings.prometheus:
				case Strings.volcanics:
					entity.setRotationPoint(0);
					break;
				default:
					entity.setRotationPoint(2);
				}
				player.level.addFreshEntity(entity);
				player.level.playSound(player, player.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
				entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, (1F + (dmgMult * 2)), 0);

				if (player.getMainHandItem() != null && player.getMainHandItem().getItem() == this) {
					player.swing(InteractionHand.MAIN_HAND);
				} else if (player.getOffhandItem() != null && player.getOffhandItem().getItem() == this) {
					player.swing(InteractionHand.OFF_HAND);
				}
			}
		}
	}

}