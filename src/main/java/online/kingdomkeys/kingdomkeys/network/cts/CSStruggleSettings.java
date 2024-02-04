package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSStruggleSettings {
	
	String name;
	byte size;
	int dmgMult;
	BlockPos pos,c1,c2;
	//TODO Ring positions
	
	public CSStruggleSettings() {}

	public CSStruggleSettings(Struggle struggle) {
		this.name = struggle.getName();
		this.size = struggle.getSize();
		this.dmgMult = struggle.getDamageMult();
		this.pos = struggle.getPos();
		this.c1 = struggle.getC1();
		this.c2 = struggle.getC2();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeUtf(this.name);
		buffer.writeByte(this.size);
		buffer.writeInt(this.dmgMult);
		buffer.writeBlockPos(this.pos);
		buffer.writeBlockPos(this.c1);
		buffer.writeBlockPos(this.c2);
	}

	public static CSStruggleSettings decode(FriendlyByteBuf buffer) {
		CSStruggleSettings msg = new CSStruggleSettings();
		int length = buffer.readInt();
		msg.name = buffer.readUtf(length);		
		
		msg.size = buffer.readByte();
		msg.dmgMult = buffer.readInt();
		msg.pos = buffer.readBlockPos();
		msg.c1 = buffer.readBlockPos();
		msg.c2 = buffer.readBlockPos();
		return msg;
	}

	public static void handle(CSStruggleSettings message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
			Struggle p = worldData.getStruggleFromBlockPos(message.pos);
			
			p.setSize(message.size);
			p.setDamageMult(message.dmgMult);
			p.setName(message.name);
			p.setC1(message.c1);
			p.setC2(message.c2);
			
			Utils.syncWorldData(player.level(), worldData);
		});
		ctx.get().setPacketHandled(true);
	}

}
