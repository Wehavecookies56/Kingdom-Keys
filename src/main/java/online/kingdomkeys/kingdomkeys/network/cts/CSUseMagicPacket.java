package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
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
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if (playerData.getMP() >= 0 && !playerData.getRecharge()) {
					int cost = ModMagic.registry.getValue(new ResourceLocation(message.name)).getCost();
					playerData.remMP(cost);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);

					int level = 0;
					
					if(message.target.equals("")) {
		            	ModMagic.registry.getValue(new ResourceLocation(message.name)).onUse(player, player, level);
					} else {
						PlayerEntity targetEntity = Utils.getPlayerByName(player.world, message.target);
		            	ModMagic.registry.getValue(new ResourceLocation(message.name)).onUse(targetEntity, player, level);
					}
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
