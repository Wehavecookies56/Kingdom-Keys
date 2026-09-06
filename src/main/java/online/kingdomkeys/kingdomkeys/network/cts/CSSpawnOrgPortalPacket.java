package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.OrgPortalEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSSpawnOrgPortalPacket(BlockPos pos, BlockPos destPos, ResourceKey<Level> dimension) implements Packet {

	public static final Type<CSSpawnOrgPortalPacket> TYPE = new Type<>(KingdomKeys.rl("cs_spawn_org_portal"));

	public static final StreamCodec<FriendlyByteBuf, CSSpawnOrgPortalPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, CSSpawnOrgPortalPacket::pos,
			BlockPos.STREAM_CODEC, CSSpawnOrgPortalPacket::destPos,
			ResourceKey.streamCodec(Registries.DIMENSION), CSSpawnOrgPortalPacket::dimension,
			CSSpawnOrgPortalPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (!(context.player() instanceof ServerPlayer player)) {
			return;
		}

		ServerLevel origin = player.serverLevel();
		ServerLevel destination = player.getServer().getLevel(dimension);

		if (destination == null) {
			return;
		}

		PlayerData playerData = PlayerData.get(player);
		if (playerData != null) {
			playerData.remMP(300);
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		}

		openPortal(origin, pos, pos, true);
		openPortal(destination, destPos, destPos.above(), false);
	}

	private void openPortal(ServerLevel level, BlockPos pos, BlockPos spawnAt, boolean teleports) {
		level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(pos), 3, pos);
		level.playSound(null, pos, ModSounds.portal.get(), SoundSource.PLAYERS, 2F, 1F);
		level.addFreshEntity(new OrgPortalEntity(level, spawnAt, destPos, dimension, teleports));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
