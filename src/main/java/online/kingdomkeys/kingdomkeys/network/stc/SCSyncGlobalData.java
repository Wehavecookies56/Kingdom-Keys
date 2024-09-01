package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCSyncGlobalData(int entity, CompoundTag data) implements Packet {

	public static final Type<SCSyncGlobalData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_global_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncGlobalData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			SCSyncGlobalData::entity,
			ByteBufCodecs.COMPOUND_TAG,
			SCSyncGlobalData::data,
			SCSyncGlobalData::new
	);

	public SCSyncGlobalData(LivingEntity entity) {
		this(entity.getId(), GlobalData.get(entity).serializeNBT(entity.level().registryAccess()));
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			GlobalData globalData = GlobalData.get((LivingEntity) Minecraft.getInstance().level.getEntity(entity));
			globalData.deserializeNBT(Minecraft.getInstance().level.registryAccess(), data);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
