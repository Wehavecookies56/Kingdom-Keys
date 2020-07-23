package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncExtendedWorld;

public class CSPartyRemove {
	
	String name, username;
	UUID uuid;
	boolean priv;
	
	public CSPartyRemove() {}

	public CSPartyRemove(Party party) {
		this.name = party.getName();
		this.uuid = party.getLeader().getUUID();
		this.username = party.getLeader().getUsername();
		this.priv = party.getPriv();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		
		buffer.writeUniqueId(this.uuid);
		
		buffer.writeInt(this.username.length());
		buffer.writeString(this.username);
		
		buffer.writeBoolean(this.priv);
	}

	public static CSPartyRemove decode(PacketBuffer buffer) {
		CSPartyRemove msg = new CSPartyRemove();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
		
		msg.uuid = buffer.readUniqueId();
		
		length = buffer.readInt();
		msg.username = buffer.readString(length);
		
		msg.priv = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSPartyRemove message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ExtendedWorldData worldData = ExtendedWorldData.get(player.world);
			Party p = worldData.getPartyFromName(message.name);
			worldData.removeParty(p);
			
			PacketHandler.sendToAll(new SCSyncExtendedWorld(worldData), player.world);
		});
		ctx.get().setPacketHandled(true);
	}

}
