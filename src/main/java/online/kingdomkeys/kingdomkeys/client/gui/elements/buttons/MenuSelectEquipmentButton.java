package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentSelectorScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipKeychain;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuSelectEquipmentButton extends MenuButtonBase {

	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	MenuEquipmentSelectorScreen parent;
	int slot;
	Minecraft minecraft;

	public MenuSelectEquipmentButton(ItemStack stack, int slot, int x, int y, int widthIn, MenuEquipmentSelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				if (slot != -1) {
					PlayerEntity player = Minecraft.getInstance().player;
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if(Utils.findSummoned(player.inventory, playerData.getEquippedKeychain(DriveForm.NONE), false) > -1)
						PacketHandler.sendToServer(new CSSummonKeyblade(true));
					PacketHandler.sendToServer(new CSEquipKeychain(parent.form, slot));
					ItemStack stackToEquip = player.inventory.getStackInSlot(slot);
					ItemStack stackPreviouslyEquipped = playerData.equipKeychain(parent.form, stackToEquip);
					player.inventory.setInventorySlotContents(slot, stackPreviouslyEquipped);
				} else {
					Minecraft.getInstance().displayGuiScreen(new MenuEquipmentScreen());
				}
			}
		});
		this.stack = stack;
		width = (int) (parent.width * 0.264F);
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		this.slot = slot;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fr = minecraft.fontRenderer;
		isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.color4f(1, 1, 1, 1);
		ItemCategory category = ItemCategory.TOOL;
		
		KeybladeItem keyblade;
		if(ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY) || !(stack.getItem() instanceof IKeychain)) {
			keyblade = null;
		} else {
			keyblade = ((IKeychain) stack.getItem()).toSummon();
		}
		if (visible) {
			RenderHelper.disableStandardItemLighting();
			RenderHelper.setupGuiFlatDiffuseLighting();
			float itemWidth = parent.width * 0.264F;
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.push();
			RenderSystem.enableBlend();
			RenderSystem.enableAlphaTest();
			RenderSystem.color4f(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
			matrixStack.translate(x + 0.6F, y, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			blit(matrixStack, 0, 0, 166, 34, 18, 28);
			for (int i = 0; i < (itemWidth * 2) - (17 + 17); i++) {
				blit(matrixStack, 17 + i, 0, 184, 34, 2, 28);
			}
			blit(matrixStack, (int) ((itemWidth * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.color4f(1, 1, 1, 1);
			blit(matrixStack, 6, 4, category.getU(), category.getV(), 20, 20);
			matrixStack.pop();
			String itemName;
			if (keyblade == null) { //Name to display
				itemName = "---";
			} else {
				itemName = new ItemStack(keyblade).getDisplayName().getString();
			}
			drawString(matrixStack, minecraft.fontRenderer, itemName, x + 15, y + 3, 0xFFFFFF);
			
			if (selected || isHovered) { //Render stuff on the right
				minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				matrixStack.push();
				{
					RenderSystem.enableBlend();
					RenderSystem.enableAlphaTest();
					matrixStack.translate(x + 0.6F, y, 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					blit(matrixStack, 0, 0, 128, 34, 18, 28);
					for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
						blit(matrixStack, 17 + i, 0, 146, 34, 2, 28);
					}
					blit(matrixStack, (int) ((itemWidth * 2) - 17), 0, 148, 34, 17, 28);
				}
				matrixStack.pop();
				
				if(keyblade != null) {
					float iconPosX = parent.width * 0.66F;
					float iconPosY = parent.height * 0.1833F;
					float iconHeight = parent.height * 0.3148F;
					RenderHelper.disableStandardItemLighting();
					RenderHelper.setupGuiFlatDiffuseLighting();
					matrixStack.push();
					{
						RenderSystem.enableAlphaTest();
						matrixStack.translate(iconPosX, iconPosY, 0);
						matrixStack.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
						minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(keyblade), 0, 0);
					}
					matrixStack.pop();
					float strPosX = parent.width * 0.685F;
					float strPosY = parent.height * 0.5185F;
					float strNumPosX = parent.width * 0.78F;
					float magPosY = parent.height * 0.5657F;
					
					String strengthStr = String.valueOf(((int) keyblade.getStrength(stack)));
					String magicStr = String.valueOf(((int) keyblade.getMagic(stack)));
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
					int strength = playerData.getStrength() + ((int) keyblade.getStrength(stack));
					int magic = playerData.getMagic() + ((int) keyblade.getMagic(stack));
					String totalStrengthStr = String.valueOf(strength);
                    String totalMagicStr = String.valueOf(magic);
					String openBracketStr = " [ ";
					String openBracketMag = " [ ";
					String totalStr = String.valueOf(strength);
					String totalMag = String.valueOf(magic);
					if (totalStr.length() == 1) {
						openBracketStr += " ";
					}
					if (totalMag.length() == 1) {
						openBracketMag += " ";
					}
					
					drawString(matrixStack, fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Strength).getString(), (int) strPosX, (int) strPosY, 0xEE8603);
					drawString(matrixStack, fr, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
					drawString(matrixStack, fr, openBracketStr, (int) strNumPosX + fr.getStringWidth(strengthStr), (int) strPosY, 0xBF6004);
					drawString(matrixStack, fr, totalStrengthStr, (int) strNumPosX + fr.getStringWidth(strengthStr) + fr.getStringWidth(openBracketStr), (int) strPosY, 0xFBEA21);
					drawString(matrixStack, fr, "]", (int) strNumPosX + fr.getStringWidth(strengthStr) + fr.getStringWidth(openBracketStr) + fr.getStringWidth(totalStrengthStr), (int) strPosY, 0xBF6004);

					drawString(matrixStack, fr, new TranslationTextComponent(Strings.Gui_Menu_Status_Magic).getString(), (int) strPosX, (int) magPosY, 0xEE8603);
					drawString(matrixStack, fr, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
					drawString(matrixStack, fr, openBracketMag, (int) strNumPosX + fr.getStringWidth(magicStr), (int) magPosY, 0xBF6004);
					drawString(matrixStack, fr, totalMagicStr, (int) strNumPosX + fr.getStringWidth(magicStr) + fr.getStringWidth(openBracketMag), (int) magPosY, 0xFBEA21);
					drawString(matrixStack, fr, "]", (int) strNumPosX + fr.getStringWidth(magicStr) + fr.getStringWidth(openBracketMag) + fr.getStringWidth(totalMagicStr), (int) magPosY, 0xBF6004);

					float tooltipPosX = parent.width * 0.3333F;
					float tooltipPosY = parent.height * 0.8F;
					Utils.drawSplitString(minecraft.fontRenderer, keyblade.getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
				}
			}
			RenderHelper.disableStandardItemLighting();
			RenderHelper.setupGuiFlatDiffuseLighting();
			float labelWidth = parent.width * 0.1953F;
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.push();
			{
				RenderSystem.enableBlend();
				//RenderSystem.enableAlpha();
				RenderSystem.color4f(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
				matrixStack.translate(x + width + 2.1F, y, 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				blit(matrixStack, 0, 0, 219, 34, 15, 28);
				
				for (int i = 0; i < (labelWidth * 2) - (17 + 14); i++) {
					blit(matrixStack, 14 + i, 0, 184, 34, 2, 28);
				}
				blit(matrixStack, (int) ((labelWidth * 2) - 17), 0, 186, 34, 17, 28);
			}
			matrixStack.pop();
			String label = "N/A";
			/*if (item != null && item.getKeyblade() instanceof KeybladeItem) {
				KeybladeItem itemRealKeyblade = (KeybladeItem) item.getKeyblade();
				label = (itemRealKeyblade.getAbility() != null) ? Utils.translateToLocal(itemRealKeyblade.getAbility().getName()): "N/A";
			}*/
			float centerX = (labelWidth / 2) - (minecraft.fontRenderer.getStringWidth(label) / 2);
			drawString(matrixStack, minecraft.fontRenderer, label, (int) (x + width + centerX), y + 3, labelColour);
		}
		
	}

	@Override
	public void playDownSound(SoundHandler soundHandler) {
		soundHandler.play(SimpleSound.master(ModSounds.menu_in.get(), 1.0F, 1.0F));
	}
}
