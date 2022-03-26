package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

public class SCSyncOrgPortalPacket {

	BlockPos pos;
    BlockPos destPos;
    ResourceKey<Level> dimension;

	public SCSyncOrgPortalPacket() {
	}

	public SCSyncOrgPortalPacket(BlockPos pos, BlockPos dest, ResourceKey<Level> dim) {
		this.pos = pos;
        this.destPos = dest;
        this.dimension = dim;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
        buffer.writeBlockPos(destPos);
        buffer.writeResourceLocation(dimension.location());
	}

	public static SCSyncOrgPortalPacket decode(FriendlyByteBuf buffer) {
		SCSyncOrgPortalPacket msg = new SCSyncOrgPortalPacket();
		msg.pos = buffer.readBlockPos();
        msg.destPos = buffer.readBlockPos();
        msg.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
		return msg;
	}

	public static void handle(final SCSyncOrgPortalPacket msg, Supplier<NetworkEvent.Context> ctx) {
		
		ctx.get().enqueueWork(() -> {
			Player player = KingdomKeys.proxy.getClientPlayer();
			OrgPortalEntity portal;
	    	if(msg.pos != msg.destPos)
	    		portal = new OrgPortalEntity(player.level, player, msg.pos, msg.destPos, msg.dimension, true);
	    	else
	    		portal = new OrgPortalEntity(player.level, player, msg.pos, msg.destPos, msg.dimension, false);
	    	
	    	player.level.addFreshEntity(portal);
	    });
		ctx.get().setPacketHandled(true);
	}

}
