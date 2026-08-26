package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCSyncWorldData(CompoundTag data) implements Packet {

	public static final Type<SCSyncWorldData> TYPE = new Type<>(KingdomKeys.rl("sc_sync_world_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncWorldData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			SCSyncWorldData::data,
			SCSyncWorldData::new
	);

	public SCSyncWorldData(MinecraftServer server) {
		this(WorldData.get(server).save(new CompoundTag(), server.registryAccess()));
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncWorldData(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
