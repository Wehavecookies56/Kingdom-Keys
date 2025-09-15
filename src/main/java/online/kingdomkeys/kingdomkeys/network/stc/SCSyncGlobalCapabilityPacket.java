package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityPacket {
	//Sync to client global capabilities
	private int level, stopModelTicks;
	private ArrayList<Float> stopDmg = new ArrayList<>();
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityPacket() {
	}

	public SCSyncGlobalCapabilityPacket(IGlobalCapabilities capability) {
		this.stopDmg = capability.getStopDamage();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
		this.level = capability.getLevel();
		this.stopModelTicks = capability.getStopModelTicks();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.stopDmg.size());
		for(Float unit : stopDmg) {
			buffer.writeFloat(unit);
		}
		buffer.writeBoolean(this.castleOblivionMarker);
		buffer.writeInt(this.level);
		buffer.writeInt(this.stopModelTicks);

	}

	public static SCSyncGlobalCapabilityPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityPacket msg = new SCSyncGlobalCapabilityPacket();
		int len = buffer.readInt();
		for(int i = 0; i < len; i++) {
			msg.stopDmg.add(buffer.readFloat());
		}
		msg.castleOblivionMarker = buffer.readBoolean();
		msg.level = buffer.readInt();
		msg.stopModelTicks = buffer.readInt();
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IGlobalCapabilities> globalData = Minecraft.getInstance().player.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
			globalData.ifPresent(cap -> {
				cap.setStopDamage(message.stopDmg);
				cap.setCastleOblivionMarker(message.castleOblivionMarker);
				cap.setLevel(message.level);
				cap.setStopModelTicks(message.stopModelTicks);
			});
		});
		ctx.get().setPacketHandled(true);
	}

}
