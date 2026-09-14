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

	private static final int DESIGN_Y = 16;
	private static final int DESIGN_W = 16, DESIGN_H = 16;
	private static final int DESIGN_BAND_LEFT = 44, DESIGN_BAND_RIGHT = 132;

	private static final int PANEL_U = 176, PANEL_V = 0;
	private static final int PANEL_W = 68, PANEL_H = 94;
	private static final int PANEL_TOP = 12, PANEL_OVERLAP = 4;

	private static final int STAND_LEFT = 8, STAND_RIGHT = 62;
	private static final int STAND_TOP = -6, STAND_BOTTOM = 88;
	private static final int STAND_SCALE = 40;

	private ArmorStand previewStand;
	private ItemStack lastPreviewStack = ItemStack.EMPTY;

	private final Map<Integer, Button> designButtons = new LinkedHashMap<>();

	public ApprenticeClothStationScreen(ApprenticeClothStationMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
		this.imageWidth = 176;
		this.imageHeight = 186;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	private int panelX() {
		return leftPos + imageWidth - PANEL_OVERLAP;
	}

	@Override
	protected void init() {
		super.init();
		leftPos -= (PANEL_W - PANEL_OVERLAP) / 2;

		ensurePreviewStand();

		designButtons.clear();

		int count = UnionApprenticeArmorItem.MAX_DESIGN - UnionApprenticeArmorItem.MIN_DESIGN + 1;
		int rowLeft = designRowLeft(count);

		for (int i = UnionApprenticeArmorItem.MIN_DESIGN; i <= UnionApprenticeArmorItem.MAX_DESIGN; i++) {
			final int design = i;
			int x = leftPos + rowLeft + (design - UnionApprenticeArmorItem.MIN_DESIGN) * DESIGN_W;
			Button button = Button.builder(Component.literal(String.valueOf(design)), b -> selectDesign(design)).bounds(x, topPos + DESIGN_Y, DESIGN_W, DESIGN_H).tooltip(Tooltip.create(Component.translatable("tooltip.kingdomkeys.apprentice.design", design))).build();

			designButtons.put(design, button);
			addRenderableWidget(button);
		}

		refreshDesignButtons();
	}

	private static int designRowLeft(int count) {
		int slack = (DESIGN_BAND_RIGHT - DESIGN_BAND_LEFT) - count * DESIGN_W;
		return DESIGN_BAND_LEFT + Math.max(0, slack / 2);
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

		int panelX = panelX();
		int panelY = topPos + PANEL_TOP;
		gui.blit(TEXTURE, panelX, panelY, PANEL_U, PANEL_V, PANEL_W, PANEL_H);

		gui.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

		if (previewStand != null) {
			InventoryScreen.renderEntityInInventoryFollowsMouse(gui, panelX + STAND_LEFT, panelY + STAND_TOP, panelX + STAND_RIGHT, panelY + STAND_BOTTOM, STAND_SCALE, 0.0625F, mouseX, mouseY, previewStand);
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
