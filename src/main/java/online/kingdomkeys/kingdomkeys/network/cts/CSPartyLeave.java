package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;

public class CSPartyLeave {
	
	String name;
	UUID playerUUID;
	
	public CSPartyLeave() {}

	public CSPartyLeave(Party party, UUID playerUUID) {
		this.name = party.getName();
		this.playerUUID = playerUUID;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
				
		buffer.writeUniqueId(this.playerUUID);
	}

	public static CSPartyLeave decode(PacketBuffer buffer) {
		CSPartyLeave msg = new CSPartyLeave();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
				
		msg.playerUUID = buffer.readUniqueId();
		return msg;
	}

	public static void handle(CSPartyLeave message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
			Party p = worldData.getPartyFromName(message.name);
			p.removeMember(message.playerUUID);
			
			PacketHandler.sendToAll(new SCSyncWorldCapability(worldData), player);
		});
		ctx.get().setPacketHandled(true);
	}

}
