package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSSetAerialDodgeTicksPacket {

	private int ticks;
	private boolean hasJumped;

	public CSSetAerialDodgeTicksPacket() {
	}

	public CSSetAerialDodgeTicksPacket(boolean hasJumped, int ticks) {
		this.hasJumped = hasJumped;
		this.ticks = ticks;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(hasJumped);
		buffer.writeInt(this.ticks);
	}

	public static CSSetAerialDodgeTicksPacket decode(FriendlyByteBuf buffer) {
		CSSetAerialDodgeTicksPacket msg = new CSSetAerialDodgeTicksPacket();
		msg.hasJumped = buffer.readBoolean();
		msg.ticks = buffer.readInt();
		return msg;
	}

	public static void handle(CSSetAerialDodgeTicksPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setHasJumpedAerialDodge(message.hasJumped);
			playerData.setAerialDodgeTicks(message.ticks);
			
			PacketHandler.syncToAllAround(player, playerData);
		});
		ctx.get().setPacketHandled(true);
	}

}
