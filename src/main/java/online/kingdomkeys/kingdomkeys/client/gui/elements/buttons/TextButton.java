package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;

public class TextButton extends Button {
	private static final long BLINK_PERIOD = 1000L;

	private final int color;
	private final Component plain;
	private final Component underlined;

	private boolean blinkUnderline;

	public TextButton(int x, int y, int width, Component message, int color, OnPress onPress) {
		super(x, y, width, 9, message, onPress, DEFAULT_NARRATION);
		this.color = color;
		this.plain = message;
		this.underlined = ComponentUtils.mergeStyles(message.copy(), Style.EMPTY.withUnderlined(true));
	}

	public TextButton blinkUnderline() {
		this.blinkUnderline = true;
		return this;
	}

	private boolean showUnderline() {
		if (!active) {
			return false;
		}

		if (isHoveredOrFocused()) {
			return true;
		}

		return blinkUnderline && Util.getMillis() % BLINK_PERIOD < BLINK_PERIOD / 2;
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		gui.drawString(Minecraft.getInstance().font, showUnderline() ? underlined : plain, getX(), getY(), color);
	}
}
