package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class MenuButtonBase extends Button {

	boolean selected = false;
	public MenuButtonBase(int widthIn, int heightIn, int width, int height, String text, IPressable onPress) {
		super(widthIn, heightIn, width, height, new TranslationTextComponent(text), onPress);
		
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	
}
