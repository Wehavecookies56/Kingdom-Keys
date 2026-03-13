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
    private final List<HUDElement> elements = new ArrayList<>();
    private HUDElement selected;

    private boolean dragging = false;
    private float dragOffsetX;
    private float dragOffsetY;

    public HUDEditorScreen() {
        super(Component.literal("HUD Editor"));
        this.elements.addAll(ClientUtils.HUD_ELEMENTS);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int buttonWidth = (int)(scaledWidth * 0.23F);
        addRenderableWidget(rpButton = new MenuButton(scaledWidth/2 - buttonWidth - 20, 5, buttonWidth, Utils.translateToLocal("gui.menu.config.reset_defaults"), MenuButton.ButtonType.ROUNDBUTTON, (e) -> {
            for (HUDElement element : elements) {
                element.restoreDefaultValues();
            }
        }));

        addRenderableWidget(resetButton = new MenuButton(scaledWidth/2 + 10, 5, buttonWidth, Utils.translateToLocal("gui.menu.config.reset_rp"), MenuButton.ButtonType.ROUNDBUTTON, (e) -> {
            for (HUDElement element : elements) {
                element.loadDefaultsFromJson();
            }
        }));

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        resetButton.render(guiGraphics, mouseX, mouseY, partialTick);
        rpButton.render(guiGraphics, mouseX, mouseY, partialTick);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int y=3;
        if(!ClientUtils.isKeyDown(GLFW.GLFW_KEY_H)) {
            guiGraphics.drawCenteredString(minecraft.font, Utils.translateToLocal("gui.menu.config.hud.help0"), scaledWidth / 2, y++ * 10, 0xFFFFFF);
        } else {
            for(int i= 1; i < 12; i++) {
                guiGraphics.drawCenteredString(minecraft.font, Utils.translateToLocal("gui.menu.config.hud.help"+i), scaledWidth / 2, y++ * 10, 0xFFFFFF);
            }
        }
        if(selected!=null) {
            guiGraphics.drawCenteredString(minecraft.font,Utils.translateToLocal("gui.menu.config.hud.help12"),scaledWidth / 2,y++*10+10,0xFFFFFF);
            for(String data : selected.getData()) {
                guiGraphics.drawString(minecraft.font, data,scaledWidth / 2-40,y++*10+ 10,0xFFFFFF);
            }
        }

        List<Component> list = new ArrayList<>();
        for (HUDElement element : elements) {
            if(element.isMouseOver(mouseX, mouseY)) {
                String line = (list.isEmpty() ? ChatFormatting.BOLD : "") + element.name;
                list.add(Component.translatable(ChatFormatting.WHITE + line));
                guiGraphics.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
            }
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
            case GLFW.GLFW_KEY_S -> {
                if(hasControlDown()) {
                    for (HUDElement element : elements) {
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
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
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