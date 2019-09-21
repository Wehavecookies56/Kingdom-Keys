package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import uk.co.wehavecookies56.kk.api.menu.IItemCategory;
import uk.co.wehavecookies56.kk.api.menu.ItemCategory;
import uk.co.wehavecookies56.kk.api.menu.ItemCategoryRegistry;
import uk.co.wehavecookies56.kk.client.gui.GuiMenu_Items_Player;
import uk.co.wehavecookies56.kk.client.gui.GuiStock;
import uk.co.wehavecookies56.kk.client.sound.ModSounds;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.item.base.ItemKeyblade;
import uk.co.wehavecookies56.kk.common.item.base.ItemKeychain;
import uk.co.wehavecookies56.kk.common.item.base.ItemOrgWeapon;
import uk.co.wehavecookies56.kk.common.item.org.IOrgWeapon;
import uk.co.wehavecookies56.kk.common.lib.Reference;

import java.awt.*;

public class GuiEquippedItem extends GuiButton {

	GuiScreen screenToOpen;
	ItemStack item;
	boolean selected;
	int colour, labelColour;
	GuiMenu_Items_Player parent;
	String label;
	boolean hasLabel;
	ItemCategory category;

	public GuiEquippedItem(ItemStack item, int x, int y, int colour, GuiScreen screenToOpen, ItemCategory category, GuiMenu_Items_Player parent) {
		super(0, x, y, "");
		this.item = item;
		width = (int) (parent.width * 0.264F);
		height = 14;
		this.colour = colour;
		this.screenToOpen = screenToOpen;
		this.parent = parent;
		hasLabel = false;
		this.category = category;
	}

