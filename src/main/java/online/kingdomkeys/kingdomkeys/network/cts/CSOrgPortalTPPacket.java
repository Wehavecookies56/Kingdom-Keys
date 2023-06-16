package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class CSOrgPortalTPPacket {

	ResourceKey<Level> dim;
    double x,y,z;

	public CSOrgPortalTPPacket() {
	}

	public CSOrgPortalTPPacket(ResourceKey<Level> dimension, double x, double y, double z) {
    	this.dim = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(dim.location());
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
	}

	public static CSOrgPortalTPPacket decode(FriendlyByteBuf buffer) {
		CSOrgPortalTPPacket msg = new CSOrgPortalTPPacket();
		//No idea if this how you should do this
		msg.dim = ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
		msg.x = buffer.readDouble();
		msg.y = buffer.readDouble();
		msg.z = buffer.readDouble();
		return msg;
	}

	public static void handle(CSOrgPortalTPPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			ServerLevel serverWorld = player.level.getServer().getLevel(message.dim);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
    		//If destination is the ROD lock the player there, otherwise unlock
			playerData.setRespawnROD(message.dim.location().getPath().equals("realm_of_darkness"));
			
            if(player.level.dimension().equals(message.dim)) { //Seemless tp
				ServerPlayer playerMP = (ServerPlayer) player;
				playerMP.teleportTo(message.x+0.5, message.y, message.z+0.5);
				playerMP.setDeltaMovement(0, 0, 0);
			} else {
	            player.changeDimension(serverWorld, new BaseTeleporter(message.x,message.y,message.z));
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
