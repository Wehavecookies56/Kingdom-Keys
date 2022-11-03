package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.UUID;
import java.util.function.Supplier;

public class CSPartyCreate {
	
	String name, username;
	UUID uuid;
	boolean priv;
	byte size;
	
	public CSPartyCreate() {}

	public CSPartyCreate(Party party) {
		this.name = party.getName();
		this.uuid = party.getLeader().getUUID();
		this.username = party.getLeader().getUsername();
		this.priv = party.getPriv();
		this.size = party.getSize();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		
		buffer.writeUUID(this.uuid);
		
		buffer.writeInt(this.username.length());
		buffer.writeUtf(this.username);
		
		buffer.writeBoolean(this.priv);
		
		buffer.writeByte(this.size);
	}

	public static CSPartyCreate decode(FriendlyByteBuf buffer) {
		CSPartyCreate msg = new CSPartyCreate();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
		
		msg.uuid = buffer.readUUID();
		
		length = buffer.readInt();
		msg.username = buffer.readUtf(length);
		
		msg.priv = buffer.readBoolean();
		
		msg.size = buffer.readByte();
		return msg;
	}

	public static void handle(CSPartyCreate message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
			Party party = new Party(message.name, message.uuid, message.username, message.priv, message.size); 
			worldData.addParty(party);
			Utils.syncWorldData(player.level, worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
