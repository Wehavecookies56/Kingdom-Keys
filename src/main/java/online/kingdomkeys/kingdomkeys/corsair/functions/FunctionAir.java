package online.kingdomkeys.kingdomkeys.corsair.functions;

import net.minecraft.client.entity.EntityPlayerSP;

public class FunctionAir extends BaseKeyboardFunctions {

	/**
	 * Oxygen in certain key (used with various keys)
	 * 
	 * @param player
	 * @param key
	 * @return
	 */
	static int[] getRGBKeyColor(EntityPlayerSP player, int key) {
		int[] color = { 0, 0, 0 };

		if (player == null) {
			return color;
		}

		float fill = getAir(player) / getMaxAir() * 10.0F;
		fill -= key;

		if (fill > 1.0F) {
			color[0] = 0;
			color[1] = 0;
			color[2] = 255;
		} else if (fill > 0.0F) {
			color[0] = 0;
			color[1] = 0;
			color[2] = (int) (255 * fill);
		}
		return color;
	}

	/**
	 * All oxygen in 1 key
	 * 
	 * @param player
	 * @return
	 */
	static int[] getRGBKeyColor(EntityPlayerSP player) {
		if (player == null) {
			return decimalToRGB(0);
		}
		return decimalToRGB(getRGB(getAir(player), getMaxAir()));
	}


}