package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.magic.ModMagics;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSUseMagicPacket {
	
	String name, target;
	
	public CSUseMagicPacket() {}

	public CSUseMagicPacket(String name) {
		this.name = name;
		this.target = "";
	}
	
	public CSUseMagicPacket(String name, String target) {
		this.name = name;
		this.target = target;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		buffer.writeInt(this.target.length());
		buffer.writeString(this.target);
	}

	public static CSUseMagicPacket decode(PacketBuffer buffer) {
		CSUseMagicPacket msg = new CSUseMagicPacket();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
		length = buffer.readInt();
		msg.target = buffer.readString(length);
		return msg;
	}

	public static void handle(CSUseMagicPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
				IPlayerCapabilities props = ModCapabilities.get(player);
				if (props.getMP() >= 0 && !props.getRecharge()) {
					int cost = ModMagics.registry.getValue(new ResourceLocation(message.name)).getCost();
					props.remMP(cost);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
					PlayerEntity targetEntity;
					if(message.target.equals("")) {
						targetEntity = player;
					} else {
						targetEntity = Utils.getPlayerByName(player.world, message.target);
					}
	            	ModMagics.registry.getValue(new ResourceLocation(message.name)).onUse(targetEntity);
				}
				
				PacketHandler.syncToAllAround(player, props);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