	public GuiEquippedItem(ItemStack item, int x, int y, int colour, GuiScreen screenToOpen, ItemCategory category, GuiMenu_Items_Player parent, String label, int labelColour) {
		super(0, x, y, "");
		this.item = item;
		width = (int) (parent.width * 0.264F);
		height = 14;
		this.colour = colour;
		this.screenToOpen = screenToOpen;
		this.parent = parent;
		hasLabel = true;
		this.label = label;
		this.labelColour = labelColour;
		this.category = category;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		
		float itemY = parent.height * 0.1907F;
		float bottomY = parent.height - (parent.height * 0.25F);
		if(this.y < itemY-1 || this.y > bottomY-1)
			return;
		
		hovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		Color col = Color.decode(String.valueOf(colour));
		GlStateManager.color(1, 1, 1, 1);
		float labelWidth = parent.width * 0.1348F;
		float gradientWidth = parent.width * 0.1515F;
		if (visible) {
			float itemWidth = parent.width * 0.264F;
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
			GlStateManager.pushMatrix();
			{
				GlStateManager.enableBlend();

				GlStateManager.enableAlpha();
				RenderHelper.enableGUIStandardItemLighting();
				RenderHelper.disableStandardItemLighting();
				// GlStateManager.color(1,1,1,1);
				GlStateManager.color(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
				GlStateManager.translate(x + 0.6F, y, 0);
				GlStateManager.scale(0.5F, 0.5F, 1);
				//Gradient background
				for (int i = 0; i < height * 2; i++) {
					GlStateManager.pushMatrix();
					{
						GlStateManager.scale(((gradientWidth + itemWidth + 5) * 2) / (32F), 1.1F, 1);
						drawTexturedModalRect(-13, i - 1F, 166, 63, 32, 1);
					}
					GlStateManager.popMatrix();
				}

				// Left item slot
				drawTexturedModalRect(0, 0, 166, 34, 17, 28);
				// Middle item slot
				for (int i = 0; i < (itemWidth * 2) - (17 + 17); i++) {
					drawTexturedModalRect(17 + i, 0, 184, 34, 1, 28);
				}
				// Right item slot
				drawTexturedModalRect((itemWidth * 2) - 17, 0, 186, 34, 17, 28);
				GlStateManager.color(1, 1, 1, 1);
				//Icon
				drawTexturedModalRect(6, 4, category.getU(), category.getV(), 20, 20);
			}
			GlStateManager.popMatrix();
			if (item != null) {
				String itemName = item.getDisplayName();
				if (item.getItem() instanceof ItemKeychain) {
					itemName = new ItemStack(((ItemKeychain) item.getItem()).getKeyblade()).getDisplayName();
				} else if (ItemStack.areItemStacksEqual(item, ItemStack.EMPTY)) {
					itemName = "---";
				}
				drawString(mc.fontRenderer, itemName, x + 15, y + 3, 0xFFFFFF);
			} else {
				drawString(mc.fontRenderer, "---", x + 15, y + 3, 0xFFFFFF);
			}
			if (selected || hovered) {
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
				GlStateManager.pushMatrix();
				{
					GlStateManager.enableAlpha();
					GlStateManager.translate(x + 0.6F, y, 0);
					GlStateManager.scale(0.5F, 0.5F, 1);
					// Left selected
					drawTexturedModalRect(0, 0, 128, 34, 17, 28);
					// Middle selected
					for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
						drawTexturedModalRect(17 + i, 0, 146, 34, 1, 28);
					}
					// Right selected
					drawTexturedModalRect((itemWidth * 2) - 17, 0, 148, 34, 17, 28);
				}
				GlStateManager.popMatrix();
				float iconPosX = parent.width * 0.6374F;
				float iconPosY = parent.height * 0.1833F;
				float iconHeight = parent.height * 0.3148F;
				if (item != null) {
					if (item.getItem() instanceof ItemKeychain) {
						ItemStack keyblade = new ItemStack(((ItemKeychain) item.getItem()).getKeyblade());
						// Render keyblade in the GUI
						GlStateManager.pushMatrix();
						{
							GlStateManager.enableAlpha();
							GlStateManager.translate(iconPosX, iconPosY, 0);
							GlStateManager.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
							Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(keyblade, 0, 0);
						}
						GlStateManager.popMatrix();
						float strPosX = parent.width * 0.6104F;
						float strPosY = parent.height * 0.5185F;
						float strNumPosX = parent.width * 0.7473F;
						float magPosY = parent.height * 0.5657F;
						String strengthStr = String.valueOf(((int) ((ItemKeyblade) keyblade.getItem()).getStrength()));
						String magicStr = String.valueOf(((int) ((ItemKeyblade) keyblade.getItem()).getMagic()));
						int strength = mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getStrength() + ((int) ((ItemKeyblade) keyblade.getItem()).getStrength());
						int magic = mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getMagic() + ((int) ((ItemKeyblade) keyblade.getItem()).getMagic());
						String openBracketStr = " [  ";
						String openBracketMag = " [  ";
						String totalStr = String.valueOf(strength);
						String totalMag = String.valueOf(magic);
						if (totalStr.length() == 1) {
							openBracketStr += " ";
						}
						if (totalMag.length() == 1) {
							openBracketMag += " ";
						}
						drawString(mc.fontRenderer, "Strength", (int) strPosX, (int) strPosY, 0xEE8603);
						drawString(mc.fontRenderer, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
						drawString(mc.fontRenderer, openBracketStr, (int) strNumPosX + mc.fontRenderer.getStringWidth(strengthStr), (int) strPosY, 0xBF6004);
						drawString(mc.fontRenderer, String.valueOf(strength), (int) strNumPosX + mc.fontRenderer.getStringWidth(strengthStr) + mc.fontRenderer.getStringWidth(openBracketStr), (int) strPosY, 0xFBEA21);
						drawString(mc.fontRenderer, " ]", (int) strNumPosX + mc.fontRenderer.getStringWidth(strengthStr) + mc.fontRenderer.getStringWidth(openBracketStr) + mc.fontRenderer.getStringWidth(String.valueOf(strength)), (int) strPosY, 0xBF6004);
						drawString(mc.fontRenderer, "Magic", (int) strPosX, (int) magPosY, 0xEE8603);
						drawString(mc.fontRenderer, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
						drawString(mc.fontRenderer, openBracketMag, (int) strNumPosX + mc.fontRenderer.getStringWidth(magicStr), (int) magPosY, 0xBF6004);
						drawString(mc.fontRenderer, String.valueOf(magic), (int) strNumPosX + mc.fontRenderer.getStringWidth(magicStr) + mc.fontRenderer.getStringWidth(openBracketMag), (int) magPosY, 0xFBEA21);
						drawString(mc.fontRenderer, " ]", (int) strNumPosX + mc.fontRenderer.getStringWidth(magicStr) + mc.fontRenderer.getStringWidth(openBracketMag) + mc.fontRenderer.getStringWidth(String.valueOf(magic)), (int) magPosY, 0xBF6004);
						float tooltipPosX = parent.width * 0.3333F;
						float tooltipPosY = parent.height * 0.8F;
						mc.fontRenderer.drawSplitString(((ItemKeyblade) keyblade.getItem()).description, (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
					} else if (item.getItem() instanceof ItemArmor) {
						ItemArmor armour = (ItemArmor) item.getItem();
						int armourAmount = armour.getArmorMaterial().getDamageReductionAmount(armour.armorType);

						GlStateManager.pushMatrix();
						GlStateManager.translate(iconPosX, iconPosY, 0);
						GlStateManager.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
						Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(armour), 0, 0);
						GlStateManager.popMatrix();
					} else if (item.getItem() instanceof IOrgWeapon) {
						IOrgWeapon weapon = (IOrgWeapon) item.getItem();
						GlStateManager.pushMatrix();
						GlStateManager.enableAlpha();
						GlStateManager.translate(iconPosX, iconPosY, 0);
						GlStateManager.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
						Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack((Item) weapon), 0, 0);
						GlStateManager.popMatrix();
						float strPosX = parent.width * 0.6104F;
						float strPosY = parent.height * 0.5185F;
						float strNumPosX = parent.width * 0.7473F;
						float magPosY = parent.height * 0.5657F;
						String strengthStr = String.valueOf(((int) weapon.getStrength()));
						String magicStr = String.valueOf(((int) weapon.getMagic()));
						int strength = mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getStrength() + ((int) weapon.getStrength());
						int magic = mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getMagic() + ((int) weapon.getMagic());
						String openBracketStr = " [  ";
						String openBracketMag = " [  ";
						String totalStr = String.valueOf(strength);
						String totalMag = String.valueOf(magic);
						if (totalStr.length() == 1) {
							openBracketStr += " ";
						}
						if (totalMag.length() == 1) {
							openBracketMag += " ";
						}
						drawString(mc.fontRenderer, "Strength", (int) strPosX, (int) strPosY, 0xEE8603);
						drawString(mc.fontRenderer, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
						drawString(mc.fontRenderer, openBracketStr, (int) strNumPosX + mc.fontRenderer.getStringWidth(strengthStr), (int) strPosY, 0xBF6004);
						drawString(mc.fontRenderer, String.valueOf(strength), (int) strNumPosX + mc.fontRenderer.getStringWidth(strengthStr) + mc.fontRenderer.getStringWidth(openBracketStr), (int) strPosY, 0xFBEA21);
						drawString(mc.fontRenderer, " ]", (int) strNumPosX + mc.fontRenderer.getStringWidth(strengthStr) + mc.fontRenderer.getStringWidth(openBracketStr) + mc.fontRenderer.getStringWidth(String.valueOf(strength)), (int) strPosY, 0xBF6004);
						drawString(mc.fontRenderer, "Magic", (int) strPosX, (int) magPosY, 0xEE8603);
						drawString(mc.fontRenderer, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
						drawString(mc.fontRenderer, openBracketMag, (int) strNumPosX + mc.fontRenderer.getStringWidth(magicStr), (int) magPosY, 0xBF6004);
						drawString(mc.fontRenderer, String.valueOf(magic), (int) strNumPosX + mc.fontRenderer.getStringWidth(magicStr) + mc.fontRenderer.getStringWidth(openBracketMag), (int) magPosY, 0xFBEA21);
						drawString(mc.fontRenderer, " ]", (int) strNumPosX + mc.fontRenderer.getStringWidth(magicStr) + mc.fontRenderer.getStringWidth(openBracketMag) + mc.fontRenderer.getStringWidth(String.valueOf(magic)), (int) magPosY, 0xBF6004);
						float tooltipPosX = parent.width * 0.3333F;
						float tooltipPosY = parent.height * 0.8F;
						// mc.fontRenderer.drawSplitString(weapon.description, (int)tooltipPosX+3,
						// (int)tooltipPosY+3, (int)(parent.width*0.46875F), 0x43B5E9);
					}
				}
			}
			RenderHelper.enableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.color(1, 1, 1, 1);
			if (hasLabel) {
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
				GlStateManager.pushMatrix();
				{
					GlStateManager.enableAlpha();
					RenderHelper.enableGUIStandardItemLighting();
					RenderHelper.disableStandardItemLighting();
					// GlStateManager.color(1,1,1,1);
					GlStateManager.color(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
					GlStateManager.translate(x - labelWidth, y, 0);
					GlStateManager.scale(0.5F, 0.5F, 1);

					// Left label
					drawTexturedModalRect(0, 0, 166, 34, 17, 28);
					// Middle label
					for (int i = 0; i < (labelWidth * 2) - (17 + 14); i++) {
						drawTexturedModalRect(17 + i, 0, 184, 34, 1, 28);
					}
					// Right label
					drawTexturedModalRect((labelWidth * 2) - 14, 0, 204, 34, 14, 28);
				}
				GlStateManager.popMatrix();
				float centerX = (labelWidth / 2) - (mc.fontRenderer.getStringWidth(label) / 2);
				drawString(mc.fontRenderer, label, (int) (x - labelWidth + centerX), y + 3, labelColour);
			}
		}
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if (visible && enabled) {
			if (mouseX >= x && mouseX <= x + width) {
				if (mouseY >= y && mouseY <= y + height) {
					float truePos = (mouseY - y); // scrollOffset
					// 36 = element height, need to change to be actual height
					int index = (int) (truePos) / 36;
					// parent.itemSelected = (int) (truePos) / 36;
					Minecraft.getMinecraft().displayGuiScreen(screenToOpen);
					playPressSound(mc.getSoundHandler());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.select, 1.0F));
	}
}
