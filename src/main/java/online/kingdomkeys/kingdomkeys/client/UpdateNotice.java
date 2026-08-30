package online.kingdomkeys.kingdomkeys.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.VersionChecker;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforgespi.language.IModInfo;

import online.kingdomkeys.kingdomkeys.KingdomKeys;

@EventBusSubscriber(value = Dist.CLIENT)
public class UpdateNotice {
	private static final int AVAILABLE_COLOR = 0xFFFFDD00;
	private static final int DEVELOPMENT_COLOR = 0xFFFF6666;

	private static VersionChecker.Status logged;

	private static IModInfo modInfo() {
		for (IModInfo info : ModList.get().getMods()) {
			if (info.getModId().equals(KingdomKeys.MODID)) {
				return info;
			}
		}

		return null;
	}

	@SubscribeEvent
	public static void onTitleScreenRender(ScreenEvent.Render.Post event) {
		if (!(event.getScreen() instanceof TitleScreen screen)) {
			return;
		}

		IModInfo mod = modInfo();

		if (mod == null) {
			return;
		}

		VersionChecker.CheckResult result = VersionChecker.getResult(mod);
		if (logged != result.status()) { //To prevent spam lol
			logged = result.status();
			KingdomKeys.LOGGER.info("Update check: status={} current={} target={} updateURL={}", result.status(), mod.getVersion(), result.target(), mod.getUpdateURL().orElse(null));
		}

		Component message;
		int color;

		switch (result.status()) {
			case OUTDATED, BETA_OUTDATED -> { // Local version is lower than published
				if (result.target() == null) {
					return;
				}

				message = Component.translatable(KingdomKeys.MODID + ".update.available", result.target().toString(), mod.getVersion().toString());
				color = AVAILABLE_COLOR;
			}
			case AHEAD -> { // Local version is higher than published
				message = Component.translatable(KingdomKeys.MODID + ".update.development", mod.getVersion().toString());
				color = DEVELOPMENT_COLOR;
			}
			default -> {
				return;
			}
		}

		GuiGraphics gui = event.getGuiGraphics();
		gui.drawString(screen.getMinecraft().font, message, 2, 2, color);
	}
}
