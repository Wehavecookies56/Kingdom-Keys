package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class MenuAbilitiesButton extends MenuButtonBase {

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	private int endWidth = 11;

	private int leftU = 47, middleU = 59, rightU = 61;
	private int vPos = 118;
	private int selectedVPos = 138;
	

	private int middleWidth;
	private int apMiddleWidth;

	public AbilityType abilityType;

	private String text;

	private boolean selected;
	private int ap;
	Minecraft minecraft;
	public boolean equipped = false;
	public int index = 0;
	public boolean isVisual = false;
	
	public MenuAbilitiesButton(int x, int y, int widthIn, String buttonText, Ability.AbilityType type, Button.OnPress onPress) {
		super(x, y, 22 + widthIn, 20, buttonText, onPress);
		text = buttonText;
		middleWidth = widthIn;
		apMiddleWidth = widthIn/3;
		abilityType = type;
		minecraft = Minecraft.getInstance();
	}

	public MenuAbilitiesButton(int buttonPosX, int buttonPosY, int buttonWidth, String abilityName, int finalJ, AbilityType type, Button.OnPress onPress) {
		this(buttonPosX, buttonPosY, buttonWidth, abilityName, type, onPress);
		index = finalJ;
	}

	@ParametersAreNonnullByDefault
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX > x+1 && mouseY >= y+1 && mouseX < x + width-1 && mouseY < y + height-1;
		
		if (visible) {
			matrixStack.pushPose();
			renderColor();
			
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			RenderSystem.setShaderTexture(0, texture);
			if (isHovered && active) { //Hovered button
				drawButton(matrixStack, isHovered);
				drawString(matrixStack, minecraft.font, getMessage().getString().substring(getMessage().getString().indexOf(":")+1), x + 20, y + 6, new Color(255, 255, 255).hashCode());
				if(abilityType != AbilityType.WEAPON && abilityType != AbilityType.ACCESSORY) {
					drawString(matrixStack, minecraft.font, "AP", x +endWidth + middleWidth+ apMiddleWidth-5, y + 6, new Color(255, 255, 0).hashCode());
					drawString(matrixStack, minecraft.font, ap+"", x +endWidth + middleWidth+ apMiddleWidth+10, y + 6, new Color(255,255,255).hashCode());
				}
			} else {
				if(active) {//Not hovered but fully visible
					drawButton(matrixStack, isHovered);
					drawString(matrixStack, minecraft.font, getMessage(), x + 20, y + 6, new Color(255, 255, 255).hashCode());
					if(abilityType != AbilityType.WEAPON && abilityType != AbilityType.ACCESSORY) {
						drawString(matrixStack, minecraft.font, "AP", x +endWidth + middleWidth+ apMiddleWidth-5, y + 6, new Color(255, 255, 0).hashCode());
						drawString(matrixStack, minecraft.font, ap+"", x +endWidth + middleWidth+ apMiddleWidth+10, y + 6, new Color(255,255,255).hashCode());
					}
				} else {//Not hovered and selected (not fully visible)
					drawButton(matrixStack, isHovered);
					drawString(matrixStack, minecraft.font, getMessage(), x + 20, y + 6, new Color(100,100,100).hashCode());
					if(abilityType != AbilityType.WEAPON && abilityType != AbilityType.ACCESSORY) {
						drawString(matrixStack, minecraft.font, "AP", x +endWidth + middleWidth+ apMiddleWidth-5, y + 6, new Color(255, 255, 0).hashCode());
						drawString(matrixStack, minecraft.font, ap+"", x +endWidth + middleWidth+ apMiddleWidth+10, y + 6, new Color(255,255,255).hashCode());
					}
				}
			}
			matrixStack.popPose();
		}
	}
	
	private void renderColor() {
		if (abilityType != null) {
			switch (abilityType) {
			case ACTION:
				RenderSystem.setShaderColor(0, 0, 0.4F, 1.0F);
				break;
			case GROWTH:
				RenderSystem.setShaderColor(0.4F, 0.4F, 0, 1.0F);
				break;
			case SUPPORT:
				RenderSystem.setShaderColor(0, 0.4F, 0, 1.0F);
				break;
			case WEAPON:
				RenderSystem.setShaderColor(0.4F, 0, 0, 1.0F);
				break;
			case ACCESSORY:
				RenderSystem.setShaderColor(0F, 0.5F, 0.7F, 1.0F);
			}
		}
	}

	private void drawButton(PoseStack matrixStack, boolean hovered) {
		//Ability name
		matrixStack.pushPose();
		{
			blit(matrixStack, x, y, leftU, vPos, endWidth, height);
			for (int i = 0; i < middleWidth; i++) {
				blit(matrixStack, x + i + endWidth, y, middleU, vPos, 1, height);
			}
			blit(matrixStack, x + endWidth + middleWidth, y, rightU, vPos, endWidth, height);
		}
		matrixStack.popPose();
		
		if(abilityType != AbilityType.WEAPON && abilityType != AbilityType.ACCESSORY) {
			//AP Cost
			RenderSystem.setShaderColor(0.3F, 0.24F, 0, 1.0F);
			blit(matrixStack, x+middleWidth+endWidth+10, y-1, 72, 117, endWidth, height);
			for (int i = 0; i < apMiddleWidth; i++) {
				blit(matrixStack, x +middleWidth+ i + endWidth+19, y, middleU, vPos, 1, height);
			}
			blit(matrixStack, x + endWidth + middleWidth+apMiddleWidth +19, y, rightU, vPos, endWidth, height);
		}
		//Equipped/Unequipped icon
		matrixStack.pushPose();
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			//System.out.println(index);
			equipped = ModCapabilities.getPlayer(Minecraft.getInstance().player).isAbilityEquipped(text, index) || isVisual;
			if(!equipped && abilityType != AbilityType.WEAPON && abilityType != AbilityType.ACCESSORY) {
				blit(matrixStack, x+6, y+4, 74, 102, 12, 12);
			} else {
				blit(matrixStack, x+6, y+4, 87, 102, 12, 12);
			}
		}
		matrixStack.popPose();
		
		//Hovered outline
		if(hovered) {
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				blit(matrixStack, x, y, leftU, selectedVPos, endWidth, height);
				for (int i = 0; i < middleWidth; i++)
					blit(matrixStack, x + i + endWidth, y, middleU, selectedVPos, 1, height);
				blit(matrixStack, x + endWidth + middleWidth, y, rightU, selectedVPos, endWidth, height);
			}
			matrixStack.popPose();
		}
		
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if(isHovered && abilityType != AbilityType.WEAPON && abilityType != AbilityType.ACCESSORY && !isVisual)
			return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		else 
			return false;
	}

	public boolean isHovered() {
		return isHovered && visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getText() {
		return text;
	}

	@Override
	public void playDownSound(SoundManager soundHandlerIn) {
		soundHandlerIn.play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F, 1.0F));
	}

	public void setAP(int apCost) {
		this.ap = apCost;
	}

}
