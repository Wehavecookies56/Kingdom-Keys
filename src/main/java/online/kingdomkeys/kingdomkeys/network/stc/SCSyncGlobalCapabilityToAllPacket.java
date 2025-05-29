package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.GlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityToAllPacket {
	//Send packet to everyone to render gravity flat for example
	int id;
	private int level, stopModelTicks;
	private float stopDmg;
	private boolean castleOblivionMarker, isKO;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopDmg = capability.getStopDamage();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
		this.level = capability.getLevel();
		this.stopModelTicks = capability.getStopModelTicks();
		this.isKO = capability.isKO();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeFloat(this.stopDmg);
		buffer.writeBoolean(this.castleOblivionMarker);
		buffer.writeInt(this.level);
		buffer.writeInt(this.stopModelTicks);
		buffer.writeBoolean(this.isKO);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.castleOblivionMarker = buffer.readBoolean();
		msg.level = buffer.readInt();
		msg.stopModelTicks = buffer.readInt();
		msg.isKO = buffer.readBoolean();
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.id);

			IGlobalCapabilities cache = new GlobalCapabilities();
			cache.setStopDamage(message.stopDmg);
			cache.setCastleOblivionMarker(message.castleOblivionMarker);
			cache.setLevel(message.level);
			cache.setStopModelTicks(message.stopModelTicks);
			cache.setKO(message.isKO);

			if (message.id != Minecraft.getInstance().player.getId()) {
				ModCapabilities.mobDataClientCache.put(message.id, cache);
			}

			if (entity != null) {
				LazyOptional<IGlobalCapabilities> globalData = entity.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
				globalData.ifPresent(cap -> {
					cap.setStopDamage(message.stopDmg);
					cap.setCastleOblivionMarker(message.castleOblivionMarker);
					cap.setLevel(message.level);
					cap.setStopModelTicks(message.stopModelTicks);
					cap.setKO(message.isKO);
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
