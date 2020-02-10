package online.kingdomkeys.kingdomkeys.corsair.functions;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.corsair.lib.CorsairUtils;

public class FunctionItemDamage extends BaseKeyboardFunctions {
	static int[] getRGBKeyColor(ClientPlayerEntity player, int slot) {
		int[] color = { 0, 0, 0 };

		if (player == null) {
			return CorsairUtils.defaultRGB;
		}
		PlayerInventory inventory = player.inventory;
		ItemStack stack = inventory.getStackInSlot(slot);
		if (slot == player.inventory.currentItem) {
			return new int[] { 0, 0, 255 };
		} else {
			return decimalToRGB(getKeyColor(stack, player));
		}
	}

	static int[] getRGBKeyColorOffhand(ClientPlayerEntity player) {
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

	static int[] getRGBKeyColorArmor(ClientPlayerEntity player, int slot) {
		if (player == null) {
			return CorsairUtils.defaultRGB;
		}
		ItemStack stack = player.inventory.getStackInSlot(36 + slot);
		return decimalToRGB(getKeyColor(stack, player));
	}

	private static int getKeyColor(ItemStack stack, ClientPlayerEntity player) {
		if ((!stack.isEmpty()) && (stack.isDamageable())) {
			Item item = stack.getItem();
			if (item instanceof BowItem) {
				return getRGB(countAmmo(player), 128);
			}
			return item.getRGBDurabilityForDisplay(stack);
		}
		return 0;
	}

	private static int countAmmo(ClientPlayerEntity player) {
		int ammo = 0;

		if ((player.getHeldItem(Hand.OFF_HAND).getItem() instanceof ArrowItem)) {
			ammo += player.getHeldItem(Hand.OFF_HAND).getCount();
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack itemstack = player.inventory.getStackInSlot(i);
			if ((itemstack.getItem() instanceof ArrowItem)) {
				ammo += itemstack.getCount();
			}
		}

		return ammo;
	}
}