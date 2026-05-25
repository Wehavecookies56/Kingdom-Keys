package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSMeldRecipe;
import online.kingdomkeys.kingdomkeys.synthesis.melding.Melding;
import online.kingdomkeys.kingdomkeys.synthesis.melding.MeldingRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MeldingScreen extends MenuFilterable {
	MenuBox boxL, boxMT, boxMB, boxR;
	MenuButton meld;
	private MenuButton back;
	private ItemStack selected1 = ItemStack.EMPTY;
	private ItemStack selected2 = ItemStack.EMPTY;
	private Melding currentMelding;

	private int selectedSlot1 = -1;
	private int selectedSlot2 = -1;
	private int lastInventoryHash;

	public MeldingScreen() {
		super(Strings.Gui_Melding, new Color(0, 0, 255));
		drawSeparately = true;
	}

	protected void action(String string) {
		switch (string) {
			case "meld" -> {
				PacketHandler.sendToServer(new CSMeldRecipe(currentMelding.getRegistryName(), selectedSlot1, selectedSlot2));
				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);

				selected1 = ItemStack.EMPTY;
				selected2 = ItemStack.EMPTY;

				selectedSlot1 = -1;
				selectedSlot2 = -1;

				currentMelding = null;
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		int hash = 1;
		// Normal inventory
		for(ItemStack stack : minecraft.player.getInventory().items) {
			hash = 31 * hash + ItemStack.hashItemAndComponents(stack);
			hash = 31 * hash + stack.getCount();
		}

		// Equipped magics
		for(Map.Entry<Integer, ItemStack> entry : playerData.getEquippedMagics().entrySet()) {
			hash = 31 * hash + entry.getKey();

			ItemStack stack = entry.getValue();
			hash = 31 * hash + ItemStack.hashItemAndComponents(stack);
			hash = 31 * hash + stack.getCount();
		}

		if(hash != lastInventoryHash) {
			lastInventoryHash = hash;
			initItems();
		}
	}

	@Override
	public void action(ResourceLocation loc, ItemStack stack) {
		//Ignore cuz it is used only with the default 1 select element
	}

	private void handleSelection(ItemStack clicked, int slot) {
		if (selectedSlot1 == slot) { //Unselect first
			selectedSlot1 = -1;
			selected1 = ItemStack.EMPTY;
		} else if (selectedSlot2 == slot) { //Unselect second
			selectedSlot2 = -1;
			selected2 = ItemStack.EMPTY;
		} else if (selected1.isEmpty()) { //Select first
			selectedSlot1 = slot;
			selected1 = clicked.copy();
		} else if (selected2.isEmpty()) { //Select second
			selectedSlot2 = slot;
			selected2 = clicked.copy();
		}

		currentMelding = !selected1.isEmpty() && !selected2.isEmpty() ? findMelding(selected1, selected2) : null;

		initItems();
	}

	@Override
	public void init() {
		super.init();
		float boxPosX = (float) width * 0.14F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.3F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) (boxWidth * 1.1F), (int) middleHeight, 1, new Color(40, 4, 255));
		boxMT = new MenuBox(boxL.getX() + boxL.getWidth(), (int) (topBarHeight), (int) (boxWidth * 0.7F), (int) (middleHeight * 0.5F), 1, new Color(108, 40, 40));
		boxMB = new MenuBox(boxMT.getX(), boxMT.getY() + boxMT.getHeight(), boxMT.getWidth(), boxMT.getHeight(), 1, new Color(108, 40, 40));
		boxR = new MenuBox(boxMT.getX() + (int) (boxWidth * 0.7F), (int) topBarHeight, (int) (boxWidth), (int) (middleHeight), 1, new Color(4, 68, 4));
		int scrollTop = (int) topBarHeight;
		int scrollBot = (int) (scrollTop + middleHeight);
		scrollBar = new MenuScrollBar(boxL.getX() + boxL.getWidth() - 17, scrollTop, scrollBot, (int) middleHeight, 0, true);
		addRenderableWidget(scrollBar);

		initItems();

		buttonWidth = ((float) width * 0.1F);

		meld = new MenuButton(boxR.getX() + boxR.getWidth() / 2 - (int) (buttonWidth + 22) / 2, boxR.getY() + boxR.getHeight() - 22, (int) buttonWidth, Strings.Gui_Melding_Meld, MenuButton.ButtonType.ROUNDBUTTON, (e) -> {
			action("meld");
		});
		meld.setCenterText(true);
		addRenderableWidget(meld);
		buttonWidth = ((float) width * 0.07F);
		addRenderableWidget(back = new MenuButton((int) this.buttonPosX, (int) topBarHeight + 10, (int) buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new MenuItemsScreen())));


	}

	@Override
	public void initItems() {
		inventory.forEach(this::removeWidget);
		inventory.clear();

		float invPosX = boxL.getX() + 5;
		float invPosY = boxL.getY() + 5;

		List<SlotEntry> entries = new ArrayList<>();

		Map<Integer, ItemStack> equippedMagics = playerData.getEquippedMagics();
		for (Map.Entry<Integer, ItemStack> entry : equippedMagics.entrySet()) {
			int slot = -1000 - entry.getKey(); //Avoid normal inv slots conflict
			ItemStack stack = entry.getValue();

			if (stack.isEmpty())
				continue;

			if (!(stack.getItem() instanceof MagicSpellItem))
				continue;

			if (!isMeldingIngredient(stack.getItem()))
				continue;

			entries.add(new SlotEntry(slot, stack.copy(), true));
		}

		for (int slot = 0; slot < minecraft.player.getInventory().items.size(); slot++) {
			ItemStack stack = minecraft.player.getInventory().items.get(slot);

			if (stack.isEmpty()) // Only accept items
				continue;

			if (!(stack.getItem() instanceof MagicSpellItem magic)) //only accept magics
				continue;

			if (!isMeldingIngredient(stack.getItem())) // Only accept real ingredients
				continue;

			entries.add(new SlotEntry(slot, stack.copy(), false));
		}

		entries.sort(
				Comparator.<SlotEntry>comparingInt(e -> e.equipped ? 0 : 1)
					.thenComparingInt(e -> {
						ItemStack stack = e.stack;
						boolean canCurrentlyMeld = stack.getItem() instanceof MagicSpellItem magic && magic.canMeld(stack);

						return canCurrentlyMeld ? 0 : 1;
					})
				//.thenComparing(e -> e.stack.getHoverName().getString())
		);

		ItemStack base = !selected1.isEmpty() ? selected1 : selected2;

		for (int i = 0; i < entries.size(); i++) {
			SlotEntry entry = entries.get(i);
			ItemStack stack = entry.stack.copy();
			int slot = entry.slot;

			MenuStockItem item = new MenuStockItem(this, BuiltInRegistries.ITEM.getKey(stack.getItem()), stack, (int) invPosX, (int) (invPosY + i * 14), boxL.getWidth() - scrollBar.getWidth() - 6, false) {
				@Override
				public void onPress() {
					ItemStack clicked = stack.copy();

					// Si ya hay 2 seleccionados y este no es uno de ellos -> bloquear
					boolean alreadySelected = selectedSlot1 == slot || selectedSlot2 == slot;

					if (!alreadySelected && !selected1.isEmpty() && !selected2.isEmpty()) {
						return;
					}

					ItemStack base = !selected1.isEmpty() ? selected1 : selected2;

					boolean compatible = base.isEmpty() || isCompatible(base, clicked) || ItemStack.isSameItemSameComponents(base, clicked) || alreadySelected;

					if (!compatible)
						return;

					if (stack.getItem() instanceof MagicSpellItem magic) {
						if (!magic.canMeld(stack)) {
							return;
						}
					}

					handleSelection(clicked, slot);
				}

				@Override
				public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
					textColor = ChatFormatting.WHITE;

					ItemStack base = !getSelected1().isEmpty() ? getSelected1() : getSelected2();
					boolean compatible = base.isEmpty() || isCompatible(base, stack) || ItemStack.isSameItemSameComponents(base, stack);

					if (stack.getItem() instanceof MagicSpellItem spell) {
						float percent = spell.getExpPercent(stack);
						int red = (int) (255 * (1F - percent));
						int green = (int) (255 * percent);
						int color = (red << 16) | (green << 8);

						String text = (int) percent * 100 + "%";
						int x = getX() + getWidth() - minecraft.font.width(text) - 4;
						gui.drawString(minecraft.font, text, x, getY() + 3, color);

						if (!spell.canMeld(stack)) {
							textColor = ChatFormatting.DARK_GRAY;
						}
					}
					if (!compatible) {
						textColor = ChatFormatting.DARK_GRAY;
					}
					boolean twoSelected = !selected1.isEmpty() && !selected2.isEmpty();
					boolean alreadySelected = selectedSlot1 == slot || selectedSlot2 == slot;

					if(twoSelected && !alreadySelected) {
						textColor = ChatFormatting.DARK_GRAY;
					}
					super.renderWidget(gui, mouseX, mouseY, partialTicks);
				}
			};

			if(entry.equipped) {
				item.setBackgroundColor(new Color(60, 40, 127));
			}

			boolean compatible = base.isEmpty() || isCompatible(base, stack);

			if (!compatible) {
				//shouldn't reach this I think
			}

			// Resaltar seleccionados
			if (selectedSlot1 == slot || selectedSlot2 == slot) {
				item.setBackgroundColor(new Color(0, 120, 255));
			}

			inventory.add(item);
		}

		inventory.forEach(this::addWidget);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxMT.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxMB.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxR.renderWidget(gui, mouseX, mouseY, partialTicks);

		if (filterBar != null)
			filterBar.render(gui, mouseX, mouseY, partialTicks);

		if (scrollBar != null)
			scrollBar.render(gui, mouseX, mouseY, partialTicks);

		// Scroll
		if (!inventory.isEmpty()) {
			int listHeight = (inventory.get(inventory.size() - 1).getY() + 20) - inventory.get(0).getY() + 3;

			scrollBar.setContentHeight(listHeight);
		}

		// Render inventory items
		for (Renderable renderable : this.inventory) {
			if (renderable instanceof MenuStockItem menuStockItem) {
				gui.enableScissor(boxL.getX() + 2, scrollBar.getY() + 2, boxL.getX() + boxL.getWidth(), scrollBar.getBottom() - 5);
				renderable.render(gui, mouseX, mouseY, partialTicks);
				gui.disableScissor();
			} else {
				renderable.render(gui, mouseX, mouseY, partialTicks);
			}
		}

		playerData = PlayerData.get(minecraft.player);

		// Selected magics
		int centerTopX = boxMT.getX() + boxMT.getWidth() / 2;
		int centerTopY = boxMT.getY() + 10;

		int centerBottomX = boxMB.getX() + boxMB.getWidth() / 2;
		int centerBottomY = boxMB.getY() + 10;

		int rightCenterX = boxR.getX() + boxR.getWidth() / 2;
		int rightCenterY = boxR.getY() + 10;

		PoseStack pose = gui.pose();

		pose.pushPose();
		{
			int ingSize = 50;
			// Magic 1
			if (!selected1.isEmpty()) {
				ClientUtils.drawItemAsIcon(selected1, pose, centerTopX - 8, centerTopY + 15, ingSize);
				String resultName = selected1.getHoverName().getString();
				gui.drawCenteredString(minecraft.font, resultName, centerTopX, centerTopY + 50, 0xFFFFFF);
			}

			// Magic 2
			if (!selected2.isEmpty()) {
				ClientUtils.drawItemAsIcon(selected2, pose, centerBottomX - 8, centerBottomY + 15, ingSize);
				String resultName = selected2.getHoverName().getString();
				gui.drawCenteredString(minecraft.font, resultName, centerBottomX, centerBottomY + 50, 0xFFFFFF);
			}

			String tierText;
			// Result
			if (currentMelding != null) {
				ItemStack result = new ItemStack(currentMelding.getResult(), currentMelding.getAmount());
				ClientUtils.drawItemAsIcon(result, pose, rightCenterX - 8, rightCenterY + 45, 80);

				String resultName = result.getHoverName().getString();
				gui.drawCenteredString(minecraft.font, resultName, rightCenterX, rightCenterY + 96, 0xFFFFFF);
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Shop_Buy_Cost) + " " + currentMelding.getCost(), boxR.getX() + 10, boxR.getY() + 10, playerData.getMunny() >= currentMelding.getCost() ? 0x00FF00 : 0xFF0000);

				tierText = Utils.translateToLocal(Strings.Gui_Shop_Tier) + " " + Utils.getTierFromInt(currentMelding.getTier());
				int tierColor = ModConfigs.SERVER.requireMeldingTier.get() && playerData.getSynthLevel() >= currentMelding.getTier() ? 0x00FF00 : 0xFF0000;
				gui.drawString(minecraft.font, tierText, boxR.getX() + boxR.getWidth() - minecraft.font.width(tierText) - 10, boxR.getY() + 10, tierColor);

				meld.visible = true;
				meld.active = playerData.getMunny() >= currentMelding.getCost() && !ModConfigs.SERVER.requireMeldingTier.get() || playerData.getSynthLevel() >= currentMelding.getTier();
				if (player.getInventory().getFreeSlot() == -1) {
					meld.active = false;
					meld.setMessage(Component.translatable(Strings.Gui_Shop_NoSpace));
				}
			} else {
				if (!selected1.isEmpty() || !selected2.isEmpty()) { //Only show ????? if at least one ingredient has been selected
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Shop_Buy_Cost)+" ???", boxR.getX() + 10, boxR.getY() + 10, 0x777777);
					gui.drawCenteredString(minecraft.font, "?????", rightCenterX, boxR.getY() + boxR.getHeight() - 20, 0x777777);
					tierText = Utils.translateToLocal(Strings.Gui_Shop_Tier)+" ?";
					gui.drawString(minecraft.font, tierText, boxR.getX() + boxR.getWidth() - minecraft.font.width(tierText) - 10, boxR.getY() + 10, 0x777777);
				}
				meld.active = false;
				meld.visible = false;
			}

		}

		pose.popPose();

		meld.render(gui, mouseX, mouseY, partialTicks);
		back.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		scrollBar.mouseReleased(pMouseX, pMouseY, pButton);
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
		scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
		updateScroll();
		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}

	public void updateScroll() {
		inventory.forEach(button -> {
			button.offsetY = (int) scrollBar.scrollOffset;
		});
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		if (mouseX >= boxL.getX() && mouseX <= scrollBar.getX() + scrollBar.getWidth())
			scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
		updateScroll();
		return false;
	}

	/**
	 * Checks if the two selected items match in a meld
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	private Melding findMelding(ItemStack first, ItemStack second) {
		Item item1 = first.getItem();
		Item item2 = second.getItem();

		for (Melding melding : MeldingRegistry.getInstance().getValues()) {
			boolean matches = (melding.getIngredient1() == item1 && melding.getIngredient2() == item2) || (melding.getIngredient1() == item2 && melding.getIngredient2() == item1);

			if (matches)
				return melding;
		}

		return null;
	}

	public boolean isCompatible(ItemStack first, ItemStack other) {
		if (first.isEmpty())
			return true;

		return findMelding(first, other) != null;
	}

	public ItemStack getSelected1() {
		return selected1;
	}

	public ItemStack getSelected2() {
		return selected2;
	}

	public boolean isSelected(ItemStack stack, int slot) {
		return selectedSlot1 == slot || selectedSlot2 == slot;
	}

	private boolean isMeldingIngredient(Item item) {
		for (Melding melding : MeldingRegistry.getInstance().getValues()) {
			if (melding.getIngredient1() == item || melding.getIngredient2() == item) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Aux class
	 */
	private static class SlotEntry {
		final int slot;
		final ItemStack stack;
		final boolean equipped;

		SlotEntry(int slot, ItemStack stack, boolean equipped) {
			this.slot = slot;
			this.stack = stack;
			this.equipped = equipped;
		}
	}
}
