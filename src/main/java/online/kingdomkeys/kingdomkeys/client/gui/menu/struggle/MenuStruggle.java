package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleDelete;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleJoin;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleLeave;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleReady;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.StruggleHandler;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;


public class MenuStruggle extends MenuBackground {
	BlockPos boardPos;

	public MenuStruggle(BlockPos pos) {
		super(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Menu_Title), new Color(252, 173, 3));
		minecraft = Minecraft.getInstance();
		boardPos = pos;
	}


	public enum buttons {
		CREATE, JOIN, SETTINGS, READY, DELETE, LEAVE
	}

	MenuButton create, join, settings, ready, delete, leave;
	final List<String> joinReasons = new java.util.ArrayList<>();
	final List<String> readyReasons = new java.util.ArrayList<>();

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case CREATE -> minecraft.setScreen(new StruggleCreate(boardPos));
			case JOIN -> {
				Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
				if (s != null) {
					PacketHandler.sendToServer(new CSStruggleJoin(s.getName()));
				}
			}
			case SETTINGS -> minecraft.setScreen(new StruggleSettings(boardPos));
			case READY -> {
				Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
				if (s != null) {
					PacketHandler.sendToServer(new CSStruggleReady(s.getName()));
				}
			}
			case DELETE -> {
				Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
				if (s != null) {
					PacketHandler.sendToServer(new CSStruggleDelete(s.getName()));
				}
			}
			case LEAVE -> {
				Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
				if (s != null) {
					PacketHandler.sendToServer(new CSStruggleLeave(s.getName()));
				}
			}
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int start = (int)topBarHeight + 5;
		int pos = 0;

		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		addRenderableWidget(create = new MenuButton((int) buttonPosX, start, (int) buttonWidth, Strings.Gui_Menu_Struggle_Create_Button, ButtonType.BUTTON, true, (e) -> {action(buttons.CREATE);}));
		addRenderableWidget(join = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Strings.Gui_Menu_Struggle_Join_Button, ButtonType.BUTTON, true, (e) -> {action(buttons.JOIN);}));
		addRenderableWidget(settings = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Strings.Gui_Menu_Struggle_Settings_Button, ButtonType.BUTTON, true, (e) -> {action(buttons.SETTINGS);}));
		addRenderableWidget(ready = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Strings.Gui_Menu_Struggle_Ready, ButtonType.BUTTON, true, (e) -> {action(buttons.READY);}));
		addRenderableWidget(delete = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Strings.Gui_Menu_Struggle_Delete_Button, ButtonType.BUTTON, true, (e) -> {action(buttons.DELETE);}));
		addRenderableWidget(leave = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Strings.Gui_Menu_Struggle_Leave_Button, ButtonType.BUTTON, true, (e) -> {action(buttons.LEAVE);}));

		updateButtons();
	}

	private void updateButtons() {
		Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
		boolean isParticipant = s != null && s.hasParticipant(minecraft.player.getUUID());
		boolean isOwner = s != null && s.getOwnerId() != null && s.getOwnerId().equals(minecraft.player.getUUID());

		create.visible = (s == null);
		join.visible = (s != null) && !isParticipant;
		joinReasons.clear();
		if (join.visible) {
			boolean hasWeapon = StruggleHandler.findAnyWeaponSlot(minecraft.player.getInventory()) != null;
			join.active = s.isConfigured() && hasWeapon;
			if (!s.isConfigured()) {
				joinReasons.add(Strings.Gui_Menu_Struggle_Reason_Not_Configured);
			}
			if (!hasWeapon) {
				joinReasons.add("kingdomkeys.struggle.no_weapon");
			}
		}
		settings.visible = isOwner;
		delete.visible = isOwner;
		leave.visible = isParticipant && !isOwner && !s.isInProgress();
		ready.visible = isParticipant && !s.isInProgress();
		readyReasons.clear();
		if (ready.visible) {
			boolean hasWeapon = StruggleHandler.findAnyWeaponSlot(minecraft.player.getInventory()) != null;
			boolean enoughPlayers = s.getParticipants().size() >= 2;
			boolean canReady = s.isConfigured() && enoughPlayers && hasWeapon;
			ready.active = canReady;
			if (!s.isConfigured()) {
				readyReasons.add(Strings.Gui_Menu_Struggle_Reason_Not_Configured);
			}
			if (!enoughPlayers) {
				readyReasons.add(Strings.Gui_Menu_Struggle_Reason_Waiting);
			}
			if (!hasWeapon) {
				readyReasons.add("kingdomkeys.struggle.no_weapon");
			}

			boolean isReady = s.getParticipant(minecraft.player.getUUID()).isReady();
			ready.setMessage(Component.literal(isReady ? Utils.translateToLocal(Strings.Gui_Menu_Struggle_Cancel_Ready) : Utils.translateToLocal(Strings.Gui_Menu_Struggle_Ready)));
		}

		float topBarHeight = (float) height * 0.17F;
		int start = (int) topBarHeight + 5;
		int row = 0;
		for (MenuButton button : new MenuButton[]{create, join, settings, ready, delete, leave}) {
			if (button.visible) {
				button.setY(start + 18 * row);
				row++;
			}
		}
	}

	private void showFloatingTooltip(GuiGraphics gui, MenuButton button, List<String> reasons, int mouseX, int mouseY) {
		if (!button.visible || !button.isHovered() || reasons.isEmpty())
			return;
		List<Component> lines = new java.util.ArrayList<>();
		for (String reasonKey : reasons) {
			MutableComponent line = Component.literal(Utils.translateToLocal(reasonKey)).withStyle(ChatFormatting.RED);
			lines.add(line);
		}
		gui.renderTooltip(minecraft.font, lines, java.util.Optional.empty(), mouseX, mouseY);
	}

	private String modeLabel(Struggle.Mode mode) {
		return Utils.translateToLocal(Strings.Gui_Menu_Struggle + "." + mode.name().toLowerCase());
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		drawTip(gui);
		updateButtons();
		drawStruggle(WorldData.getClient(), gui, boardPos);

		showFloatingTooltip(gui, join, joinReasons, mouseX, mouseY);
		showFloatingTooltip(gui, ready, readyReasons, mouseX, mouseY);

		Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
		if (s != null) {
			float scale = 1.5F;
			gui.pose().pushPose();
			{
				gui.pose().scale(scale, scale, 1);
				gui.drawString(minecraft.font, Component.literal("[" + s.getParticipants().size() + "/" + s.getSize() + "] " + s.getName() + " (" + modeLabel(s.getMode()) + ")").withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
			}
			gui.pose().popPose();
		}
	}

}