package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSSyncArmorColor {

    public CSSyncArmorColor() {}

    int color;
    boolean glint;

    public CSSyncArmorColor(int color, boolean glint) {
        this.color = color;
        this.glint = glint;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(color);
        buffer.writeBoolean(glint);
    }

    public static CSSyncArmorColor decode(FriendlyByteBuf buffer) {
        CSSyncArmorColor msg = new CSSyncArmorColor();
        msg.color = buffer.readInt();
        msg.glint = buffer.readBoolean();
        return msg;
    }

    public static void handle(CSSyncArmorColor message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
           Player player = ctx.get().getSender();
           IPlayerData playerData = ModData.getPlayer(player);
           playerData.setArmorColor(message.color);
           playerData.setArmorGlint(message.glint);
           PacketHandler.syncToAllAround(player, playerData);
        });
        ctx.get().setPacketHandled(true);
    }

}
