package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.function.Supplier;

public class CSSetAlignment {

    public CSSetAlignment() {}

    Utils.OrgMember alignment;

    public CSSetAlignment(Utils.OrgMember alignment) {
        this.alignment = alignment;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(alignment.ordinal());
    }

    public static CSSetAlignment decode(PacketBuffer buffer) {
        CSSetAlignment msg = new CSSetAlignment();
        msg.alignment = Utils.OrgMember.values()[buffer.readInt()];
        return msg;
    }

    public static void handle(CSSetAlignment message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            playerData.setAlignment(message.alignment);
        });
        ctx.get().setPacketHandled(true);
    }

}
