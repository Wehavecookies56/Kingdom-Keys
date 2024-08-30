package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

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
			
			Player target = player.level().getPlayerByUUID(message.playerUUID);
			IPlayerData targetPlayerData = ModData.getPlayer(target);
			if(!targetPlayerData.getPartiesInvited().contains(message.name)) {
				targetPlayerData.addPartiesInvited(message.name);
				
				IWorldCapabilities worldData = ModData.getWorld(player.level());
				Party p = worldData.getPartyFromName(message.name);
				target.sendSystemMessage(Component.translatable(ChatFormatting.YELLOW+"You got an invitation to "+p.getName()));
			}
			
			
			PacketHandler.sendTo(new SCSyncPlayerData(targetPlayerData), (ServerPlayer)target);
		});
		ctx.get().setPacketHandled(true);
	}

}
