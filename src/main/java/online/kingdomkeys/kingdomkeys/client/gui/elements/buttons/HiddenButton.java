package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.gui.widget.button.Button;

public class HiddenButton extends Button {
	public HiddenButton(int x, int y, int width, int height, IPressable onPress) {
		super(x, y, width, height, "", onPress);
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		
	}
}