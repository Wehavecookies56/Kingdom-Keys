package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.api.event.ChoiceEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class CSSetChoice {

    SoAState state, choice;
    BlockPos pedestal;
    boolean confirm = false;

    public CSSetChoice() {}

    public CSSetChoice(SoAState state, SoAState choice, BlockPos pedestal) {
        this.state = state;
        this.choice = choice;
        this.pedestal = pedestal;
    }

    public CSSetChoice(SoAState state, boolean confirm) {
        this(state, SoAState.NONE, null);
        this.confirm = confirm;
        pedestal = new BlockPos(0, 0, 0);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByte(state.get());
        buffer.writeByte(choice.get());
        buffer.writeBoolean(confirm);
        buffer.writeBlockPos(pedestal);
    }

    public static CSSetChoice decode(FriendlyByteBuf buffer) {
        CSSetChoice msg = new CSSetChoice();
        msg.state = SoAState.fromByte(buffer.readByte());
        msg.choice = SoAState.fromByte(buffer.readByte());
        msg.confirm = buffer.readBoolean();
        msg.pedestal = buffer.readBlockPos();
        return msg;
    }

    public static void handle(CSSetChoice message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            if (message.state == SoAState.CONFIRM) {
                //go back to choices or continue to complete state and leave dimension
                if (message.confirm) {
                    //travel back
                    playerData.setSoAState(SoAState.COMPLETE);
                    ServerLevel dimension = player.level().getServer().getLevel(playerData.getReturnDimension());
                    player.changeDimension(dimension, new BaseTeleporter(playerData.getReturnLocation().x, playerData.getReturnLocation().y, playerData.getReturnLocation().z));
                    SoAState.applyStatsForChoices(player, playerData, false);
                    MinecraftForge.EVENT_BUS.post(new ChoiceEvent(player, playerData.getChosen(), playerData.getSacrificed()));
                } else {
                    //reset to before choice
                    playerData.setChoicePedestal(new BlockPos(0, 0, 0));
                    playerData.setSacrificePedestal(new BlockPos(0, 0, 0));
                    playerData.setChoice(SoAState.NONE);
                    playerData.setSacrifice(SoAState.NONE);
                    playerData.setSoAState(SoAState.CHOICE);
                }
            } else {
                if (message.state == SoAState.CHOICE) {
                    playerData.setChoicePedestal(message.pedestal);
                    playerData.setChoice(message.choice);
                    playerData.setSoAState(SoAState.SACRIFICE);
                } else if (message.state == SoAState.SACRIFICE) {
                    playerData.setSacrificePedestal(message.pedestal);
                    playerData.setSacrifice(message.choice);
                    playerData.setSoAState(SoAState.CONFIRM);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
