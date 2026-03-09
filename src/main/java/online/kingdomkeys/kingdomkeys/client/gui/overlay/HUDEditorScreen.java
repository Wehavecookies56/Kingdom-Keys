package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUDElement;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HUDEditorScreen extends Screen {

    private boolean renderOutline = true;
    private final List<HUDElement> elements = new ArrayList<>();
    private HUDElement selected;

    private boolean dragging = false;
    private float dragOffsetX;
    private float dragOffsetY;

    public HUDEditorScreen() {
        super(Component.literal("HUD Editor"));
        //Order is important for overlapping boxes
        this.elements.addAll(List.of(DriveGui.ELEMENT, MPGui.ELEMENT, ShotlockGUI.ELEMENT, HPGui.ELEMENT, CommandMenuGui.ELEMENT));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int y=1;
        guiGraphics.drawString(minecraft.font,"First of all select the anchor point by clicking the element and SPACE",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Left Click and drag an element to move it",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Use ARROW KEYS to move it in tiny gaps",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Hold SHIFT + ARROW KEYS to move it in bigger gaps",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Use SCROLL WHEEL to scale it up",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Use SHIFT + SCROLL WHEEL to rotate it",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Press LEFT ALT to show or hide outlines",100,y++*10,0xFFFFFF);
        guiGraphics.drawString(minecraft.font,"Right click on a selected item to reset it to default",100,y++*10,0xFFFFFF);


        for (HUDElement element : elements) {
            if(renderOutline) {
                element.renderEditorBox(guiGraphics);
            }
            element.selected = selected == element;
        }
    }

    @Override
    public void onClose() {
        for (HUDElement element : elements) {
            element.saveConfig();
        }
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(selected == null) {
            return false;
        }

        float step = hasControlDown() ? 1F : 0.1F;

        boolean right = selected.anchor == HUDAnchorPosition.TOP_RIGHT || selected.anchor == HUDAnchorPosition.CENTER_RIGHT || selected.anchor == HUDAnchorPosition.BOTTOM_RIGHT;
        boolean bottom = selected.anchor == HUDAnchorPosition.BOTTOM_LEFT || selected.anchor == HUDAnchorPosition.BOTTOM_CENTER || selected.anchor == HUDAnchorPosition.BOTTOM_RIGHT;

        float xStep = right ? -step : step;
        float yStep = bottom ? -step : step;

        switch (keyCode) {
            case GLFW.GLFW_KEY_LEFT -> selected.x -= xStep;
            case GLFW.GLFW_KEY_RIGHT -> selected.x += xStep;

            case GLFW.GLFW_KEY_UP -> selected.y -= yStep;
            case GLFW.GLFW_KEY_DOWN -> selected.y += yStep;

            case GLFW.GLFW_KEY_SPACE -> {
                HUDAnchorPosition[] anchors = HUDAnchorPosition.values();
                selected.anchor = anchors[(selected.anchor.ordinal() + 1) % anchors.length];
            }
            case GLFW.GLFW_KEY_LEFT_ALT -> {
                renderOutline = !renderOutline;
            }
            case GLFW.GLFW_KEY_S -> {
                if(hasControlDown()) {
                    selected.saveConfig();
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
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
        } else if (button == 1) {
            for (HUDElement element : elements) {
                if(element == selected) {
                    System.out.println("Right click for selected element "+element.x);
                    element.restoreDefaultValues();
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