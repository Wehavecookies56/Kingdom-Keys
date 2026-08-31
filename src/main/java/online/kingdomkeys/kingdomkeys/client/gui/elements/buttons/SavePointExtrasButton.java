package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class SavePointExtrasButton extends ScrollableButtonBase {
    public static final float EXTRA_BUTTON_ALPHA_BG = 0.5F;

    public SavePointExtrasButton(int pX, int pY, int pWidth, Component pMessage, OnPress pOnPress) {
        super(new Builder(pMessage, pOnPress).bounds(pX, pY, pWidth, Minecraft.getInstance().font.lineHeight + 2));
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        isHovered = isMouseOver(pMouseX, pMouseY);
        drawLabel(getMessage(), pGuiGraphics);
    }

    public void drawLabel(Component text, GuiGraphics gui) {
        drawLabel(text, gui, Color.WHITE.getRGB());
    }

    public void drawLabel(Component text, GuiGraphics gui, int textColour) {
        gui.pose().pushPose();
        gui.pose().translate(0, 0, 1);
        gui.setColor(1, 1, 1, EXTRA_BUTTON_ALPHA_BG);
        int colour = Color.BLACK.getRGB();
        if (isHovered) {
            colour = Color.LIGHT_GRAY.getRGB();
        }
        gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colour);
        gui.setColor(1, 1, 1, 1);
        gui.pose().translate(0.5F, 0.8F, 1);
        if (Minecraft.getInstance().font.width(getMessage().getVisualOrderText()) > getWidth()) {
            gui.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
            gui.drawString(Minecraft.getInstance().font, text, getX() + 1, getY() + 1, textColour);
            gui.disableScissor();
        } else {
            gui.drawCenteredString(Minecraft.getInstance().font, text, getX() + (getWidth() / 2), getY() + 1, textColour);
        }
        gui.pose().popPose();
    }
}
