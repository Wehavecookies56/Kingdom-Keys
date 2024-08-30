package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.GuiOverlay;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.GuiOverlay.LevelUpData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;

public record SCShowOverlayPacket(String _type, int munny, String driveForm, UUID player, String playerName, int level, int color, List<String> messages) implements Packet {

	public static final Type<SCShowOverlayPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_show_overlay"));

	public static final StreamCodec<FriendlyByteBuf, SCShowOverlayPacket> STREAM_CODEC = StreamCodecs.composite(
			ByteBufCodecs.STRING_UTF8,
			SCShowOverlayPacket::_type,
			ByteBufCodecs.INT,
			SCShowOverlayPacket::munny,
			ByteBufCodecs.STRING_UTF8,
			SCShowOverlayPacket::driveForm,
			UUIDUtil.STREAM_CODEC,
			SCShowOverlayPacket::player,
			ByteBufCodecs.STRING_UTF8,
			SCShowOverlayPacket::playerName,
			ByteBufCodecs.INT,
			SCShowOverlayPacket::level,
			ByteBufCodecs.INT,
			SCShowOverlayPacket::color,
			ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
			SCShowOverlayPacket::messages,
			SCShowOverlayPacket::new
	);

	public SCShowOverlayPacket(String type) {
		this(type, 0, "", Util.NIL_UUID, "", 0, 0, List.of());
	}

	public SCShowOverlayPacket(String type, int munny) {
		this(type, munny, "", Util.NIL_UUID, "", 0, 0, List.of());
	}

	public SCShowOverlayPacket(String type, String driveForm) {
		this(type, 0, driveForm, Util.NIL_UUID, "", 0, 0, List.of());

	}

	//Party player
	public SCShowOverlayPacket(String type, UUID player, String playerName, int level, int color, List<String> messages) {
		this(type, 0, "", player, playerName, level, color, messages);
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			Minecraft mc = Minecraft.getInstance();
			long time = System.currentTimeMillis()/1000;
			switch(_type) {
				case "exp":
					GuiOverlay.showExp = true;
					GuiOverlay.timeExp = time;
					break;
				case "munny":
					GuiOverlay.showMunny = true;
					GuiOverlay.timeMunny = time;
					GuiOverlay.munnyGet = munny;
					break;
				case "levelup":
					LevelUpData instance = new GuiOverlay.LevelUpData();
					instance.timeLevelUp = time;
					instance.notifTicks = 0;
					instance.playerUUID = player;
					instance.playerName = playerName;
					instance.messages = messages;
					instance.lvl = level;
					instance.color = color;
					GuiOverlay.levelUpList.add(0,instance);
					break;
				case "drivelevelup":
					GuiOverlay.showDriveLevelUp = true;
					GuiOverlay.timeDriveLevelUp = time;
					GuiOverlay.driveForm = driveForm;
					GuiOverlay.driveNotifTicks = 0;
					break;
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
