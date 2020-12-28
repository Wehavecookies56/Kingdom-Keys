package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;
import online.kingdomkeys.kingdomkeys.world.utils.TeleporterOrgPortal;

public class CSOrgPortalTPPacket {

	int dim;
    double x,y,z;

	public CSOrgPortalTPPacket() {
	}

	public CSOrgPortalTPPacket(int dimension, double x, double y, double z) {
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

	public static CSOrgPortalTPPacket decode(PacketBuffer buffer) {
		CSOrgPortalTPPacket msg = new CSOrgPortalTPPacket();
		msg.dim = buffer.readInt();
		msg.x = buffer.readDouble();
		msg.y = buffer.readDouble();
		msg.z = buffer.readDouble();
		return msg;
	}

	public static void handle(CSOrgPortalTPPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
	    	//BlockPos pos = new BlockPos(message.x,message.y,message.z);
            player.changeDimension(DimensionType.getById(message.dim), new BaseTeleporter(message.x,message.y,message.z));

	       // new TeleporterOrgPortal((ServerWorld) player.world).teleport(player, pos, message.dim);
	     //   player.setPosition(message.x,message.y,message.z);
	    	//new TeleporterOrgPortal((ServerWorld) player.world).teleport(player, pos, message.dim);
		});
		ctx.get().setPacketHandled(true);
	}

}
