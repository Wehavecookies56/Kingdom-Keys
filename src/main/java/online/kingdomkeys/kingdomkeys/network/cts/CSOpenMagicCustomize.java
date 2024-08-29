package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMagicCustomize;

public class CSOpenMagicCustomize {

    public CSOpenMagicCustomize() {}

    public void encode(FriendlyByteBuf buffer) {}

    public static CSOpenMagicCustomize decode(FriendlyByteBuf buffer) {
        return new CSOpenMagicCustomize();
    }

    public static void handle(CSOpenMagicCustomize message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerData cap = ModData.getPlayer(player);
            PacketHandler.sendTo(new SCOpenMagicCustomize(cap.getMagicsMap()), ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}
