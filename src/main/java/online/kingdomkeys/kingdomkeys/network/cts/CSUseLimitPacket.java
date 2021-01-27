package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSUseLimitPacket {
	
	String name;
	int level;
	
	public CSUseLimitPacket() {}

	public CSUseLimitPacket(String name) {
		this.name = name;
		this.level = 0;
	}
	
	public CSUseLimitPacket(String name, int level) {
		this.name = name;
		this.level = level;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		buffer.writeInt(this.level);
	}

	public static CSUseLimitPacket decode(PacketBuffer buffer) {
		CSUseLimitPacket msg = new CSUseLimitPacket();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
		msg.level = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseLimitPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				Limit limit = ModLimits.registry.getValue(new ResourceLocation(message.name));
				int cost = limit.getLevels().get(message.level);
				if (playerData.getDP() >= cost) {
					System.out.println(playerData.getDP());
					playerData.remDP(cost);
					System.out.println(playerData.getDP());
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
					ModLimits.registry.getValue(new ResourceLocation(message.name)).onUse(player, message.level);
					
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
