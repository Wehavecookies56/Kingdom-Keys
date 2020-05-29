package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.worldgen.TeleporterOrgPortal;

public class PacketOrgPortalTP {

	int dim;
    double x,y,z;

	public PacketOrgPortalTP() {
	}

	public PacketOrgPortalTP(int dimension, double x, double y, double z) {
    	this.dim = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(dim);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
	}

	public static PacketOrgPortalTP decode(PacketBuffer buffer) {
		PacketOrgPortalTP msg = new PacketOrgPortalTP();
		msg.dim = buffer.readInt();
		msg.x = buffer.readDouble();
		msg.y = buffer.readDouble();
		msg.z = buffer.readDouble();
		return msg;
	}

	public static void handle(PacketOrgPortalTP message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
	    	BlockPos pos = new BlockPos(message.x,message.y,message.z);
	        
	          	new TeleporterOrgPortal((ServerWorld) player.world).teleport(player, pos, message.dim);;	    
	     //   player.setPosition(message.x,message.y,message.z);
	    	//new TeleporterOrgPortal((ServerWorld) player.world).teleport(player, pos, message.dim);
		});
		ctx.get().setPacketHandled(true);
	}

}
