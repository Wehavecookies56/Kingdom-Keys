package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
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

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		
		buffer.writeUUID(this.uuid);
		
		buffer.writeInt(this.username.length());
		buffer.writeUtf(this.username);
		
		buffer.writeBoolean(this.priv);
	}

	public static CSPartyDisband decode(FriendlyByteBuf buffer) {
		CSPartyDisband msg = new CSPartyDisband();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
		
		msg.uuid = buffer.readUUID();
		
		length = buffer.readInt();
		msg.username = buffer.readUtf(length);
		
		msg.priv = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSPartyDisband message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
			Party p = worldData.getPartyFromName(message.name);
			if(p != null)
				worldData.removeParty(p);
			
			Utils.syncWorldData(player.level, worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
