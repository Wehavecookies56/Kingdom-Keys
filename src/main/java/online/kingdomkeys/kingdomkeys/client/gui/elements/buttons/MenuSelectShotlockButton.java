package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuShotlockSelectorScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.BagItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.ShotlockItem;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipShotlock;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;

public class MenuSelectShotlockButton extends MenuButtonBase {
	public int slot;
	public ItemStack stack;
	boolean selected;
	int colour, labelColour;
	MenuShotlockSelectorScreen parent;
	Minecraft minecraft;

	public MenuSelectShotlockButton(ItemStack stack, int slot, int x, int y, int widthIn, MenuShotlockSelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				Player player = Minecraft.getInstance().player;
				PlayerData playerData = PlayerData.get(player);
				boolean fromBag = slot <= MenuShotlockSelectorScreen.BAG_OFFSET;
				ItemStack stackToEquip;

				if (fromBag) {
					int bagSlot = Math.abs(slot - MenuShotlockSelectorScreen.BAG_OFFSET);
					if (!Utils.hasOnlyOneBag(player, BagItem.Type.SHOTLOCKS_BAG)) //Only one bag should be in the inv
						return;

					ItemStack shotlockBag = Utils.getItemInInventory(player, ModItems.shotlocksBag.get());
					if (shotlockBag.isEmpty()) return;
					if (!(shotlockBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv)) return;

					stackToEquip = bagInv.getStackInSlot(bagSlot);
				} else {
					stackToEquip = slot >= 0 ? player.getInventory().getItem(slot) : ItemStack.EMPTY;
				}

				if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Shotlock(player, playerData.getEquippedShotlock(), stackToEquip, slot, 0)).isCanceled()) {
					PacketHandler.sendToServer(new CSEquipShotlock(slot));
					ItemStack stackPreviouslyEquipped = playerData.equipShotlock(stackToEquip);

					if (stackPreviouslyEquipped != null) {
						if (fromBag) {
							int bagSlot = Math.abs(slot - MenuShotlockSelectorScreen.BAG_OFFSET);
							ItemStack shotlockBag = Utils.getItemInInventory(player, ModItems.shotlocksBag.get());

							if (shotlockBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv) {
								bagInv.setStackInSlot(bagSlot, stackPreviouslyEquipped);
							}
						} else {
							player.getInventory().setItem(slot, stackPreviouslyEquipped);
						}
					}
					b.visible = false;
				}
			}
		});

		this.stack = stack;
		this.slot = slot;
		width = widthIn;
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
		Color col = Color.decode(String.valueOf(colour));
		ItemCategory category = ItemCategory.SHOTLOCK;

		Shotlock shotlock = (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ShotlockItem shotlockItem)) ? null : ModShotlocks.registry.get(shotlockItem.getShotlock());

		if (visible) {
			Lighting.setupForFlatItems();
			matrixStack.pushPose();
			{
				RenderSystem.enableBlend();

				RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
				matrixStack.translate(getX() + 0.6F, getY(), 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				gui.blit(Constants.MENU_TEXTURE, 0, 0, 166, 34, 18, 28);
				gui.blit(Constants.MENU_TEXTURE, 16, 0, ((width * 2) - (17 + 17)) + 2, 28, 186, 34, 2, 28, 256, 256);
				gui.blit(Constants.MENU_TEXTURE, ((width * 2) - 17), 0, 186, 34, 17, 28);
				RenderSystem.setShaderColor(1, 1, 1, 1);
				gui.blit(Constants.MENU_TEXTURE, 6, 4, category.getU(), category.getV(), 20, 20);
			}
			matrixStack.popPose();

			String shName = shotlock == null ? "---" : stack.getHoverName().getString();
			gui.drawString(minecraft.font, shName, getX() + 15, getY() + 3, 0xFFFFFF);

			// show shotlock level and exp in the button
			if (shotlock != null && shotlock.getMaxLevel() > 1) {
				ShotlockItem shotlockItem = (ShotlockItem) stack.getItem();
				String text = Utils.translateToLocal("gui.magicspell.lvl_short", shotlockItem.getLocalLevel(stack));
				int x = getX() + getWidth() - minecraft.font.width(text) - 4;
				gui.drawString(minecraft.font, text, x, getY() + 2, 0xFFFFFF);

				float percent = shotlockItem.getLocalPercent(stack);
				int barWidth = minecraft.font.width(text);
				int percentWidth = (int) (barWidth * percent);
				gui.blit(Constants.MENU_TEXTURE, getX() + getWidth() - barWidth - 5, getY() + getHeight() - 4, barWidth, 2, 161, 67, 1, 5, 256, 256);
				gui.blit(Constants.MENU_TEXTURE, getX() + getWidth() - barWidth - 5, getY() + getHeight() - 4, percentWidth, 2, 163, 67, 1, 5, 256, 256);
			}

			if (isHovered || selected) {
				matrixStack.pushPose();
				{
					RenderSystem.enableBlend();
					matrixStack.translate(getX() + 0.6F, getY(), 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					gui.blit(Constants.MENU_TEXTURE, 0, 0, 128, 34, 18, 28);
					gui.blit(Constants.MENU_TEXTURE, 16, 0, ((width * 2) - (17 * 2)) + 2, 28, 148, 34, 2, 28, 256, 256);
					gui.blit(Constants.MENU_TEXTURE, ((width * 2) - 17), 0, 148, 34, 17, 28);
					RenderSystem.disableBlend();
				}
				matrixStack.popPose();
			}
		}
	}

	public void renderData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		Font fr = minecraft.font;
		Shotlock shotlock = (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ShotlockItem shotlockItem)) ? null : ModShotlocks.registry.get(shotlockItem.getShotlock());
		PoseStack matrixStack = gui.pose();

		if (isButtonRendered(mouseY) && (selected || isHovered) && shotlock != null) {
			float iconPosX = parent.width * 0.565F;
			float iconPosY = parent.height * 0.20F;
			float iconHeight = parent.height * 0.3148F;
			Lighting.setupForFlatItems();
			matrixStack.pushPose();
			{
				matrixStack.translate(iconPosX, iconPosY, 0);
				matrixStack.scale(0.0625F * iconHeight, 0.0625F * iconHeight, 1);
				ClientUtils.drawItemAsIcon(stack, matrixStack, 0, 0, 16);
			}
			matrixStack.popPose();

			float strPosX = parent.boxR.getX() + 10;
			float posY = parent.boxR.getY() + parent.boxR.getHeight() / 2F + 20;

			Component maxLocksText = Component.translatable("gui.shotlockitem.max_locks", shotlock.getMaxLocks());
			gui.drawString(fr, maxLocksText, (int) strPosX, (int) posY, 0xEEEE03);
			posY += 20;

			ShotlockItem shotlockItem = (ShotlockItem) stack.getItem();
			if (shotlock.getMaxLevel() > 1) {
				Component text;
				if (shotlockItem.isMaxed(stack)) {
					text = Component.translatable("gui.synthesis.exp").append(": MAX");
				} else {
					text = Component.translatable("gui.magicspell.exp_short", shotlockItem.getLocalExp(stack), shotlockItem.getLocalMaxExp());
				}
				gui.drawString(fr, text, (int) strPosX, (int) posY, 0xEEEE03);

				text = Component.translatable("gui.magicspell.lvl_short", shotlockItem.getLocalLevel(stack));
				gui.drawString(fr, text, (int) strPosX, (int) posY - 10, 0xEEEE03);

				float percent = shotlockItem.getLocalPercent(stack);
				int barWidth = (int) (parent.boxR.getWidth() * 0.8F);
				int percentWidth = (int) (barWidth * percent);

				gui.blit(Constants.MENU_TEXTURE, (int) strPosX, (int) posY + 10, barWidth, 5, 161, 67, 1, 5, 256, 256);
				gui.blit(Constants.MENU_TEXTURE, (int) strPosX, (int) posY + 10, percentWidth, 5, 163, 67, 1, 5, 256, 256);
			}
		}
	}

	public boolean isButtonRendered(double mouseY) {
		return mouseY >= parent.scrollBar.getY() && mouseY <= parent.scrollBar.getBottom() + 2;
	}

	@Override
	public void playDownSound(SoundManager soundHandler) {
		soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_in.get(), 1.0F, 1.0F));
	}
}
