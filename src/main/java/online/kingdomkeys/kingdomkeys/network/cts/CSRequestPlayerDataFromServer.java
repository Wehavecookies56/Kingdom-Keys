package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.stc.SCSendPlayerDataToClient;

public record CSRequestPlayerDataFromServer() implements Packet {

    public static final Type<CSRequestPlayerDataFromServer> TYPE = new Type<>(KingdomKeys.rl("sc_request_player_data_from_server"));
    public static final StreamCodec<FriendlyByteBuf, CSRequestPlayerDataFromServer> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSRequestPlayerDataFromServer());

    @Override
    public void handle(IPayloadContext context) {}

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public Packet reply(IPayloadContext context) {
        return new SCSendPlayerDataToClient(context.player());
    }
}
