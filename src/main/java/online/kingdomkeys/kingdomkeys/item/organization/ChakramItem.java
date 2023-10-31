package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ChakramItem extends OrgSwordItem implements IOrgWeapon {

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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Level level = player.level();
		int slot = hand == InteractionHand.OFF_HAND ? player.getInventory().getContainerSize() - 1 : player.getInventory().selected;
		if (!level.isClientSide && stack != null) {
			player.setItemInHand(hand, ItemStack.EMPTY);
			KKThrowableEntity entity = new KKThrowableEntity(level);
			
			switch (ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath()) {
			case Strings.eternalFlames:
			case Strings.prometheus:
			case Strings.volcanics:
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
}