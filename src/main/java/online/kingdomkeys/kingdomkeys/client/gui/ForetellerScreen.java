package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.ShopScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.lib.DuelDifficulty;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStartDuel;
import online.kingdomkeys.kingdomkeys.network.cts.CSStartTraining;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ForetellerShop;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ForetellerScreen extends MenuBackground implements IPlayerDataRequester {
	private static final int BUTTON_GAP = 18;
	private static final int BACK_GAP = 28;

	/** Which list is up. The submenus are this screen redrawn, not screens of their own. */
	private enum Page { MAIN, TRAIN, DUEL }

	private final Union union;
	private Page page = Page.MAIN;

	public ForetellerScreen(PlayerData playerData, Union union) {
		super(union.getTranslationKey(), new Color(union.getColour()));
		this.union = union;
		this.playerData = playerData;
		drawPlayerInfo = true;
	}

	public static String shopFor(Union union) {
		return ForetellerShop.shopFor(union);
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();

		Minecraft mc = Minecraft.getInstance();

		float topBarHeight = (float) height * 0.17F;
		int buttonY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		switch (page) {
			case MAIN -> {
				addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Foreteller_Redeem), ButtonType.BUTTON, e -> {
					mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);
					mc.setScreen(new ShopScreen(playerData, shopFor(union), this));
				}));

				buttonY += BUTTON_GAP;

				addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Foreteller_Train), ButtonType.BUTTON, e -> {
					mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);
					page = Page.TRAIN;
					rebuildWidgets();
				}));

				buttonY += BUTTON_GAP;

				addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Foreteller_Duel), ButtonType.BUTTON, e -> {
					mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);
					page = Page.DUEL;
					rebuildWidgets();
				}));
			}

			case TRAIN -> {
				List<RoomEncounter> lessons = ModJsonRegistries.TRAINING_ENCOUNTER.get().getValues().stream()
						.sorted(Comparator.comparingInt(RoomEncounter::getExperience))
						.toList();

				for (RoomEncounter lesson : lessons) {
					String level = lesson.isDynamicLevel() ? Utils.translateToLocal(Strings.Gui_Level_Dynamic) : Component.translatable(Strings.Gui_Duel_Level, lesson.getLevel()).getString();
					String reward = Component.translatable(Strings.Gui_Training_Reward, lesson.getExperience(), lesson.getLux()).getString();

					MenuButton button = new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Component.translatable(lesson.getTranslationKey()).getString(), ButtonType.BUTTON, e -> {
						mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);
						mc.setScreen(null);
						PacketHandler.sendToServer(new CSStartTraining(lesson.getRegistryName()));
					});
					button.setTip(level + " - " + reward);
					addRenderableWidget(button);

					buttonY += BUTTON_GAP;
				}

				buttonY += BACK_GAP;

				addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, e -> {
					mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0F, 1.0F);
					page = Page.MAIN;
					rebuildWidgets();
				}));
			}

			case DUEL -> {
				for (DuelDifficulty difficulty : DuelDifficulty.values()) {
					String level = difficulty.isDynamic() ? Utils.translateToLocal(Strings.Gui_Level_Dynamic) : Component.translatable(Strings.Gui_Duel_Level, difficulty.getLevel()).getString();

					MenuButton button = new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Component.translatable(difficulty.getTranslationKey()).getString(), ButtonType.BUTTON, e -> {
						mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);
						mc.setScreen(null);
						PacketHandler.sendToServer(new CSStartDuel(difficulty));
					});
					button.setTip(level);
					addRenderableWidget(button);

					buttonY += BUTTON_GAP;
				}

				buttonY += BACK_GAP;

				addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, e -> {
					mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0F, 1.0F);
					page = Page.MAIN;
					rebuildWidgets();
				}));
			}
		}
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
