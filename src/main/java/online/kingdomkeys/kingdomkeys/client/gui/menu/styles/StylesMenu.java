package online.kingdomkeys.kingdomkeys.client.gui.menu.styles;

import net.minecraft.client.gui.GuiGraphics;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSChangeStyle;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class StylesMenu extends MenuBackground {
    ActualWindow window = ActualWindow.SINGLE;
    private MenuButton backButton, singleButton, dualButton;
    private MenuButton sora, roxas, riku, terra, aqua, ventus;

    private MenuButton kh2RoxasDual, daysRoxasDual;
    private final List<MenuButton> singleStyleList = new ArrayList<>();
    private List<MenuButton> dualStyleList = new ArrayList<>();

    public StylesMenu() {
        super(Strings.Gui_Menu_Style, new Color(0, 0, 255));
    }

    @Override
    public void init() {

        super.init();
        initSingle();
        initDualStyle();
        addRenderableWidget(singleButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.single"), MenuButton.ButtonType.BUTTON, e -> window = ActualWindow.SINGLE));
        addRenderableWidget(dualButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (18), (int) buttonWidth, Utils.translateToLocal("gui.menu.style.dual"), MenuButton.ButtonType.BUTTON, e -> window = ActualWindow.DUAL));
        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (36), (int) buttonWidth, Utils.translateToLocal("gui.menu.back"), MenuButton.ButtonType.BUTTON, e -> GuiHelper.openMenu()));
    }

    private void initSingle(){
        int pos = 0;
        IPlayerData playerCapabilities = ModData.getPlayer(minecraft.player);
        addRenderableWidget(sora = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.sora"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setSingleStyle(SingleChoices.SORA);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.SORA.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(roxas = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.roxas"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setSingleStyle(SingleChoices.ROXAS);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.ROXAS.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(riku = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.riku"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setSingleStyle(SingleChoices.RIKU);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.RIKU.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(terra = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.terra"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setSingleStyle(SingleChoices.TERRA);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.TERRA.toString(), HandStyle.SINGLE.toString()));
        } ));
        addRenderableWidget(aqua = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.aqua"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setSingleStyle(SingleChoices.AQUA);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.AQUA.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(ventus = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.ventus"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setSingleStyle(SingleChoices.VENTUS);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.VENTUS.toString(), HandStyle.SINGLE.toString()));
        }));

        singleStyleList.add(sora);
        singleStyleList.add(roxas);
        singleStyleList.add(riku);
        singleStyleList.add(terra);
        singleStyleList.add(aqua);
        singleStyleList.add(ventus);
    }

    private void initDualStyle(){
        int pos = 0;

        IPlayerData playerCapabilities = ModData.getPlayer(minecraft.player);
        addRenderableWidget(kh2RoxasDual = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.kh2roxasdual"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setDualStyle(DualChoices.KH2_ROXAS_DUAL);
            PacketHandler.sendToServer(new CSChangeStyle(DualChoices.KH2_ROXAS_DUAL.toString(), HandStyle.DUAL.toString()));
        }));
        addRenderableWidget(daysRoxasDual = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.daysroxasdual"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerCapabilities.setDualStyle(DualChoices.DAYS_ROXAS_DUAL);
            PacketHandler.sendToServer(new CSChangeStyle(DualChoices.DAYS_ROXAS_DUAL.toString(), HandStyle.DUAL.toString()));
        }));

        dualStyleList.add(kh2RoxasDual);
        dualStyleList.add(daysRoxasDual);
    }
    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);
        IPlayerData playerCapabilities = ModData.getPlayer(minecraft.player);
        for (MenuButton b : singleStyleList) {
            b.active = false;
            b.visible = false;
        }
        for (MenuButton b : dualStyleList) {
            b.active = false;
            b.visible = false;
        }
        gui.drawString(minecraft.font, playerCapabilities.getSingleStyle().toString(), 300, 50, 0xFF9900);
        gui.drawString(minecraft.font, playerCapabilities.getDualStyle().toString(), 300, 70, 0xFF9900);
        switch (window) {
            case SINGLE -> {
                for (MenuButton b : singleStyleList) {
                    b.active = true;
                    b.visible = true;

                }
            }
            case DUAL -> {

                for (MenuButton b : dualStyleList) {
                    b.active = true;
                    b.visible = true;
                }
            }
        }
    }

    private void setStyle(SingleChoices c) {

    }

    enum ActualWindow {
        SINGLE, DUAL
    }

}
