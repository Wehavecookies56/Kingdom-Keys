package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KnifeItem extends OrgSwordItem implements IOrgWeapon {
	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.LARXENE;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		Level level = player.level();
		int slot = hand == InteractionHand.OFF_HAND ? player.getInventory().getContainerSize() - 1 : player.getInventory().selected;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

		if (stack != null && !playerData.getRecharge()) {
			int cost = 10;
			cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
			playerData.remMP(Math.max(1, cost));
			player.swing(hand);

			if (!level.isClientSide) {
				player.setItemInHand(hand, ItemStack.EMPTY);
				for (int i = -5; i <= 3; i += 2) {
					KKThrowableEntity entity = new KKThrowableEntity(level);

					switch (ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath()) {
					case Strings.foudre:
					case Strings.trancheuse:
					case Strings.orage:
					case Strings.tourbillon:
					case Strings.tempete:
					case Strings.carmin:
					case Strings.meteore:
					case Strings.etoile:
					case Strings.irregulier:
					case Strings.dissonance:
					case Strings.eruption:
					case Strings.soleilCouchant:
					case Strings.indigo:
					case Strings.vague:
					case Strings.deluge:
					case Strings.rafale:
					case Strings.typhon:
					case Strings.extirpeur:
					case Strings.croixDuSud:
					case Strings.lumineuse:
					case Strings.clairDeLune:
					case Strings.volDeNuit:
					case Strings.demoiselle:
					case Strings.ampoule:
						entity.setRotationPoint(1);
						break;
					}

					entity.setData(DamageCalculation.getMagicDamage(player) * (playerData.getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.2f), player.getUUID(), slot, stack);
					entity.setPos(player.position().x, player.getEyePosition().y, player.position().z);

					entity.getEntityData().set(KKThrowableEntity.ITEMSTACK, stack);
					entity.shootFromRotation(player, player.getXRot(), player.getYRot() + i, 0.0F, 3.0F, 0F);
					level.addFreshEntity(entity);
				}
				return super.use(world, player, hand);
			}
		}
		return InteractionResultHolder.consume(stack);
	}
}
