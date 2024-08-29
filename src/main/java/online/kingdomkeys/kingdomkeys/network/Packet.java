package online.kingdomkeys.kingdomkeys.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface Packet extends CustomPacketPayload {
    void handle(IPayloadContext context);
}
