package online.kingdomkeys.kingdomkeys.integration.corsair.functions;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import online.kingdomkeys.kingdomkeys.integration.corsair.lib.CorsairUtils;

public class FunctionReset extends BaseKeyboardFunctions {

	/**
	 * Reset 1 key
	 * 
	 * @param player
	 * @return
	 */
	static int[] getRGBKeyColor(ClientPlayerEntity player) {
			return CorsairUtils.defaultRGB;
		
	}
}
