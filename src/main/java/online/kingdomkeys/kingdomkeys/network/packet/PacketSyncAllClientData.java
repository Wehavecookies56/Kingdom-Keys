package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class PacketSyncAllClientData {

	public PacketSyncAllClientData() {
	}

	public void encode(PacketBuffer buffer) {

	}

	public static PacketSyncAllClientData decode(PacketBuffer buffer) {
		PacketSyncAllClientData msg = new PacketSyncAllClientData();

		return msg;
	}

	public static void handle(final PacketSyncAllClientData message, Supplier<NetworkEvent.Context> ctx) {
		PlayerEntity player = ctx.get().getSender();

		IPlayerCapabilities props = ModCapabilities.get(player);
		PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) player);

		ctx.get().setPacketHandled(true);
	}

}
