package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSSpawnOrgPortalPacket {

	BlockPos pos;
	BlockPos destPos;
	int dimension;

	public CSSpawnOrgPortalPacket() {
	}

	public CSSpawnOrgPortalPacket(BlockPos pos, BlockPos dest, int dim) {
		this.pos = pos;
		this.destPos = dest;
		this.dimension = dim;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeBlockPos(destPos);
		buffer.writeInt(dimension);
	}

	public static CSSpawnOrgPortalPacket decode(PacketBuffer buffer) {
		CSSpawnOrgPortalPacket msg = new CSSpawnOrgPortalPacket();
		msg.pos = buffer.readBlockPos();
		msg.destPos = buffer.readBlockPos();
		msg.dimension = buffer.readInt();
		return msg;
	}

	public static void handle(CSSpawnOrgPortalPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			// PacketDispatcher.sendToAllAround(new SpawnPortalParticles(pos), player,
			// 64.0D);
			OrgPortalEntity portal = new OrgPortalEntity(player.world, player, message.pos, message.destPos, message.dimension, true);
			player.world.addEntity(portal);

			OrgPortalEntity destPortal = new OrgPortalEntity(player.world, player, message.destPos.up(), message.destPos, message.dimension, false);
			player.world.addEntity(destPortal);
			
			PacketHandler.sendToAll(new SCSyncOrgPortalPacket(message.pos, message.destPos, message.dimension), player.world);

		});
		ctx.get().setPacketHandled(true);
	}

}
