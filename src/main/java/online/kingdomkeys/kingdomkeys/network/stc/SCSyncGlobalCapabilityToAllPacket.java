package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
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
	private ArrayList<Float> stopDmg = new ArrayList<>();
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityToAllPacket() {
	}

	public SCSyncGlobalCapabilityToAllPacket(int id, IGlobalCapabilities capability) {
		this.id = id;
		this.stopDmg = capability.getStopDamage();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
		this.level = capability.getLevel();
		this.stopModelTicks = capability.getStopModelTicks();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeInt(this.stopDmg.size());
		for(Float unit : stopDmg) {
			buffer.writeFloat(unit);
		}		buffer.writeBoolean(this.castleOblivionMarker);
		buffer.writeInt(this.level);
		buffer.writeInt(this.stopModelTicks);
	}

	public static SCSyncGlobalCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityToAllPacket msg = new SCSyncGlobalCapabilityToAllPacket();
		msg.id = buffer.readInt();
		int len = buffer.readInt();
		for(int i = 0; i < len; i++) {
			msg.stopDmg.add(buffer.readFloat());
		}		msg.castleOblivionMarker = buffer.readBoolean();
		msg.level = buffer.readInt();
		msg.stopModelTicks = buffer.readInt();
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
				});
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
