package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.ContainerSynthesisBag;

public class GuiSynthesisBag extends ContainerScreen<ContainerSynthesisBag> {

	private static final ResourceLocation textureS = new ResourceLocation(KingdomKeys.MODID+":textures/gui/synthesis_bag_s.png");
	private static final ResourceLocation textureM = new ResourceLocation(KingdomKeys.MODID+":textures/gui/synthesis_bag_m.png");
	private static final ResourceLocation textureL = new ResourceLocation(KingdomKeys.MODID+":textures/gui/synthesis_bag_l.png");

	public GuiSynthesisBag(ContainerSynthesisBag container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = I18n.format("Synthesis Bag");
		font.drawString(s, xSize / 2 - font.getStringWidth(s) / 2, -11, 4210752);
		//font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		CompoundNBT nbt = playerInventory.getCurrentItem().getOrCreateTag();
		int bagLevel = nbt.getInt("level");
		mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID+":textures/gui/synthesis_bag_"+bagLevel+".png"));

		int k = (width - xSize) / 2;
		int l = (height - 200) / 2;
		blit(k, l, 0, 0, xSize, 250);

		/*for (Slot slot : container.inventorySlots) {
			if (slot instanceof SlotItemHandler && !slot.getHasStack()) {
				ItemStack stack = new ItemStack(ModBlocks.blazingOre);
				int x = guiLeft + slot.xPos;
				int y = guiTop + slot.yPos;
				//mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
				mc.fontRenderer.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666);
			}
		}*/
	}

}
