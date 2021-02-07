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
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSPartyDisband {
	
	String name, username;
	UUID uuid;
	boolean priv;
	
	public CSPartyDisband() {}

	public CSPartyDisband(Party party) {
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

	public static CSPartyDisband decode(PacketBuffer buffer) {
		CSPartyDisband msg = new CSPartyDisband();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
		
		msg.uuid = buffer.readUniqueId();
		
		length = buffer.readInt();
		msg.username = buffer.readString(length);
		
		msg.priv = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSPartyDisband message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
			Party p = worldData.getPartyFromName(message.name);
			if(p != null)
				worldData.removeParty(p);
			
			Utils.syncWorldData(player.world, worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
