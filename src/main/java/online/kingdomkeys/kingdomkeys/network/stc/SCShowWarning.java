package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCShowWarning(Component body) implements Packet {
	public static final Type<SCShowWarning> TYPE = new Type<>(KingdomKeys.rl("sc_show_warning"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SCShowWarning> STREAM_CODEC = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC, SCShowWarning::body,
			SCShowWarning::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.showWarning(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
