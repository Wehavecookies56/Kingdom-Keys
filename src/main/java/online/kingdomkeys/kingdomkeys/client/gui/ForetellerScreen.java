package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.ShopScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ForetellerShop;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.Color;

public class ForetellerScreen extends MenuBackground implements IPlayerDataRequester {
	private final Union union;

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

		float topBarHeight = (float) height * 0.17F;
		int buttonY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(new MenuButton((int) buttonPosX, buttonY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Foreteller_Redeem), ButtonType.BUTTON, e -> {
			Minecraft mc = Minecraft.getInstance();
			mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0F, 1.0F);
			mc.setScreen(new ShopScreen(playerData, shopFor(union), this));
		}));
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
