package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrgPortalPacket;

public record CSSpawnOrgPortalPacket(BlockPos pos, BlockPos destPos, ResourceKey<Level> dimension) implements Packet {

	public static final Type<CSSpawnOrgPortalPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_spawn_org_portal"));

	public static final StreamCodec<FriendlyByteBuf, CSSpawnOrgPortalPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			CSSpawnOrgPortalPacket::pos,
			BlockPos.STREAM_CODEC,
			CSSpawnOrgPortalPacket::destPos,
			ResourceKey.streamCodec(Registries.DIMENSION),
			CSSpawnOrgPortalPacket::dimension,
			CSSpawnOrgPortalPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		player.level().playSound(null, pos, ModSounds.portal.get(), SoundSource.PLAYERS, 2F, 1F);
		player.level().playSound(null, destPos, ModSounds.portal.get(), SoundSource.PLAYERS, 2F, 1F);

		PlayerData playerData = PlayerData.get(player);
		playerData.remMP(300);
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
		OrgPortalEntity portal = new OrgPortalEntity(player.level(), pos, destPos, dimension, true);
		player.level().addFreshEntity(portal);

		OrgPortalEntity destPortal = new OrgPortalEntity(player.level(), destPos.above(), destPos, dimension, false);
		player.level().addFreshEntity(destPortal);

		PacketHandler.sendToAll(new SCSyncOrgPortalPacket(pos, destPos, dimension));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
