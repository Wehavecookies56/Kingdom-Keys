package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuButton extends MenuButtonBase {

	private ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	private int endWidth = 11;

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
	private boolean selected;

	public MenuButton(int x, int y, int widthIn, String buttonText, ButtonType type, Button.IPressable onPress) {
		super(x, y, 22 + widthIn, 20, Utils.translateToLocal(buttonText), onPress);
		middleWidth = widthIn;
		this.type = type;
	}

	public MenuButton(int x, int y, int widthIn, String buttonText, ButtonType type, boolean hasTooltip, Button.IPressable onPress) {
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
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		//if(!isSelected())
			isHovered = mouseX > x + 1 && mouseY >= y + 1 && mouseX < x + width - 1 && mouseY < y + height - 1;
		/*if(isHovered()) {
			selected = false;
		}*/

		if (visible) {
			matrixStack.push();
			RenderSystem.color3f(1, 1, 1);

			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			Minecraft.getInstance().textureManager.bindTexture(texture);
			if (isHovered && active) { // Hovered button
				x += 10;
				drawButton(matrixStack, true);
				drawString(matrixStack, Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(255, 255, 255).hashCode());
				x -= 10;
			} else {
				if (active) {// Not hovered but fully visible
					drawButton(matrixStack, false);
					drawString(matrixStack, Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(255, 255, 255).hashCode());
				} else {// Not hovered and selected (not fully visible)
					if (selected) {
						x += 10;
						drawButton(matrixStack, false);
						drawString(matrixStack, Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(100, 100, 100).hashCode());
						x -= 10;
					} else {
						drawButton(matrixStack, false);
						drawString(matrixStack, Minecraft.getInstance().fontRenderer, getMessage(), x + 12, y + 6, new Color(100, 100, 100).hashCode());
					}
				}
			}
			RenderSystem.color3f(1, 1, 1);
			matrixStack.pop();
		}
	}

	private void drawButton(MatrixStack matrixStack, boolean hovered) {
		int leftU = 0, middleU = 0, rightU = 0;
		int vPos = 0, selVPos = 0;
		switch (type) { // Set the local values to the corresponding fields
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

		}

		vPos = hovered || selected ? selVPos : vPos;

		blit(matrixStack, x, y, leftU, vPos, endWidth, height);
		for (int i = 0; i < middleWidth; i++)
			blit(matrixStack, x + i + endWidth, y, middleU, vPos, 1, height);
		blit(matrixStack, x + endWidth + middleWidth, y, rightU, vPos, endWidth, height);

	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		if (isHovered)
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
	public void playDownSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F, 1.0F));
	}


}
