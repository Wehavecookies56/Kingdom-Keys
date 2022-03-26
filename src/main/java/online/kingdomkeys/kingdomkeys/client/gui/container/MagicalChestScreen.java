package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.MagicalChestContainer;

public class MagicalChestScreen extends AbstractContainerScreen<MagicalChestContainer> {

	private static final String texture = KingdomKeys.MODID+":textures/gui/magical_chest.png";

    public MagicalChestScreen(MagicalChestContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageHeight = 186;

    }

    @Override
    public void render(PoseStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
        this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
    	this.font.draw(matrixStack, this.title.getString(), 8.0F, 6.0F, 4210752);
        this.font.draw(matrixStack, this.playerInventoryTitle.getString(), 8.0F, (float)(this.imageHeight - 93), 4210752);
        //super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    }

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindForSetup(new ResourceLocation(texture));

        int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		blit(matrixStack, xPos, yPos, 0, 0, imageWidth, imageHeight);
	}
}
