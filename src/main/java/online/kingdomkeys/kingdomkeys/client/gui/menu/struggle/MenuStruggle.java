package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
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
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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

	final ResourceLocation texture = KingdomKeys.rl("textures/gui/menu/menu_button.png");

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
		super.width = width;
		super.height = height;
		super.init();
		float topBarHeight = (float) height * 0.17F;
		int start = (int)topBarHeight + 5;
		int pos = 0;

		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		addRenderableWidget(create = new MenuButton((int) buttonPosX, start, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Create_Button), ButtonType.BUTTON, true, (e) -> {action(buttons.CREATE);}));
		addRenderableWidget(join = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Join_Button), ButtonType.BUTTON, true, (e) -> {action(buttons.JOIN);}));
		addRenderableWidget(settings = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Settings_Button), ButtonType.BUTTON, true, (e) -> {action(buttons.SETTINGS);}));
		addRenderableWidget(ready = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Ready), ButtonType.BUTTON, true, (e) -> {action(buttons.READY);}));
		addRenderableWidget(delete = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Delete_Button), ButtonType.BUTTON, true, (e) -> {action(buttons.DELETE);}));
		addRenderableWidget(leave = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Leave_Button), ButtonType.BUTTON, true, (e) -> {action(buttons.LEAVE);}));

		updateButtons();
	}

	private void updateButtons() {
		Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
		boolean isParticipant = s != null && s.hasParticipant(minecraft.player.getUUID());
		boolean isOwner = s != null && s.getOwner() != null && s.getOwner().getUUID().equals(minecraft.player.getUUID());

		create.visible = (s == null);
		join.visible = (s != null) && !isParticipant;
		settings.visible = isOwner;
		delete.visible = isOwner;
		// Owner uses Delete to end the whole match; anyone else who joined can just Leave instead.
		leave.visible = isParticipant && !isOwner && !s.isInProgress();

		// Only usable once the arena corners are set, there are at least 2 combatants, and it's not
		// already fighting - matches the conditions StruggleHandler checks server-side to start.
		ready.visible = isParticipant && !s.isInProgress() && s.isConfigured() && s.getParticipants().size() >= 2;
		if (ready.visible) {
			boolean isReady = s.getParticipant(minecraft.player.getUUID()).isReady();
			ready.setMessage(Component.literal(isReady ? Utils.translateToLocal(Strings.Gui_Menu_Struggle_Cancel_Ready) : Utils.translateToLocal(Strings.Gui_Menu_Struggle_Ready)));
		}

		// Pack every currently-visible button one after another (no gaps left behind by hidden ones).
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

	private String modeLabel(Struggle.Mode mode) {
		return Utils.translateToLocal(Strings.Gui_Menu_Struggle + "." + mode.name().toLowerCase());
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		updateButtons();
		drawStruggle(WorldData.getClient(), gui, boardPos);

		Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
		if (s != null) {
			// Same spot/style Party uses for "[members/size] party name" - here also showing the mode.
			gui.drawString(minecraft.font, Component.literal("[" + s.getParticipants().size() + "/" + s.getSize() + "] " + s.getName() + " (" + modeLabel(s.getMode()) + ")").withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);

			boolean isParticipant = s.hasParticipant(minecraft.player.getUUID());
			if (isParticipant && !ready.visible && !s.isInProgress()) {
				String reason;
				if (!s.isConfigured()) {
					reason = "Ask the owner to set the arena corners in Settings first.";
				} else if (s.getParticipants().size() < 2) {
					reason = "Waiting for at least one more player to join.";
				} else {
					reason = "";
				}
				if (!reason.isEmpty()) {
					float topBarHeight = (float) height * 0.17F;
					int start = (int) topBarHeight + 5;
					int visibleCount = 0;
					for (MenuButton button : new MenuButton[]{create, join, settings, ready, delete, leave}) {
						if (button.visible) visibleCount++;
					}
					gui.drawString(minecraft.font, reason, (int) ((float) width * 0.03F), start + 18 * visibleCount, 0xFF5555);
				}
			}
		}
	}

}
