package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSUseLimitPacket {
	int index;
	int targetID;
	
	public CSUseLimitPacket() {}
	
	public CSUseLimitPacket(int index) {
		this.index = index;
		this.targetID = -1;
	}

	public CSUseLimitPacket(LivingEntity target, int index) {
		this.index = index;
		this.targetID = target.getEntityId();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.index);
		buffer.writeInt(this.targetID);
	}

	public static CSUseLimitPacket decode(PacketBuffer buffer) {
		CSUseLimitPacket msg = new CSUseLimitPacket();
		msg.index = buffer.readInt();
		msg.targetID = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseLimitPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				Limit limit = Utils.getPlayerLimitAttacks(player).get(message.index);
				int cost = limit.getCost();
				if (playerData.getDP() >= cost) {
					playerData.remDP(cost);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
					if(message.targetID > -1) {
						limit.onUse(player, (LivingEntity) player.world.getEntityByID(message.targetID));
					} else {
						limit.onUse(player, player);
					}
					
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
