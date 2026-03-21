package online.kingdomkeys.kingdomkeys.client.gui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.client.MenuButtonRegisterEvent;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.abilities.MenuAbilitiesScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.config.MenuConfigScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.customize.MenuCustomizeScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuItemsScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.journal.MenuJournalScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Leader;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Member;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_None;
import online.kingdomkeys.kingdomkeys.client.gui.menu.status.MenuStatusScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.styles.StylesMenu;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuScreen extends MenuBackground {
	MenuBox box;

	public MenuScreen(PlayerData playerData) {
		super(Strings.Gui_Menu_Main_Title, new Color(0,0,255));
		minecraft = Minecraft.getInstance();
		this.playerData = playerData;
	}

    private final ArrayList<MenuButton> menuButtons = new ArrayList<>();

    public enum buttons {
		ITEMS, ABILITIES, CUSTOMIZE, PARTY, STATUS, JOURNAL, CONFIG, STYLES
    }

	PlayerData playerData;

	MenuButton items, abilities, customize, party, status, journal, config, style;

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case ITEMS -> minecraft.setScreen(new MenuItemsScreen());
			case ABILITIES -> minecraft.setScreen(new MenuAbilitiesScreen());
			case PARTY -> {
				Party p = WorldData.getClient().getPartyFromMember(minecraft.player.getUUID());
				if (p == null) {
					minecraft.setScreen(new GuiMenu_Party_None());
				} else {
					boolean isLeader = false;
					for(Member leader : p.getLeaders()) {
						if(leader.getUUID().equals(minecraft.player.getUUID())) {
							isLeader = true;
							break;
						}
					}
					if(isLeader) {
						minecraft.setScreen(new GuiMenu_Party_Leader());
					} else {
						minecraft.setScreen(new GuiMenu_Party_Member());
					}
				}
			}
			case STATUS -> minecraft.setScreen(new MenuStatusScreen());
			case CUSTOMIZE -> minecraft.setScreen(new MenuCustomizeScreen());
			case JOURNAL -> {
				if (KingdomKeys.patchouliLoaded) {
					online.kingdomkeys.kingdomkeys.integration.patchouli.PatchouliIntegration.openJournal();
				} else {
					minecraft.setScreen(new MenuJournalScreen());
				}
			}

			case CONFIG -> minecraft.setScreen(new MenuConfigScreen());
			case STYLES -> minecraft.setScreen(new StylesMenu(playerData));
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int startY = (int)topBarHeight + 5;
		int pos = 0;

		float buttonX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;
        menuButtons.clear();
        menuButtons.add(items = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Items, ButtonType.BUTTON, true, e -> action(buttons.ITEMS)));
        menuButtons.add(abilities = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Abilities, ButtonType.BUTTON, true, e -> action(buttons.ABILITIES)));
        menuButtons.add(customize = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Customize, ButtonType.BUTTON, true, e -> action(buttons.CUSTOMIZE)));
        menuButtons.add(party = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Party, ButtonType.BUTTON, true, e -> action(buttons.PARTY)));
        menuButtons.add(status = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Status, ButtonType.BUTTON, true, e -> action(buttons.STATUS)));
        menuButtons.add(journal = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Journal, ButtonType.BUTTON, true, e -> action(buttons.JOURNAL)));

        if (KingdomKeys.efmLoaded) {
            menuButtons.add(style = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Style, ButtonType.BUTTON, true, e -> action(buttons.STYLES)));
        }

        menuButtons.add(config = new MenuButton((int) buttonX, startY + 18 * pos++, (int) buttonWidth, Strings.Gui_Menu_Main_Button_Config, ButtonType.BUTTON, true, e -> action(buttons.CONFIG)));

        NeoForge.EVENT_BUS.post(new MenuButtonRegisterEvent(this, menuButtons));

        for (MenuButton button : menuButtons) {
            addRenderableWidget(button);
        }

        updateButtons();

		int sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int sh = Minecraft.getInstance().getWindow().getGuiScaledHeight();

		mapW = (int)middleHeight;
		mapH = (int)middleHeight;
		mapX = (int) (sw * 0.5F);
		mapY = (int)topBarHeight;
		box = new MenuBox(mapX, mapY,mapW,mapH, 1.0F, new Color(0, 100, 100));

	}

	private void updateButtons() {
		items.visible = true;
		abilities.visible = true;
		customize.visible = true;
		party.visible = true;
		status.visible = true;
		journal.visible = true;
		config.visible = true;
		if(KingdomKeys.efmLoaded)
			style.visible = true;
		customize.active = true;
		journal.active = true;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);

		PoseStack pose = gui.pose();
		pose.pushPose();
		{
			pose.translate(0, 0, 0);
			Party.Member m = new Party.Member(minecraft.player.getUUID(), minecraft.player.getDisplayName().getString());
			drawPlayer(gui, null, 0, m);
		}
		pose.popPose();
		box.render(gui, mouseX, mouseY, partialTicks);

		renderMap(gui);
	}

	//CO Map stuff
	private float mapOffsetX = 0;
	private float mapOffsetY = 0;

	private boolean draggingMap = false;
	private double lastMouseX, lastMouseY;

	private int mapX, mapY, mapW, mapH;

	public static List<RoomData> rooms = new ArrayList<>();
	private static final ResourceLocation ROOM_TEX = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/co/room.png");

	public void renderMap(GuiGraphics guiGraphics) {
		if (!CastleOblivionHandler.inInterior(getMinecraft().player) || rooms.isEmpty())
			return;

		int tileSize = 20;
		int originX = mapX + mapW / 2;
		int originY = mapY + mapH / 2;

		RoomData currentRoom = null;
		for (RoomData roomData : rooms) {
			if (roomData.getGenerated() != null) {
				if (roomData.getGenerated().inRoom(minecraft.player.blockPosition())) {
					currentRoom = roomData;
					break;
				}
			}
		}

		float centerOffsetX = 0;
		float centerOffsetY = -10;

		if (currentRoom != null) {
			float gx = -currentRoom.pos.x() * 2 * tileSize;
			float gy = -currentRoom.pos.y() * 2 * tileSize;

			float angle = (float) Math.toRadians(45);

			float rotatedX = (float)(gx * Math.cos(angle) - gy * Math.sin(angle));
			float rotatedY = (float)(gx * Math.sin(angle) + gy * Math.cos(angle));

			centerOffsetX += -rotatedX;
			centerOffsetY += -rotatedY;
		}

		enableScissor(mapX, mapY, mapW, mapH);

		guiGraphics.pose().pushPose();
		{
			guiGraphics.pose().translate(originX + mapOffsetX + centerOffsetX, originY + mapOffsetY + centerOffsetY, 0);

			guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(45));

			for (RoomData roomData : rooms) {
				int x = -roomData.pos.x() * 2;
				int y = -roomData.pos.y() * 2;

				int px = x * tileSize;
				int py = y * tileSize;

				boolean isCurrent = roomData == currentRoom;

				if (roomData.getGenerated() == null) {
					guiGraphics.setColor(0.8F, 0.7F, 0.2F, 1);
				} else if (isCurrent) {
					guiGraphics.setColor(0.2F, 0.9F, 1F, 1);
				} else {
					guiGraphics.setColor(0.9F, 0.9F, 0.8F, 1);
				}

				guiGraphics.blit(ROOM_TEX, px, py, tileSize, tileSize, 0, 0, 16, 16, 16, 16);
				guiGraphics.setColor(1, 1, 1, 1);

				for (Map.Entry<RoomDirection, DoorData> entry : roomData.getDoors().entrySet()) {
					RoomDirection dir = entry.getKey();
					DoorData data = entry.getValue();

					if (data.getType() == DoorData.Type.NONE)
						continue;

					if (dir != RoomDirection.EAST && dir != RoomDirection.SOUTH)
						continue;

					int dx = 0;
					int dy = 0;

					switch (dir) {
						case EAST -> dx = -1;
						case SOUTH -> dy = -1;
					}

					RoomData neighbor = getRoomAt(roomData.pos.x() + dx, roomData.pos.y() + dy);
					if (neighbor == null)
						continue;

					boolean open = false;

					if (roomData.getGenerated() != null && neighbor.getGenerated() != null) {
						CardDoorTileEntity te1 = roomData.getGenerated().getDoorTE(minecraft.level, dir);
						CardDoorTileEntity te2 = neighbor.getGenerated().getDoorTE(minecraft.level, dir.opposite());

						if (te1 != null && te1.isOpen())
							open = true;
						if (te2 != null && te2.isOpen())
							open = true;
					}

					int color = open ? 0xFF00FF00 : 0xFFFFFF00;
					int thickness = Math.max(2, tileSize / 5);

					switch (dir) {
						case EAST -> guiGraphics.fill(px + tileSize, py + tileSize / 2 - thickness / 2, px + tileSize * 2, py + tileSize / 2 + thickness / 2, color);
						case SOUTH -> guiGraphics.fill(px + tileSize / 2 - thickness / 2, py + tileSize, px + tileSize / 2 + thickness / 2, py + tileSize * 2, color);
					}
				}
			}

			drawKeybladeIcon(guiGraphics, currentRoom, tileSize);
		}
		guiGraphics.pose().popPose();

		RenderSystem.disableScissor();
	}

	private void drawKeybladeIcon(GuiGraphics guiGraphics, RoomData currentRoom, int tileSize) {
		if (currentRoom != null) {
			guiGraphics.pose().pushPose();

			int x = -currentRoom.pos.x() * 2;
			int y = -currentRoom.pos.y() * 2;

			int px = x * tileSize;
			int py = y * tileSize;

			guiGraphics.pose().translate(px + tileSize / 2f, py + tileSize / 2f, 0);

			float rotation = Mth.wrapDegrees(minecraft.player.getYRot() - 45);
			guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));

			float iconScale = 16F;
			guiGraphics.pose().scale(iconScale, iconScale, 1f);

			ItemStack stack = playerData.getEquippedKeychain(DriveForm.NONE);
			ItemStack item = stack;
			if (stack.getItem() instanceof IKeychain kc) {
				item = new ItemStack(kc.toSummon());
			}
			ClientUtils.drawItemAsIcon(item, guiGraphics.pose(), -8, -8, 1);

			guiGraphics.pose().popPose();
		}
	}

	private void enableScissor(int x, int y, int w, int h) {
		double scale = Minecraft.getInstance().getWindow().getGuiScale();

		int scissorX = (int)(x * scale);
		int scissorY = (int)((Minecraft.getInstance().getWindow().getGuiScaledHeight() - (y + h)) * scale);
		int scissorW = (int)(w * scale);
		int scissorH = (int)(h * scale);

		RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);
	}
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isInsideMap(mouseX, mouseY)) {
			draggingMap = true;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
		if (draggingMap) {
			mapOffsetX += mouseX - lastMouseX;
			mapOffsetY += mouseY - lastMouseY;

			lastMouseX = mouseX;
			lastMouseY = mouseY;
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dx, dy);
	}
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		draggingMap = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	private boolean isInsideMap(double mouseX, double mouseY) {
		return mouseX >= mapX && mouseX <= mapX + mapW &&
				mouseY >= mapY && mouseY <= mapY + mapH;
	}

	private RoomData getRoomAt(int x, int y) {
		for (RoomData r : rooms) {
			if (r.pos.x() == x && r.pos.y() == y) {
				return r;
			}
		}
		return null;
	}
}
