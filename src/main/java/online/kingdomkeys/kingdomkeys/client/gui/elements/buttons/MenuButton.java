package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

public class MenuButton extends MenuButtonBase {

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	private int endWidth = 11;

	private int bLeftU = 0, bMiddleU = 12, bRightU = 14;
	private int bVPos = 0;
	private int bSelectedVPos = 20;

	private int sbLeftU = 22, sbMiddleU = 34, sbRightU = 36;
	private int sbVPos = 118;
	private int sbSelectedVPos = 138;

	private int rbLeftU = 22, rbMiddleU = 34, rbRightU = 36;
	private int rbVPos = 158;
	private int rbSelectedVPos = 178;

	private int middleWidth;

	private int offset;

	private String data, tip;

	public void setCenterText(boolean b) {
		this.centerText = b;
	}

	public enum ButtonType {
		BUTTON, SUBBUTTON, ROUNDBUTTON
	}

	ButtonType type;
	private boolean selected;
	private boolean centerText;

	public MenuButton(int x, int y, int widthIn, String buttonText, ButtonType type, Button.OnPress onPress) {
		super(x, y, 22 + widthIn, 20, Utils.translateToLocal(buttonText), onPress);
		middleWidth = widthIn;
		this.type = type;
	}

	public MenuButton(int x, int y, int widthIn, String buttonText, ButtonType type, boolean hasTooltip, Button.OnPress onPress) {
		this(x, y, widthIn, buttonText, type, onPress);
		if(hasTooltip)
			this.tip = buttonText+".desc";
	}

	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getTip() {
		return tip;
	}
	
	public void setTip(String tip) {
		this.tip = tip;
	}

	@ParametersAreNonnullByDefault
	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX > getX() + 1 && mouseY >= getY() + 1 && mouseX < getX() + width - 1 && mouseY < getY() + height - 1;
		PoseStack matrixStack = gui.pose();
		if (visible) {
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);

				RenderSystem.enableBlend();
				RenderSystem.setShaderTexture(0, texture);
				int btnMargin = 8;
				int textX = centerText ? getX() + getWidth() / 2 - Minecraft.getInstance().font.width(getMessage()) / 2 : getX() + btnMargin;

				if (isHovered && active) { // Hovered button
					setX(getX() + offset);
					drawButton(gui, true);
					ClientUtils.drawScrollingString(gui,Minecraft.getInstance().font,getMessage(),textX+offset,textX+offset+getWidth()-btnMargin*2,getY()+6,new Color(255,255,255).hashCode());
					setX(getX() - offset);
				} else {
					if (active) {// Not hovered but fully visible
						drawButton(gui, false);
						ClientUtils.drawScrollingString(gui,Minecraft.getInstance().font,getMessage(),textX,textX+getWidth()-btnMargin*2,getY()+6,new Color(255,255,255).hashCode());
					} else {// Not hovered and selected (not fully visible)
						if (selected) {
							setX(getX() + offset);
							drawButton(gui, false);
							ClientUtils.drawScrollingString(gui,Minecraft.getInstance().font,getMessage(),textX+offset,textX+offset+getWidth()-btnMargin*2,getY()+6,new Color(100,100,100).hashCode());
							setX(getX() - offset);
						} else {
							drawButton(gui, false);
							ClientUtils.drawScrollingString(gui,Minecraft.getInstance().font,getMessage(),textX,textX+getWidth()-btnMargin*2,getY()+6,new Color(100,100,100).hashCode());
						}
					}
				}

				RenderSystem.disableBlend();
				RenderSystem.setShaderColor(1, 1, 1, 1);
			}
			matrixStack.popPose();
		}
	}

	private void drawButton(GuiGraphics gui, boolean hovered) {
		int leftU = 0, middleU = 0, rightU = 0;
		int vPos = 0, selVPos = 0;
		switch (type) { // Set the local values to the corresponding fields
		case BUTTON:
			leftU = bLeftU;
			middleU = bMiddleU;
			rightU = bRightU;
			vPos = bVPos;
			selVPos = bSelectedVPos;
			offset = 10;
			break;
		case SUBBUTTON:
			leftU = sbLeftU;
			middleU = sbMiddleU;
			rightU = sbRightU;
			vPos = sbVPos;
			selVPos = sbSelectedVPos;
			offset = 10;
			break;
		case ROUNDBUTTON:
			leftU = rbLeftU;
			middleU = rbMiddleU;
			rightU = rbRightU;
			vPos = rbVPos;
			selVPos = rbSelectedVPos;
			offset = 0;
			break;
		}

		vPos = hovered || selected ? selVPos : vPos;

		gui.blit(texture, getX(), getY(), leftU, vPos, endWidth, height);
		for (int i = 0; i < middleWidth; i++)
			gui.blit(texture, getX() + i + endWidth, getY(), middleU, vPos, 1, height);
		gui.blit(texture, getX() + endWidth + middleWidth, getY(), rightU, vPos, endWidth, height);

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

	@Override
	public void setWidth(int pWidth) {
		super.setWidth(pWidth);
		middleWidth = pWidth;
	}
}
