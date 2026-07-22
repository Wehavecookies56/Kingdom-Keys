package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleReady;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MenuStruggle extends MenuBackground {
	BlockPos boardPos;

	public MenuStruggle(BlockPos pos) {
		super("Menu", new Color(252, 173, 3));
		minecraft = Minecraft.getInstance();
		boardPos = pos;
	}
	

	public enum buttons {
		CREATE, JOIN, SETTINGS, READY
    }

	MenuButton create, join, settings, ready;

	final ResourceLocation texture = KingdomKeys.rl("textures/gui/menu/menu_button.png");

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case CREATE -> minecraft.setScreen(new StruggleCreate(boardPos));
			case JOIN -> minecraft.setScreen(new StruggleJoin(boardPos));
			case SETTINGS -> minecraft.setScreen(new StruggleSettings(boardPos));
			case READY -> {
				Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
				if (s != null) {
					PacketHandler.sendToServer(new CSStruggleReady(s.getName()));
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

		addRenderableWidget(create = new MenuButton((int) buttonPosX, start, (int) buttonWidth, "Create match", ButtonType.BUTTON, true, (e) -> {action(buttons.CREATE);}));
		addRenderableWidget(join = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, "Join match", ButtonType.BUTTON, true, (e) -> {action(buttons.JOIN);}));
		addRenderableWidget(settings = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, "Struggle Settings", ButtonType.BUTTON, true, (e) -> {action(buttons.SETTINGS);}));
		addRenderableWidget(ready = new MenuButton((int) buttonPosX, start + 18 * ++pos, (int) buttonWidth, "Ready", ButtonType.BUTTON, true, (e) -> {action(buttons.READY);}));

		updateButtons();
	}

	private void updateButtons() {
		Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
		boolean isParticipant = s != null && s.hasParticipant(minecraft.player.getUUID());

		create.visible = (s == null);
		join.visible = (s != null) && !isParticipant;
		settings.visible = (s != null) && s.getOwner() != null && s.getOwner().getUUID().equals(minecraft.player.getUUID());

		// Only usable once the arena corners are set, there are at least 2 combatants, and it's not
		// already fighting - matches the conditions StruggleHandler checks server-side to start.
		ready.visible = isParticipant && !s.isInProgress() && s.isConfigured() && s.getParticipants().size() >= 2;
		if (ready.visible) {
			boolean isReady = s.getParticipant(minecraft.player.getUUID()).isReady();
			ready.setMessage(net.minecraft.network.chat.Component.literal(isReady ? "Cancel Ready" : "Ready"));
		}
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		updateButtons();
		drawStruggle(WorldData.getClient(), gui, boardPos);
	}

}
