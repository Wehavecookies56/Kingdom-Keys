package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSSyncCapabilityToAllFromClientPacket {
	//Packet to sync to all from client
	public CSSyncCapabilityToAllFromClientPacket() {
	}

	public void encode(PacketBuffer buffer) {
		
	}

	public static CSSyncCapabilityToAllFromClientPacket decode(PacketBuffer buffer) {
		CSSyncCapabilityToAllFromClientPacket msg = new CSSyncCapabilityToAllFromClientPacket();
		
		return msg;
	}

	public static void handle(final CSSyncCapabilityToAllFromClientPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			PacketHandler.syncToAllAround(player, ModCapabilities.get(player));
		});
		ctx.get().setPacketHandled(true);
	}

}
