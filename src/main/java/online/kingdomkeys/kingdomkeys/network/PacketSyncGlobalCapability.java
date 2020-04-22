package online.kingdomkeys.kingdomkeys.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSyncGlobalCapability {

	private int stoppedTicks;

	public PacketSyncGlobalCapability() {
	}

	public PacketSyncGlobalCapability(IGlobalCapabilities capability) {
		this.stoppedTicks = capability.getStoppedTicks();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.stoppedTicks);
	}

	public static PacketSyncGlobalCapability decode(PacketBuffer buffer) {
		PacketSyncGlobalCapability msg = new PacketSyncGlobalCapability();
		msg.stoppedTicks = buffer.readInt();
		return msg;
	}

	public static void handle(final PacketSyncGlobalCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IGlobalCapabilities> props = Minecraft.getInstance().player.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
			props.ifPresent(cap -> cap.setStoppedTicks(message.stoppedTicks));
		});
		ctx.get().setPacketHandled(true);
	}

}
