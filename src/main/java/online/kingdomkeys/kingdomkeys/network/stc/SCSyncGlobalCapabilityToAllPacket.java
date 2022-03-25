package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityToAllPacket {
	//Send packet to everyone to render gravity flat for example
	int id;
	private int stopTicks, flatTicks;
	private float stopDmg;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getDamage();
		this.flatTicks = capability.getFlatTicks();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(this.stopTicks);
		buffer.writeFloat(this.stopDmg);
		buffer.writeInt(this.flatTicks);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		msg.stopTicks = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.flatTicks = buffer.readInt();
		
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.id);
			
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
