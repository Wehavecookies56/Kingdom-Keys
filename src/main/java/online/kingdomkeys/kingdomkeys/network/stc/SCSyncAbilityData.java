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
import online.kingdomkeys.kingdomkeys.ability.AbilityData;
import online.kingdomkeys.kingdomkeys.ability.AbilityDataDeserializer;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

import java.util.ArrayList;
import java.util.List;

public record SCSyncAbilityData(List<String> names, List<String> data) implements Packet {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(AbilityData.class, new AbilityDataDeserializer()).setPrettyPrinting().create();

	public static final Type<SCSyncAbilityData> TYPE = new Type<>(KingdomKeys.rl("sc_sync_ability_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncAbilityData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), SCSyncAbilityData::names,
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), SCSyncAbilityData::data,
			SCSyncAbilityData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncAbilityData(this);
		}
		KingdomKeys.LOGGER.info("Ability data sync complete");
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
