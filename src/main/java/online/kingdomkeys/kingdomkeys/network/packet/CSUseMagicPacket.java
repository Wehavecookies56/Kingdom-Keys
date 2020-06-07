package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.ModMagics;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSUseMagicPacket {
	
	String name;
	
	public CSUseMagicPacket() {}

	public CSUseMagicPacket(String name) {
		this.name = name;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
	}

	public static CSUseMagicPacket decode(PacketBuffer buffer) {
		CSUseMagicPacket msg = new CSUseMagicPacket();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
		return msg;
	}

	public static void handle(CSUseMagicPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			 IPlayerCapabilities props = ModCapabilities.get(player);
			// props.setMaxMP(120);
			// props.setMP(props.getMaxMP());
			if (props.getMP() >= 0 && !props.getRecharge()) {
				int cost = ModMagics.registry.getValue(new ResourceLocation(message.name)).getCost();
				props.remMP(cost);
				PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
            	ModMagics.registry.getValue(new ResourceLocation(message.name)).onUse(player);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
