package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
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
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationData;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataDeserializer;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCSyncOrganizationData(List<String> names, List<String> data) implements Packet {
	
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(OrganizationData.class, new OrganizationDataDeserializer()).setPrettyPrinting().create();

	public static final CustomPacketPayload.Type<SCSyncOrganizationData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_organization_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncOrganizationData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
			SCSyncOrganizationData::names,
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
			SCSyncOrganizationData::data,
			SCSyncOrganizationData::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncOrgData(this);
		}
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

}
