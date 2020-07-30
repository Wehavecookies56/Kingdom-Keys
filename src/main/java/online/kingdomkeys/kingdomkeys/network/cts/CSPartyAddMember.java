package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncExtendedWorld;

public class CSPartyAddMember {
	
	String name;
	UUID memberUUID;
	String memberName;
	
	public CSPartyAddMember() {}

	public CSPartyAddMember(Party party, PlayerEntity member) {
		this.name = party.getName();
		this.memberUUID = member.getUniqueID();
		this.memberName = member.getDisplayName().getFormattedText();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		
		buffer.writeUniqueId(this.memberUUID);
		
		buffer.writeInt(this.memberName.length());
		buffer.writeString(this.memberName);
	}

	public static CSPartyAddMember decode(PacketBuffer buffer) {
		CSPartyAddMember msg = new CSPartyAddMember();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);		
		
		msg.memberUUID = buffer.readUniqueId();
		
		length = buffer.readInt();
		msg.memberName = buffer.readString(length);
		
		return msg;
	}

	public static void handle(CSPartyAddMember message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
			for(Party p : worldData.getParties()) {
				if(p.getName().equals(message.name))
					p.addMember(message.memberUUID, message.memberName);
				PlayerEntity target = player.world.getPlayerByUuid(message.memberUUID);
				ModCapabilities.get(target).removePartiesInvited(message.name);
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.get(target)), (ServerPlayerEntity)target);
				//System.out.println(ModCapabilities.get(target).getPartiesInvited());
			}
			PacketHandler.sendToAll(new SCSyncExtendedWorld(worldData), player);
		});
		ctx.get().setPacketHandled(true);
	}

}
