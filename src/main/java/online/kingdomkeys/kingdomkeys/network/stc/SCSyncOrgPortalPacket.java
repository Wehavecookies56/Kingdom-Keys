package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

public class SCSyncOrgPortalPacket {

	BlockPos pos;
    BlockPos destPos;
    int dimension;

	public SCSyncOrgPortalPacket() {
	}

	public SCSyncOrgPortalPacket(BlockPos pos, BlockPos dest, int dim) {
		this.pos = pos;
        this.destPos = dest;
        this.dimension = dim;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
        buffer.writeBlockPos(destPos);
        buffer.writeInt(dimension);
	}

	public static SCSyncOrgPortalPacket decode(PacketBuffer buffer) {
		SCSyncOrgPortalPacket msg = new SCSyncOrgPortalPacket();
		msg.pos = buffer.readBlockPos();
        msg.destPos = buffer.readBlockPos();
        msg.dimension = buffer.readInt();
		return msg;
	}

	public static void handle(final SCSyncOrgPortalPacket msg, Supplier<NetworkEvent.Context> ctx) {
		
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();
			OrgPortalEntity portal;
	    	if(msg.pos != msg.destPos)
	    		portal = new OrgPortalEntity(player.world, player, msg.pos, msg.destPos, msg.dimension, true);
	    	else
	    		portal = new OrgPortalEntity(player.world, player, msg.pos, msg.destPos, msg.dimension, false);
	    	
	    	player.world.addEntity(portal);
	    });
		ctx.get().setPacketHandled(true);
	}

}
