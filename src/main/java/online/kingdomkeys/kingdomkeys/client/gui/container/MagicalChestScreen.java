package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CheckboxButton;
import online.kingdomkeys.kingdomkeys.container.MagicalChestContainer;
import online.kingdomkeys.kingdomkeys.container.PedestalContainer;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPedestalConfig;

public class MagicalChestScreen extends ContainerScreen<MagicalChestContainer> {

	private static final String texture = KingdomKeys.MODID+":textures/gui/magical_chest.png";

    public MagicalChestScreen(MagicalChestContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.ySize = 186;

    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    	Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(texture));

        int xPos = (width - xSize) / 2;
		int yPos = (height / 2) - (ySize / 2);
		blit(xPos, yPos, 0, 0, xSize, ySize);

    }
}
