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
	private int stopTicks, flatTicks;
	private float stopDmg;
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getDamage();
		this.flatTicks = capability.getFlatTicks();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(this.stopTicks);
		buffer.writeFloat(this.stopDmg);
		buffer.writeInt(this.flatTicks);
		buffer.writeBoolean(this.castleOblivionMarker);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		msg.stopTicks = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.flatTicks = buffer.readInt();
		msg.castleOblivionMarker = buffer.readBoolean();

		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.id);
			
			if (entity != null) {
				LazyOptional<IGlobalCapabilities> globalData = entity.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
				globalData.ifPresent(cap -> {
					cap.setStoppedTicks(message.stopTicks);
					cap.setDamage(message.stopDmg);
					cap.setFlatTicks(message.flatTicks);
					cap.setCastleOblivionMarker(message.castleOblivionMarker);
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
