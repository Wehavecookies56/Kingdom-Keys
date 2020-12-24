package online.kingdomkeys.kingdomkeys.client.gui.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.ConfirmChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuPopup;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSTravelToSoA;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NoChoiceMenuPopup extends MenuPopup {

    @Override
    public void OK() {
        //teleport to SoA
        PlayerEntity player = Minecraft.getInstance().player;
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        playerData.setReturnDimension(player);
        playerData.setReturnLocation(player);
        playerData.setSoAState(SoAState.CHOICE);
        PacketHandler.sendToServer(new CSTravelToSoA());
        Minecraft.getInstance().displayGuiScreen(null);
        SoAMessages.INSTANCE.clearMessage();
        SoAMessages.INSTANCE.queueMessages(
                new SoAMessages.Title("Station of Awakening", "Dive to the heart"),
                new SoAMessages.Title(null, "Power sleeps within you.", 10, 35, 20),
                new SoAMessages.Title(null, "If you give it form...", 10, 35, 20),
                new SoAMessages.Title(null, "It will give you strength.", 10, 35, 20),
                new SoAMessages.Title(null, "Choose well.", 10, 35, 20)
        );
    }

    @Override
    public void CANCEL() {
        //close menu
        Minecraft.getInstance().displayGuiScreen(null);
    }

    @Override
    public List<String> getTextToDisplay() {
        return Arrays.asList("Before you can open the menu.",  "You must make a choice.");
    }
}
