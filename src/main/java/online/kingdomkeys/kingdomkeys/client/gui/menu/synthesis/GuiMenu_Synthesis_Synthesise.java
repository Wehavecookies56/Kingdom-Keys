package online.kingdomkeys.kingdomkeys.client.gui.menu.synthesis;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class GuiMenu_Synthesis_Synthesise extends GuiMenu_Background {
		
	GuiMenuButton keyblades, items, back;
	
		
	public GuiMenu_Synthesis_Synthesise() {
		super(Strings.Gui_Synthesis_Synthesise, new Color(0,255,0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		switch(string) {
		case "keyblades":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Synthesis_Synthesise_Keyblades());
			break;
		case "items":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Synthesis_Synthesise_Items());
			break;
		case "back":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Synthesis());
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {

	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addButton(keyblades = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Keyblades), ButtonType.BUTTON, (e) -> { action("keyblades"); }));
		addButton(items = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Items), ButtonType.BUTTON, (e) -> { action("items"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (2 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	/*@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		
		int buttonX = (int)(width*0.25);
		
		
	}*/
	
}
