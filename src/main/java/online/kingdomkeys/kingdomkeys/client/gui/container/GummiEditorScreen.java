package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.GummiEditorContainer;
import org.jetbrains.annotations.NotNull;

public class GummiEditorScreen extends AbstractContainerScreen<GummiEditorContainer> {

	private static final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/gummi_editor.png");

	public GummiEditorScreen(GummiEditorContainer container, Inventory inventory, Component title) {
		super(container, inventory, title);
		this.imageHeight = 186;
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground(gui);
		super.render(gui, p_render_1_, p_render_2_, p_render_3_);
		this.renderTooltip(gui, p_render_1_, p_render_2_);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
		gui.drawString(font, this.title.getString(), 8.0F, 6.0F, 4210752, false);
		gui.drawString(font, this.playerInventoryTitle.getString(), 8.0F, (float) (this.imageHeight - 96 + 2), 4210752, false);

		// super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		gui.blit(texture, xPos, yPos, 0, 0, imageWidth, imageHeight);

	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		/*
		 * rotationSpeedSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_,
		 * p_mouseReleased_5_); bobSpeedSlider.mouseReleased(p_mouseReleased_1_,
		 * p_mouseReleased_3_, p_mouseReleased_5_);
		 * scaleSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_,
		 * p_mouseReleased_5_); heightSlider.mouseReleased(p_mouseReleased_1_,
		 * p_mouseReleased_3_, p_mouseReleased_5_);
		 */
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
}
