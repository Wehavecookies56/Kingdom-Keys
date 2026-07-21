package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenStruggleMenu(BlockPos pos) implements Packet {

	public static final Type<SCOpenStruggleMenu> TYPE = new Type<>(KingdomKeys.rl("sc_open_struggle_menu"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenStruggleMenu> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			SCOpenStruggleMenu::pos,
			SCOpenStruggleMenu::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.openStruggleMenu(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
