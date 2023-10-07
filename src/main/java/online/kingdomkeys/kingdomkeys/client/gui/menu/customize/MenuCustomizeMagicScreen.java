package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.config.ClientConfig;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MenuCustomizeMagicScreen extends MenuBackground {

    MenuBox boxLeft, boxRight;

    static class MagicButton {
        MenuButton button;
        int level;
        boolean display;

        public MagicButton(MenuButton button, int level, boolean display) {
            this.button = button;
            this.level = level;
            this.display = display;
        }

        public MenuButton getButton() {
            return button;
        }

        public void setButton(MenuButton button) {
            this.button = button;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }
    }

    LinkedHashMap<ResourceLocation, MagicButton> displayedMagic, allMagic;

    MenuButton back, save;

    MenuScrollBar leftScroll, rightScroll;

    int buttonsX = 0;

    public MenuCustomizeMagicScreen(LinkedHashMap<String, int[]> knownMagic) {
        super(Strings.Gui_Menu_Customize_Magic, new Color(0,0,255));
        drawPlayerInfo = false;
        displayedMagic = new LinkedHashMap<>();
        allMagic = new LinkedHashMap<>();
        knownMagic.forEach((s, ints) -> {
            if (ModMagic.registry.get().containsKey(new ResourceLocation(s))) {
                allMagic.put(new ResourceLocation(s), new MagicButton(new MenuButton(0, 0, 100, ModMagic.registry.get().getValue(new ResourceLocation(s)).getTranslationKey(ints[0]), MenuButton.ButtonType.BUTTON, pButton -> magicAction(new ResourceLocation(s))), ints[0], false));
            }
        });
        ModConfigs.magicDisplayedInCommandMenu.forEach(magic -> {
            ResourceLocation magicKey = new ResourceLocation(magic);
            if (allMagic.containsKey(magicKey)) {
                MagicButton magicButton = allMagic.get(magicKey);
                magicButton.setDisplay(true);
                displayedMagic.put(magicKey, magicButton);
                allMagic.remove(magicKey);
            }
        });
    }

    protected void magicAction(ResourceLocation magic) {
        MagicButton magicButton;
        if (allMagic.containsKey(magic)) {
            magicButton = allMagic.get(magic);
        } else {
            magicButton = displayedMagic.get(magic);
        }
        if (!magicButton.display) {
            magicButton.setDisplay(true);
            displayedMagic.put(magic, magicButton);
            allMagic.remove(magic);
        } else {
            magicButton.setDisplay(false);
            allMagic.put(magic, magicButton);
            displayedMagic.remove(magic);
        }
        updateMagicButtons(false);
    }

    protected void action(String string) {
        switch(string) {
            case "back":
                Minecraft.getInstance().setScreen(new MenuCustomizeScreen());
                break;
            case "save":
                ModConfigs.setMagicDisplayedInCommandMenu(displayedMagic.keySet().stream().map(ResourceLocation::toString).toList());
                break;
        }
    }

    float boxLeftPosX;
    float boxRightPosX;
    float topBarHeight;
    float boxWidth;

    float leftScrollOffset;
    float rightScrollOffset;

    @Override
    public void init() {
        boxLeftPosX = (float) width * 0.25F;
        boxRightPosX = (float) width * 0.5F;
        topBarHeight = (float) height * 0.17F;
        boxWidth = (float) width * 0.25F;
        float middleHeight = (float) height * 0.6F;

        boxLeft = new MenuBox((int) boxLeftPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        boxRight = new MenuBox((int) boxRightPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        buttonsX = boxLeft.getX() + 10;

        super.init();
        addRenderableWidget(rightScroll = new MenuScrollBar((int) (boxRightPosX + boxWidth - 14), (int) topBarHeight, 14, 1, (int) topBarHeight, (int) (topBarHeight + middleHeight)));
        addRenderableWidget(leftScroll = new MenuScrollBar((int) (boxLeftPosX + boxWidth - 14), (int) topBarHeight, 14, 1, (int) topBarHeight, (int) (topBarHeight + middleHeight)));
        updateMagicButtons(true);
        addRenderableWidget(save = new MenuButton((int) buttonPosX, (int) topBarHeight + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Save"), MenuButton.ButtonType.BUTTON, (e) -> action("save")));
        addRenderableWidget(back = new MenuButton((int) buttonPosX, (int) topBarHeight + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, (e) -> action("back")));

    }

    public void updateMagicButtons(boolean init) {
        this.renderables.clear();
        for (int i = 0; i < displayedMagic.size(); i++) {
            ResourceLocation key = displayedMagic.keySet().stream().toList().get(i);
            MenuButton button = displayedMagic.get(key).button;
            button.setX((int) boxRightPosX);
            button.setY((int) (topBarHeight - rightScrollOffset + 15 + (i * 20)));
            button.setWidth((int) boxWidth - 22 - 14);
            addRenderableWidget(button);
        }
        for (int i = 0; i < allMagic.size(); i++) {
            ResourceLocation key = allMagic.keySet().stream().toList().get(i);
            MenuButton button = allMagic.get(key).button;
            button.setX((int) boxLeftPosX);
            button.setY((int) (topBarHeight - leftScrollOffset + 15 + (i * 20)));
            button.setWidth((int) boxWidth - 22 - 14);
            addRenderableWidget(button);
        }
        if (!init) {
            addRenderableWidget(rightScroll);
            addRenderableWidget(leftScroll);
            addRenderableWidget(save);
            addRenderableWidget(back);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        boxLeft.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
        boxRight.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
        //same for both
        int scrollBarHeight = rightScroll.getBottom() - rightScroll.top;
        int rightListHeight = 0;
        int leftListHeight = 0;
        if (!displayedMagic.isEmpty()) {
            rightListHeight = displayedMagic.get((ResourceLocation) displayedMagic.keySet().toArray()[displayedMagic.size()-1]).button.getY()+20 - displayedMagic.get((ResourceLocation) displayedMagic.keySet().toArray()[0]).button.getY();
        }
        if (!allMagic.isEmpty()) {
            leftListHeight = (allMagic.get((ResourceLocation) allMagic.keySet().toArray()[allMagic.size() - 1]).button.getY() + 20) - allMagic.get((ResourceLocation) allMagic.keySet().toArray()[0]).button.getY();
        }
        if (scrollBarHeight >= rightListHeight + 15) {
            rightScroll.visible = false;
            rightScroll.active = false;
        } else {
            rightScroll.visible = true;
            rightScroll.active = true;
        }
        if (scrollBarHeight >= leftListHeight + 15) {
            leftScroll.visible = false;
            leftScroll.active = false;
        } else {
            leftScroll.visible = true;
            leftScroll.active = true;
        }
        float buttonRelativeToRightBar = rightScroll.getY() - (rightScroll.top-1);
        float buttonRelativeToLeftBar = leftScroll.getY() - (leftScroll.top-1);
        float rightScrollPos = Math.min(buttonRelativeToRightBar != 0 ? buttonRelativeToRightBar / (scrollBarHeight) : 0, 1);
        float leftScrollPos = Math.min(buttonRelativeToLeftBar != 0 ? buttonRelativeToLeftBar / (scrollBarHeight) : 0, 1);
        rightScrollOffset = rightScrollPos*(rightListHeight-scrollBarHeight);
        leftScrollOffset = leftScrollPos*(leftListHeight-scrollBarHeight);

        updateMagicButtons(false);
        drawSeparately = true;
        drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
        drawCenteredString(matrixStack, Minecraft.getInstance().font, "Hidden", (int) (boxLeftPosX + (boxWidth / 2)), (int) topBarHeight + 3, 0xFFFFFF);
        drawCenteredString(matrixStack, Minecraft.getInstance().font, "Command Menu", (int) (boxRightPosX + (boxWidth / 2)), (int) topBarHeight + 3, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
  }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        rightScroll.mouseClicked(pMouseX, pMouseY, pButton);
        leftScroll.mouseClicked(pMouseX, pMouseY, pButton);
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        rightScroll.mouseReleased(pMouseX, pMouseY, pButton);
        leftScroll.mouseReleased(pMouseX, pMouseY, pButton);
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        rightScroll.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        leftScroll.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (pMouseX >= (Minecraft.getInstance().screen.width / 2F)) {
            rightScroll.mouseScrolled(pMouseX, pMouseY, pDelta);
        } else {
            leftScroll.mouseScrolled(pMouseX, pMouseY, pDelta);
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
}
