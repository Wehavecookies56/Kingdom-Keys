package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncGlobalCapabilityPacket {
	//Sync to client global capabilities
	private int stoppedTicks, flatTicks, level;
	private float stopDmg;
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityPacket() {
	}

	public SCSyncGlobalCapabilityPacket(IGlobalCapabilities capability) {
		this.stoppedTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getStopDamage();
		this.flatTicks = capability.getFlatTicks();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
		this.level = capability.getLevel();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.stoppedTicks);
		buffer.writeFloat(this.stopDmg);
		buffer.writeInt(this.flatTicks);
		buffer.writeBoolean(this.castleOblivionMarker);
		buffer.writeInt(this.level);
	}

	public static SCSyncGlobalCapabilityPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityPacket msg = new SCSyncGlobalCapabilityPacket();
		msg.stoppedTicks = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.flatTicks = buffer.readInt();
		msg.castleOblivionMarker = buffer.readBoolean();
		msg.level = buffer.readInt();
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IGlobalCapabilities> globalData = Minecraft.getInstance().player.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
			globalData.ifPresent(cap -> {
				cap.setStoppedTicks(message.stoppedTicks);
				cap.setStopDamage(message.stopDmg);
				cap.setFlatTicks(message.flatTicks);
				cap.setCastleOblivionMarker(message.castleOblivionMarker);
				cap.setLevel(message.level);
			});
		});
		ctx.get().setPacketHandled(true);
	}

}
