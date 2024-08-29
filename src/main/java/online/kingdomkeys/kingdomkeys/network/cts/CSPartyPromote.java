package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSPartyPromote {
	
	String name;
	UUID playerUUID;
	
	public CSPartyPromote() {}

	public CSPartyPromote(Party party, UUID playerUUID) {
		this.name = party.getName();
		this.playerUUID = playerUUID;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
				
		buffer.writeUUID(this.playerUUID);
	}

	public static CSPartyPromote decode(FriendlyByteBuf buffer) {
		CSPartyPromote msg = new CSPartyPromote();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);
				
		msg.playerUUID = buffer.readUUID();
		return msg;
	}

	public static void handle(CSPartyPromote message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModData.getWorld(player.level());
			Party p = worldData.getPartyFromName(message.name);
			Member member = null;
			for(Member m : p.getMembers()) {
				if(m.getUUID().equals(message.playerUUID)){
					member = m;
				}
			}
			if(member != null) {
				member.setIsLeader(!member.isLeader());
			}
			Utils.syncWorldData(player.level(), worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
