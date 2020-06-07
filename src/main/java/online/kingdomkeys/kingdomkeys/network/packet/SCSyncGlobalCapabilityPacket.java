package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityPacket {
	//Sync to client global capabilities
	private int stoppedTicks, stopDmg, flatTicks;

	public SCSyncGlobalCapabilityPacket() {
	}

	public SCSyncGlobalCapabilityPacket(IGlobalCapabilities capability) {
		this.stoppedTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getDamage();
		this.flatTicks = capability.getFlatTicks();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.stoppedTicks);
		buffer.writeInt(this.stopDmg);
		buffer.writeInt(this.flatTicks);
	}

	public static SCSyncGlobalCapabilityPacket decode(PacketBuffer buffer) {
		SCSyncGlobalCapabilityPacket msg = new SCSyncGlobalCapabilityPacket();
		msg.stoppedTicks = buffer.readInt();
		msg.stopDmg = buffer.readInt();
		msg.flatTicks = buffer.readInt();
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IGlobalCapabilities> props = Minecraft.getInstance().player.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
			props.ifPresent(cap -> cap.setStoppedTicks(message.stoppedTicks));
			props.ifPresent(cap -> cap.setDamage(message.stopDmg));
			props.ifPresent(cap -> cap.setFlatTicks(message.flatTicks));
		});
		ctx.get().setPacketHandled(true);
	}

}
