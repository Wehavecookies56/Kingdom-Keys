package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenUnionScreen(Union union) implements Packet {

	public static final Type<SCOpenUnionScreen> TYPE = new Type<>(KingdomKeys.rl("sc_open_union_screen"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenUnionScreen> STREAM_CODEC = StreamCodec.composite(
		Union.STREAM_CODEC, SCOpenUnionScreen::union,
		SCOpenUnionScreen::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.openUnion(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
