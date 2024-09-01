package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSOrgPortalTPPacket(ResourceKey<Level> dim, BlockPos pos) implements Packet {

	public static final Type<CSOrgPortalTPPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_org_portal_tp"));

	public static final StreamCodec<FriendlyByteBuf, CSOrgPortalTPPacket> STREAM_CODEC = StreamCodec.composite(
			ResourceKey.streamCodec(Registries.DIMENSION),
			CSOrgPortalTPPacket::dim,
			BlockPos.STREAM_CODEC,
			CSOrgPortalTPPacket::pos,
			CSOrgPortalTPPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		ServerLevel serverWorld = player.level().getServer().getLevel(dim);
		PlayerData playerData = PlayerData.get(player);
		//If destination is the ROD lock the player there, otherwise unlock
		playerData.setRespawnROD(dim.location().getPath().equals("realm_of_darkness"));

		if(player.level().dimension().equals(dim)) { //Seemless tp
			ServerPlayer playerMP = (ServerPlayer) player;
			playerMP.teleportTo(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
			playerMP.setDeltaMovement(0, 0, 0);
		} else {
			player.changeDimension(new DimensionTransition(serverWorld, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), pEntity -> {}));
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
