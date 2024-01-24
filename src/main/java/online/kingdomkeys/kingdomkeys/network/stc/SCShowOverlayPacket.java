package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.GuiOverlay;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.GuiOverlay.LevelUpData;

public class SCShowOverlayPacket {

	String type;
	int munny;
	String driveForm, playerName;
	UUID player;
	int level, color;
	List<String> messages = new ArrayList<String>();

	public SCShowOverlayPacket() {
	}

	public SCShowOverlayPacket(String type) {
		this.type = type;
		this.driveForm = "";
		this.playerName = "";
		
		this.player = Util.NIL_UUID;
		this.messages = List.of();
	}

	public SCShowOverlayPacket(String type, int munny) {
		this.type = type;
		this.munny = munny;
		this.driveForm = "";
		this.playerName = "";
		
		this.player = Util.NIL_UUID;
		this.messages = List.of();
	}

	public SCShowOverlayPacket(String type, String driveForm) {
		this.type = type;
		this.driveForm = driveForm;
		this.playerName = "";

		this.player = Util.NIL_UUID;
		this.messages = List.of();
	}
	
	//Party player
	public SCShowOverlayPacket(String type, UUID player, String playerName, int level, int color, List<String> messages) {
		this.type = type;
		this.player = player;
		this.playerName = playerName;
		this.messages = messages;
		this.level = level;
		this.color = color;
		this.driveForm = "";
	}

	public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.type, 50);
        buffer.writeInt(this.munny);
        buffer.writeUtf(this.driveForm, 50);
        buffer.writeUtf(this.playerName, 50);
        buffer.writeUUID(this.player);
        buffer.writeInt(this.level);
        buffer.writeInt(this.color);
        int size = this.messages.size();
       // System.out.println(size);
        buffer.writeInt(size);
        for(int i = 0; i < size;i++) {
        	//System.out.println(messages.get(i));
        	buffer.writeUtf(messages.get(i),150);
        }
	}

	public static SCShowOverlayPacket decode(FriendlyByteBuf buffer) {
		SCShowOverlayPacket msg = new SCShowOverlayPacket();
		msg.type = buffer.readUtf(50);
		msg.munny = buffer.readInt();
		msg.driveForm = buffer.readUtf(50);
		msg.playerName = buffer.readUtf(50);
		msg.player = buffer.readUUID();
		msg.level = buffer.readInt();
		msg.color = buffer.readInt();
		int size = buffer.readInt();
		for(int i = 0; i < size;i++) {
			msg.messages.add(buffer.readUtf(150));
		}
		return msg;
	}

	public static void handle(final SCShowOverlayPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			long time = System.currentTimeMillis()/1000;
			switch(msg.type) {
				case "exp":
		            GuiOverlay.showExp = true;
		            GuiOverlay.timeExp = time;		
					break;
				case "munny":
		            GuiOverlay.showMunny = true;
		            GuiOverlay.timeMunny = time;
		            GuiOverlay.munnyGet = msg.munny;
					break;
				case "levelup":
					LevelUpData instance = new GuiOverlay.LevelUpData();
					instance.timeLevelUp = time;
					instance.notifTicks = 0;
					instance.playerUUID = msg.player;
					instance.playerName = msg.playerName;
					instance.messages = msg.messages;
					instance.lvl = msg.level;
					instance.color = msg.color;
					GuiOverlay.levelUpList.add(0,instance);
					break;
				case "drivelevelup":
		            GuiOverlay.showDriveLevelUp = true;
		            GuiOverlay.timeDriveLevelUp = time;
		            GuiOverlay.driveForm = msg.driveForm;
		            GuiOverlay.driveNotifTicks = 0;
					break;
			}
			
	    });
		ctx.get().setPacketHandled(true);
	}

}
