package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class PacketSetAerialDodgeTicks {

	private int ticks;
	private boolean hasJumped;

	public PacketSetAerialDodgeTicks() {
	}

	public PacketSetAerialDodgeTicks(boolean hasJumped, int ticks) {
		this.hasJumped = hasJumped;
		this.ticks = ticks;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(hasJumped);
		buffer.writeInt(this.ticks);
	}

	public static PacketSetAerialDodgeTicks decode(PacketBuffer buffer) {
		PacketSetAerialDodgeTicks msg = new PacketSetAerialDodgeTicks();
		msg.hasJumped = buffer.readBoolean();
		msg.ticks = buffer.readInt();
		return msg;
	}

	public static void handle(PacketSetAerialDodgeTicks message, final Supplier<NetworkEvent.Context> ctx) {
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
