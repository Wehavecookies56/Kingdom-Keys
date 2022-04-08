package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.GuiOverlay;

public class SCShowOverlayPacket {

	String type;
	int munny;
	String driveForm;

	public SCShowOverlayPacket() {
	}

	public SCShowOverlayPacket(String type) {
		this.type = type;
		this.driveForm = "";
	}

	public SCShowOverlayPacket(String type, int munny) {
		this.type = type;
		this.munny = munny;
		this.driveForm = "";
	}

	public SCShowOverlayPacket(String type, String driveForm) {
		this.type = type;
		this.driveForm = driveForm;
	}

	public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type);
        buffer.writeInt(this.munny);
        buffer.writeUtf(this.driveForm);
	}

	public static SCShowOverlayPacket decode(FriendlyByteBuf buffer) {
		SCShowOverlayPacket msg = new SCShowOverlayPacket();
		msg.type = buffer.readUtf(50);
		msg.munny = buffer.readInt();
		msg.driveForm = buffer.readUtf(25);
		return msg;
	}

	public static void handle(final SCShowOverlayPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			long time = System.currentTimeMillis()/1000;
			if (msg.type.equals("exp")) {
	            GuiOverlay.showExp = true;
	            GuiOverlay.timeExp = time;
	            
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
