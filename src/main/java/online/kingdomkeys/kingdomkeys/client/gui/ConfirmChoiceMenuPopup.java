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
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetChoice;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber
public class ConfirmChoiceMenuPopup extends MenuPopup {

    SoAState state, choice;

    //TODO localise
    List<String> warrior = Arrays.asList("The power of the warrior.", "Invincible courage.", "A sword of terrible destruction.");
    List<String> guardian = Arrays.asList("The power of the guardian.", "Kindness to aid friends.", "A shield to repel all.");
    List<String> mystic = Arrays.asList("The power of the mystic.", "Inner strength.", "A staff of wonder and ruin.");

    final String choiceConfirm = "Is this the power you seek?";
    final String sacrificeConfirm = "You give up this power?";

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
        return "Yes.";
    }

    @Nonnull
    @Override
    public String CANCELString() {
        if (state == SoAState.CONFIRM)
            return "Maybe not.";
        return "No.";
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
        PacketHandler.sendToServer(new CSSetChoice(state, choice, pedestal));
        Minecraft.getInstance().displayGuiScreen(null);
        if (state == SoAState.CHOICE) {
            SoAMessages.INSTANCE.clearMessage();
            SoAMessages.INSTANCE.queueMessages(
                    new SoAMessages.Title(null, "Your path is set.", 10, 35, 20),
                    new SoAMessages.Title(null, "Now, what will you give up in exchange?", 10, 70, 20)
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
                    new SoAMessages.Title(null, "Choose carefully.", 10, 35, 20),
                    new SoAMessages.Title(null, "What form will your power take?", 10, 70, 20)
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
                return "of the Warrior.";
            case GUARDIAN:
                return "of the Guardian.";
            case MYSTIC:
                return "of the Mystic.";
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
            displayText.add("You've chose the power");
            displayText.add(getStringForChoice(playerData.getChosen()));
            displayText.add("You've given up the power");
            displayText.add(getStringForChoice(playerData.getSacrificed()));
            displayText.add("Is this the form you choose?");
        }
        return displayText;
    }

    //Show messages again and hide selected pedestals if player quits during the process
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event) {
        if (event.getPhase() == EventPriority.LOWEST) {
            if (event.getEntity().dimension == ModDimensions.DIVE_TO_THE_HEART_TYPE) {
                if (event.getEntity() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) event.getEntity();
                    IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
                    TileEntity teChoice = event.getWorld().getTileEntity(playerData.getChoicePedestal());
                    TileEntity teSacrifice = event.getWorld().getTileEntity(playerData.getSacrificePedestal());
                    if (teChoice != null) {
                        ((PedestalTileEntity) teChoice).hide = true;
                    }
                    if (teSacrifice != null) {
                        ((PedestalTileEntity) teSacrifice).hide = true;
                    }
                    if (playerData.getSoAState() == SoAState.CHOICE) {
                        SoAMessages.INSTANCE.clearMessage();
                        SoAMessages.INSTANCE.queueMessages(
                                new SoAMessages.Title("Station of Awakening", "Dive to the heart"),
                                new SoAMessages.Title(null, "Power sleeps within you.", 10, 35, 20),
                                new SoAMessages.Title(null, "If you give it form...", 10, 35, 20),
                                new SoAMessages.Title(null, "It will give you strength.", 10, 35, 20),
                                new SoAMessages.Title(null, "Choose well.", 10, 35, 20)
                        );
                    }
                    if (playerData.getSoAState() == SoAState.SACRIFICE) {
                        SoAMessages.INSTANCE.clearMessage();
                        SoAMessages.INSTANCE.queueMessages(
                                new SoAMessages.Title(null, "Your path is set.", 10, 35, 20),
                                new SoAMessages.Title(null, "Now, what will you give up in exchange?", 10, 70, 20)
                        );
                    }
                    if (playerData.getSoAState() == SoAState.CONFIRM) {
                        Minecraft.getInstance().displayGuiScreen(new ConfirmChoiceMenuPopup(SoAState.CONFIRM, SoAState.NONE, new BlockPos(0, 0, 0)));
                    }
                }
            }
        }
    }

}
