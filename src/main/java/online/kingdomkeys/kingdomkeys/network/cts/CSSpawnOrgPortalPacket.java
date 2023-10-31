package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrgPortalPacket;

public class CSSpawnOrgPortalPacket {

	BlockPos pos;
	BlockPos destPos;
	ResourceKey<Level> dimension;

	public CSSpawnOrgPortalPacket() {
	}

	public CSSpawnOrgPortalPacket(BlockPos pos, BlockPos dest, ResourceKey<Level> dim) {
		this.pos = pos;
		this.destPos = dest;
		this.dimension = dim;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeBlockPos(destPos);
		buffer.writeResourceLocation(dimension.location());
	}

	public static CSSpawnOrgPortalPacket decode(FriendlyByteBuf buffer) {
		CSSpawnOrgPortalPacket msg = new CSSpawnOrgPortalPacket();
		msg.pos = buffer.readBlockPos();
		msg.destPos = buffer.readBlockPos();
		msg.dimension = ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation());
		return msg;
	}

	public static void handle(CSSpawnOrgPortalPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			player.level().playSound(null, message.pos, ModSounds.portal.get(), SoundSource.PLAYERS, 2F, 1F);
			player.level().playSound(null, message.destPos, ModSounds.portal.get(), SoundSource.PLAYERS, 2F, 1F);

			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.remMP(300);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
			OrgPortalEntity portal = new OrgPortalEntity(player.level(), player, message.pos, message.destPos, message.dimension, true);
			player.level().addFreshEntity(portal);

			OrgPortalEntity destPortal = new OrgPortalEntity(player.level(), player, message.destPos.above(), message.destPos, message.dimension, false);
			player.level().addFreshEntity(destPortal);
			
			PacketHandler.sendToAllPlayers(new SCSyncOrgPortalPacket(message.pos, message.destPos, message.dimension));

		});
		ctx.get().setPacketHandled(true);
	}

}
