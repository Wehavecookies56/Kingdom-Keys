package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenShortcutsCustomize;

public class CSOpenShortcutsCustomize {

    public CSOpenShortcutsCustomize() {}

    public void encode(FriendlyByteBuf buffer) {}

    public static CSOpenShortcutsCustomize decode(FriendlyByteBuf buffer) {
        return new CSOpenShortcutsCustomize();
    }

    public static void handle(CSOpenShortcutsCustomize message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities cap = ModCapabilities.getPlayer(player);
            PacketHandler.sendTo(new SCOpenShortcutsCustomize(cap.getMagicsMap()), ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }
}
