package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.function.Supplier;

public class CSTravelToSoA {

    public CSTravelToSoA() {}

    public void encode(FriendlyByteBuf buffer) {

    }

    public static CSTravelToSoA decode(FriendlyByteBuf buffer) {
        CSTravelToSoA msg = new CSTravelToSoA();
        return msg;
    }

    public static void handle(CSTravelToSoA message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            playerData.setReturnDimension(player);
            playerData.setReturnLocation(player);
            playerData.setSoAState(SoAState.CHOICE);
            ServerLevel dimension = player.level.getServer().getLevel(ModDimensions.DIVE_TO_THE_HEART);
            player.changeDimension(dimension, new BaseTeleporter(0, 28, 0));
        });
        ctx.get().setPacketHandled(true);
    }

}
