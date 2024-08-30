package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public class CSUseLimitPacket {
	
	ResourceLocation limit;
	int targetID;
	
	public CSUseLimitPacket() {}

	
	public CSUseLimitPacket(ResourceLocation limit) {
		this.limit = limit;
		this.targetID = -1;
	}

	public CSUseLimitPacket(LivingEntity target, ResourceLocation limit) {
		this.limit = limit;
		this.targetID = target.getId();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.limit);
		buffer.writeInt(this.targetID);
	}

	public static CSUseLimitPacket decode(FriendlyByteBuf buffer) {
		CSUseLimitPacket msg = new CSUseLimitPacket();
		msg.limit = buffer.readResourceLocation();
		msg.targetID = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseLimitPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
				IPlayerData playerData = ModData.getPlayer(player);
				Limit limit = ModLimits.registry.get().getValue(message.limit);
				int cost = limit.getCost();
				if (playerData.getDP() >= cost) {
					playerData.remDP(cost);
					playerData.setLimitCooldownTicks(limit.getCooldown());
					PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer)player);
					if(message.targetID > -1) {
						limit.onUse(player, (LivingEntity) player.level().getEntity(message.targetID));
					} else {
						limit.onUse(player, player);
					}
					
				}
				
				PacketHandler.syncToAllAround(player, playerData);
			
		});
		ctx.get().setPacketHandled(true);
	}

}