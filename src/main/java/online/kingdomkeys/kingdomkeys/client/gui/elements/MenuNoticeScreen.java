package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MenuNoticeScreen extends Screen {
	private static final int BOX_WIDTH = 200;
	private static final int BUTTON_WIDTH = 60;
	private static final int PADDING = 14;

	/** Same tone the vanilla screens use to push what's behind them back. */
	private static final int DIM = 0xA0101010;

	private final Screen parent;
	private final Component body;
	private final Color colour;

	private MenuBox box;

	public MenuNoticeScreen(Screen parent, Component title, Component body) {
		this(parent, title, body, new Color(112, 31, 35));
	}

	public MenuNoticeScreen(Screen parent, Component title, Component body, Color colour) {
		super(title);
		this.parent = parent;
		this.body = body;
		this.colour = colour;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		super.init();

		int textWidth = BOX_WIDTH - PADDING * 2;
		int lines = font.split(body, textWidth).size();
		int boxHeight = PADDING * 2 + font.lineHeight * 2 + font.lineHeight * lines + 26;

		int boxX = (width - BOX_WIDTH) / 2;
		int boxY = (height - boxHeight) / 2;

		box = new MenuBox(boxX, boxY, BOX_WIDTH, boxHeight, 0.9F, colour);

		MenuButton accept = new MenuButton(width / 2 - BUTTON_WIDTH / 2, boxY + boxHeight - PADDING - 12, BUTTON_WIDTH, Strings.Gui_Menu_Accept, MenuButton.ButtonType.ROUNDBUTTON, b -> onClose());
		accept.setCenterText(true);
		addRenderableWidget(accept);
	}

	/**
	 * The screen this came from stays on show behind, dimmed, so this reads as a box on top of it rather
	 * than a place you navigated to. Replaces the vanilla backdrop entirely - Screen.renderBackground
	 * would draw the panorama or the dirt over the parent.
	 */
	@Override
	public void renderBackground(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (parent != null) {
			// -1,-1: the cursor belongs to this box now, nothing behind should light up under it
			parent.render(gui, -1, -1, partialTicks);
		}

		gui.fill(0, 0, width, height, DIM);
		box.renderWidget(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);

		int textX = box.getX() + PADDING;
		int textY = box.getY() + PADDING;

		gui.drawCenteredString(font, title, width / 2, textY, 0xFFFF5555);

		List<FormattedCharSequence> split = font.split(body, BOX_WIDTH - PADDING * 2);
		for (int i = 0; i < split.size(); i++) {
			gui.drawString(font, split.get(i), textX, textY + font.lineHeight * 2 + font.lineHeight * i, 0xFFFFFF);
		}
	}

	/** The parent is still being drawn, so it has to be laid out again too or it keeps the old size. */
	@Override
	public void resize(@NotNull Minecraft minecraft, int width, int height) {
		if (parent != null) {
			parent.resize(minecraft, width, height);
		}

		super.resize(minecraft, width, height);
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}
