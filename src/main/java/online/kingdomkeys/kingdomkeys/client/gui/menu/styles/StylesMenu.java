package online.kingdomkeys.kingdomkeys.client.gui.menu.styles;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.style.KKFightingStyle;
import online.kingdomkeys.kingdomkeys.integration.epicfight.style.KKStyleRegistry;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSChangeStyle;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StylesMenu extends MenuBackground {

    private static final int ROW_HEIGHT = 18;
    private static final int COLUMN_GAP = 6;

    ActualWindow window = ActualWindow.SINGLE;

    private MenuButton backButton, singleButton, dualButton;

    private final Map<MenuButton, KKFightingStyle> styleList = new LinkedHashMap<>();

    PlayerData playerData;

    public StylesMenu(PlayerData playerData) {
        super(Strings.Gui_Menu_Style, new Color(0, 0, 255));
        this.playerData = playerData;
    }

    @Override
    public void init() {
        super.init();

        styleList.clear();
        initStyles(HandStyle.SINGLE);
        initStyles(HandStyle.DUAL);

        addRenderableWidget(singleButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5, (int) buttonWidth, Utils.translateToLocal("gui.menu.style.single"), MenuButton.ButtonType.BUTTON, e -> window = ActualWindow.SINGLE));
        addRenderableWidget(dualButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (18), (int) buttonWidth, Utils.translateToLocal("gui.menu.style.dual"), MenuButton.ButtonType.BUTTON, e -> window = ActualWindow.DUAL));
        addRenderableWidget(backButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (36), (int) buttonWidth, Utils.translateToLocal("gui.menu.back"), MenuButton.ButtonType.BUTTON, e -> PacketHandler.sendToServer(new CSOpenMenu())));

        dualButton.active = true;
    }

    private void initStyles(HandStyle hand) {
        List<KKFightingStyle> styles = KKStyleRegistry.of(hand);

        int top = (int) topBarHeight + 5;
        int left = (int) (buttonPosX + 50 + buttonWidth);
        int perColumn = Math.max(1, (height - top - 10) / ROW_HEIGHT);

        for (int i = 0; i < styles.size(); i++) {
            KKFightingStyle style = styles.get(i);
            ResourceLocation id = style.getId();

            int x = left + (i / perColumn) * ((int) buttonWidth + COLUMN_GAP);
            int y = top + (i % perColumn) * ROW_HEIGHT;

            MenuButton button = new MenuButton(x, y, (int) buttonWidth, Utils.translateToLocal(style.getName()), MenuButton.ButtonType.BUTTON, e -> {
                if (hand == HandStyle.DUAL) {
                    playerData.setDualStyle(id);
                } else {
                    playerData.setSingleStyle(id);
                }
                PacketHandler.sendToServer(new CSChangeStyle(id));
            });

            addRenderableWidget(button);
            styleList.put(button, style);
        }
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        super.render(gui, mouseX, mouseY, partialTicks);

        for (MenuButton button : styleList.keySet()) {
            button.active = false;
            button.visible = false;
        }

        gui.pose().pushPose();
        {
            float scale = 1.5F;
            gui.pose().scale(scale, scale, 1);
            gui.drawString(minecraft.font, Component.literal(nameOf(HandStyle.SINGLE) + " / " + nameOf(HandStyle.DUAL)).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
        }
        gui.pose().popPose();

        HandStyle shown = window == ActualWindow.DUAL ? HandStyle.DUAL : HandStyle.SINGLE;

        for (Map.Entry<MenuButton, KKFightingStyle> entry : styleList.entrySet()) {
            KKFightingStyle style = entry.getValue();

            if (style.getHand() != shown) {
                continue;
            }

            entry.getKey().visible = true;
            entry.getKey().active = style.isUnlocked(playerData) && !style.getId().equals(chosen(shown));
        }
    }

    private ResourceLocation chosen(HandStyle hand) {
        return hand == HandStyle.DUAL ? playerData.getDualStyle() : playerData.getSingleStyle();
    }

    private String nameOf(HandStyle hand) {
        ResourceLocation id = chosen(hand);
        KKFightingStyle style = KKStyleRegistry.get(id);
        return style == null ? String.valueOf(id) : Utils.translateToLocal(style.getName());
    }

    enum ActualWindow {
        SINGLE, DUAL
    }
}
