package online.kingdomkeys.kingdomkeys.client.gui.menu.styles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSChangeStyle;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
public class StylesMenu extends MenuBackground {
    ActualWindow window = ActualWindow.SINGLE;
    private MenuButton backButton, singleButton, dualButton;
    private MenuButton sora, roxas, riku, terra, aqua, ventus;

    private MenuButton kh2RoxasDual, daysRoxasDual;

    private final Map<MenuButton, SingleChoices> singleStyleList = new LinkedHashMap<>();
    private final Map<MenuButton, DualChoices> dualStyleList = new LinkedHashMap<>();

    private final Set<MenuButton> unavailable = new HashSet<>();

    PlayerData playerData;

    public StylesMenu(PlayerData playerData) {
        super(Strings.Gui_Menu_Style, new Color(0, 0, 255));
        this.playerData = playerData;
    }

    @Override
    public void init() {
        super.init();
        initSingle();
        initDualStyle();
        addRenderableWidget(singleButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.single"), MenuButton.ButtonType.BUTTON, e -> window = ActualWindow.SINGLE));
        addRenderableWidget(dualButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (18), (int) buttonWidth, Utils.translateToLocal("gui.menu.style.dual"), MenuButton.ButtonType.BUTTON, e -> window = ActualWindow.DUAL));
        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (36), (int) buttonWidth, Utils.translateToLocal("gui.menu.back"), MenuButton.ButtonType.BUTTON, e -> PacketHandler.sendToServer(new CSOpenMenu())));

        unavailable.add(roxas);
        unavailable.add(terra);
        unavailable.add(ventus);

        dualButton.active = false;

    }

    private void initSingle(){
        int pos = 0;
        addRenderableWidget(sora = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.sora"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setSingleStyle(SingleChoices.SORA);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.SORA.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(riku = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.riku"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setSingleStyle(SingleChoices.RIKU);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.RIKU.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(roxas = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.roxas"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setSingleStyle(SingleChoices.ROXAS);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.ROXAS.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(terra = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.terra"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setSingleStyle(SingleChoices.TERRA);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.TERRA.toString(), HandStyle.SINGLE.toString()));
        } ));
        addRenderableWidget(aqua = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.aqua"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setSingleStyle(SingleChoices.AQUA);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.AQUA.toString(), HandStyle.SINGLE.toString()));
        }));
        addRenderableWidget(ventus = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18* ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.ventus"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setSingleStyle(SingleChoices.VENTUS);
            PacketHandler.sendToServer(new CSChangeStyle(SingleChoices.VENTUS.toString(), HandStyle.SINGLE.toString()));
        }));

        singleStyleList.put(sora, SingleChoices.SORA);
        singleStyleList.put(riku, SingleChoices.RIKU);
        singleStyleList.put(roxas, SingleChoices.ROXAS);
        singleStyleList.put(terra, SingleChoices.TERRA);
        singleStyleList.put(aqua, SingleChoices.AQUA);
        singleStyleList.put(ventus, SingleChoices.VENTUS);

    }

    private void initDualStyle(){
        int pos = 0;

        addRenderableWidget(kh2RoxasDual = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.kh2roxasdual"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setDualStyle(DualChoices.KH2_ROXAS_DUAL);
            PacketHandler.sendToServer(new CSChangeStyle(DualChoices.KH2_ROXAS_DUAL.toString(), HandStyle.DUAL.toString()));
        }));
        addRenderableWidget(daysRoxasDual = new MenuButton((int) ( buttonPosX+ 50 + buttonWidth), (int) topBarHeight + 5 + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.daysroxasdual"), MenuButton.ButtonType.BUTTON, e ->
        {
            playerData.setDualStyle(DualChoices.DAYS_ROXAS_DUAL);
            PacketHandler.sendToServer(new CSChangeStyle(DualChoices.DAYS_ROXAS_DUAL.toString(), HandStyle.DUAL.toString()));
        }));

        dualStyleList.put(kh2RoxasDual, DualChoices.KH2_ROXAS_DUAL);
        dualStyleList.put(daysRoxasDual, DualChoices.DAYS_ROXAS_DUAL);
    }
    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);

        for (MenuButton b : singleStyleList.keySet()) {
            b.active = false;
            b.visible = false;
        }
        for (MenuButton b : dualStyleList.keySet()) {
            b.active = false;
            b.visible = false;
        }

        gui.pose().pushPose();
        {
            float scale = 1.5F;
            gui.pose().scale(scale, scale, 1);
            gui.drawString(minecraft.font, Component.literal(playerData.getSingleStyle().toString()+" / "+playerData.getDualStyle().toString()).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
        }
        gui.pose().popPose();

        switch (window) {
            case SINGLE -> {
                for (Map.Entry<MenuButton, SingleChoices> entry : singleStyleList.entrySet()) {
                    entry.getKey().visible = true;
                    entry.getKey().active = !unavailable.contains(entry.getKey()) && playerData.getSingleStyle() != entry.getValue();
                }
            }
            case DUAL -> {
                for (Map.Entry<MenuButton, DualChoices> entry : dualStyleList.entrySet()) {
                    entry.getKey().visible = true;
                    entry.getKey().active = !unavailable.contains(entry.getKey()) && playerData.getDualStyle() != entry.getValue();
                }
            }
        }
    }

    enum ActualWindow {
        SINGLE, DUAL
    }

}
