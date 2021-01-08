package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

public class SCSyncOrgPortalPacket {

	BlockPos pos;
    BlockPos destPos;
    RegistryKey<World> dimension;

	public SCSyncOrgPortalPacket() {
	}

	public SCSyncOrgPortalPacket(BlockPos pos, BlockPos dest, RegistryKey<World> dim) {
		this.pos = pos;
        this.destPos = dest;
        this.dimension = dim;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
        buffer.writeBlockPos(destPos);
        buffer.writeResourceLocation(dimension.getLocation());
	}

	public static SCSyncOrgPortalPacket decode(PacketBuffer buffer) {
		SCSyncOrgPortalPacket msg = new SCSyncOrgPortalPacket();
		msg.pos = buffer.readBlockPos();
        msg.destPos = buffer.readBlockPos();
        msg.dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation());
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
