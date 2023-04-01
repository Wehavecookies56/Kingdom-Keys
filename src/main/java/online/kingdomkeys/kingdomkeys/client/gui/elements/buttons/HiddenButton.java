package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.Builder;
import net.minecraft.network.chat.Component;

public class HiddenButton extends Button {
	public HiddenButton(int x, int y, int width, int height, OnPress onPress) {
		super(new Builder(Component.translatable(""),onPress).bounds(x,y, width, height));		
	}

	@Override
	public void render(PoseStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		
	}
}