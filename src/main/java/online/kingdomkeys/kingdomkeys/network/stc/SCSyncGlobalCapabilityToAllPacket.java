package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityToAllPacket {
	//Send packet to everyone to render gravity flat for example
	int id;
	private int stopTicks, flatTicks, level, aeroTicks, aeroLevel, stopModelTicks;
	private float stopDmg;
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getStopDamage();
		this.flatTicks = capability.getFlatTicks();
		this.aeroTicks = capability.getAeroTicks();
		this.aeroLevel = capability.getAeroLevel();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
		this.level = capability.getLevel();
		this.stopModelTicks = capability.getStopModelTicks();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(this.stopTicks);
		buffer.writeFloat(this.stopDmg);
		buffer.writeInt(this.flatTicks);
		buffer.writeInt(this.aeroTicks);
		buffer.writeInt(this.aeroLevel);
		buffer.writeBoolean(this.castleOblivionMarker);
		buffer.writeInt(this.level);
		buffer.writeInt(this.stopModelTicks);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		msg.stopTicks = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.flatTicks = buffer.readInt();
		msg.aeroTicks = buffer.readInt();
		msg.aeroLevel = buffer.readInt();
		msg.castleOblivionMarker = buffer.readBoolean();
		msg.level = buffer.readInt();
		msg.stopModelTicks = buffer.readInt();
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.id);
			
			if (entity != null) {
				LazyOptional<IGlobalCapabilities> globalData = entity.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
				globalData.ifPresent(cap -> {
					cap.setStoppedTicks(message.stopTicks);
					cap.setStopDamage(message.stopDmg);
					cap.setFlatTicks(message.flatTicks);
					cap.setAeroTicks(message.aeroTicks, message.aeroLevel);
					cap.setCastleOblivionMarker(message.castleOblivionMarker);
					cap.setLevel(message.level);
					cap.setStopModelTicks(message.stopModelTicks);
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}

}