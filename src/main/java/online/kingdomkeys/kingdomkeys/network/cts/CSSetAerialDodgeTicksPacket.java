package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
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

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(hasJumped);
		buffer.writeInt(this.ticks);
	}

	public static CSSetAerialDodgeTicksPacket decode(PacketBuffer buffer) {
		CSSetAerialDodgeTicksPacket msg = new CSSetAerialDodgeTicksPacket();
		msg.hasJumped = buffer.readBoolean();
		msg.ticks = buffer.readInt();
		return msg;
	}

	public static void handle(CSSetAerialDodgeTicksPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setHasJumpedAerialDodge(message.hasJumped);
			props.setAerialDodgeTicks(message.ticks);
			//System.out.println(message.gliding);
			PacketHandler.syncToAllAround(player, props);
		});
		ctx.get().setPacketHandled(true);
	}

}
