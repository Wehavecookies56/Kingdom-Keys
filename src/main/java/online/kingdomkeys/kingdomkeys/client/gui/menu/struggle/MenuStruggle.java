package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;
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
		CREATE, JOIN, SETTINGS
	}

	MenuButton create, join, settings;

	final ResourceLocation texture = KingdomKeys.rl("textures/gui/menu/menu_button.png");

	protected void action(buttons buttonID) {
		switch (buttonID) {
			case CREATE -> minecraft.setScreen(new StruggleCreate(boardPos));
			case JOIN -> minecraft.setScreen(new StruggleJoin(boardPos));
			case SETTINGS -> minecraft.setScreen(new StruggleSettings(boardPos));

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

		updateButtons();
	}

	private void updateButtons() {
		Struggle s = WorldData.getClient().getStruggleFromBlockPos(boardPos);
		create.visible = (s == null);
		join.visible = (s != null) && !s.hasParticipant(minecraft.player.getUUID());
		settings.visible = (s != null) && s.getOwner() != null && s.getOwner().getUUID().equals(minecraft.player.getUUID());
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		Party.Member m = new Party.Member(this.player.getUUID(), this.player.getDisplayName().getString());
		drawPlayer(gui, null,0, m);
	}
}