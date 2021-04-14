package online.kingdomkeys.kingdomkeys.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.container.SynthesisBagContainer;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSUpgradeSynthesisBagPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SynthesisBagScreen extends ContainerScreen<SynthesisBagContainer> {

	private static final String textureBase = KingdomKeys.MODID + ":textures/gui/synthesis_bag_";
	int[] texHeight = { 140, 176, 212 };
	int bagLevel = 0;
	HiddenButton upgrade;

	public SynthesisBagScreen(SynthesisBagContainer container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		CompoundNBT nbt = playerInventory.getCurrentItem().getOrCreateTag();
		bagLevel = nbt.getInt("level");
		this.ySize = texHeight[bagLevel];
		this.xSize = 193;
		addButton(upgrade = new HiddenButton((width - xSize) / 2 + xSize - 20, (height / 2) - (ySize / 2) + 17, 18, 18, (e) -> {
			upgrade();
		}));
		
		super.init();
	}
	
	private void upgrade() {
		if (bagLevel < 2) {
			if(ModCapabilities.getPlayer(minecraft.player).getMunny() >= Utils.getBagCosts(bagLevel)) {
				PacketHandler.sendToServer(new CSUpgradeSynthesisBagPacket());
				onClose();
			}
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, x, y, partialTicks);
		this.renderHoveredTooltip(matrixStack, x, y);
		List<ITextComponent> list = new ArrayList<ITextComponent>();
		upgrade.visible = bagLevel < 2;
		
		if(upgrade.visible) {
			if (x >= upgrade.x && x <= upgrade.x + upgrade.getWidth()) {
				if (y >= upgrade.y && y <= upgrade.y + upgrade.getHeight()) {
					list.add(new TranslationTextComponent("gui.synthesisbag.upgrade"));					
					list.add(new TranslationTextComponent(TextFormatting.YELLOW+ new TranslationTextComponent("gui.synthesisbag.munny").getString()+": "+Utils.getBagCosts(bagLevel)));
					if(ModCapabilities.getPlayer(minecraft.player).getMunny() < Utils.getBagCosts(bagLevel)) {
						list.add(new TranslationTextComponent(TextFormatting.RED+ new TranslationTextComponent("gui.synthesisbag.notenoughmunny").getString()));
					}
					func_243308_b(matrixStack, list, x, y);
				}
			}
		}
		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		String s = I18n.format("Synthesis Bag LV." + (bagLevel + 1));
		font.drawString(matrixStack, s, xSize / 2 -17 / 2 - font.getStringWidth(s) / 2, 5, 4210752);
		// font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2,
		// 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(textureBase + bagLevel + ".png"));

		int xPos = (width - xSize) / 2;
		int yPos = (height / 2) - (ySize / 2);
		blit(matrixStack, xPos, yPos, 0, 0, xSize, ySize);

		/*
		 * for (Slot slot : container.inventorySlots) { if (slot instanceof
		 * SlotItemHandler && !slot.getHasStack()) { ItemStack stack = new
		 * ItemStack(ModBlocks.blazingOre); int x = guiLeft + slot.xPos; int y = guiTop
		 * + slot.yPos; //mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
		 * mc.fontRenderer.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666); } }
		 */
	}

}
