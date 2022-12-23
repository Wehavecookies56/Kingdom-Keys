package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class MenuButtonBase extends Button {

	boolean selected = false;
	public MenuButtonBase(int xIn, int yIn, int width, int height, String text, OnPress onPress) {
		super(xIn, yIn, width, height, new TranslatableComponent(text), onPress);
		
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	
}
