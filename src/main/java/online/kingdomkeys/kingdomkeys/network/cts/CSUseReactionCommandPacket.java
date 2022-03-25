package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public class CSUseReactionCommandPacket {
	
	int index;
	//int targetID;
	
	public CSUseReactionCommandPacket() {}

	
	public CSUseReactionCommandPacket(int level) {
		this.index = level;
		//this.targetID = -1;
	}

	/*public CSUseReactionCommandPacket(LivingEntity target, int level) {
		this.index = level;
		//this.targetID = target.getEntityId();
	}*/

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.index);
		//buffer.writeInt(this.targetID);
	}

	public static CSUseReactionCommandPacket decode(FriendlyByteBuf buffer) {
		CSUseReactionCommandPacket msg = new CSUseReactionCommandPacket();
		msg.index = buffer.readInt();
		//msg.targetID = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseReactionCommandPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			String reactionName = playerData.getReactionCommands().get(message.index);
			ReactionCommand reaction = ModReactionCommands.registry.get().getValue(new ResourceLocation(reactionName));
			reaction.onUse(player, player);
				
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}