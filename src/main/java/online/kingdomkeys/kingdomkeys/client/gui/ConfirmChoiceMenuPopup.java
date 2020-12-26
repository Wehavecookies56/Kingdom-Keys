package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuPopup;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetChoice;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfirmChoiceMenuPopup extends MenuPopup {

    SoAState state, choice;

    //TODO localise

    List<String> warrior = Arrays.asList(Strings.SoA_Warrior1, Strings.SoA_Warrior2, Strings.SoA_Warrior3);
    List<String> guardian = Arrays.asList(Strings.SoA_Guardian1, Strings.SoA_Guardian2, Strings.SoA_Guardian3);
    List<String> mystic = Arrays.asList(Strings.SoA_Mystic1, Strings.SoA_Mystic2, Strings.SoA_Mystic3);

    final String choiceConfirm = Strings.SoA_ChoiceConfirm;
    final String sacrificeConfirm = Strings.SoA_SacrificeConfirm;

    //Just for the purpose of passing to the packet
    BlockPos pedestal;

    public ConfirmChoiceMenuPopup(SoAState state, SoAState choice, BlockPos pedestal) {
        this.state = state;
        this.choice = choice;
        this.pedestal = pedestal;
    }

    @Nonnull
    @Override
    public String OKString() {
        return Strings.SoA_Ok;
    }

    @Nonnull
    @Override
    public String CANCELString() {
        if (state == SoAState.CONFIRM)
            return Strings.SoA_ConfirmCancel;
        return Strings.SoA_Cancel;
    }

    @Override
    public void OK() {
        //set choice
        Minecraft mc = Minecraft.getInstance();
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
        TileEntity te = mc.world.getTileEntity(pedestal);
        if (state == SoAState.CONFIRM) {
            playerData.setSoAState(SoAState.COMPLETE);
            PacketHandler.sendToServer(new CSSetChoice(state, true));
        } else {
            if (state == SoAState.CHOICE) {
                playerData.setChoicePedestal(pedestal);
                playerData.setChoice(choice);
                playerData.setSoAState(SoAState.SACRIFICE);
                if (te != null) {
                    //Hiding on client only
                    ((PedestalTileEntity) te).hide = true;
                }
            } else if (state == SoAState.SACRIFICE) {
                playerData.setSacrificePedestal(pedestal);
                playerData.setSacrifice(choice);
                playerData.setSoAState(SoAState.CONFIRM);
                if (te != null) {
                    //Hiding on client only
                    ((PedestalTileEntity) te).hide = true;
                }
            }
        }
        if (state != SoAState.CONFIRM) {
            PacketHandler.sendToServer(new CSSetChoice(state, choice, pedestal));
        }
        Minecraft.getInstance().displayGuiScreen(null);
        if (state == SoAState.CHOICE) {
            SoAMessages.INSTANCE.clearMessage();
            SoAMessages.INSTANCE.queueMessages(
                    new SoAMessages.Title(null, Strings.SoA_SacrificeIntro1, 10, 35, 20),
                    new SoAMessages.Title(null, Strings.SoA_SacrificeIntro2, 10, 70, 20)
            );
        }
        if (state == SoAState.SACRIFICE) {
            Minecraft.getInstance().displayGuiScreen(new ConfirmChoiceMenuPopup(SoAState.CONFIRM, SoAState.NONE, new BlockPos(0, 0, 0)));
        }
    }

    @Override
    public void CANCEL() {
        //close menu
        if (state == SoAState.CONFIRM) {
            SoAMessages.INSTANCE.clearMessage();
            SoAMessages.INSTANCE.queueMessages(
                    new SoAMessages.Title(null, Strings.SoA_ResetIntro1, 10, 35, 20),
                    new SoAMessages.Title(null, Strings.SoA_ResetIntro2, 10, 70, 20)
            );
            Minecraft mc = Minecraft.getInstance();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);
            TileEntity teChoice = mc.world.getTileEntity(playerData.getChoicePedestal());
            TileEntity teSacrifice = mc.world.getTileEntity(playerData.getSacrificePedestal());
            playerData.setChoicePedestal(new BlockPos(0, 0, 0));
            playerData.setSacrificePedestal(new BlockPos(0, 0, 0));
            playerData.setChoice(SoAState.NONE);
            playerData.setSacrifice(SoAState.NONE);
            playerData.setSoAState(SoAState.CHOICE);
            if (teChoice != null) {
                ((PedestalTileEntity)teChoice).hide = false;
            }
            if (teSacrifice != null) {
                ((PedestalTileEntity)teSacrifice).hide = false;
            }
            PacketHandler.sendToServer(new CSSetChoice(state, false));
        }
        Minecraft.getInstance().displayGuiScreen(null);
    }

    public String getStringForChoice(SoAState state) {
        switch (state) {
            case WARRIOR:
                return Strings.SoA_ConfirmWarrior;
            case GUARDIAN:
                return Strings.SoA_ConfirmGuardian;
            case MYSTIC:
                return Strings.SoA_ConfirmMystic;
        }
        return "This ain't right";
    }

    @Override
    public List<String> getTextToDisplay() {
        List<String> displayText = new ArrayList<>();
        if (state != SoAState.CONFIRM) {
            switch (choice) {
                case WARRIOR:
                    displayText.addAll(warrior);
                    break;
                case GUARDIAN:
                    displayText.addAll(guardian);
                    break;
                case MYSTIC:
                    displayText.addAll(mystic);
                    break;
                default:
                    return Collections.singletonList("This ain't right");
            }
            switch (state) {
                case CHOICE:
                    displayText.add(choiceConfirm);
                    break;
                case SACRIFICE:
                    displayText.add(sacrificeConfirm);
                    break;
                default:
                    return Collections.singletonList("This ain't right");
            }
        } else {
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
            displayText.add(Strings.SoA_Confirm1);
            displayText.add(getStringForChoice(playerData.getChosen()));
            displayText.add(Strings.SoA_Confirm3);
            displayText.add(getStringForChoice(playerData.getSacrificed()));
            displayText.add(Strings.SoA_Confirm5);
        }
        return displayText;
    }
}
