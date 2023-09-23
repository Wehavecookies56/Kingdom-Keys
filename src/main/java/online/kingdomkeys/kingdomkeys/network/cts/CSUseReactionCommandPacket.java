package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public class CSUseReactionCommandPacket {
	
	int index;
	int lockedOnEntity;
	
	public CSUseReactionCommandPacket() {}

	
	public CSUseReactionCommandPacket(int level, LivingEntity lockedOnEntity) {
		this.index = level;
		System.out.println("RC P: "+lockedOnEntity);
		this.lockedOnEntity = lockedOnEntity == null ? -1 : lockedOnEntity.getId();
	}

	/*public CSUseReactionCommandPacket(LivingEntity target, int level) {
		this.index = level;
		//this.targetID = target.getEntityId();
	}*/

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.index);
		buffer.writeInt(this.lockedOnEntity);
	}

	public static CSUseReactionCommandPacket decode(FriendlyByteBuf buffer) {
		CSUseReactionCommandPacket msg = new CSUseReactionCommandPacket();
		msg.index = buffer.readInt();
		msg.lockedOnEntity = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseReactionCommandPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			String reactionName = playerData.getReactionCommands().get(message.index);
			ReactionCommand reaction = ModReactionCommands.registry.get().getValue(new ResourceLocation(reactionName));
			reaction.onUse(player, player, (LivingEntity) player.level.getEntity(message.lockedOnEntity));
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}