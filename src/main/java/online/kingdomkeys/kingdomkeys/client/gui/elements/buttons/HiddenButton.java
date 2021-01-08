package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

public class HiddenButton extends Button {
	public HiddenButton(int x, int y, int width, int height, IPressable onPress) {
		super(x, y, width, height, new TranslationTextComponent(""), onPress);
	}

	@Override
	public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		
	}
}