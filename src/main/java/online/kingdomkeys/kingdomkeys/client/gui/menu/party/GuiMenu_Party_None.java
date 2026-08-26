package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_None extends MenuBackground {
	
	MenuButton back, create, join;
	WorldData worldData;

	Party party;

	//Not in party
	//0 = not in party
	//1 = creating (create)
	//2 = Looking for party (join)
	//In party
	//3 = Leader view
	//4 = Member view
	
	public GuiMenu_Party_None() {
		super(Strings.Gui_Menu_Party, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			PacketHandler.sendToServer(new CSOpenMenu());
			break;
		case "create":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Create());
			break;
		case "join":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Join());
			break;
		
		}
		
		updateButtons();
	}

	private void updateButtons() {
		create.visible = true;
		join.visible = true;
		back.visible = true;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(create = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Strings.Gui_Menu_Party_Create, ButtonType.BUTTON, true, (e) -> { action("create"); }));
		addRenderableWidget(join = new MenuButton((int) buttonPosX, button_statsY + (18), (int) buttonWidth, Strings.Gui_Menu_Party_Join, ButtonType.BUTTON, true, (e) -> { action("join"); }));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Strings.Gui_Menu_Back, ButtonType.BUTTON, true, (e) -> { action("back"); }));
	
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		drawParty(worldData, gui);
	}
	
}
