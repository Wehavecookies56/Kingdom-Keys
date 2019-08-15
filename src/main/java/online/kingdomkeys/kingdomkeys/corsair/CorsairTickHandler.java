package online.kingdomkeys.kingdomkeys.corsair;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.event.TickEvent;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;

public class CorsairTickHandler {

	public KeyboardManager keyboardManager;

	public CorsairTickHandler(KeyboardManager keyboardManager) {
		this.keyboardManager = keyboardManager;
	}

	private boolean loadedLayout = false;

	@SubscribeEvent
	public void worldTick(TickEvent.ClientTickEvent event) {
		if (keyboardManager != null) {
			if (!loadedLayout) {
				keyboardManager.resetKeyboard();
				loadedLayout = true;
			}

			if (event.side == LogicalSide.CLIENT) {
				// this.keyboardManager.showLogo();
				keyboardManager.updateKeys();
			}

			if (EntityEvents.isHostiles) {
				keyboardManager.setDefaultColor(new int[]{255, 0, 0});
			} else {
				keyboardManager.setDefaultColor(new int[]{0, 0, 255});
			}
		}
	}
}
