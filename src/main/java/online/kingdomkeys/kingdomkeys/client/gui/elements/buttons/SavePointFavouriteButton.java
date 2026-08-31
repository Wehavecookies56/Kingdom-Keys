package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.SavePointScreen;

import java.awt.*;
import java.util.UUID;

// The pin toggle that sits in the corner of every save point tile. Unlike rename and retake there is
// one of these per save point, since any of them can be pinned - including the one you're stood on,
// whose own button is inactive.
public class SavePointFavouriteButton extends SavePointExtrasButton {

	public static final Component FAVOURITE = Component.literal("★");
	public static final Component NOT_FAVOURITE = Component.literal("☆");

	private static final int COLOUR = 0xFFFFD75A;

	private final SavePointScreen parent;
	private final UUID destination;

	private static final int GAP = 1;

	public SavePointFavouriteButton(SavePointScreen parent, int pX, int pY, UUID destination) {
		super(pX, pY, width() - GAP, NOT_FAVOURITE, pButton -> parent.toggleFavourite(destination));
		this.parent = parent;
		this.destination = destination;
	}

	public static int width() {
		return Math.max(Minecraft.getInstance().font.width(FAVOURITE), Minecraft.getInstance().font.width(NOT_FAVOURITE)) + 4;
	}

	public UUID getDestination() {
		return destination;
	}

	@Override
	public Component getMessage() {
		return parent.isFavourite(destination) ? FAVOURITE : NOT_FAVOURITE;
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
		isHovered = isMouseOver(pMouseX, pMouseY);

		gui.pose().pushPose();
		{
			gui.pose().translate(0, 0, 1);
			if (destination.equals(parent.hovered)) {
				gui.setColor(1, 1, 1, EXTRA_BUTTON_ALPHA_BG);
				gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), isHovered ? Color.LIGHT_GRAY.getRGB() : Color.BLACK.getRGB());
				gui.setColor(1, 1, 1, 1);
			}

			gui.drawCenteredString(Minecraft.getInstance().font, getMessage(), getX() + (getWidth() / 2), getY() + 1, COLOUR);
		}
		gui.pose().popPose();
	}
}
