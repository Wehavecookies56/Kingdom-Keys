package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

import java.util.UUID;
import java.util.function.Supplier;

public class CSPartyInvite {
	
	String name;
	UUID playerUUID;
	
	public CSPartyInvite() {}

	public CSPartyInvite(Party party, UUID playerUUID) {
		this.name = party.getName();
		this.playerUUID = playerUUID;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
				
		buffer.writeUUID(this.playerUUID);
	}

	public static CSPartyInvite decode(FriendlyByteBuf buffer) {
		CSPartyInvite msg = new CSPartyInvite();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
				
		msg.playerUUID = buffer.readUUID();
		return msg;
	}

	public static void handle(CSPartyInvite message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			
			Player target = player.level.getPlayerByUUID(message.playerUUID);
			IPlayerCapabilities targetPlayerData = ModCapabilities.getPlayer(target);
			if(!targetPlayerData.getPartiesInvited().contains(message.name)) {
				targetPlayerData.addPartiesInvited(message.name);
				
				IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
				Party p = worldData.getPartyFromName(message.name);
				target.sendMessage(new TranslatableComponent(ChatFormatting.YELLOW+p.getLeader().getUsername()+" has invited you to "+p.getName()), Util.NIL_UUID);
			}
			
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(targetPlayerData), (ServerPlayer)target);
		});
		ctx.get().setPacketHandled(true);
	}

}
