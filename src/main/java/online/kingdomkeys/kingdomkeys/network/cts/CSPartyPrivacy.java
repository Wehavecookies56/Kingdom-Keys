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

public class CSPartyPrivacy {
	
	String name;
	boolean priv;
	
	public CSPartyPrivacy() {}

	public CSPartyPrivacy(Party party, boolean priv) {
		this.name = party.getName();
		this.priv = priv;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
		
		buffer.writeBoolean(this.priv);
	}

	public static CSPartyPrivacy decode(PacketBuffer buffer) {
		CSPartyPrivacy msg = new CSPartyPrivacy();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);		
		msg.priv = buffer.readBoolean();
		return msg;
	}

	public static void handle(CSPartyPrivacy message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ExtendedWorldData worldData = ExtendedWorldData.get(player.world);
			for(Party p : worldData.getParties()) {
				if(p.getName().equals(message.name))
					p.setPriv(message.priv);
					//worldData.removeParty(party);
			}
			PacketHandler.sendToAll(new SCSyncExtendedWorld(worldData), player.world);
		});
		ctx.get().setPacketHandled(true);
	}

}
