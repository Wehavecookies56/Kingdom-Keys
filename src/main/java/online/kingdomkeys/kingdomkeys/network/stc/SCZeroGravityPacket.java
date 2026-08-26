package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCZeroGravityPacket(boolean value) implements Packet {

    public static final Type<SCZeroGravityPacket> TYPE = new Type<>(KingdomKeys.rl("sc_zero_gravity"));

    public static final StreamCodec<FriendlyByteBuf, SCZeroGravityPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SCZeroGravityPacket::value,
            SCZeroGravityPacket::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.zeroGravity(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
