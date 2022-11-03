package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.function.Supplier;

public class CSSetGlidingPacket {

	private boolean gliding;

	public CSSetGlidingPacket() {
	}

	public CSSetGlidingPacket(boolean gliding) {
		this.gliding = gliding;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.gliding);
	}

	public static CSSetGlidingPacket decode(FriendlyByteBuf buffer) {
		CSSetGlidingPacket msg = new CSSetGlidingPacket();
		msg.gliding = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSSetGlidingPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setIsGliding(message.gliding);
			PacketHandler.syncToAllAround(player, playerData);
		});
		ctx.get().setPacketHandled(true);
	}

}
