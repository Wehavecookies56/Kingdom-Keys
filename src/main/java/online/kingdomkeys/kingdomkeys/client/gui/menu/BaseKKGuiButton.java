package online.kingdomkeys.kingdomkeys.client.gui.menu;

import net.minecraft.client.gui.widget.button.Button;

public class BaseKKGuiButton extends Button {

	boolean selected = false;
	public BaseKKGuiButton(int widthIn, int heightIn, int width, int height, String text, IPressable onPress) {
		super(widthIn, heightIn, width, height, text, onPress);
		// TODO Auto-generated constructor stub
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	
}
