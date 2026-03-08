package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUDElement;

import java.util.ArrayList;
import java.util.List;

public class HUDEditorScreen extends Screen {

    private final List<HUDElement> elements = new ArrayList<>();
    private HUDElement selected;

    private boolean dragging = false;
    private float dragOffsetX;
    private float dragOffsetY;

    public HUDEditorScreen(List<HUDElement> hudElements) {
        super(Component.literal("HUD Editor"));
        this.elements.addAll(hudElements);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        for (HUDElement element : elements) {
            element.renderEditorBox(guiGraphics);
        }
      //  super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int w = minecraft.getWindow().getGuiScaledWidth();
            int h = minecraft.getWindow().getGuiScaledHeight();

            for (HUDElement element : elements) {
                if (element.isMouseOver(mouseX, mouseY)) {
                    selected = element;

                    float px = element.getPixelX(w);
                    float py = element.getPixelY(h);

                    dragOffsetX = (float) mouseX - px;
                    dragOffsetY = (float) mouseY - py;

                    dragging = true;

                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging && selected != null) {
            int w = minecraft.getWindow().getGuiScaledWidth();
            int h = minecraft.getWindow().getGuiScaledHeight();

            float newPx = (float) mouseX - dragOffsetX;
            float newPy = (float) mouseY - dragOffsetY;

            //Relative (gotta change HUDElement too)
            /*
            switch (selected.anchor) {
                case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> selected.x = newPx / w;
                case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> selected.x = (w - newPx - selected.width * selected.scaleX) / w;
                case TOP_CENTER, CENTER, BOTTOM_CENTER -> selected.x = (newPx - w / 2f + (selected.width * selected.scaleX / 2f)) / w;
            }

            switch (selected.anchor) {
                case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> selected.y = newPy / h;
                case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> selected.y = (h - newPy - selected.height * selected.scaleY) / h;
                case CENTER_LEFT, CENTER, CENTER_RIGHT -> selected.y = (newPy - h / 2f + (selected.height * selected.scaleY / 2f)) / h;
            }*/

            switch (selected.anchor) {

                case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> selected.x = newPx;
                case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> selected.x = w - newPx - selected.getScaledWidth();
                case TOP_CENTER, CENTER, BOTTOM_CENTER -> selected.x = newPx - (w / 2f) + selected.getScaledWidth() / 2f;
            }

            switch (selected.anchor) {
                case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> selected.y = newPy;
                case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> selected.y = h - newPy - selected.getScaledHeight();
                case CENTER_LEFT, CENTER, CENTER_RIGHT -> selected.y = newPy - (h / 2f) + selected.getScaledHeight() / 2f;
            }
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (selected != null) {
            if (hasShiftDown()) {
                selected.rotation += (float) (scrollY * 5f);
            } else {
                selected.scaleX += (float) (scrollY * 0.05f);
                selected.scaleY += (float) (scrollY * 0.05f);
            }

            selected.scaleX = Math.max(0.1f, selected.scaleX);
            selected.scaleY = Math.max(0.1f, selected.scaleY);

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}