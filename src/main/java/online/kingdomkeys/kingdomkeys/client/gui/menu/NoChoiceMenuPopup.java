package online.kingdomkeys.kingdomkeys.client.gui.menu;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuPopup;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSTravelToSoA;

public class NoChoiceMenuPopup extends MenuPopup {

    @Override
    public void OK() {
        //teleport to SoA
        Player player = Minecraft.getInstance().player;
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        playerData.setReturnDimension(player);
        playerData.setReturnLocation(player);
        playerData.setSoAState(SoAState.CHOICE);
        PacketHandler.sendToServer(new CSTravelToSoA());
        Minecraft.getInstance().setScreen(null);
        SoAMessages.INSTANCE.clearMessage();
        SoAMessages.INSTANCE.queueMessages(
                new SoAMessages.Title(Strings.SoA_Title, Strings.SoA_Subtitle),
                new SoAMessages.Title(null, Strings.SoA_ChoiceIntro1, 20, 60, 20),
                new SoAMessages.Title(null, Strings.SoA_ChoiceIntro2, 20, 60, 20),
                new SoAMessages.Title(null, Strings.SoA_ChoiceIntro3, 20, 60, 20),
                new SoAMessages.Title(null, Strings.SoA_ChoiceIntro4, 20, 60, 20)
        );
    }

    @Override
    public void CANCEL() {
        //close menu
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public List<String> getTextToDisplay() {
        return Arrays.asList(Strings.SoA_Menu1,  Strings.SoA_Menu2);
    }
}
