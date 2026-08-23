package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleJoin;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StruggleJoin extends MenuBackground {
	BlockPos boardPos;

	MenuButton back;

	WorldData worldData;

	private final List<MenuButton> matchButtons = new ArrayList<>();

	private final List<String> shown = new ArrayList<>();

	public StruggleJoin(BlockPos pos) {
		super(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Join_Title), new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
		boardPos = pos;
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new MenuStruggle(boardPos));
			break;
		}
	}

	private void join(String struggleName) {
		Struggle s = WorldData.getClient().getStruggleFromName(struggleName);

		if(s == null || s.getParticipants().size() >= s.getSize() || s.hasParticipant(minecraft.player.getUUID())) {
			return;
		}

		PacketHandler.sendToServer(new CSStruggleJoin(s.getName()));

		minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
		minecraft.setScreen(null);
	}

	private void refreshMatches() {
		worldData = WorldData.getClient();

		List<Struggle> listed = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for(Struggle s : worldData.getStruggles()) {
			if(s != null) {
				listed.add(s);
				labels.add("[" + s.getParticipants().size() + "/" + s.getSize() + "] " + s.getName());
			}
		}

		if(labels.equals(shown)) {
			return;
		}

		for(MenuButton button : matchButtons) {
			removeWidget(button);
		}

		matchButtons.clear();
		shown.clear();
		shown.addAll(labels);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 0; i < listed.size(); i++) {
			String name = listed.get(i).getName();

			MenuButton button = new MenuButton((int)(width * 0.3F), button_statsY + (i * 18), (int)(buttonWidth * 2), labels.get(i), ButtonType.BUTTON, (e) -> join(name));

			matchButtons.add(button);
			addRenderableWidget(button);
		}
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		clearWidgets();

		matchButtons.clear();
		shown.clear();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));

		refreshMatches();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		refreshMatches();
	}
}
