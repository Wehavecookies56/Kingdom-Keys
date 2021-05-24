package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class CSOrgPortalTPPacket {

	RegistryKey<World> dim;
    double x,y,z;

	public CSOrgPortalTPPacket() {
	}

	public CSOrgPortalTPPacket(RegistryKey<World> dimension, double x, double y, double z) {
    	this.dim = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeResourceLocation(dim.getLocation());
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
	}

	public static CSOrgPortalTPPacket decode(PacketBuffer buffer) {
		CSOrgPortalTPPacket msg = new CSOrgPortalTPPacket();
		//No idea if this how you should do this
		msg.dim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, buffer.readResourceLocation());
		msg.x = buffer.readDouble();
		msg.y = buffer.readDouble();
		msg.z = buffer.readDouble();
		return msg;
	}

	public static void handle(CSOrgPortalTPPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ServerWorld serverWorld = player.world.getServer().getWorld(message.dim);
			
            if(player.world.getDimensionKey().equals(message.dim)) { //Seemless tp
				ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
				playerMP.setPositionAndUpdate(message.x+0.5, message.y, message.z+0.5);
				playerMP.setMotion(0, 0, 0);
			} else {
	            player.changeDimension(serverWorld, new BaseTeleporter(message.x,message.y,message.z));
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
