package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSSetNotifColor {

    public CSSetNotifColor() {}

    int color;

    public CSSetNotifColor(int color) {
        this.color = color;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(color);
    }

    public static CSSetNotifColor decode(FriendlyByteBuf buffer) {
        CSSetNotifColor msg = new CSSetNotifColor();
        msg.color = buffer.readInt();
        return msg;
    }

    public static void handle(CSSetNotifColor message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
           Player player = ctx.get().getSender();
           IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
           playerData.setNotifColor(message.color);
           PacketHandler.syncToAllAround(player, playerData);
        });
        ctx.get().setPacketHandled(true);
    }

}
