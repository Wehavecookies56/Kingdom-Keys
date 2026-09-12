package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Leather-armor style dye mixing for apprentice primary/secondary colors.
 * Multiple dyes in the same group intensify / average the resulting RGB.
 */
public final class ApprenticeDyeHelper {

	private ApprenticeDyeHelper() {
	}

	/**
	 * Mix dye colors with an optional existing base color (leather formula).
	 *
	 * @param dyes          dye item stacks (empty stacks ignored)
	 * @param existingColor current color on the piece (0xRRGGBB)
	 * @param hasExisting   whether the piece already has a meaningful color to blend with
	 * @return mixed 0xRRGGBB, or existingColor if no dyes were present
	 */
	public static int mixColors(List<ItemStack> dyes, int existingColor, boolean hasExisting) {
		List<Integer> colors = new ArrayList<>();
		for (ItemStack stack : dyes) {
			if (stack.isEmpty() || !(stack.getItem() instanceof DyeItem dyeItem)) {
				continue;
			}
			colors.add(dyeItem.getDyeColor().getTextureDiffuseColor());
		}

		if (colors.isEmpty()) {
			return existingColor & 0xFFFFFF;
		}

		int[] totals = new int[3];
		int maxChannelSum = 0;
		int count = 0;

		if (hasExisting) {
			int r = (existingColor >> 16) & 0xFF;
			int g = (existingColor >> 8) & 0xFF;
			int b = existingColor & 0xFF;
			maxChannelSum += Math.max(r, Math.max(g, b));
			totals[0] += r;
			totals[1] += g;
			totals[2] += b;
			count++;
		}

		for (int color : colors) {
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = color & 0xFF;
			maxChannelSum += Math.max(r, Math.max(g, b));
			totals[0] += r;
			totals[1] += g;
			totals[2] += b;
			count++;
		}

		int avgR = totals[0] / count;
		int avgG = totals[1] / count;
		int avgB = totals[2] / count;
		float avgMax = (float) maxChannelSum / (float) count;
		float maxAvg = (float) Math.max(avgR, Math.max(avgG, avgB));
		if (maxAvg > 0.0F) {
			float scale = avgMax / maxAvg;
			avgR = (int) (avgR * scale);
			avgG = (int) (avgG * scale);
			avgB = (int) (avgB * scale);
		}

		return (avgR << 16) | (avgG << 8) | avgB;
	}

	public static boolean hasAnyDye(List<ItemStack> dyes) {
		for (ItemStack stack : dyes) {
			if (!stack.isEmpty() && stack.getItem() instanceof DyeItem) {
				return true;
			}
		}
		return false;
	}

}
