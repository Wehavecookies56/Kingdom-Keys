package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncExtendedWorld;

public class CSPartySettings {
	
	String name;
	boolean priv;
	byte size;
	
	public CSPartySettings() {}

	public CSPartySettings(Party party) {
		this.name = party.getName();
		this.priv = party.getPriv();
		this.size = party.getSize();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		
		buffer.writeBoolean(this.priv);
		
		buffer.writeByte(this.size);
	}

	public static CSPartySettings decode(PacketBuffer buffer) {
		CSPartySettings msg = new CSPartySettings();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);		
		msg.priv = buffer.readBoolean();
		msg.size = buffer.readByte();
		return msg;
	}

	public static void handle(CSPartySettings message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ExtendedWorldData worldData = ExtendedWorldData.get(player.world);
			Party p = worldData.getPartyFromName(message.name);
			p.setPriv(message.priv);
			p.setSize(message.size);
			
			PacketHandler.sendToAll(new SCSyncExtendedWorld(worldData), player.world);
		});
		ctx.get().setPacketHandled(true);
	}

}
