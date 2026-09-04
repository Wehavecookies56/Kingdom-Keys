package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenForetellerScreen(Union union, CompoundTag playerData) implements Packet {

	public static final Type<SCOpenForetellerScreen> TYPE = new Type<>(KingdomKeys.rl("sc_open_foreteller_screen"));

	public static final StreamCodec<FriendlyByteBuf, SCOpenForetellerScreen> STREAM_CODEC = StreamCodec.composite(
			Union.STREAM_CODEC, SCOpenForetellerScreen::union,
			ByteBufCodecs.COMPOUND_TAG, SCOpenForetellerScreen::playerData,
			SCOpenForetellerScreen::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.openForeteller(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
