package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.HUDElement;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.config.MenuConfigScreen;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HUDEditorScreen extends Screen {
    private Button resetButton, rpButton;
    private boolean renderOutline = true;
    private HUDElement selected;

    private boolean dragging = false;
    private float dragOffsetX;
    private float dragOffsetY;

    private static final double DRAG_SAFEZONE = 2;

    private double pressX, pressY;
    private boolean dragged;

    private boolean tookFirst;

    // What was under the cursor when the button went down, in registry order, so clicking walks the pile
    private List<HUDElement> underCursor = new ArrayList<>();

    public HUDEditorScreen() {
        super(Component.translatable("kingdomkeys.gui.hud_editor.title"));
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int buttonWidth = (int)(scaledWidth * 0.23F);
        addRenderableWidget(rpButton = new MenuButton(scaledWidth/2 - buttonWidth - 20, 5, buttonWidth, Utils.translateToLocal("gui.menu.config.reset_defaults"), MenuButton.ButtonType.ROUNDBUTTON, (e) -> {
            for (HUDElement element : HUDElement.REGISTRY) {
                element.restoreDefaultValues();
            }
        }));

        addRenderableWidget(resetButton = new MenuButton(scaledWidth/2 + 10, 5, buttonWidth, Utils.translateToLocal("gui.menu.config.reset_rp"), MenuButton.ButtonType.ROUNDBUTTON, (e) -> {
            for (HUDElement element : HUDElement.REGISTRY) {
                element.loadDefaultsFromJson();
            }
        }));

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        resetButton.render(guiGraphics, mouseX, mouseY, partialTick);
        rpButton.render(guiGraphics, mouseX, mouseY, partialTick);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int y=5;
        if(!ClientUtils.isKeyDown(GLFW.GLFW_KEY_H)) {
            guiGraphics.drawCenteredString(minecraft.font, Component.translatable("gui.menu.config.hud.help0", Component.literal("H").withStyle(ChatFormatting.DARK_RED)), scaledWidth / 2, y++ * 10, 0xFFFFFF);
        } else {
            for(int i = 1; i < 14; i++) {
                guiGraphics.drawCenteredString(minecraft.font, Utils.translateToLocal("gui.menu.config.hud.help"+i), scaledWidth / 2, y++ * 10, 0xFFFFFF);
            }
        }
        if(selected != null) {
            guiGraphics.drawCenteredString(minecraft.font,Utils.translateToLocal("gui.menu.config.hud.help14"),scaledWidth / 2,y++*10+10,0xFFFFFF);
            for(String data : selected.getData()) {
                guiGraphics.drawString(minecraft.font, data,scaledWidth / 2-40,y++*10+ 10,0xFFFFFF);
            }
        }

        List<Component> list = new ArrayList<>();
        for (HUDElement element : HUDElement.REGISTRY) {
            if(element.isMouseOver(mouseX, mouseY)) {
                String line = (element == selected ? ChatFormatting.BOLD : "") + element.name;
                list.add(Component.translatable(ChatFormatting.WHITE + line));
            }
            if(renderOutline) {
                element.renderEditorBox(guiGraphics);
            }
            element.selected = selected == element;
        }

        // Once, and after the boxes. Drawing it inside the loop put one tooltip on screen per element under
        // the cursor, each holding the names found so far, so the short ones showed from behind the long one
        if(!list.isEmpty()) {
            guiGraphics.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public void onClose() {
        for (HUDElement element : HUDElement.REGISTRY) {
            element.saveConfig();
        }
        super.onClose();
        minecraft.setScreen(new MenuConfigScreen());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(selected == null) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        float step = hasControlDown() ? 0.1F : 1F;

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
            case GLFW.GLFW_KEY_V -> {
                selected.visible = !selected.visible;
            }
            case GLFW.GLFW_KEY_S -> {
                if(hasControlDown()) {
                    for (HUDElement element : HUDElement.REGISTRY) {
                        element.saveConfig();
                    }
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

            underCursor = elementsAt(mouseX, mouseY);

            if (!underCursor.isEmpty()) {
                // Whatever was already picked stays picked if it is in the pile, so dragging carries on
                // with the one you had rather than jumping to whichever happens to be on top
                tookFirst = !underCursor.contains(selected);

                if (tookFirst) {
                    selected = underCursor.getFirst();
                }

                dragOffsetX = (float) mouseX - selected.getPixelX(w);
                dragOffsetY = (float) mouseY - selected.getPixelY(h);

                pressX = mouseX;
                pressY = mouseY;
                dragged = false;
                dragging = true;

                return true;
            }
        } else if (button == 1) {
            for (HUDElement element : HUDElement.REGISTRY) {
                if (element.isMouseOver(mouseX, mouseY)) {
                    if(hasShiftDown()){
                        element.restoreDefaultValues();
                    } else {
                        element.loadDefaultsFromJson();
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging && selected != null) {
            // Held still, or near enough. A mouse wobbles a pixel while the button goes down, and moving
            // the element by that pixel would make every click a tiny nudge
            if (!dragged && Math.abs(mouseX - pressX) + Math.abs(mouseY - pressY) < DRAG_SAFEZONE) {
                return true;
            }

            dragged = true;

            int w = minecraft.getWindow().getGuiScaledWidth();
            int h = minecraft.getWindow().getGuiScaledHeight();

            float newPx = (float) mouseX - dragOffsetX;
            float newPy = (float) mouseY - dragOffsetY;

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
        // A click that went nowhere, on a spot where elements overlap, means the next one down. Otherwise
        // whatever is first in the registry would be the only one you could ever get hold of.
        //
        // Not when the press had to pick one to begin with: that click already did its job by landing on
        // the first of the pile, and moving on would make the first one impossible to select
        if (button == 0 && dragging && !dragged && !tookFirst && underCursor.size() > 1) {
            int at = underCursor.indexOf(selected);
            selected = underCursor.get((at + 1) % underCursor.size());
        }

        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    /** Everything the cursor is over, in registry order, which is the order clicking walks them in */
    private List<HUDElement> elementsAt(double mouseX, double mouseY) {
        List<HUDElement> found = new ArrayList<>();

        for (HUDElement element : HUDElement.REGISTRY) {
            if (element.isMouseOver(mouseX, mouseY)) {
                found.add(element);
            }
        }

        return found;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (selected != null) {
            if (hasShiftDown()) {
                selected.rotation = (selected.rotation + (float)(scrollY * 5f)) % 360f;
                if (selected.rotation < 0) {
                    selected.rotation += 360f;
                }
            } else {
                if(ClientUtils.isKeyDown(GLFW.GLFW_KEY_X)) {
                    selected.scaleX += (float) (scrollY * 0.05f);
                } else if(ClientUtils.isKeyDown(GLFW.GLFW_KEY_Y)) {
                    selected.scaleY += (float) (scrollY * 0.05f);
                } else{
                    selected.scaleX += (float) (scrollY * 0.05f);
                    selected.scaleY += (float) (scrollY * 0.05f);
                }
            }

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}