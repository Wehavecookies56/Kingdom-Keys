package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.GummiEditorContainer;

public class GummiEditorScreen extends ContainerScreen<GummiEditorContainer> {

	private static final String texture = KingdomKeys.MODID + ":textures/gui/gummi_editor.png";

	public GummiEditorScreen(GummiEditorContainer container, PlayerInventory inventory, ITextComponent title) {
		super(container, inventory, title);
		this.ySize = 186;
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
	public void tick() {
		super.tick();
	}

	@Override
	public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredTooltip(matrixStack, p_render_1_, p_render_2_);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		this.font.drawString(matrixStack, this.title.getString(), 8.0F, 6.0F, 4210752);
		this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);

		// super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(texture));

		int xPos = (width - xSize) / 2;
		int yPos = (height / 2) - (ySize / 2);
		blit(matrixStack, xPos, yPos, 0, 0, xSize, ySize);

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
