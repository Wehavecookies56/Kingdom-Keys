package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

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


	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.position);
		buffer.writeInt(this.level);
		buffer.writeString(this.magic,100);
	}

	public static CSSetShortcutPacket decode(PacketBuffer buffer) {
		CSSetShortcutPacket msg = new CSSetShortcutPacket();
		msg.position = buffer.readInt();
		msg.level = buffer.readInt();
		msg.magic = buffer.readString(100);
		return msg;
	}

	public static void handle(CSSetShortcutPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(message.magic.equals("")) {
				playerData.removeShortcut(message.position);
			} else {
				playerData.changeShortcut(message.position, message.magic, message.level);
			}
				
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}