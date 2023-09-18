package online.kingdomkeys.kingdomkeys.client.gui.menu.styles;

import com.mojang.blaze3d.vertex.PoseStack;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
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

    private void initSingle() {
        int pos = 0;
        IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(minecraft.player);
        addRenderableWidget(sora = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.sora"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.SORA, HandStyle.SINGLE));
            playerCapabilities.setSingleStyle(SingleChoices.SORA);
        }));
        addRenderableWidget(roxas = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.roxas"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.ROXAS, HandStyle.SINGLE));
            playerCapabilities.setSingleStyle(SingleChoices.ROXAS);
        }));
        addRenderableWidget(riku = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.riku"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.RIKU, HandStyle.SINGLE));
            playerCapabilities.setSingleStyle(SingleChoices.RIKU);
        }));
        addRenderableWidget(terra = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.terra"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.TERRA, HandStyle.SINGLE));
            playerCapabilities.setSingleStyle(SingleChoices.TERRA);
        } ));
        addRenderableWidget(aqua = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.aqua"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.AQUA, HandStyle.SINGLE));
            playerCapabilities.setSingleStyle(SingleChoices.AQUA);
        }));
        addRenderableWidget(ventus = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.ventus"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.VENTUS, HandStyle.SINGLE));
            playerCapabilities.setSingleStyle(SingleChoices.VENTUS);
        }));

        singleStyleList.add(sora);
        singleStyleList.add(roxas);
        singleStyleList.add(riku);
        singleStyleList.add(terra);
        singleStyleList.add(aqua);
        singleStyleList.add(ventus);

    }

    private void initDualStyle()
    {
        int pos = 0;

        IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(minecraft.player);
        addRenderableWidget(kh2RoxasDual = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.kh2roxasdual"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(DualChoices.KH2_ROXAS_DUAL, HandStyle.DUAL));
            playerCapabilities.setDualStyle(DualChoices.KH2_ROXAS_DUAL);
        }));
        addRenderableWidget(daysRoxasDual = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.daysroxasdual"), MenuButton.ButtonType.BUTTON, e ->
        {
            PacketHandler.sendToServer(new CSChangeStyle(DualChoices.DAYS_ROXAS_DUAL, HandStyle.DUAL));
            playerCapabilities.setDualStyle(DualChoices.DAYS_ROXAS_DUAL);
        }));

        dualStyleList.add(kh2RoxasDual);
        dualStyleList.add(daysRoxasDual);
    }
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        IPlayerCapabilities playerCapabilities = ModCapabilities.getPlayer(minecraft.player);
        for (MenuButton b : singleStyleList) {
            b.active = false;
            b.visible = false;
        }
        for (MenuButton b : dualStyleList) {
            b.active = false;
            b.visible = false;
        }
        drawString(matrixStack ,minecraft.font, playerCapabilities.getSingleStyle().toString(), 300, 50, 0xFF9900);
        drawString(matrixStack ,minecraft.font, playerCapabilities.getDualStyle().toString(), 300, 70, 0xFF9900);
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


