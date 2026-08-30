package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;

@OnlyIn(Dist.CLIENT)
public class UpdateLinksScreen extends Screen {
	private static final String CURSEFORGE = "https://www.curseforge.com/minecraft/mc-mods/kingdom-keys-re-coded";
	private static final String MODRINTH = "https://modrinth.com/mod/kingdom-keys-2";

	private static final int BUTTON_MIDDLE = 90;
	private static final int BUTTON_WIDTH = BUTTON_MIDDLE + 22;

	private final Screen parent;
	private final String version;

	int y;

	public UpdateLinksScreen(Screen parent, String version) {
		super(Component.translatable(KingdomKeys.MODID + ".update.links.title"));
		this.parent = parent;
		this.version = version;
	}

	@Override
	protected void init() {
		y = height / 2 - 10;

		MenuButton curseforge = new MenuButton(width / 2 - BUTTON_WIDTH - 4, y, BUTTON_MIDDLE, KingdomKeys.MODID + ".update.links.curseforge", MenuButton.ButtonType.ROUNDBUTTON, b -> ConfirmLinkScreen.confirmLinkNow(this, CURSEFORGE));
		curseforge.setCenterText(true);
		addRenderableWidget(curseforge);

		MenuButton modrinth = new MenuButton(width / 2 + 4, y, BUTTON_MIDDLE, KingdomKeys.MODID + ".update.links.modrinth", MenuButton.ButtonType.ROUNDBUTTON, b -> ConfirmLinkScreen.confirmLinkNow(this, MODRINTH));
		modrinth.setCenterText(true);
		addRenderableWidget(modrinth);

		MenuButton back = new MenuButton(width / 2 - BUTTON_WIDTH / 2, y + 28, BUTTON_MIDDLE, Strings.Gui_Menu_Back, MenuButton.ButtonType.ROUNDBUTTON, b -> onClose());
		back.setCenterText(true);
		addRenderableWidget(back);
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		gui.drawCenteredString(font, title, width / 2, y - 40, 0xFFFFFF);
		gui.drawCenteredString(font, Component.translatable(KingdomKeys.MODID + ".update.links.body", version == null ? "?" : version), width / 2, y - 20, 0xFFFFFF);
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}
