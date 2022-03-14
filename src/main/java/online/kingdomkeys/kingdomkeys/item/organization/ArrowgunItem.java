package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ArrowgunItem extends OrgSwordItem implements IOrgWeapon {
	int ammo = 10, reload = 30, tempAmmo;

	/*
	 * public ArrowgunItem(String name, int ammo, int reload) { super(); this.ammo =
	 * ammo; this.reload = reload; }
	 */

	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.XIGBAR;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!player.isSneaking()) {
			if (player.getHeldItem(hand).getTag() != null && player.getHeldItem(hand).getTag().getInt("ammo") > 0) {
				world.playSound(player, player.getPosition(), ModSounds.sharpshooterbullet.get(), SoundCategory.PLAYERS, 1F, 1F);
				ArrowgunShotEntity bullet = new ArrowgunShotEntity(world, player);
				bullet.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 3F, 0);
				world.addEntity(bullet);

				//player.swingArm(Hand.MAIN_HAND);
				tempAmmo = player.getHeldItem(hand).getTag().getInt("ammo") - 1;

				player.getHeldItem(hand).getTag().putInt("ammo", tempAmmo);
				if(tempAmmo == 0) {
					player.getHeldItem(hand).getTag().putInt("ammo", getMaxAmmo(player));
					player.getCooldownTracker().setCooldown(this, reload);
					world.playSound(null, player.getPosition(), ModSounds.arrowgunReload.get(), SoundCategory.PLAYERS, 1F, 1F);
				}
			}

		} else {
			CompoundNBT nbt = player.getHeldItem(hand).getTag();

			if (nbt.getInt("ammo") > 0) {
				world.playSound(player, player.getPosition(), ModSounds.arrowgunReload.get(), SoundCategory.PLAYERS, 1F, 1F);

				player.getCooldownTracker().setCooldown(this, reload / nbt.getInt("ammo"));
				player.getHeldItem(hand).getTag().putInt("ammo", getMaxAmmo(player));
				player.swingArm(Hand.MAIN_HAND);
			}
			return super.onItemRightClick(world, player, hand);
		}

		return ActionResult.resultConsume(player.getHeldItem(hand));
	}

	private int getMaxAmmo(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if(playerData.isAbilityEquipped(Strings.synchBlade)) {
			return ammo*2;
		}
		return ammo;
	}

	@Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof PlayerEntity && !world.isRemote) {
			PlayerEntity player = (PlayerEntity) entity;
			if (!itemStack.hasTag()) {
				itemStack.setTag(new CompoundNBT());
				itemStack.getTag().putInt("ammo", getMaxAmmo(player));
			}

			if (itemStack.getTag().getInt("ammo") == 0) {
				
			}
		}
	}
}
