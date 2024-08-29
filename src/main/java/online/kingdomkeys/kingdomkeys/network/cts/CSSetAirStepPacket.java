package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.function.Supplier;

public class CSSetAirStepPacket {

	private BlockPos pos;

	public CSSetAirStepPacket() {
	}

	public CSSetAirStepPacket(BlockPos pos) {
		this.pos = pos;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(this.pos);
	}

	public static CSSetAirStepPacket decode(FriendlyByteBuf buffer) {
		CSSetAirStepPacket msg = new CSSetAirStepPacket();
		msg.pos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(CSSetAirStepPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerData playerData = ModData.getPlayer(player);
			playerData.setAirStep(message.pos);
			PacketHandler.syncToAllAround(player, playerData);
		});
		ctx.get().setPacketHandled(true);
	}

}
