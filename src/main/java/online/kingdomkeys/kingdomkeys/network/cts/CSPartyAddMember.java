package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSPartyAddMember {
	
	String name;
	UUID memberUUID;
	String memberName;
	
	public CSPartyAddMember() {}

	public CSPartyAddMember(Party party, Player member) {
		this.name = party.getName();
		this.memberUUID = member.getUUID();
		this.memberName = member.getDisplayName().getString();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		
		buffer.writeUUID(this.memberUUID);
		
		buffer.writeInt(this.memberName.length());
		buffer.writeUtf(this.memberName);
	}

	public static CSPartyAddMember decode(FriendlyByteBuf buffer) {
		CSPartyAddMember msg = new CSPartyAddMember();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);		
		
		msg.memberUUID = buffer.readUUID();
		
		length = buffer.readInt();
		msg.memberName = buffer.readUtf(length);
		
		return msg;
	}

	public static void handle(CSPartyAddMember message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
			for(Party p : worldData.getParties()) {
				if(p.getName().equals(message.name))
					p.addMember(message.memberUUID, message.memberName);
				Player target = player.level.getPlayerByUUID(message.memberUUID);
				ModCapabilities.getPlayer(target).removePartiesInvited(message.name);
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(target)), (ServerPlayer)target);
			}
			Utils.syncWorldData(player.level, worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
