package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuEquipmentScreen;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.equipment.MenuShotlockSelectorScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSEquipShotlock;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;

public class MenuSelectShotlockButton extends MenuButtonBase {

	String shotlockName;
	boolean selected;
	int colour, labelColour;
	MenuShotlockSelectorScreen parent;
	Minecraft minecraft;

	final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public MenuSelectShotlockButton(String shotlockName, int x, int y, int widthIn, MenuShotlockSelectorScreen parent, int colour) {
		super(x, y, widthIn, 20, "", b -> {
			if (b.visible && b.active) {
				PacketHandler.sendToServer(new CSEquipShotlock(shotlockName));
			} else {
				Minecraft.getInstance().setScreen(new MenuEquipmentScreen());
			}
		});
		
		this.shotlockName = shotlockName;
		width = (int) (parent.width * 0.264F);
		height = 14;
		this.colour = colour;
		this.labelColour = 0xFFEB1C;
		this.parent = parent;
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
        Font fr = minecraft.font;
		isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ItemCategory category = ItemCategory.SHOTLOCK;
		
		Shotlock shotlock = null;
		if(shotlockName != null && !shotlockName.equals("")) {
			shotlock = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlockName));
		}
		
		if (visible) {
			Lighting.setupForFlatItems();
			float itemWidth = parent.width * 0.292F;
			matrixStack.pushPose();
			RenderSystem.enableBlend();
			
			RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
			matrixStack.translate(getX() + 0.6F, getY(), 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			gui.blit(texture, 0, 0, 166, 34, 18, 28);
			gui.blit(texture, 16, 0, (int) ((itemWidth * 2) - (17 + 17))+1, 28, 186, 34, 2, 28, 256, 256);
			gui.blit(texture, (int) ((itemWidth * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			gui.blit(texture, 6, 4, category.getU(), category.getV(), 20, 20);
			matrixStack.popPose();
			String shName;
			if (shotlock == null || shotlock.equals("")) { //Name to display
				shName = "---";
			} else {
				shName = shotlock.getTranslationKey();
			}
			gui.drawString(minecraft.font, Utils.translateToLocal(shName), getX() + 15, getY() + 3, 0xFFFFFF);
			
			if (selected || isHovered) { //Render stuff on the right
				matrixStack.pushPose();
				{
					RenderSystem.enableBlend();
					
					matrixStack.translate(getX() + 0.6F, getY(), 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					gui.blit(texture, 0, 0, 128, 34, 18, 28);
					gui.blit(texture, 16, 0, (int) ((itemWidth * 2) - (17 * 2))+1, 28, 148, 34, 2, 28, 256, 256);
					gui.blit(texture, (int) ((itemWidth * 2) - 17), 0, 148, 34, 17, 28);
				}
				matrixStack.popPose();
			}
			Lighting.setupForFlatItems();
			float labelWidth = parent.width * 0.18F;
			RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.pushPose();
			{
				RenderSystem.enableBlend();
				//RenderSystem.enableAlpha();
				RenderSystem.setShaderColor(col.getRed() / 255F, col.getGreen() / 255F, col.getBlue() / 255F, 1);
				matrixStack.translate(getX() + width + 14, getY(), 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				
				gui.blit(texture, 0, 0, 219, 34, 15, 28);
				gui.blit(texture, 14, 0, (int) ((labelWidth * 2) - (17 + 14)), 28, 186, 34, 2, 28, 256, 256);
				gui.blit(texture, (int) ((labelWidth * 2) - 17), 0, 186, 34, 17, 28);
			}
			matrixStack.popPose();
			String label = shotlock == null ? "N/A" : "Max: "+shotlock.getMaxLocks();
			float centerX = (labelWidth / 2) - (minecraft.font.width(label) / 2);
			gui.drawString(minecraft.font, label, (int) (getX() + width + centerX) + 14, getY() + 3, labelColour);
		}
		
	}

	@Override
	public void playDownSound(SoundManager soundHandler) {
		soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_in.get(), 1.0F, 1.0F));
	}
}
