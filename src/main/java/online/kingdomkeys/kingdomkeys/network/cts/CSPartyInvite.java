package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncExtendedWorld;

public class CSPartyInvite {
	
	String name;
	UUID playerUUID;
	
	public CSPartyInvite() {}

	public CSPartyInvite(Party party, UUID playerUUID) {
		this.name = party.getName();
		this.playerUUID = playerUUID;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
				
		buffer.writeUniqueId(this.playerUUID);
	}

	public static CSPartyInvite decode(PacketBuffer buffer) {
		CSPartyInvite msg = new CSPartyInvite();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
				
		msg.playerUUID = buffer.readUniqueId();
		return msg;
	}

	public static void handle(CSPartyInvite message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			//ExtendedWorldData worldData = ExtendedWorldData.get(player.world);
			/*Party p = worldData.getPartyFromName(message.name);
			p.removeMember(message.playerUUID);*/
			
			PlayerEntity target = player.world.getPlayerByUuid(message.playerUUID);
			IPlayerCapabilities tProps = ModCapabilities.get(target);
			if(!tProps.getPartiesInvited().contains(message.name)) {
				tProps.addPartiesInvited(message.name);
			}
			
			System.out.println(tProps.getPartiesInvited());
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(tProps), (ServerPlayerEntity)target);
			//PacketHandler.sendToAll(new SCSyncExtendedWorld(worldData), player.world);
		});
		ctx.get().setPacketHandled(true);
	}

}
