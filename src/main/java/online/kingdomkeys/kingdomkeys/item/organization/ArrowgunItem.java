package online.kingdomkeys.kingdomkeys.item.organization;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ArrowgunItem extends OrgWeaponItem implements IOrgWeapon {
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

				// world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(),
				// ModSounds.sharpshooterbullet.get(), SoundCategory.PLAYERS, 0.5F, 1F, false);
				ArrowgunShotEntity bullet = new ArrowgunShotEntity(world, player);
				bullet.shoot(player.rotationPitch, player.rotationYaw, 0, 4f, 0);
				world.addEntity(bullet);


				// if (!player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) {
				player.swingArm(Hand.MAIN_HAND);
				tempAmmo = player.getHeldItem(hand).getTag().getInt("ammo") - 1;
				/*
				 * } else { tempAmmo = player.getHeldItem(hand).getTag().getInt("ammo"); }
				 */
				player.getHeldItem(hand).getTag().putInt("ammo", tempAmmo);
			}

		} else {
			/*
			 * if (player.getHeldItemMainhand().getItemDamage() == 0) { if (!world.isRemote)
			 * { player.getHeldItemMainhand().setItemDamage(1);
			 * player.inventory.setInventorySlotContents(player.inventory.currentItem,
			 * player.getHeldItemMainhand()); player.world.playSound((EntityPlayer) null,
			 * player.getPosition(), ModSounds.summon, SoundCategory.MASTER, 1.0f, 1.0f); }
			 * } else { if (!world.isRemote) {
			 * player.getHeldItemMainhand().setItemDamage(0);
			 * player.inventory.setInventorySlotContents(player.inventory.currentItem,
			 * player.getHeldItemMainhand()); player.world.playSound((EntityPlayer) null,
			 * player.getPosition(), ModSounds.unsummon, SoundCategory.MASTER, 1.0f, 1.0f);
			 * } }
			 */
			return super.onItemRightClick(world, player, hand);
		}

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			if (!itemStack.hasTag()) {
				itemStack.setTag(new CompoundNBT());
				itemStack.getTag().putInt("ammo", ammo);
				itemStack.getTag().putInt("reload", reload);
			}

			if (itemStack.getTag().getInt("ammo") == 0) {

				if (itemStack.getTag().getInt("reload") > 0) {
					itemStack.getTag().putInt("reload", itemStack.getTag().getInt("reload") - 1);
				} else {
	            	world.playSound(player, player.getPosition(), ModSounds.arrowgunReload.get(), SoundCategory.PLAYERS, 1F, 1F);
					itemStack.getTag().putInt("reload", reload);
					itemStack.getTag().putInt("ammo", ammo);
				}

			}
		}
	}
}
