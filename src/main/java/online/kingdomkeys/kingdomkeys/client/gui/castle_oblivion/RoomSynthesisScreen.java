package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CardSelectButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.BagItem;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.KeycardType;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.item.card.MinglingWorldsMapCardItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSConsumeCard;
import online.kingdomkeys.kingdomkeys.network.cts.CSGenerateRoom;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomSynthesisScreen extends MenuBackground {

	private static final int CARDS_PER_ROW = 6;
	public final CardDoorTileEntity te;
	private final List<CardEntry> availableCards = new ArrayList<>();
	private final List<CardSelectButton> cards = new ArrayList<>();
	private final DoorData.Type doorType;
	MenuBox boxB, boxL;
	MenuScrollBar scrollBar;

	public RoomSynthesisScreen(CardDoorTileEntity te) {
		super("Room Synthesis", new Color(100, 100, 100));
		this.te = te;
		this.minecraft = Minecraft.getInstance();
		this.doorType = te.getData().getType();
		if ((te.getCurrentCriteria() == null) || te.getCurrentCriteria().isEmpty()) {
			te.setCurrentCriteria(te.getData().getCardCriteria());
		}
	}

	private void onCardSelected(CardEntry entry) {
		ItemStack copyStack = entry.stack.copy();

		PacketHandler.sendToServer(new CSConsumeCard(te.getBlockPos(), entry.slot));

		te.consumeCard(entry.stack);

		updateCards();

		if (!te.getCurrentCriteria().isEmpty())
			return;

		if (doorType == DoorData.Type.NORMAL) {
			PacketHandler.sendToServer(new CSGenerateRoom(copyStack, te.getBlockPos()));
		} else if (doorType == DoorData.Type.KEY) {
			DoorData.CardCriteria criteria = te.getData().getCardCriteria().get(CardCategory.YELLOW);

			ItemStack keycard = ItemStack.EMPTY;

			if (criteria != null)
				keycard = new ItemStack(KeycardType.values()[criteria.value()].getCardForType());

			PacketHandler.sendToServer(new CSGenerateRoom(keycard, te.getBlockPos()));
		}

		te.openDoor(true);
		minecraft.setScreen(null);
	}

	private void createCardButton(CardEntry entry) {
		CardSelectButton button = new CardSelectButton(0, 0, 42, 50, entry.stack, this, b -> onCardSelected(entry));
		cards.add(button);
	}

	@Override
	public void init() {
		super.init();

		cards.clear();
		availableCards.clear();

		int cardBoxWidth = 290;
		int cardBoxHeight = 65;

		boxB = new MenuBox(bottomLeftBar.getWidth() - 20, height - cardBoxHeight - (int)(bottomBarHeight / 2), cardBoxWidth, cardBoxHeight, 1, new Color(100, 100, 100));
		boxL = new MenuBox(boxB.getX() - 100, (int)topBarHeight, 100, (int)middleHeight, 1, new Color(100, 100, 100));
		scrollBar = new MenuScrollBar(boxB.getX() + boxB.getWidth() - 17, boxB.getY(), boxB.getY() + boxB.getHeight(), boxB.getHeight(), 0, true);
		// Inventory
		for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
			ItemStack stack = minecraft.player.getInventory().getItem(i);

			if (!stack.isEmpty() && (stack.is(ModTags.MAP_CARD) || stack.is(ModTags.KEY_CARD))) {
				availableCards.add(new CardEntry(stack, i));
			}
		}

		// Bag
		if(Utils.hasOnlyOneBag(player, BagItem.Type.CARDS_BAG)) {
			ItemStack bag = player.getInventory().getItem(Utils.getCardsBagSlot(player, BagItem.Type.CARDS_BAG));

			if (!bag.isEmpty()) {
				IItemHandler inv = bag.getCapability(Capabilities.ItemHandler.ITEM);

				if (inv != null) {
					for (int i = 0; i < inv.getSlots(); i++) {
						ItemStack stack = inv.getStackInSlot(i);

						if (!stack.isEmpty()) {
							availableCards.add(new CardEntry(stack, -1000 - i));
						}
					}
				}
			}
		} else {
			KingdomKeys.LOGGER.debug("More than one cards bag found, ignoring.");
		}

		for (CardEntry entry : availableCards) {
			createCardButton(entry);
		}

		cards.forEach(this::addWidget);
		updateScroll();
	}

	void updateCards() {
		for (CardSelectButton button : List.copyOf(cards)) {
			if (button.stack.isEmpty()) {
				removeWidget(button);
				cards.remove(button);
			}
		}

		updateScroll();
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		boxB.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
		boxL.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
		scrollBar.render(guiGraphics, mouseX, mouseY, partialTicks);

		Component text = Component.literal("");
		if (te.getCurrentCriteria().size() == 1) {
			Map.Entry<CardCategory, DoorData.CardCriteria> o = te.getCurrentCriteria().entrySet().stream().toList().getFirst();
			text = o.getValue().toDescriptiveString(o.getKey() == CardCategory.YELLOW);
		}
		guiGraphics.drawString(minecraft.font, text, topRightBar.getPosX()+11, (int) topBarHeight-20, 0xFFFFFF);

		text = Component.translatable("co.available_cards");
		guiGraphics.drawCenteredString(minecraft.font, text, boxB.getX() + boxB.getWidth() / 2, boxB.getY() - 10, 0xFFFFFF);

		int iconSize = 72;
		int totalWidth = te.getCurrentCriteria().size() * iconSize;
		int criteriaX = width / 2 - totalWidth / 2;
		int criteriaY = (int)topBarHeight + 10;
		for (Map.Entry<CardCategory, DoorData.CardCriteria> critera : te.getCurrentCriteria().entrySet()) {
			guiGraphics.pose().pushPose();
			{
				guiGraphics.pose().translate(criteriaX, criteriaY, 1);
				guiGraphics.pose().scale(3, 3, 1);
				switch (critera.getKey()) {
					case YELLOW -> {
						ClientUtils.drawItemAsIcon(new ItemStack(KeycardType.values()[te.getData().getCardCriteria().get(CardCategory.YELLOW).value()].getCardForType()), guiGraphics.pose(), 8, 8, 32);
						criteriaX += 72;
					}
					case RGB, RED, GREEN, BLUE -> {
						Color colour = switch (critera.getKey()) {
							case RED -> Color.RED;
							case GREEN -> Color.GREEN;
							case BLUE -> Color.BLUE;
							case RGB -> Color.WHITE;
							default -> throw new IllegalStateException("Unexpected value: " + critera.getKey());
						};
						RenderSystem.setShaderColor(colour.getRed(), colour.getGreen(), colour.getBlue(), 1);
						guiGraphics.blit(KingdomKeys.rl("textures/gui/co/card_outline.png"), 0, 0, 0, 0, 32, 32, 32, 32);
						RenderSystem.setShaderColor(1, 1, 1, 1);
						Component val = Component.literal(critera.getValue().toString()).withStyle(ClientUtils.KK_Font_EXP);
						guiGraphics.drawString(minecraft.font, val, 16 - minecraft.font.width(val)/2, 14, 0xFFFFFF, false);
						criteriaX += 72;
					}
				}
			}
			guiGraphics.pose().popPose();
		}

		CardSelectButton hoveredCard = null;
		guiGraphics.enableScissor(boxB.getX() - 2, scrollBar.getY() + 2, boxB.getX() + boxB.getWidth(), scrollBar.getBottom() - 5);
		for (CardSelectButton card : cards) {
			card.render(guiGraphics, mouseX, mouseY, partialTicks);
			if (card.isHovered())
				hoveredCard = card;
		}
		guiGraphics.disableScissor();

		//Hovered card details, had to do it here because of the scissor killing the rendering "offscreen"
		if (hoveredCard != null) {
			PoseStack matrixStack = guiGraphics.pose();
			matrixStack.pushPose();
			{
				if (hoveredCard.active) {
					matrixStack.translate(boxL.getX()+25, boxL.getY()+27, 0);
					guiGraphics.drawCenteredString(minecraft.font, Utils.translateToLocal(hoveredCard.stack.getItem().getName(hoveredCard.stack).getString()), 26, -20, 0xFFFFFF);

					matrixStack.scale(5, 5, 1);
					matrixStack.translate(-2.5F, -2.5F, 20);
					ClientUtils.drawItemAsIcon(hoveredCard.stack, matrixStack, 0, 0, 16);
					matrixStack.scale(0.7F, 0.7F, 1);
					matrixStack.translate(13, 14, 150);

					if (hoveredCard.card instanceof MapCardItem mapCardItem) {
						if (mapCardItem.getCategory() != CardCategory.YELLOW && mapCardItem.getCategory() != CardCategory.RGB) {
							matrixStack.pushPose();
							matrixStack.scale(0.6F, 0.8F, 1);
							guiGraphics.drawString(minecraft.font, Component.literal(MapCardItem.getCardValue(hoveredCard.stack)+"").withStyle(ClientUtils.KK_Font_EXP), 1, 3, 0xFFDD00);
							matrixStack.popPose();
						}

						matrixStack.translate(-10, 9.5, 150);
						matrixStack.scale(0.3F, 0.3F, 1);
						if (mapCardItem.getRoomType() != null) {
							boolean minglingWorlds = mapCardItem instanceof MinglingWorldsMapCardItem;
							Component category = Component.translatable("co.category");
							String cat = minglingWorlds ? "? ? ?" : mapCardItem.getRoomType().getCategory().toString();
							guiGraphics.drawString(minecraft.font, category, -15, 0, 0x888888);
							guiGraphics.drawString(minecraft.font, Component.literal(cat).withStyle(ClientUtils.KK_Font_EXP), 0, 10, 0xFFFF00);

							if (minglingWorlds || mapCardItem.getRoomType().getEnemies() != null) {
								Component enemies = Component.translatable("co.enemies");
								String enemyStars = minglingWorlds ? "? ? ?" : mapCardItem.getRoomType().getEnemies().getStars();

								guiGraphics.drawString(minecraft.font, enemies, -15, 40, 0x888888);
								guiGraphics.drawString(minecraft.font, Component.literal(enemyStars).withStyle(ClientUtils.KK_Font_EXP), 0, 50, 0xFFFF00);
							}

							Component size = Component.translatable("co.room_size");
							String sizeStars = minglingWorlds ? "? ? ?" : mapCardItem.getRoomType().getSize().getStars();
							guiGraphics.drawString(minecraft.font, size, -15, 20, 0x888888);
							guiGraphics.drawString(minecraft.font, Component.literal(sizeStars).withStyle(ClientUtils.KK_Font_EXP), 0, 30, 0xFFFF00);
						}
					}
				}
			}
			matrixStack.popPose();
		}

	}

	private void updateScroll() {
		int cardsStartX = boxB.getX() + 10;
		int cardsStartY = boxB.getY() + 8;

		int rows = (cards.size() + CARDS_PER_ROW - 1) / CARDS_PER_ROW;

		for (int i = 0; i < cards.size(); i++) {
			CardSelectButton card = cards.get(i);

			int col = i % CARDS_PER_ROW;
			int row = i / CARDS_PER_ROW;

			card.setX(cardsStartX + col * 42);
			card.setY(cardsStartY + row * 50 - (int) scrollBar.scrollOffset);
		}

		int contentHeight = 15 + rows * 50;
		scrollBar.setContentHeight(contentHeight);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		if (mouseX >= boxB.getX() && mouseX <= boxB.getX() + boxB.getWidth()) {
			scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
			updateScroll();
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		scrollBar.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		scrollBar.mouseReleased(mouseX, mouseY, button);
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		scrollBar.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		updateScroll();
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	private static class CardEntry {
		final ItemStack stack;
		final int slot;

		CardEntry(ItemStack stack, int slot) {
			this.stack = stack;
			this.slot = slot;
		}
	}
}