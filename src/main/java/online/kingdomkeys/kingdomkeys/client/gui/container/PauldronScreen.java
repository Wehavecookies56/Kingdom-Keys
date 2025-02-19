package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.PauldronContainer;

import java.util.Set;

public class PauldronScreen extends AbstractContainerScreen<PauldronContainer> {

    PauldronContainer menu;

    private float xMouse;
    private float yMouse;

    public PauldronScreen(PauldronContainer menu, Inventory pPlayerInventory, Component title) {
        super(menu, pPlayerInventory, title);
        minecraft = Minecraft.getInstance();
        this.menu = menu;
    }

    @Override
    protected void init() {
        this.imageHeight = 166;
        this.imageWidth = 176;

        super.init();
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, 77, this.titleLabelY, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        this.xMouse = (float)pMouseX;
        this.yMouse = (float)pMouseY;
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        ItemStack stack = menu.pauldron;
        if (!minecraft.player.getInventory().hasAnyOf(Set.of(stack.getItem()))) {
            onClose();
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int xPos = (width - imageWidth) / 2;
        int yPos = (height / 2) - (imageHeight / 2);
        guiGraphics.blit(new ResourceLocation(KingdomKeys.MODID, "textures/gui/pauldron_inv.png"), xPos, yPos, 0, 0, imageWidth, imageHeight);
        LocalPlayer clone = new LocalPlayer(minecraft, minecraft.level, minecraft.getConnection(), minecraft.player.getStats(), minecraft.player.getRecipeBook(), false, false);
        clone.getInventory().armor.set(3, menu.pauldronInv.getStackInSlot(0));
        clone.getInventory().armor.set(2, menu.pauldronInv.getStackInSlot(1));
        clone.getInventory().armor.set(1, menu.pauldronInv.getStackInSlot(2));
        clone.getInventory().armor.set(0, menu.pauldronInv.getStackInSlot(3));
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, leftPos + 51, topPos + 70, 30, (float)(leftPos + 51) - this.xMouse, (float)(this.topPos + 75 - 50) - this.yMouse, clone);
    }
}
