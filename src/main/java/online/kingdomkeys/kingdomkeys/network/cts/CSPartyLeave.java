package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSPartyLeave {
	
	String name;
	UUID playerUUID;
	
	public CSPartyLeave() {}

	public CSPartyLeave(Party party, UUID playerUUID) {
		this.name = party.getName();
		this.playerUUID = playerUUID;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
				
		buffer.writeUUID(this.playerUUID);
	}

	public static CSPartyLeave decode(FriendlyByteBuf buffer) {
		CSPartyLeave msg = new CSPartyLeave();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
				
		msg.playerUUID = buffer.readUUID();
		return msg;
	}

	public static void handle(CSPartyLeave message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModData.getWorld(player.level());
			Party p = worldData.getPartyFromName(message.name);
			p.removeMember(message.playerUUID);
			
			Utils.syncWorldData(player.level(), worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
