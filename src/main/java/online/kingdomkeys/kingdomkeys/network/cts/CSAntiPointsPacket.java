package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.ModData;

public class CSAntiPointsPacket {

	int points;

	public CSAntiPointsPacket() {
	}

	public CSAntiPointsPacket(int points) {
		this.points = points;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.points);
	}

	public static CSAntiPointsPacket decode(FriendlyByteBuf buffer) {
		CSAntiPointsPacket msg = new CSAntiPointsPacket();
		msg.points = buffer.readInt();
		return msg;
	}

	public static void handle(CSAntiPointsPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerData playerData = ModData.getPlayer(player);
			playerData.setAntiPoints(playerData.getAntiPoints() + message.points);
		});
		ctx.get().setPacketHandled(true);
	}

}
