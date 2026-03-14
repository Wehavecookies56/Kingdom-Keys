package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

public class MenuButton extends MenuButtonBase {
	private final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	private final int endWidth = 11;

	private final int bLeftU = 0;
	private final int bMiddleU = 12;
	private final int bRightU = 14;
	private final int bVPos = 0;
	private final int bSelectedVPos = 20;

	private final int sbLeftU = 22;
	private final int sbMiddleU = 34;
	private final int sbRightU = 36;
	private final int sbVPos = 118;
	private final int sbSelectedVPos = 138;

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
		if (hasTooltip)
			this.tip = buttonText + ".desc";
	}

	public MenuButton setCenterText(){
		this.centerText = true;
		return this;
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
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		isHovered = mouseX > getX() + 1 && mouseY >= getY() + 1 && mouseX < getX() + width - 1 && mouseY < getY() + height - 1;
		Font font = Minecraft.getInstance().font;
		PoseStack matrixStack = gui.pose();
		if (visible) {
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);

				RenderSystem.enableBlend();
				RenderSystem.setShaderTexture(0, texture);

				int btnMargin = 8;
				int textX = getX() + btnMargin;
				int activeColor = new Color(255, 255, 255).hashCode();
				int disabledColor = new Color(100, 100, 100).hashCode();

				boolean shouldOffset = (isHovered && active) || (!active && selected);
				int drawColor = (active ? activeColor : disabledColor);
				int x = getX();

				if (shouldOffset)
					setX(x + offset);

				drawButton(gui, isHovered && active, partialTicks);

				int drawX = textX + (shouldOffset ? offset : 0);
				ClientUtils.drawScrollingString(gui,font, getMessage(), drawX, drawX + getWidth() - (btnMargin*2)-1, getY() + 6, drawColor, centerText);

				if (shouldOffset)
					setX(x);

				RenderSystem.disableBlend();
				RenderSystem.setShaderColor(1, 1, 1, 1);
			}
			matrixStack.popPose();
		}
	}

	private void drawButton(GuiGraphics gui, boolean hovered, float partialTicks) {
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

		//Ball
		if(hovered) {
			float ballScale = 0.5F;
			int u = 55;
			int v = 22;

			gui.pose().pushPose();
			{
				float radiusX = 4.5F;
				float radiusY = 6F;
				float centerX = getX() + getWidth() - radiusX -3;
				float centerY = getY() + 3;

				float delta = ClientEvents.ballRot - ClientEvents.prevBallRot;

				if (delta < -180F)
					delta += 360F;
				if (delta > 180F)
					delta -= 360F;

				float interpRot = ClientEvents.prevBallRot + delta * partialTicks;

				float t = (float)Math.toRadians(-interpRot);

				float x = centerX + (float)Math.cos(t * 3F + Math.PI / 2F) * radiusX;
				float y = centerY + (float)Math.sin(t * 2F) * radiusY;

				gui.pose().pushPose();
				gui.pose().translate(x, y, 0);
				gui.pose().scale(ballScale, ballScale, 1F);

				gui.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/drivebar.png"), 0, 0, u, v, 11, 11);

				gui.pose().popPose();
			}
			gui.pose().popPose();
		}
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