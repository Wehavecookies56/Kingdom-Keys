package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ScytheItem extends OrgSwordItem implements IOrgWeapon {

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.MARLUXIA;
    }
    
    @Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Level level = player.level();
		int slot = hand == InteractionHand.OFF_HAND ? player.getInventory().getContainerSize() - 1 : player.getInventory().selected;
		PlayerData playerData = PlayerData.get(player);

		if (stack != null && !playerData.getRecharge() && playerData.isAbilityEquipped(Strings.strikeRaid)) {
			int cost = 10;
    		cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
			playerData.remMP(Math.max(1, cost));
			player.swing(hand);

			if(!level.isClientSide) {
				player.setItemInHand(hand, ItemStack.EMPTY);
				KKThrowableEntity entity = new KKThrowableEntity(level);
				
				switch (BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath()) {
				case Strings.gracefulDahlia:
				case Strings.proudAmaryllis:
				case Strings.madSafflower:
				case Strings.tragicAllium:
				case Strings.mournfulCineria:
				case Strings.pseudoSilene:
				case Strings.faithlessDigitalis:
				case Strings.grimMuscari:
				case Strings.docileVallota:
				case Strings.partingIpheion:
				case Strings.gallantAchillea:
				case Strings.noblePeony:
				case Strings.fearsomeAnise:
				case Strings.vindictiveThistle:
				case Strings.fairHelianthus:
				case Strings.stirringLadle:
				case Strings.daintyBellflowers:
					entity.setRotationPoint(1);
					break;				
				default:
					entity.setRotationPoint(0);
				}
				
				
				entity.setData(DamageCalculation.getOrgStrengthDamage(player, stack), player.getUUID(), slot, stack);
				entity.setPos(player.position().x, player.getEyePosition().y, player.position().z);
	
				entity.getEntityData().set(KKThrowableEntity.ITEMSTACK, stack);
	
				entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3F, 0F);
				level.addFreshEntity(entity);
				return super.use(world, player, hand);	
			}
		}		
		return InteractionResultHolder.consume(stack);
	}
}
