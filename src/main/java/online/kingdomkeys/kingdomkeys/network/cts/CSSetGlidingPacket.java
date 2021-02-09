package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSSetGlidingPacket {

	private boolean gliding;

	public CSSetGlidingPacket() {
	}

	public CSSetGlidingPacket(boolean gliding) {
		this.gliding = gliding;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(this.gliding);
	}

	public static CSSetGlidingPacket decode(PacketBuffer buffer) {
		CSSetGlidingPacket msg = new CSSetGlidingPacket();
		msg.gliding = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSSetGlidingPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setIsGliding(message.gliding);
			PacketHandler.syncToAllAround(player, playerData);
		});
		ctx.get().setPacketHandled(true);
	}

}
