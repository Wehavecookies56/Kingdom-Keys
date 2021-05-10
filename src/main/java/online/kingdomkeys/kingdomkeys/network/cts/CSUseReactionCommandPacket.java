package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;

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

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.index);
		//buffer.writeInt(this.targetID);
	}

	public static CSUseReactionCommandPacket decode(PacketBuffer buffer) {
		CSUseReactionCommandPacket msg = new CSUseReactionCommandPacket();
		msg.index = buffer.readInt();
		//msg.targetID = buffer.readInt();
		return msg;
	}

	public static void handle(CSUseReactionCommandPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			String reactionName = playerData.getReactionCommands().get(message.index);
			ReactionCommand reaction = ModReactionCommands.registry.getValue(new ResourceLocation(reactionName));
			reaction.onUse(player, player);
				
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}