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
import online.kingdomkeys.kingdomkeys.synthesis.melding.Melding;

import java.util.ArrayList;
import java.util.List;

public record SCSyncMeldingData(List<Melding> recipes) implements Packet {

	public static final Type<SCSyncMeldingData> TYPE = new Type<>(KingdomKeys.rl("sc_sync_melding_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncMeldingData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, Melding.STREAM_CODEC),
			SCSyncMeldingData::recipes,
			SCSyncMeldingData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncMeldingData(this);
		}
		KingdomKeys.LOGGER.info("Melding recipe data sync complete");
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
