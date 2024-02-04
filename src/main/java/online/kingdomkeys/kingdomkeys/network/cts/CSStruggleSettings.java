package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSStruggleSettings {
	
	String name;
	//boolean priv;
	byte size;
	float dmgMult;
	
	public CSStruggleSettings() {}

	public CSStruggleSettings(Struggle struggle) {
		this.name = struggle.getName();
		//this.priv = struggle.getPriv();
		this.size = struggle.getSize();
		this.dmgMult = struggle.getDamageMult();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		//buffer.writeBoolean(this.priv);
		buffer.writeByte(this.size);
		buffer.writeFloat(this.dmgMult);
	}

	public static CSStruggleSettings decode(FriendlyByteBuf buffer) {
		CSStruggleSettings msg = new CSStruggleSettings();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);		
		//msg.priv = buffer.readBoolean();
		msg.size = buffer.readByte();
		msg.dmgMult = buffer.readFloat();
		return msg;
	}

	public static void handle(CSStruggleSettings message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
			Struggle p = worldData.getStruggleFromName(message.name);
			//p.setPriv(message.priv);
			p.setSize(message.size);
			p.setDamageMult(message.dmgMult);
			
			Utils.syncWorldData(player.level(), worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
