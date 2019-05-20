package online.kingdomkeys.kingdomkeys.corsair.functions;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import online.kingdomkeys.kingdomkeys.corsair.lib.CorsairUtils;

public class FunctionItemDamage extends BaseKeyboardFunctions {
	static int[] getRGBKeyColor(EntityPlayerSP player, int slot) {
		int[] color = { 0, 0, 0 };

		if (player == null) {
			return CorsairUtils.defaultRGB;
		}
		InventoryPlayer inventory = player.inventory;
		ItemStack stack = inventory.getStackInSlot(slot);
		if (slot == player.inventory.currentItem) {
			return new int[] { 0, 0, 255 };
		} else {
			return decimalToRGB(getKeyColor(stack, player));
		}
	}

	static int[] getRGBKeyColorOffhand(EntityPlayerSP player) {
		if (player == null) {
			return CorsairUtils.defaultRGB;
		}
		ItemStack stack = player.getHeldItemOffhand();
		System.out.println(stack);
		if (!ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY)) {
			return new int[] { 0, 0, 255 };
		}
		return decimalToRGB(getKeyColor(stack, player));
	}

	static int[] getRGBKeyColorArmor(EntityPlayerSP player, int slot) {
		if (player == null) {
			return CorsairUtils.defaultRGB;
		}
		ItemStack stack = player.inventory.getStackInSlot(36 + slot);
		return decimalToRGB(getKeyColor(stack, player));
	}

	private static int getKeyColor(ItemStack stack, EntityPlayerSP player) {
		if ((!stack.isEmpty()) && (stack.isDamageable())) {
			Item item = stack.getItem();
			if (item instanceof ItemBow) {
				return getRGB(countAmmo(player), 128);
			}
			return item.getRGBDurabilityForDisplay(stack);
		}
		return 0;
	}

	private static int countAmmo(EntityPlayerSP player) {
		int ammo = 0;

		if ((player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemArrow)) {
			ammo += player.getHeldItem(EnumHand.OFF_HAND).getCount();
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack itemstack = player.inventory.getStackInSlot(i);
			if ((itemstack.getItem() instanceof ItemArrow)) {
				ammo += itemstack.getCount();
			}
		}

		return ammo;
	}
}