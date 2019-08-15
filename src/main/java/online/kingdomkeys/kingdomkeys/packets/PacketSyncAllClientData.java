package online.kingdomkeys.kingdomkeys.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

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

		ILevelCapabilities props = ModCapabilities.get(player);
		PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity) player);

		ctx.get().setPacketHandled(true);
	}

}
