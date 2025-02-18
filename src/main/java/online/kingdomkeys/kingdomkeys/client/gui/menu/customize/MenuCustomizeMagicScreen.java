package online.kingdomkeys.kingdomkeys.client.gui.menu.customize;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.LinkedHashMap;

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

    MenuButton back;

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
        ModConfigs.setMagicDisplayedInCommandMenu(displayedMagic.keySet().stream().map(ResourceLocation::toString).toList());
        if(displayedMagic.isEmpty() && CommandMenuGui.INSTANCE.currentSubmenu.equals(CommandMenuGui.INSTANCE.magic)) {
        	CommandMenuGui.INSTANCE.changeSubmenu(CommandMenuGui.INSTANCE.root, true);
        }
    }

    protected void action(String string) {
        switch(string) {
            case "back":
                Minecraft.getInstance().setScreen(new MenuCustomizeScreen());
                break;
        }
    }

    float boxLeftPosX;
    float boxRightPosX;
    float topBarHeight;
    float boxWidth;
    int rightListHeight = 0;
    int leftListHeight = 0;

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

        buttonPosY = (int) (topBarHeight + 5);

        super.init();
        addRenderableWidget(rightScroll = new MenuScrollBar((int) (boxRightPosX + boxWidth - 14), (int) topBarHeight, (int) (topBarHeight + middleHeight), (int) middleHeight - 3 - font.lineHeight, 0));
        addRenderableWidget(leftScroll = new MenuScrollBar((int) (boxLeftPosX + boxWidth - 14), (int) topBarHeight, (int) (topBarHeight + middleHeight), (int) middleHeight - 3 - font.lineHeight, 0));
        updateMagicButtons(true);
        addRenderableWidget(back = new MenuButton((int) buttonPosX, (int) buttonPosY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), MenuButton.ButtonType.BUTTON, (e) -> action("back")));

    }

    public void updateMagicButtons(boolean init) {
        this.renderables.clear();
        this.children().clear();
        for (int i = 0; i < displayedMagic.size(); i++) {
            ResourceLocation key = displayedMagic.keySet().stream().toList().get(i);
            MenuButton button = displayedMagic.get(key).button;
            button.setX((int) boxRightPosX);
            button.setY((int) (topBarHeight - rightScroll.scrollOffset + 15 + (i * 20)));
            button.setWidth((int) boxWidth - 22 - 14);
            addRenderableWidget(button);
        }
        for (int i = 0; i < allMagic.size(); i++) {
            ResourceLocation key = allMagic.keySet().stream().toList().get(i);
            MenuButton button = allMagic.get(key).button;
            button.setX((int) boxLeftPosX);
            button.setY((int) (topBarHeight - leftScroll.scrollOffset + 15 + (i * 20)));
            button.setWidth((int) boxWidth - 22 - 14);
            addRenderableWidget(button);
        }
        if (!init) {
            addRenderableWidget(rightScroll);
            addRenderableWidget(leftScroll);
            addRenderableWidget(back);
        }
        if (!displayedMagic.isEmpty()) {
            rightListHeight = displayedMagic.get((ResourceLocation) displayedMagic.keySet().toArray()[displayedMagic.size()-1]).button.getY()+20 - displayedMagic.get((ResourceLocation) displayedMagic.keySet().toArray()[0]).button.getY() + 5;
        }
        if (!allMagic.isEmpty()) {
            leftListHeight = (allMagic.get((ResourceLocation) allMagic.keySet().toArray()[allMagic.size() - 1]).button.getY() + 20) - allMagic.get((ResourceLocation) allMagic.keySet().toArray()[0]).button.getY() + 5;
        }
        rightScroll.setContentHeight(rightListHeight);
        leftScroll.setContentHeight(leftListHeight);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(gui, mouseX, mouseY, partialTicks);
        boxLeft.renderWidget(gui, mouseX, mouseY, partialTicks);
        boxRight.renderWidget(gui, mouseX, mouseY, partialTicks);

        updateMagicButtons(false);
        drawSeparately = true;
        gui.drawCenteredString(Minecraft.getInstance().font, Utils.translateToLocal(Strings.Gui_Menu_Customize_Magic_Hidden), (int) (boxLeftPosX + (boxWidth / 2)), (int) topBarHeight + 3, 0xFFFFFF);
        gui.drawCenteredString(Minecraft.getInstance().font, Utils.translateToLocal(Strings.Gui_Menu_Customize_Magic_Active), (int) (boxRightPosX + (boxWidth / 2)), (int) topBarHeight + 3, 0xFFFFFF);
        gui.enableScissor((int) boxLeftPosX, (int) topBarHeight + 3 + font.lineHeight, (int) boxLeft.getWidth()*4, (int) (topBarHeight + middleHeight));
        for(Renderable renderable : this.renderables) {
            if (renderable instanceof Button button) {
                if (button.getX() >= boxLeftPosX-1) {
                    renderable.render(gui, mouseX, mouseY, partialTicks);
                }
            }
        }
        gui.disableScissor();
        back.render(gui, mouseX, mouseY, partialTicks);
        rightScroll.render(gui, mouseX, mouseY, partialTicks);
        leftScroll.render(gui, mouseX, mouseY, partialTicks);
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
