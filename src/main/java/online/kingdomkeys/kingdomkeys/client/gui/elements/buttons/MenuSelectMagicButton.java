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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuMagicSelectorScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.menu.BagInventory;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;

public class MenuSelectMagicButton extends MenuButtonBase {

	final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	MenuMagicSelectorScreen parent;
	int slot;
	Minecraft minecraft;

	public MenuSelectMagicButton(ItemStack stack, int slot, int x, int y, int widthIn, MenuMagicSelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				if (slot != -1) {
					Player player = Minecraft.getInstance().player;
					PlayerData playerData = PlayerData.get(player);
					ItemStack stackToEquip;

					if (slot <= MenuMagicSelectorScreen.BAG_OFFSET) {
						int bagSlot = Math.abs(slot - MenuMagicSelectorScreen.BAG_OFFSET);
						ItemStack magicBag = Utils.getItemInAnyHand(player, ModItems.magicsBag.get());
						if (magicBag.isEmpty())
							return;
						if (!(magicBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv))
							return;

						stackToEquip = bagInv.getStackInSlot(bagSlot);
					} else {
						stackToEquip = player.getInventory().getItem(slot);
					}

					if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.Magic(player, playerData.getEquippedMagic(parent.slot), stackToEquip, slot, parent.slot)).isCanceled()) {
						PacketHandler.sendToServer(new CSEquipMagic(parent.slot, slot));
						ItemStack stackPreviouslyEquipped = playerData.equipMagic(parent.slot, stackToEquip);

						if (slot <= MenuMagicSelectorScreen.BAG_OFFSET) {
							int bagSlot = Math.abs(slot - MenuMagicSelectorScreen.BAG_OFFSET);
							ItemStack magicBag = Utils.getItemInAnyHand(player, ModItems.magicsBag.get());

							if (magicBag.getCapability(Capabilities.ItemHandler.ITEM) instanceof BagInventory bagInv) {
								bagInv.setStackInSlot(bagSlot, stackPreviouslyEquipped);
							}
						} else {
							player.getInventory().setItem(slot, stackPreviouslyEquipped);
						}

						b.visible = false;
					}
				}
			}
		});
		this.stack = stack;
		width = widthIn;
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		this.slot = slot;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
	}


	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ItemCategory category = ItemCategory.MAGICS;

		MagicSpellItem spell = (ItemStack.matches(stack, ItemStack.EMPTY) || !(stack.getItem() instanceof MagicSpellItem)) ? null : (MagicSpellItem) stack.getItem();

		if (visible) {
			Lighting.setupForFlatItems();
			matrixStack.pushPose();
			RenderSystem.enableBlend();

			RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
			matrixStack.translate(getX() + 0.6F, getY(), 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			gui.blit(texture, 0, 0, 166, 34, 18, 28);
			gui.blit(texture, 16, 0, ((width * 2) - (17 + 17)) + 2, 28, 186, 34, 2, 28, 256, 256);
			gui.blit(texture, ((width * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			gui.blit(texture, 6, 4, category.getU(), category.getV(), 20, 20);
			matrixStack.popPose();
			String magicName = spell == null ? "---" : stack.getHoverName().getString();

			gui.drawString(minecraft.font, magicName, getX() + 15, getY() + 3, 0xFFFFFF);

			if (isButtonRendered(mouseY) && (selected || isHovered)) { //Render stuff on the right
				matrixStack.pushPose();
				{
					RenderSystem.enableBlend();
					matrixStack.translate(getX() + 0.6F, getY(), 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					gui.blit(texture, 0, 0, 128, 34, 18, 28);
					gui.blit(texture, 16, 0, ((width * 2) - (17 * 2)) + 2, 28, 148, 34, 2, 28, 256, 256);
					gui.blit(texture, ((width * 2) - 17), 0, 148, 34, 17, 28);
					RenderSystem.disableBlend();
				}
				matrixStack.popPose();
			}
			Lighting.setupForFlatItems();
		}
	}

	public void renderData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		Font fr = minecraft.font;
		MagicSpellItem Magic = (ItemStack.matches(stack, ItemStack.EMPTY) || !(stack.getItem() instanceof MagicSpellItem)) ? null : (MagicSpellItem) stack.getItem();
		PoseStack matrixStack = gui.pose();
		if (isButtonRendered(mouseY) && (selected || isHovered)) { //Render stuff on the right
			if (Magic != null) {
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
				float strPosX = parent.width * 0.57F;
				float posY = parent.height * 0.55F;

				if (stack.getItem() instanceof MagicSpellItem spell) {
					Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(spell.getMagic()));
					int maxExp = magicInstance.getMaxExp(spell.getLevel());
					Component text = Component.translatable("gui.magicspell.exp_short", spell.getExp(stack), maxExp);
					gui.drawString(fr, text, (int) strPosX, (int) posY, 0xEEEE03);
				}
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
