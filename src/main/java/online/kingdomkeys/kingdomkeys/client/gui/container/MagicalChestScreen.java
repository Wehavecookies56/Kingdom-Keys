package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.MagicalChestContainer;
import org.jetbrains.annotations.NotNull;

public class MagicalChestScreen extends AbstractContainerScreen<MagicalChestContainer> {

	private static final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/magical_chest.png");

    public MagicalChestScreen(MagicalChestContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageHeight = 186;

    }

    @Override
    public void render(@NotNull GuiGraphics gui, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(gui);
        super.render(gui, p_render_1_, p_render_2_, p_render_3_);
        this.renderTooltip(gui, p_render_1_, p_render_2_);
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
    	gui.drawString(font, this.title.getString(), 8.0F, 6.0F, 4210752, false);
        gui.drawString(font, this.playerInventoryTitle.getString(), 8.0F, (float)(this.imageHeight - 93), 4210752, false);
        //super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    }

	@Override
	protected void renderBg(GuiGraphics gui, float partialTicks, int x, int y) {
		Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		gui.blit(texture, xPos, yPos, 0, 0, imageWidth, imageHeight);
	}

}
