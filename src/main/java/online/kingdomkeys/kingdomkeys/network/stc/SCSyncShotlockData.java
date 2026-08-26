package online.kingdomkeys.kingdomkeys.network.stc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.shotlock.ShotlockData;
import online.kingdomkeys.kingdomkeys.shotlock.ShotlockDataDeserializer;

import java.util.ArrayList;
import java.util.List;

public record SCSyncShotlockData(List<String> names, List<String> data) implements Packet {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(ShotlockData.class, new ShotlockDataDeserializer()).setPrettyPrinting().create();

	public static final Type<SCSyncShotlockData> TYPE = new Type<>(KingdomKeys.rl("sc_sync_shotlock_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncShotlockData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), SCSyncShotlockData::names,
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), SCSyncShotlockData::data,
			SCSyncShotlockData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncShotlockData(this);
		}
		KingdomKeys.LOGGER.info("Shotlock data sync complete");
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
