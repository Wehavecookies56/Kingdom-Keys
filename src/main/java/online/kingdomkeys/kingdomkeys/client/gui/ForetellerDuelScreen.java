package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.DuelDifficulty;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStartDuel;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;

public class ForetellerDuelScreen extends MenuBackground implements IPlayerDataRequester {
	private static final int BUTTON_GAP = 18;

	private static final int BACK_GAP = 28;

	private final Union union;
	private final ForetellerScreen parent;

	public ForetellerDuelScreen(PlayerData playerData, Union union, ForetellerScreen parent) {
		super(union.getTranslationKey(), new Color(union.getColour()));
		this.union = union;
		this.parent = parent;
		this.playerData = playerData;
		drawPlayerInfo = true;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();

		float topBarHeight = (float) height * 0.17F;
		int buttonY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for (DuelDifficulty difficulty : DuelDifficulty.values()) {
			String label = Component.translatable(difficulty.getTranslationKey()).getString() + " - " + Component.translatable(Strings.Gui_Duel_Level, difficulty.getLevel()).getString();

			addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, label, ButtonType.BUTTON, e -> start(difficulty)));
			buttonY += BUTTON_GAP;
		}

		buttonY += BACK_GAP - BUTTON_GAP;

		addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, e -> back()));
	}

	private void start(DuelDifficulty difficulty) {
		Minecraft mc = Minecraft.getInstance();
		mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);

		mc.setScreen(null);
		PacketHandler.sendToServer(new CSStartDuel(difficulty));
	}

	private void back() {
		Minecraft mc = Minecraft.getInstance();
		mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0F, 1.0F);
		mc.setScreen(parent != null ? parent : new ForetellerScreen(playerData, union));
	}

	@Override
	public void updatePlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
