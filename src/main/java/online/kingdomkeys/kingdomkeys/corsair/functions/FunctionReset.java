package online.kingdomkeys.kingdomkeys.corsair.functions;

import net.minecraft.client.entity.EntityPlayerSP;
import online.kingdomkeys.kingdomkeys.corsair.lib.CorsairUtils;

public class FunctionReset extends BaseKeyboardFunctions {

	/**
	 * Reset 1 key
	 * 
	 * @param player
	 * @return
	 */
	static int[] getRGBKeyColor(EntityPlayerSP player) {
			return CorsairUtils.defaultRGB;
		
	}
}
