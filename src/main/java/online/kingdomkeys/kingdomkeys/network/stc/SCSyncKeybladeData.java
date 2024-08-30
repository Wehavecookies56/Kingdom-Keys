package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataDeserializer;

public record SCSyncKeybladeData(List<String> names, List<String> data) implements Packet {
	
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(KeybladeData.class, new KeybladeDataDeserializer()).setPrettyPrinting().create();

	public static final Type<SCSyncKeybladeData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_keyblade_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncKeybladeData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
			SCSyncKeybladeData::names,
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
			SCSyncKeybladeData::data,
			SCSyncKeybladeData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncKeybladeData(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
