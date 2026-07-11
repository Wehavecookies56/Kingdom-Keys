package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CardPackScreen extends MenuBackground {

	private static final int CARD_SPACING = 44;
	private static final int CARD_COUNT = 5;

	private static final ResourceLocation CARD_BACK = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/co/card_back.png");
	private static final int CARD_W = 32;
	private static final int CARD_H = 32;
	private final List<ItemStack> cards;
	private final float[] revealProgress = new float[5];
	public MenuBox box;
	private MenuButton takeButton;

	public CardPackScreen(List<ItemStack> cards) {
		super(Strings.Gui_CardPack_Title, new Color(255, 128, 255));
		this.cards = cards;

		Arrays.fill(revealProgress, -1F);
	}

	private float easeOutBack(float t) {
		float c1 = 1.70158F;
		float c3 = c1 + 1F;

		return 1F + c3 * (float) Math.pow(t - 1F, 3) + c1 * (float) Math.pow(t - 1F, 2);
	}

	@Override
	public void init() {
		super.init();
		box = new MenuBox((int) (width * 0.25F), (int) topBarHeight, (int) (width * 0.5F), (int) middleHeight, 1F, new Color(100, 100, 100));
		takeButton = new MenuButton(box.getX() + box.getWidth() / 2 - 30, box.getY() + 145, 80, "gui.done", MenuButton.ButtonType.ROUNDBUTTON, b -> {
					boolean allRevealed = true;

					for (int i = 0; i < revealProgress.length; i++) {
						if (revealProgress[i] < 0F) {
							revealProgress[i] = 0F;
							allRevealed = false;
						}
					}

					if (allRevealed) {
						onClose();
					}
				});
		takeButton.visible = false;
		addRenderableWidget(takeButton);
	}

	@Override
	public void tick() {
		boolean all = true;

		for (int i = 0; i < revealProgress.length; i++) {
			if (revealProgress[i] >= 0F && revealProgress[i] < 1F) {
				revealProgress[i] = Math.min(1F, revealProgress[i] + 0.08F);
			}

			if (revealProgress[i] < 1F) {
				all = false;
			}
		}

		takeButton.visible = true;

		takeButton.setMessage(Component.translatable(all ? "gui.done" : "co.card_pack.reveal_all"));
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (int i = 0; i < 5; i++) {
			int x = cardX(i);
			int y = box.getY() + 45;

			if (mouseX >= x && mouseX < x + CARD_W && mouseY >= y && mouseY < y + CARD_H) {
				if (revealProgress[i] < 0F) {
					revealProgress[i] = 0F;
					return true;
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		//super.render(gui, mouseX, mouseY, partialTick);
		drawMenuBackground(gui, mouseX, mouseY, partialTick);
		box.render(gui, mouseX, mouseY, partialTick);
		takeButton.render(gui, mouseX, mouseY, partialTick);

		for (int i = 0; i < 5; i++) {
			renderCard(gui, i, mouseX, mouseY);
		}
	}

	private void renderCard(GuiGraphics gui, int slot, int mouseX, int mouseY) {
		int x = cardX(slot);
		int y = box.getY() + 45;

		ItemStack stack = cards.get(slot);

		float p = revealProgress[slot];

		boolean animating = p >= 0F;
		boolean showFront = p >= 0.5F;

		float scale;

		if (!animating) {
			scale = 1.5F;
		} else if (!showFront) {
			scale = Mth.lerp(p * 2F, 1.5F, 0F);
		} else {
			float t = (p - 0.5F) * 2F;
			scale = easeOutBack(t) * 3F;
		}

		boolean hovered = revealProgress[slot] < 0 && isHovered(x, y, mouseX, mouseY);

		if (hovered && !showFront) scale *= 1.15F;

		if (!showFront) {
			if (stack.getItem() instanceof MapCardItem card) {
				switch (card.category) {
					case RED -> gui.setColor(0.5F, 0F, 0.1F, 1F);
					case GREEN -> gui.setColor(0F, 0.478F, 0F, 1F);
					case BLUE -> gui.setColor(0F, 0.157F, 0.604F, 1F);
				}
			}

			gui.pose().pushPose();
			{
				float cx = x + CARD_W / 2f;
				float cy = y + CARD_H / 2f;

				gui.pose().translate(cx, cy - (hovered ? 4 : 0), 0);
				gui.pose().scale(scale, scale, 1);

				gui.blit(CARD_BACK, -CARD_W / 2, -CARD_H / 2, 0, 0, CARD_W, CARD_H, CARD_W, CARD_H);
			}
			gui.pose().popPose();
			gui.setColor(1,1,1,1);
			return;
		}

		// Revealed card
		gui.pose().pushPose();
		{
			float cx = x + CARD_W / 2f;
			float cy = y + CARD_H / 2f;

			gui.pose().translate(cx, cy - (hovered ? 4 : 0), 0);
			gui.pose().scale(scale, scale, 1);

			gui.renderItem(stack, -8, -8);

			int value = MapCardItem.getCardValue(stack);

			Component val = Component.literal(String.valueOf(value)).withStyle(ChatFormatting.YELLOW).withStyle(ClientUtils.KK_Font_EXP);
			gui.pose().scale(0.6F,0.6F, 1);
			gui.pose().translate(0,5,200);

			gui.drawString(minecraft.font, val, 0, 0, 0xFFFFFF);

		}
		gui.pose().popPose();

		float textScale = 0.4F;

		gui.pose().pushPose();
		{
			gui.pose().translate(x + CARD_W / 2f, y + 44, 0);
			gui.pose().scale(textScale, textScale, 1);

			Component name = stack.getHoverName();
			gui.drawCenteredString(font, name, 0, 0, 0xFFFFFF);
		}
		gui.pose().popPose();
	}

	private int cardX(int slot) {
		int totalWidth = (CARD_COUNT - 1) * CARD_SPACING + CARD_W;
		int startX = box.getX() + (box.getWidth() - totalWidth) / 2;
		return startX + slot * CARD_SPACING;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	private boolean isHovered(int x, int y, int mouseX, int mouseY) {
		return mouseX >= x && mouseX < x + CARD_W && mouseY >= y-16 && mouseY < y + CARD_H+16;
	}

}