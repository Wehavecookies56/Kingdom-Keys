package online.kingdomkeys.kingdomkeys.network.stc;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCHeartlessTransition() implements Packet {
	public static final Type<SCHeartlessTransition> TYPE = new Type<>(KingdomKeys.rl("sc_heartless_transition"));
	public static final StreamCodec<ByteBuf, SCHeartlessTransition> STREAM_CODEC = StreamCodec.unit(new SCHeartlessTransition());

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.heartlessTransition();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
