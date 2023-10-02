package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.capability.PlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.PlayerCapabilitiesProvider;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMagicCustomize;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class CSOpenMagicCustomize {

    public CSOpenMagicCustomize() {}

    public void encode(FriendlyByteBuf buffer) {}

    public static CSOpenMagicCustomize decode(FriendlyByteBuf buffer) {
        return new CSOpenMagicCustomize();
    }

    public static void handle(CSOpenMagicCustomize message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities cap = ModCapabilities.getPlayer(player);
            PacketHandler.sendTo(new SCOpenMagicCustomize(cap.getMagicsMap()), ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}
