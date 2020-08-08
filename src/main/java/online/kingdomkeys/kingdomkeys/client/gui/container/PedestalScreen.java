package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.PedestalContainer;

public class PedestalScreen extends ContainerScreen<PedestalContainer> {

	private static final String texture = KingdomKeys.MODID+":textures/gui/pedestal.png";

    public PedestalScreen(PedestalContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
		//Minecraft.getInstance().getMainWindow().getHeight();
		this.ySize = 186;

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
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
