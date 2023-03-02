package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MenuButtonBase extends Button {

	boolean selected = false;
	public MenuButtonBase(int widthIn, int heightIn, int width, int height, String text, OnPress onPress, CreateNarration narration) {
		super(widthIn, heightIn, width, height, Component.translatable(text), onPress, narration);
		
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	
}
