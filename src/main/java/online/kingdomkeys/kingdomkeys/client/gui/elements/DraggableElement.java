package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class DraggableElement extends Button {

    int defaultX, defaultY, localX, localY;

    boolean dragging = false;

    public DraggableElement(int x, int y, int width, int height) {
        super(builder(Component.empty(), button -> {}).bounds(x, y, width, height));
        this.defaultX = x;
        this.defaultY = y;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Color.RED.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight()) {
            dragging = true;
            localX = (int) mouseX - getX();
            localY = (int) mouseY - getY();
        }
        super.onClick(mouseX, mouseY);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        if (dragging) {
            setX((int) mouseX - localX);
            setY((int) mouseY - localY);
        }
        super.onDrag(mouseX, mouseY, dragX, dragY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        dragging = false;
        super.onRelease(mouseX, mouseY);
    }
}
