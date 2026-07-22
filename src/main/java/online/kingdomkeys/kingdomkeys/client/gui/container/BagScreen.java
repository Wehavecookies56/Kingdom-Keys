package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.BagItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.menu.BagMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSUpgradeBagPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BagScreen extends AbstractContainerScreen<BagMenu> {

	private static final int[] TEX_HEIGHT = {140, 176, 212, 248};

	private int bagLevel = 0;

	private HiddenButton upgradeButton;

	public BagScreen(BagMenu container, net.minecraft.world.entity.player.Inventory playerInv, Component title) {
		super(container, playerInv, title);
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		bagLevel = menu.bag.getOrDefault(ModComponents.BAG_LEVEL, 0);
		imageHeight = TEX_HEIGHT[bagLevel];
		imageWidth = 193;

		super.init();

		addRenderableWidget(upgradeButton = new HiddenButton(leftPos + imageWidth - 20, topPos + 17, 18, 18, (e) -> upgrade()));
	}

	private void upgrade() {
		if (bagLevel < 3) {
			if (PlayerData.get(minecraft.player).getMunny() >= Utils.getBagCosts(bagLevel)) {
				PacketHandler.sendToServer(new CSUpgradeBagPacket());
				onClose();
			}
		}
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type) {
		if (slot != null && slot.hasItem() && ItemStack.isSameItemSameComponents(slot.getItem(), menu.bag)) {
			return;
		}

		super.slotClicked(slot, slotId, mouseButton, type);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		ItemStack stack = menu.bag;

		if (stack.isEmpty() || !(stack.getItem() instanceof BagItem)) {
			onClose();
			return;
		}
		renderBackground(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		renderTooltip(gui, mouseX, mouseY);
		upgradeButton.visible = bagLevel < 3;

		if (upgradeButton.visible && mouseX >= upgradeButton.getX() && mouseX <= upgradeButton.getX() + upgradeButton.getWidth() && mouseY >= upgradeButton.getY() && mouseY <= upgradeButton.getY() + upgradeButton.getHeight()) {
			List<Component> list = new ArrayList<>();
			list.add(Component.translatable("gui.synthesisbag.upgrade"));
			list.add(Component.literal(ChatFormatting.YELLOW + Component.translatable("gui.synthesisbag.munny").getString() + ": " + Utils.getBagCosts(bagLevel)));

			if (PlayerData.get(minecraft.player).getMunny() < Utils.getBagCosts(bagLevel)) {
				list.add(Component.literal(ChatFormatting.RED + Component.translatable("gui.synthesisbag.notenoughmunny").getString()));
			}

			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		String s = title.getString() + " LV." + (bagLevel + 1);
		gui.drawString(font, s, imageWidth / 2 - 17 / 2 - font.width(s) / 2, 5, 4210752, false);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		int xPos = leftPos;
		int yPos = topPos;

		String textureBase = "textures/gui/synthesis_bag_";
		gui.blit(KingdomKeys.rl(textureBase + bagLevel + ".png"), xPos, yPos, 0, 0, imageWidth, imageHeight);
	}
}