package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class CSAntiPointsPacket {

	int points;

	public CSAntiPointsPacket() {
	}

	public CSAntiPointsPacket(int points) {
		this.points = points;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.points);
	}

	public static CSAntiPointsPacket decode(PacketBuffer buffer) {
		CSAntiPointsPacket msg = new CSAntiPointsPacket();
		msg.points = buffer.readInt();
		return msg;
	}

	public static void handle(CSAntiPointsPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setAntiPoints(playerData.getAntiPoints() + message.points);
		});
		ctx.get().setPacketHandled(true);
	}

}
