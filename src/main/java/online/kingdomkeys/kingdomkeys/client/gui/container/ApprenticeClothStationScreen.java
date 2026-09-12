package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;
import online.kingdomkeys.kingdomkeys.menu.ApprenticeClothStationMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSApprenticeClothDesign;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApprenticeClothStationScreen extends AbstractContainerScreen<ApprenticeClothStationMenu> {
	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/gui/apprentice_cloth_station.png");

	/** The row of design buttons: where it starts, how far apart, and how big each one is. */
	private static final int DESIGN_X = 52, DESIGN_Y = 16;
	private static final int DESIGN_W = 18, DESIGN_H = 16;

	private ArmorStand previewStand;
	private ItemStack lastPreviewStack = ItemStack.EMPTY;

	private final Map<Integer, Button> designButtons = new LinkedHashMap<>();

	public ApprenticeClothStationScreen(ApprenticeClothStationMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 176;
		this.imageHeight = 186;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	protected void init() {
		super.init();
		ensurePreviewStand();

		designButtons.clear();

		for (int i = UnionApprenticeArmorItem.MIN_DESIGN; i <= UnionApprenticeArmorItem.MAX_DESIGN; i++) {
			final int design = i;
			Button button = Button.builder(Component.literal(String.valueOf(design)), b -> selectDesign(design)).bounds(leftPos + DESIGN_X + (design - 1) * DESIGN_W, topPos + DESIGN_Y, DESIGN_W, DESIGN_H).tooltip(Tooltip.create(Component.translatable("tooltip.kingdomkeys.apprentice.design", design))).build();

			designButtons.put(design, button);
			addRenderableWidget(button);
		}

		refreshDesignButtons();
	}

	private void selectDesign(int design) {
		PacketDistributor.sendToServer(new CSApprenticeClothDesign(menu.TE.getBlockPos(), design));
		menu.setSelectedDesign(design);
		refreshDesignButtons();
	}

	private void refreshDesignButtons() {
		int selected = menu.getSelectedDesign();
		designButtons.forEach((design, button) -> button.active = design != selected);
	}

	private void ensurePreviewStand() {
		if (previewStand == null && minecraft != null && minecraft.level != null) {
			previewStand = new ArmorStand(minecraft.level, 0, 0, 0);
			previewStand.setNoBasePlate(true);
			previewStand.setShowArms(false);
			previewStand.setInvisible(false);
		}
	}

	private void updatePreviewEquipment() {
		ensurePreviewStand();
		if (previewStand == null) {
			return;
		}

		ItemStack shown = menu.getSlot(ApprenticeClothStationMenu.MENU_OUTPUT).getItem();
		if (shown.isEmpty()) {
			shown = menu.getSlot(ApprenticeClothStationMenu.MENU_INPUT).getItem();
		}

		if (ItemStack.isSameItemSameComponents(shown, lastPreviewStack)) {
			return;
		}
		lastPreviewStack = shown.copy();

		previewStand.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
		previewStand.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
		previewStand.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
		previewStand.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);

		if (!shown.isEmpty() && shown.getItem() instanceof UnionApprenticeArmorItem armor) {
			EquipmentSlot slot = armor.getEquipmentSlot();
			previewStand.setItemSlot(slot, shown.copy());
		}
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		updatePreviewEquipment();
		refreshDesignButtons();

		super.render(gui, mouseX, mouseY, partialTick);
		this.renderTooltip(gui, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		gui.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		// Armor stand preview, in the gap between the input column and the secondary dyes
		if (previewStand != null) {
			int standX = leftPos + 114;
			int standY = topPos + 70;
			InventoryScreen.renderEntityInInventoryFollowsMouse(gui, standX - 20, standY - 55, standX + 20, standY + 5, 30, 0.0625F, mouseX, mouseY, previewStand);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		gui.drawString(this.font, this.title, imageWidth / 2 - minecraft.font.width(this.title) / 2, this.titleLabelY, 0x404040, false);
		gui.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);

		gui.drawString(this.font, Component.translatable("gui.kingdomkeys.apprentice.primary"), 8, 6, 0x404040, false);
		gui.drawString(this.font, Component.translatable("gui.kingdomkeys.apprentice.secondary"), 120, 6, 0x404040, false);
	}

	@Override
	public void removed() {
		super.removed();
		previewStand = null;
		lastPreviewStack = ItemStack.EMPTY;
		designButtons.clear();
	}
}
