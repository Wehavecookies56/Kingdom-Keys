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
	private int stopTicks, flatTicks, level;
	private float stopDmg;
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getStopDamage();
		this.flatTicks = capability.getFlatTicks();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
		this.level = capability.getLevel();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(this.stopTicks);
		buffer.writeFloat(this.stopDmg);
		buffer.writeInt(this.flatTicks);
		buffer.writeBoolean(this.castleOblivionMarker);
		buffer.writeInt(this.level);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		msg.stopTicks = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.flatTicks = buffer.readInt();
		msg.castleOblivionMarker = buffer.readBoolean();
		msg.level = buffer.readInt();
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
					cap.setCastleOblivionMarker(message.castleOblivionMarker);
					cap.setLevel(message.level);
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
