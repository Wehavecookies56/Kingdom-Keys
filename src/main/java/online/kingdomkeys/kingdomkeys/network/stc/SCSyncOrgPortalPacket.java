package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCSyncOrgPortalPacket(BlockPos pos, BlockPos destPos, ResourceKey<Level> dimension) implements Packet {

	public static final Type<SCSyncOrgPortalPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_org_portal"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncOrgPortalPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			SCSyncOrgPortalPacket::pos,
			BlockPos.STREAM_CODEC,
			SCSyncOrgPortalPacket::destPos,
			ResourceKey.streamCodec(Registries.DIMENSION),
			SCSyncOrgPortalPacket::dimension,
			SCSyncOrgPortalPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncOrgPortal(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
