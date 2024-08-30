package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public class CSSetShortcutPacket {
	
	int position, level;
	String magic;
	//int targetID;
	
	public CSSetShortcutPacket() {}

	public CSSetShortcutPacket(int positon, int level, String magic) {
		this.position = positon;
		this.level = level;
		this.magic = magic;
	}


	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.position);
		buffer.writeInt(this.level);
		buffer.writeUtf(this.magic,100);
	}

	public static CSSetShortcutPacket decode(FriendlyByteBuf buffer) {
		CSSetShortcutPacket msg = new CSSetShortcutPacket();
		msg.position = buffer.readInt();
		msg.level = buffer.readInt();
		msg.magic = buffer.readUtf(100);
		return msg;
	}

	public static void handle(CSSetShortcutPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerData playerData = ModData.getPlayer(player);
			if(message.magic.equals("")) {
				playerData.removeShortcut(message.position);
			} else {
				playerData.changeShortcut(message.position, message.magic, message.level);
			}
				
			PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}