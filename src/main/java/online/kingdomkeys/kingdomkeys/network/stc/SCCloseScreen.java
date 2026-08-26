package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

/** Tells the client to close whatever screen it currently has open (if any). */
public record SCCloseScreen() implements Packet {

	public static final Type<SCCloseScreen> TYPE = new Type<>(KingdomKeys.rl("sc_close_screen"));

	public static final StreamCodec<FriendlyByteBuf, SCCloseScreen> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new SCCloseScreen());

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.closeScreen();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
