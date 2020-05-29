package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class PacketSyncCapabilityToAllFromClient {
	//Packet to sync to all from client
	public PacketSyncCapabilityToAllFromClient() {
	}

	public void encode(PacketBuffer buffer) {
		
	}

	public static PacketSyncCapabilityToAllFromClient decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAllFromClient msg = new PacketSyncCapabilityToAllFromClient();
		
		return msg;
	}

	public static void handle(final PacketSyncCapabilityToAllFromClient message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			PacketHandler.syncToAllAround(player, ModCapabilities.get(player));
		});
		ctx.get().setPacketHandled(true);
	}

}
