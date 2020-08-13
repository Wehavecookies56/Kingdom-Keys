package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.BaseKKGuiButton;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class GuiKeychainListItem extends BaseKKGuiButton {

	ItemStack stack;
	boolean selected;
	int colour, labelColour;
	GuiWeapons parent;
	int slot;
	Minecraft minecraft;

	public GuiKeychainListItem(ItemStack item, int slot, int x, int y, int widthIn, GuiWeapons parent, int colour, Button.IPressable onPress) {
		super(x, y, widthIn, 20, "", onPress);
		this.stack = item;
		width = (int) (parent.width * 0.264F);
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		this.slot = slot;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.color4f(1, 1, 1, 1);
		//ItemCategory category = ItemCategory.TOOL;
		
		KeychainItem item;
		if(ItemStack.areItemStacksEqual(stack, new ItemStack(Items.AIR))) {
			item = null;
		} else {
			item = (KeychainItem) stack.getItem();
		}
		if (visible) {
			RenderHelper.disableStandardItemLighting();
			RenderHelper.setupGuiFlatDiffuseLighting();
			float itemWidth = parent.width * 0.264F;
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			RenderSystem.pushMatrix();
			RenderSystem.enableBlend();
			//RenderSystem.enableAlpha();
			RenderSystem.color4f(col.getRed(), col.getGreen(), col.getBlue(), 1);
			RenderSystem.translated(x + 0.6F, y, 0);
			RenderSystem.scaled(0.5F, 0.5F, 1);
			blit(0, 0, 166, 34, 18, 28);
			for (int i = 0; i < (itemWidth * 2) - (17 + 17); i++) {
				blit(17 + i, 0, 184, 34, 2, 28);
			}
			blit((int) ((itemWidth * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.color4f(1, 1, 1, 1);
			//blit(6, 4, category.getU(), category.getV(), 20, 20);
			RenderSystem.popMatrix();
			String itemName;
			if (item == null) { //Name to display
				itemName = "---";
			} else {
				itemName = new ItemStack(item.getKeyblade()).getDisplayName().getFormattedText();
			}
			drawString(minecraft.fontRenderer, itemName, x + 15, y + 3, 0xFFFFFF);
			
			if (selected || isHovered) { //Render stuff on the right
				minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				RenderSystem.pushMatrix();
				RenderSystem.enableBlend();
				//RenderSystem.enableAlpha();
				RenderSystem.translated(x + 0.6F, y, 0);
				RenderSystem.scaled(0.5F, 0.5F, 1);
				blit(0, 0, 128, 34, 18, 28);
				for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
					blit(17 + i, 0, 146, 34, 2, 28);
				}
				blit((int) ((itemWidth * 2) - 17), 0, 148, 34, 17, 28);
				RenderSystem.popMatrix();
				
				if(item != null && item.getKeyblade() != null) {
					float iconPosX = parent.width * 0.6374F;
					float iconPosY = parent.height * 0.1833F;
					float iconHeight = parent.height * 0.3148F;
					RenderHelper.disableStandardItemLighting();
					RenderHelper.setupGuiFlatDiffuseLighting();
					RenderSystem.pushMatrix();
					//RenderSystem.enableAlpha();
					RenderSystem.translated(iconPosX, iconPosY, 0);
					RenderSystem.scaled((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
					minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(item.getKeyblade()), 0, 0);

					//minecraft.getRenderManager().renderItem().renderItemAndEffectIntoGUI(new ItemStack(item.getKeyblade()), 0, 0);
					RenderSystem.popMatrix();
					float strPosX = parent.width * 0.6104F;
					float strPosY = parent.height * 0.5185F;
					float strNumPosX = parent.width * 0.7473F;
					float magPosY = parent.height * 0.5657F;
					
					String strengthStr = String.valueOf(((int) item.getKeyblade().getStrength()));
					String magicStr = String.valueOf(((int) item.getKeyblade().getMagic()));
					int strength = ModCapabilities.getPlayer(minecraft.player).getStrength() + ((int) item.getKeyblade().getStrength());
					int magic = ModCapabilities.getPlayer(minecraft.player).getMagic() + ((int) item.getKeyblade().getMagic());
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
					drawString(minecraft.fontRenderer, "Strength", (int) strPosX, (int) strPosY, 0xEE8603);
					drawString(minecraft.fontRenderer, strengthStr, (int) strNumPosX, (int) strPosY, 0xFFFFFF);
					drawString(minecraft.fontRenderer, openBracketStr+ String.valueOf(strength), (int) strNumPosX + minecraft.fontRenderer.getStringWidth(strengthStr), (int) strPosY, 0xBF6004);
					drawString(minecraft.fontRenderer, String.valueOf(strength), (int) strNumPosX + minecraft.fontRenderer.getStringWidth(strengthStr) + minecraft.fontRenderer.getStringWidth(openBracketStr), (int) strPosY, 0xFBEA21);
					drawString(minecraft.fontRenderer, " ]", (int) strNumPosX + minecraft.fontRenderer.getStringWidth(strengthStr) + minecraft.fontRenderer.getStringWidth(openBracketStr) + minecraft.fontRenderer.getStringWidth(String.valueOf(strength)), (int) strPosY, 0xBF6004);
					
					drawString(minecraft.fontRenderer, "Magic", (int) strPosX, (int) magPosY, 0xEE8603);
					drawString(minecraft.fontRenderer, magicStr, (int) strNumPosX, (int) magPosY, 0xFFFFFF);
					drawString(minecraft.fontRenderer, openBracketMag, (int) strNumPosX + minecraft.fontRenderer.getStringWidth(magicStr), (int) magPosY, 0xBF6004);
					drawString(minecraft.fontRenderer, String.valueOf(magic), (int) strNumPosX + minecraft.fontRenderer.getStringWidth(magicStr) + minecraft.fontRenderer.getStringWidth(openBracketMag), (int) magPosY, 0xFBEA21);
					drawString(minecraft.fontRenderer, " ]", (int) strNumPosX + minecraft.fontRenderer.getStringWidth(magicStr) + minecraft.fontRenderer.getStringWidth(openBracketMag) + minecraft.fontRenderer.getStringWidth(String.valueOf(magic)), (int) magPosY, 0xBF6004);
					
					float tooltipPosX = parent.width * 0.3333F;
					float tooltipPosY = parent.height * 0.8F;
					minecraft.fontRenderer.drawSplitString(item.getKeyblade().getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
				}
			}
			RenderHelper.disableStandardItemLighting();
			RenderHelper.setupGuiFlatDiffuseLighting();
			float labelWidth = parent.width * 0.1953F;
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			RenderSystem.pushMatrix();
			{
				RenderSystem.enableBlend();
				//RenderSystem.enableAlpha();
				RenderSystem.color4f(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
				RenderSystem.translated(x + width + 2.1F, y, 0);
				RenderSystem.scaled(0.5F, 0.5F, 1);
				blit(0, 0, 219, 34, 15, 28);
				
				for (int i = 0; i < (labelWidth * 2) - (17 + 14); i++) {
					blit(14 + i, 0, 184, 34, 2, 28);
				}
				blit((int) ((labelWidth * 2) - 17), 0, 186, 34, 17, 28);
			}
			RenderSystem.popMatrix();
			String label = "N/A";
			/*if (item != null && item.getKeyblade() instanceof KeybladeItem) {
				KeybladeItem itemRealKeyblade = (KeybladeItem) item.getKeyblade();
				label = (itemRealKeyblade.getAbility() != null) ? Utils.translateToLocal(itemRealKeyblade.getAbility().getName()): "N/A";
			}*/
			float centerX = (labelWidth / 2) - (minecraft.fontRenderer.getStringWidth(label) / 2);
			drawString(minecraft.fontRenderer, label, (int) (x + width + centerX), y + 3, labelColour);
		}
		
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (visible && active) {
			if (mouseX >= x && mouseX <= x + width) {
				if (mouseY >= y && mouseY <= y + height) {
					float truePos = (float) (mouseY - y); // scrollOffset
					// 36 = element height, need to change to be actual height
					int index = (int) (truePos) / 36;
					// parent.itemSelected = (int) (truePos) / 36;
					//PacketDispatcher.sendToServer(new EquipKeychain(stack, slot, parent.chainSlot));
					minecraft.displayGuiScreen(null);
					// minecraft.displayGuiScreen(new GuiMenu_Items_Player());
					//playPressSound(minecraft.getSoundHandler());
					return true;
				}
			}
		}
		return false;
	}

	/*@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.select, 1.0F));
	}*/

}
