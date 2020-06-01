package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.GuiOverlay;

public class ShowOverlayPacket {

	String type;
	int munny;
	String driveForm;

	public ShowOverlayPacket() {
	}

	public ShowOverlayPacket(String type) {
		this.type = type;
		this.driveForm = "";
	}

	public ShowOverlayPacket(String type, int munny) {
		this.type = type;
		this.munny = munny;
		this.driveForm = "";
	}

	public ShowOverlayPacket(String type, String driveForm) {
		this.type = type;
		this.driveForm = driveForm;
	}

	public void encode(PacketBuffer buffer) {
        buffer.writeString(this.type);
        buffer.writeInt(this.munny);
        buffer.writeString(this.driveForm);
	}

	public static ShowOverlayPacket decode(PacketBuffer buffer) {
		ShowOverlayPacket msg = new ShowOverlayPacket();
		msg.type = buffer.readString(50);
		msg.munny = buffer.readInt();
		msg.driveForm = buffer.readString(25);
		return msg;
	}

	public static void handle(final ShowOverlayPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			long time = System.currentTimeMillis()/1000;
			if (msg.type.equals("exp")) {
	            GuiOverlay.showExp = true;
	            GuiOverlay.timeExp = time;
	            //System.out.println(time);
	        }
	        if (msg.type.equals("munny")) {
	            GuiOverlay.showMunny = true;
	            GuiOverlay.timeMunny = time;
	            GuiOverlay.munnyGet = msg.munny;
	        }
	        if (msg.type.equals("levelup")) {
	            GuiOverlay.showLevelUp = true;
	            GuiOverlay.timeLevelUp = time;
	        }
	        if (msg.type.equals("drivelevelup")) {
	            GuiOverlay.showDriveLevelUp = true;
	            GuiOverlay.timeDriveLevelUp = time;
	            GuiOverlay.driveForm = msg.driveForm;
	        }	
	    });
		ctx.get().setPacketHandled(true);
	}

}
