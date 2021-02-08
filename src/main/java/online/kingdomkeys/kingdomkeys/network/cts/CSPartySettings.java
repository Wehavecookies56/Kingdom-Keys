package online.kingdomkeys.kingdomkeys.network.cts;

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

public class CSPartySettings {
	
	String name;
	boolean priv;
	byte size;
	boolean friendlyFire;
	
	public CSPartySettings() {}

	public CSPartySettings(Party party) {
		this.name = party.getName();
		this.priv = party.getPriv();
		this.size = party.getSize();
		this.friendlyFire = party.getFriendlyFire();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		buffer.writeBoolean(this.priv);
		buffer.writeByte(this.size);
		buffer.writeBoolean(this.friendlyFire);
	}

	public static CSPartySettings decode(PacketBuffer buffer) {
		CSPartySettings msg = new CSPartySettings();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);		
		msg.priv = buffer.readBoolean();
		msg.size = buffer.readByte();
		msg.friendlyFire = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSPartySettings message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
			Party p = worldData.getPartyFromName(message.name);
			p.setPriv(message.priv);
			p.setSize(message.size);
			p.setFriendlyFire(message.friendlyFire);
			
			Utils.syncWorldData(player.world, worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
