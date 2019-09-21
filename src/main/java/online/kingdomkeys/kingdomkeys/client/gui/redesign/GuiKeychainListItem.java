package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.kk.api.menu.IItemCategory;
import uk.co.wehavecookies56.kk.api.menu.ItemCategory;
import uk.co.wehavecookies56.kk.client.gui.GuiMenu_Items_Player;
import uk.co.wehavecookies56.kk.client.sound.ModSounds;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.item.base.ItemKeyblade;
import uk.co.wehavecookies56.kk.common.item.base.ItemKeychain;
import uk.co.wehavecookies56.kk.common.item.base.ItemRealKeyblade;
import uk.co.wehavecookies56.kk.common.lib.Reference;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.server.EquipKeychain;
import uk.co.wehavecookies56.kk.common.util.Utils;

import java.awt.*;

public class GuiKeychainListItem extends GuiButton {

	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	GuiWeapons parent;
	int slot;

	public GuiKeychainListItem(ItemStack item, int slot, int x, int y, GuiWeapons parent, int colour) {
		super(0, x, y, "");
		this.stack = item;
		width = (int) (parent.width * 0.264F);
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		this.slot = slot;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		hovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		Color col = Color.decode(String.valueOf(colour));
		GlStateManager.color(1, 1, 1, 1);
		ItemCategory category = ItemCategory.TOOL;
		ItemKeychain item = (ItemKeychain) stack.getItem();
		if (visible) {
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			float itemWidth = parent.width * 0.264F;
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.color(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
			GlStateManager.translate(x + 0.6F, y, 0);
			GlStateManager.scale(0.5F, 0.5F, 1);
			drawTexturedModalRect(0, 0, 166, 34, 17, 28);
			for (int i = 0; i < (itemWidth * 2) - (17 + 17); i++) {
				drawTexturedModalRect(17 + i, 0, 184, 34, 1, 28);
			}
			drawTexturedModalRect((itemWidth * 2) - 17, 0, 186, 34, 17, 28);
			GlStateManager.color(1, 1, 1, 1);
			drawTexturedModalRect(6, 4, category.getU(), category.getV(), 20, 20);
			GlStateManager.popMatrix();
			String itemName;
			if (item == null) {
				itemName = "---";
			} else {
				itemName = new ItemStack(item.getKeyblade()).getDisplayName();
			}
			drawString(mc.fontRenderer, itemName, x + 15, y + 3, 0xFFFFFF);
			if (selected || hovered) {
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.translate(x + 0.6F, y, 0);
				GlStateManager.scale(0.5F, 0.5F, 1);
				drawTexturedModalRect(0, 0, 128, 34, 17, 28);
				for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
					drawTexturedModalRect(17 + i, 0, 146, 34, 1, 28);
				}
				drawTexturedModalRect((itemWidth * 2) - 17, 0, 148, 34, 17, 28);
				GlStateManager.popMatrix();
				float iconPosX = parent.width * 0.6374F;
				float iconPosY = parent.height * 0.1833F;
				float iconHeight = parent.height * 0.3148F;
				RenderHelper.disableStandardItemLighting();
				RenderHelper.enableGUIStandardItemLighting();
				GlStateManager.pushMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.translate(iconPosX, iconPosY, 0);
				GlStateManager.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
				Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(item.getKeyblade()), 0, 0);
				GlStateManager.popMatrix();
				float strPosX = parent.width * 0.6104F;
				float strPosY = parent.height * 0.5185F;
				float strNumPosX = parent.width * 0.7473F;
				float magPosY = parent.height * 0.5657F;
				String strengthStr = String.valueOf(((int) item.getKeyblade().getStrength()));
				String magicStr = String.valueOf(((int) item.getKeyblade().getMagic()));
				int strength = mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getStrength() + ((int) item.getKeyblade().getStrength());
				int magic = mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getMagic() + ((int) item.getKeyblade().getMagic());
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
				mc.fontRenderer.drawSplitString(item.getKeyblade().description, (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
			}
			RenderHelper.disableStandardItemLighting();
			RenderHelper.enableGUIStandardItemLighting();
			float labelWidth = parent.width * 0.1953F;
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
			GlStateManager.pushMatrix();
			{
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.color(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
				GlStateManager.translate(x + width + 2.1F, y, 0);
				GlStateManager.scale(0.5F, 0.5F, 1);
				drawTexturedModalRect(0, 0, 219, 34, 14, 28);
				
				for (int i = 0; i < (labelWidth * 2) - (17 + 14); i++) {
					drawTexturedModalRect(14 + i, 0, 184, 34, 1, 28);
				}
				drawTexturedModalRect((labelWidth * 2) - 17, 0, 186, 34, 17, 28);
			}
			GlStateManager.popMatrix();
			String label = "N/A";
			if (item.getKeyblade() instanceof ItemKeyblade) {
				ItemKeyblade itemRealKeyblade = (ItemKeyblade) item.getKeyblade();
				label = (itemRealKeyblade.getAbility() != null) ? Utils.translateToLocal(itemRealKeyblade.getAbility().getName()): "N/A";
			}
			float centerX = (labelWidth / 2) - (mc.fontRenderer.getStringWidth(label) / 2);
			drawString(mc.fontRenderer, label, (int) (x + width + centerX), y + 3, labelColour);
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
					PacketDispatcher.sendToServer(new EquipKeychain(stack, slot, parent.chainSlot));
					mc.displayGuiScreen(null);
					// mc.displayGuiScreen(new GuiMenu_Items_Player());
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
