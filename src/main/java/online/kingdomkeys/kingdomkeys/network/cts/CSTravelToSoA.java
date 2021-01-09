package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.SoAState;

public class CSTravelToSoA {

    public CSTravelToSoA() {}

    public void encode(PacketBuffer buffer) {

    }

    public static CSTravelToSoA decode(PacketBuffer buffer) {
        CSTravelToSoA msg = new CSTravelToSoA();
        return msg;
    }

    public static void handle(CSTravelToSoA message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            playerData.setReturnDimension(player);
            playerData.setReturnLocation(player);
            playerData.setSoAState(SoAState.CHOICE);
            //TODO figure out this
            //player.changeDimension(ModDimensions.DIVE_TO_THE_HEART_TYPE, new BaseTeleporter(0, 28, 0));
        });
        ctx.get().setPacketHandled(true);
    }

}
