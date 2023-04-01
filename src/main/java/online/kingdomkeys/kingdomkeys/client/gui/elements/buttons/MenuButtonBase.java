package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MenuButtonBase extends Button {

	boolean selected = false;
	public MenuButtonBase(int x, int y, int width, int height, String text, OnPress onPress) {
		super(new Builder(Component.translatable(text),onPress).bounds(x, y, width, height));
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	
}
