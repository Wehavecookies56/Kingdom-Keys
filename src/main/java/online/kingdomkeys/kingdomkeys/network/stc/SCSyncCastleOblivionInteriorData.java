package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record SCSyncCastleOblivionInteriorData(CompoundTag data) implements Packet {

	public static final Type<SCSyncCastleOblivionInteriorData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_castle_oblivion_interior_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncCastleOblivionInteriorData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			SCSyncCastleOblivionInteriorData::data,
			SCSyncCastleOblivionInteriorData::new
	);

	public SCSyncCastleOblivionInteriorData(CastleOblivionData.InteriorData data, Level level) {
		this(data.save(new CompoundTag(), level.getServer().registryAccess()));
	}

	public static void syncClients(ServerLevel level) {
		PacketHandler.sendToAll(new SCSyncCastleOblivionInteriorData(CastleOblivionData.InteriorData.get(level), level));
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncCastleOblivionInterior(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
