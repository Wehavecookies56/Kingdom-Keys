package online.kingdomkeys.kingdomkeys.integration.corsair.functions;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import online.kingdomkeys.kingdomkeys.integration.corsair.lib.CorsairUtils;

public class FunctionFood extends BaseKeyboardFunctions {

	/**
	 * Hunger in certain key (used with various keys)
	 * 
	 * @param player
	 * @param key
	 * @return
	 */
	static int[] getRGBKeyColor(ClientPlayerEntity player, int key) {
		int[] color = { 0, 0, 0 };

		if (player == null) {
			return CorsairUtils.defaultRGB;
		}

		float fill = getFood(player) / 2;
		fill -= key;

		if (fill > 1.0F) {
			color[0] = 140;
			color[1] = 70;
			color[2] = 20;
		} else if (fill > 0.0F) {
			color[0] = (int) (140 * fill);
			color[1] = (int) (70 * fill);
			color[2] = (int) (20 * fill);
		}
		return color;
	}

	/**
	 * All hunger in 1 key
	 * 
	 * @param player
	 * @return
	 */
	static int[] getRGBKeyColor(ClientPlayerEntity player) {
		if (player == null) {
			return CorsairUtils.defaultRGB;
		}

		// Fill = the amount of food /2 because of the half points
		float fill = getFood(player) / 2;
		return decimalToRGB(getRGB(fill, getMaxFood()));
	}
}
