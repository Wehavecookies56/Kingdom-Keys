package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityToAllPacket {
	//Send packet to everyone to render gravity flat for example
	int id;
	private int stopTicks, stopDmg, flatTicks;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getDamage();
		this.flatTicks = capability.getFlatTicks();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(id);
		buffer.writeInt(this.stopTicks);
		buffer.writeInt(this.stopDmg);
		buffer.writeInt(this.flatTicks);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(PacketBuffer buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		msg.stopTicks = buffer.readInt();
		msg.stopDmg = buffer.readInt();
		msg.flatTicks = buffer.readInt();
		
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) Minecraft.getInstance().world.getEntityByID(message.id);
			
			if (entity != null) {
				LazyOptional<IGlobalCapabilities> globalData = entity.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
				globalData.ifPresent(cap -> cap.setStoppedTicks(message.stopTicks));
				globalData.ifPresent(cap -> cap.setDamage(message.stopDmg));
				globalData.ifPresent(cap -> cap.setFlatTicks(message.flatTicks));
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
