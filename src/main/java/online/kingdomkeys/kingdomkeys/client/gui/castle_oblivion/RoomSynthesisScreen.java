package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CardSelectButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
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
	private final List<CardSelectButton> cards = new ArrayList<>();
	private final DoorData.Type doorType;
	MenuBox boxB, boxL;
	MenuScrollBar scrollBar;

	public RoomSynthesisScreen(CardDoorTileEntity te) {
		super("Room Synthesis", new Color(100, 100, 100));
		this.te = te;
		this.minecraft = Minecraft.getInstance();
		this.doorType = te.getData().getType();
		if (te.getCurrentCriteria() == null || te.getCurrentCriteria().isEmpty()) {
			te.setCurrentCriteria(te.getData().getCardCriteria());
		}
	}

	@Override
	public void init() {
		super.init();
		cards.clear();

		int cardBoxWidth = 290;
		int cardBoxHeight = 65;

		boxB = new MenuBox(bottomLeftBar.getWidth() - 20, height - cardBoxHeight - (int) (bottomBarHeight / 2), cardBoxWidth, cardBoxHeight, 1, new Color(100, 100, 100));
		boxL = new MenuBox(boxB.getX() - 100, (int)topBarHeight, 100, (int)middleHeight, 1, new Color(100, 100, 100));
		scrollBar = new MenuScrollBar(boxB.getX() + boxB.getWidth() - 17, boxB.getY(), boxB.getY() + boxB.getHeight(), (int) middleHeight, 0, true);

		for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
			ItemStack stack = minecraft.player.getInventory().getItem(i);

			if (!stack.isEmpty() && (stack.is(ModTags.MAP_CARD) || stack.is(ModTags.KEY_CARD))) {
				CardSelectButton c = new CardSelectButton(0, 0, 42, 50, stack, this, (e) -> {
					ItemStack copyStack = stack.copy();
					PacketHandler.sendToServer(new CSConsumeCard(te.getBlockPos(), copyStack));
					te.consumeCard(stack);
					updateCards();

					if (te.getCurrentCriteria().isEmpty()) {
						if (doorType == DoorData.Type.NORMAL) {
							PacketHandler.sendToServer(new CSGenerateRoom(copyStack, te.getBlockPos()));
						} else if (doorType == DoorData.Type.KEY) {
							DoorData.CardCriteria criteria = te.getData().getCardCriteria().get(CardCategory.YELLOW);

							ItemStack keycard = ItemStack.EMPTY;
							if (criteria != null) {
								keycard = new ItemStack(KeycardType.values()[criteria.value()].getCardForType());
							}

							PacketHandler.sendToServer(new CSGenerateRoom(keycard, te.getBlockPos()));
						}

						te.openDoor(true);
						minecraft.setScreen(null);
					}
				});

				cards.add(c);
			}
		}

		cards.forEach(this::addWidget);
		updateScroll();
	}

	void updateCards() {
		CardSelectButton toRemove = null;
		for (CardSelectButton button : cards) {
			if (button.stack.isEmpty()) {
				toRemove = button;
				break;
			}
		}
		if (toRemove != null) {
			cards.remove(toRemove);
			removeWidget(toRemove);
			int x = 0;
			int y = 0;
			for (CardSelectButton cardSelectButton : cards) {
				cardSelectButton.setX((int) (width * 0.25F + (x++ * 42)));
				cardSelectButton.setY((int) (height * 0.5F + y * 50));
				if (x == 6) {
					x = 0;
					y++;
				}
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

		Component text = Component.translatable("Criteria");
		if (te.getCurrentCriteria().size() == 1) {
			Map.Entry<CardCategory, DoorData.CardCriteria> o = te.getCurrentCriteria().entrySet().stream().toList().getFirst();
			text = o.getValue().toDescriptiveString(o.getKey() == CardCategory.YELLOW);
		}
		guiGraphics.drawCenteredString(minecraft.font, text, width / 2, (int) topBarHeight-20, 0xFFFFFF);
		text = Component.translatable("Available cards");
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
						guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/co/card_outline.png"), 0, 0, 0, 0, 32, 32, 32, 32);
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
							guiGraphics.drawString(minecraft.font, "" + mapCardItem.getCardValue(hoveredCard.stack), 0, 0, 0xFFDD00);
						}

						matrixStack.translate(-10, 9.5, 150);
						matrixStack.scale(0.3F, 0.3F, 1);
						if (mapCardItem.getRoomType() != null) {
							boolean minglingWorlds = mapCardItem instanceof MinglingWorldsMapCardItem;
							Component category = Component.translatable("CATEGORY");
							String cat = minglingWorlds ? "? ? ?" : mapCardItem.getRoomType().getCategory().toString();
							guiGraphics.drawString(minecraft.font, category, -15, 0, 0x888888);
							guiGraphics.drawString(minecraft.font, cat, 0, 10, 0xFFFF00);

							Component size = Component.translatable("ROOM SIZE");
							String sizeStars = minglingWorlds ? "? ? ?" : mapCardItem.getRoomType().getSize().getStars();
							guiGraphics.drawString(minecraft.font, size, -15, 20, 0x888888);
							guiGraphics.drawString(minecraft.font, sizeStars, 0, 30, 0xFFFF00);

							if (minglingWorlds || mapCardItem.getRoomType().getEnemies() != null) {
								Component enemies = Component.translatable("ENEMIES");// (minglingWorlds ? "?" : mapCardItem.getRoomType().getEnemies().toString()));
								String enemyStars = minglingWorlds ? "? ? ?" : mapCardItem.getRoomType().getEnemies().getStars();

								guiGraphics.drawString(minecraft.font, enemies, -15, 40, 0x888888);
								guiGraphics.drawString(minecraft.font, enemyStars, 0, 50, 0xFFFF00);
							}
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

		for (int i = 0; i < cards.size(); i++) {
			CardSelectButton card = cards.get(i);

			int col = i % CARDS_PER_ROW;
			int row = i / CARDS_PER_ROW;

			card.setX(cardsStartX + col * 42);
			card.setY(cardsStartY + row * 50 - (int) scrollBar.scrollOffset);
		}

		//int rows = (cards.size() + CARDS_PER_ROW - 1) / CARDS_PER_ROW;

		int firstY, lastY, heightDiff = 0;
		if (!cards.isEmpty()) {
			firstY = cards.getFirst().getY();
			lastY = cards.getLast().getY();
			heightDiff = lastY - firstY;
		}
		//System.out.println(firstY + " " + lastY + " " + heightDiff);
		scrollBar.setContentHeight(heightDiff + 150);
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
}