package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class HiddenButton extends Button {
	public HiddenButton(int x, int y, int width, int height, OnPress onPress) {
		super(new Builder(Component.translatable(""),onPress).bounds(x,y, width, height));		
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int p_render_1_, int p_render_2_, float p_render_3_) {
		
	}
}