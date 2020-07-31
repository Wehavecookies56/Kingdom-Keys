package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class SCSyncData {

	public SCSyncData() {
	}

	String data;
	
	public SCSyncData(List<String> data, List<String> json) { //TODO add the 2 lists thing
		this.data = data;
	}
	
	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.data.length());
		buffer.writeString(this.data);
	}

	public static SCSyncData decode(PacketBuffer buffer) {
		SCSyncData msg = new SCSyncData();
		msg.data = buffer.readString();
		return msg;	
	}

	public static void handle(final SCSyncData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();
			
		});
		ctx.get().setPacketHandled(true);
	}

}
