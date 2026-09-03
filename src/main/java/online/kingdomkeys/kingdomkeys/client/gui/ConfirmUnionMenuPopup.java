package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.render.SoABridgeRenderer;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetUnion;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ConfirmUnionMenuPopup extends MenuPopup {
    private final Union union;

    public ConfirmUnionMenuPopup(Union union) {
        this.union = union;
    }

    @Nonnull
    @Override
    public String OKString() {
        return Strings.SoA_Ok;
    }

    @Nonnull
    @Override
    public String CANCELString() {
        return Strings.SoA_Cancel;
    }

    @Override
    public List<String> getTextToDisplay() {
        List<String> display = new ArrayList<>();
        display.add(union.getTranslationKey());
        display.add(union.getDescriptionKey());
        display.add(Strings.SoA_UnionConfirm);
        return display;
    }

    @Override
    public void OK() {
        Minecraft mc = Minecraft.getInstance();
        PlayerData playerData = PlayerData.get(mc.player);

        playerData.setUnion(union);
        playerData.setSoAState(SoAState.CHOICE);
        PacketHandler.sendToServer(new CSSetUnion(union));

        if (mc.level != null) {
            SoABridgeRenderer.beginReveal(mc.level.getGameTime());
        }

        mc.setScreen(null);
        SoAMessages.INSTANCE.clearMessage();

        // Runs on into the weapon choice intro, which plays over the walk to the other platform
        SoAMessages.INSTANCE.queueMessages(
                new Utils.Title(null, Strings.SoA_UnionChosen1, 10, 60, 20),
                new Utils.Title(null, Strings.SoA_UnionChosen2, 20, 60, 20),
                new Utils.Title(null, Strings.SoA_ChoiceIntro1, 20, 60, 20),
                new Utils.Title(null, Strings.SoA_ChoiceIntro2, 20, 60, 20),
                new Utils.Title(null, Strings.SoA_ChoiceIntro3, 20, 60, 20),
                new Utils.Title(null, Strings.SoA_ChoiceIntro4, 20, 60, 20)
        );
    }

    @Override
    public void CANCEL() {
        // Back to the platform: nothing has been committed yet
        Minecraft.getInstance().setScreen(null);
    }
}
