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
	private int stoppedTicks, flatTicks;
	private float stopDmg;
	private boolean castleOblivionMarker;

	public SCSyncGlobalCapabilityPacket() {
	}

	public SCSyncGlobalCapabilityPacket(IGlobalCapabilities capability) {
		this.stoppedTicks = capability.getStoppedTicks();
		this.stopDmg = capability.getDamage();
		this.flatTicks = capability.getFlatTicks();
		this.castleOblivionMarker = capability.getCastleOblivionMarker();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.stoppedTicks);
		buffer.writeFloat(this.stopDmg);
		buffer.writeInt(this.flatTicks);
		buffer.writeBoolean(this.castleOblivionMarker);
	}

	public static SCSyncGlobalCapabilityPacket decode(FriendlyByteBuf buffer) {
		SCSyncGlobalCapabilityPacket msg = new SCSyncGlobalCapabilityPacket();
		msg.stoppedTicks = buffer.readInt();
		msg.stopDmg = buffer.readFloat();
		msg.flatTicks = buffer.readInt();
		msg.castleOblivionMarker = buffer.readBoolean();
		return msg;
	}

	public static void handle(final SCSyncGlobalCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IGlobalCapabilities> globalData = Minecraft.getInstance().player.getCapability(ModCapabilities.GLOBAL_CAPABILITIES);
			globalData.ifPresent(cap -> {
				cap.setStoppedTicks(message.stoppedTicks);
				cap.setDamage(message.stopDmg);
				cap.setFlatTicks(message.flatTicks);
				cap.setCastleOblivionMarker(message.castleOblivionMarker);
			});
		});
		ctx.get().setPacketHandled(true);
	}

}
