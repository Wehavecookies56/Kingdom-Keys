package online.kingdomkeys.kingdomkeys.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.container.SynthesisBagContainer;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSUpgradeSynthesisBagPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SynthesisBagScreen extends AbstractContainerScreen<SynthesisBagContainer> {

	private static final String textureBase = KingdomKeys.MODID + ":textures/gui/synthesis_bag_";
	int[] texHeight = { 140, 176, 212 };
	int bagLevel = 0;
	HiddenButton upgrade;

	public SynthesisBagScreen(SynthesisBagContainer container, Inventory playerInv, Component title) {
		super(container, playerInv, title);
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		CompoundTag nbt = Minecraft.getInstance().player.getInventory().getSelected().getOrCreateTag();
		bagLevel = nbt.getInt("level");
		this.imageHeight = texHeight[bagLevel];
		this.imageWidth = 193;
		addRenderableWidget(upgrade = new HiddenButton((width - imageWidth) / 2 + imageWidth - 20, (height / 2) - (imageHeight / 2) + 17, 18, 18, (e) -> {
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
	public void render(PoseStack matrixStack, int x, int y, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, x, y, partialTicks);
		this.renderTooltip(matrixStack, x, y);
		List<Component> list = new ArrayList<Component>();
		upgrade.visible = bagLevel < 2;
		
		if(upgrade.visible) {
			if (x >= upgrade.getX() && x <= upgrade.getX() + upgrade.getWidth()) {
				if (y >= upgrade.getY() && y <= upgrade.getY() + upgrade.getHeight()) {
					list.add(Component.translatable("gui.synthesisbag.upgrade"));					
					list.add(Component.translatable(ChatFormatting.YELLOW+ Component.translatable("gui.synthesisbag.munny").getString()+": "+Utils.getBagCosts(bagLevel)));
					if(ModCapabilities.getPlayer(minecraft.player).getMunny() < Utils.getBagCosts(bagLevel)) {
						list.add(Component.translatable(ChatFormatting.RED+ Component.translatable("gui.synthesisbag.notenoughmunny").getString()));
					}
					renderComponentTooltip(matrixStack, list, x, y);
				}
			}
		}
		
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		String s = title.getString()+ " LV." + (bagLevel + 1);
		font.draw(matrixStack, s, imageWidth / 2 -17 / 2 - font.width(s) / 2, 5, 4210752);
		// font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2,
		// 4210752);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, new ResourceLocation(textureBase + bagLevel + ".png"));

		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		blit(matrixStack, xPos, yPos, 0, 0, imageWidth, imageHeight);

		/*
		 * for (Slot slot : container.inventorySlots) { if (slot instanceof
		 * SlotItemHandler && !slot.getHasStack()) { ItemStack stack = new
		 * ItemStack(ModBlocks.blazingOre); int x = guiLeft + slot.xPos; int y = guiTop
		 * + slot.yPos; //mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
		 * mc.fontRenderer.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666); } }
		 */
	}

}
