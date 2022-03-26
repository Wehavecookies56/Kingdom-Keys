package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
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

	
	public CSUseLimitPacket(int level) {
		this.index = level;
		this.targetID = -1;
	}

	public CSUseLimitPacket(LivingEntity target, int level) {
		this.index = level;
		this.targetID = target.getId();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.index);
		buffer.writeInt(this.targetID);
	}

	public static CSUseLimitPacket decode(FriendlyByteBuf buffer) {
		CSUseLimitPacket msg = new CSUseLimitPacket();
		msg.index = buffer.readInt();
		msg.targetID = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseLimitPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				Limit limit = Utils.getPlayerLimitAttacks(player).get(message.index);
				int cost = limit.getCost();
				if (playerData.getDP() >= cost) {
					playerData.remDP(cost);
					playerData.setLimitCooldownTicks(600);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
					if(message.targetID > -1) {
						limit.onUse(player, (LivingEntity) player.level.getEntity(message.targetID));
					} else {
						limit.onUse(player, player);
					}
					
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}