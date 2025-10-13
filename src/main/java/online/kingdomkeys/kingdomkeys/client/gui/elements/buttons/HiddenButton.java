package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HiddenButton extends Button {
	ResourceLocation texture;
	int u, v;
	public HiddenButton(int x, int y, int width, int height, OnPress onPress) {
		super(new Builder(Component.literal(""),onPress).bounds(x,y, width, height));
	}

	public HiddenButton(int x, int y, int width, int height, ResourceLocation rl, int u, int v, OnPress onPress) {
		this(x,y, width, height, onPress);
		this.texture = rl;
		this.u = u;
		this.v = v;
	}

	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int p_render_1_, int p_render_2_, float p_render_3_) {
		if(texture != null){
			if (visible) {
				PoseStack matrixStack = guiGraphics.pose();
				matrixStack.pushPose();
				{
					RenderSystem.setShaderColor(1, 1, 1, 1);
					RenderSystem.enableBlend();
					RenderSystem.setShaderTexture(0, texture);

					guiGraphics.blit(texture, getX(), getY(), u, v, width, height);

					RenderSystem.disableBlend();
					RenderSystem.setShaderColor(1, 1, 1, 1);
				}
				matrixStack.popPose();
			}
		}
	}
}