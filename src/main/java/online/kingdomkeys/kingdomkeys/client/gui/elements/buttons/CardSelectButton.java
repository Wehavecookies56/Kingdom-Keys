package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion.CardSelectionScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CardSelectButton extends MenuButtonBase {

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	/*private int endWidth = 11;

	private int bLeftU = 0, bMiddleU = 12, bRightU = 14;
	private int bVPos = 0;
	private int bSelectedVPos = 20;

	private int sbLeftU = 22, sbMiddleU = 34, sbRightU = 36;
	private int sbVPos = 118;
	private int sbSelectedVPos = 138;

	private int middleWidth;

	private String data, tip;
	
	public enum ButtonType {
		BUTTON, SUBBUTTON
	}

	AbilityType abilityType;

	ButtonType type;
	private boolean selected;*/
	
	ItemStack stack;
	MapCardItem card;
	
	Minecraft minecraft;
	CardSelectionScreen parent;
	
	public CardSelectButton(int x, int y, int widthIn, int heightIn, ItemStack stack, CardSelectionScreen cardSelectionScreen, Button.OnPress onPress) {
		super(x, y, widthIn, heightIn, Utils.translateToLocal(""), onPress);
		System.out.println(x);
		minecraft = Minecraft.getInstance();
		this.stack = stack;
		card = (MapCardItem)stack.getItem();
		parent=cardSelectionScreen;
	}

	@ParametersAreNonnullByDefault
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		//if(!isSelected())
			isHovered = mouseX > x + 1 && mouseY >= y + 1 && mouseX < x + width - 1 && mouseY < y + height - 1;
		
		if(isHovered()) {
			selected = false;
		}

		matrixStack.pushPose();
		{
			if(visible) {
	        	matrixStack.translate(x, y, 0);
	        	if(isHovered) {
	        		matrixStack.scale(4,4, 1);
	            	matrixStack.translate(-2,-2, 20);
	        	} else {
	        		matrixStack.scale(3,3, 1);
	        	}
	
				ClientUtils.drawItemAsIcon(stack, matrixStack, 0,0, 16);
				matrixStack.translate(9, 10, 150);
				int color = 0xFFFF00;
				if(card.getCardValue(stack) < parent.te.getNumber()) {
					color = 0xAAAAAA;
				}
	    		matrixStack.scale(0.7F,0.7F, 1);
				drawString(matrixStack, minecraft.font,""+card.getCardValue(stack), 0, 0, color);
			}
		}
		matrixStack.popPose();
		
		matrixStack.pushPose();
		{
			if(isHovered) {
	        	matrixStack.translate(30, 100, 0);
				drawCenteredString(matrixStack, minecraft.font,Utils.translateToLocal(stack.getItem().getName(stack).getString()), 26, -20, 0xFFFFFF);

	        	matrixStack.scale(5,5, 1);
	            matrixStack.translate(-2.5F,-2.5F, 20);
				ClientUtils.drawItemAsIcon(stack, matrixStack, 0,0, 16);
	        	matrixStack.translate(2, 16.5, 150);
	        	matrixStack.scale(0.2F,0.2F, 1);
				drawString(matrixStack, minecraft.font,"Value: "+card.getCardValue(stack), 0, 0, 0xFFFFFF);
				drawString(matrixStack, minecraft.font,"Room size: "+card.getRoomType().getProperties().getSize(), 0, 10, 0xFFFFFF);
				drawString(matrixStack, minecraft.font,"Enemies: "+card.getRoomType().getProperties().getEnemies(), 0, 20, 0xFFFFFF);
				drawString(matrixStack, minecraft.font,"Category: "+card.getRoomType().getProperties().getCategory(), 0, 30, 0xFFFFFF);
				
			}
		}
		matrixStack.popPose();

		/*if (visible) {
			matrixStack.pushPose();
			RenderSystem.setShaderColor(1, 1, 1, 1);

			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			RenderSystem.setShaderTexture(0, texture);
			if (isHovered && active) { // Hovered button
				x += 10;
				drawButton(matrixStack, true);
				drawString(matrixStack, minecraft.font, getMessage(), x + 12, y + 6, new Color(255, 255, 255).hashCode());
				x -= 10;
			} else {
				if (active) {// Not hovered but fully visible
					drawButton(matrixStack, false);
					drawString(matrixStack, minecraft.font, getMessage(), x + 12, y + 6, new Color(255, 255, 255).hashCode());
				} else {// Not hovered and selected (not fully visible)
					if (selected) {
						x += 10;
						drawButton(matrixStack, false);
						drawString(matrixStack, minecraft.font, getMessage(), x + 12, y + 6, new Color(100, 100, 100).hashCode());
						x -= 10;
					} else {
						drawButton(matrixStack, false);
						drawString(matrixStack, minecraft.font, getMessage(), x + 12, y + 6, new Color(100, 100, 100).hashCode());
					}
				}
			}
			RenderSystem.setShaderColor(1, 1, 1, 1);
			matrixStack.popPose();
		}*/
	}

	private void drawButton(PoseStack matrixStack, boolean hovered) {
		int leftU = 0, middleU = 0, rightU = 0;
		int vPos = 0, selVPos = 0;
		/*switch (type) { // Set the local values to the corresponding fields
		case BUTTON:
			leftU = bLeftU;
			middleU = bMiddleU;
			rightU = bRightU;
			vPos = bVPos;
			selVPos = bSelectedVPos;
			break;
		case SUBBUTTON:
			leftU = sbLeftU;
			middleU = sbMiddleU;
			rightU = sbRightU;
			vPos = sbVPos;
			selVPos = sbSelectedVPos;
			break;
		}*/

		//vPos = hovered || selected ? selVPos : vPos;

		/*blit(matrixStack, x, y, leftU, vPos, endWidth, height);
		for (int i = 0; i < middleWidth; i++)
			blit(matrixStack, x + i + endWidth, y, middleU, vPos, 1, height);
		blit(matrixStack, x + endWidth + middleWidth, y, rightU, vPos, endWidth, height);*/

	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (isHovered && active)
			return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		else
			return false;
	}

	public boolean isHovered() {
		return isHovered;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public void playDownSound(SoundManager soundHandlerIn) {
		soundHandlerIn.play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F, 1.0F));
	}


}
