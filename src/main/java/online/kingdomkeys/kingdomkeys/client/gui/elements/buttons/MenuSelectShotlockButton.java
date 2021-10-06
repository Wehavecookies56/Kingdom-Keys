package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import java.awt.Color;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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

public class MenuSelectShotlockButton extends MenuButtonBase {

	String shotlockName;
	boolean selected;
	int colour, labelColour;
	MenuShotlockSelectorScreen parent;
	Minecraft minecraft;

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
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Font fr = minecraft.font;
		isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		Color col = Color.decode(String.valueOf(colour));
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ItemCategory category = ItemCategory.SHOTLOCK;
		
		Shotlock shotlock = null;
		if(shotlockName != null && !shotlockName.equals("")) {
			shotlock = ModShotlocks.registry.getValue(new ResourceLocation(shotlockName));
		}
		
		if (visible) {
			Lighting.setupForFlatItems();
			float itemWidth = parent.width * 0.29F;
			minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.pushPose();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
			matrixStack.translate(x + 0.6F, y, 0);
			matrixStack.scale(0.5F, 0.5F, 1);
			blit(matrixStack, 0, 0, 166, 34, 18, 28);
			for (int i = 0; i < (itemWidth * 2) - (17 + 17); i++) {
				blit(matrixStack, 17 + i, 0, 184, 34, 2, 28);
			}
			blit(matrixStack, (int) ((itemWidth * 2) - 17), 0, 186, 34, 17, 28);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			blit(matrixStack, 6, 4, category.getU(), category.getV(), 20, 20);
			matrixStack.popPose();
			String shName;
			if (shotlock == null || shotlock.equals("")) { //Name to display
				shName = "---";
			} else {
				shName = shotlock.getTranslationKey();
			}
			drawString(matrixStack, minecraft.font, Utils.translateToLocal(shName), x + 15, y + 3, 0xFFFFFF);
			
			if (selected || isHovered) { //Render stuff on the right
				minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
				matrixStack.pushPose();
				{
					RenderSystem.enableBlend();
					matrixStack.translate(x + 0.6F, y, 0);
					matrixStack.scale(0.5F, 0.5F, 1);
					blit(matrixStack, 0, 0, 128, 34, 18, 28);
					for (int i = 0; i < (itemWidth * 2) - (17 * 2); i++) {
						blit(matrixStack, 17 + i, 0, 146, 34, 2, 28);
					}
					blit(matrixStack, (int) ((itemWidth * 2) - 17), 0, 148, 34, 17, 28);
				}
				matrixStack.popPose();
			}
			Lighting.setupForFlatItems();
			float labelWidth = parent.width * 0.15F;
			minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
			matrixStack.pushPose();
			{
				RenderSystem.enableBlend();
				//RenderSystem.enableAlpha();
				RenderSystem.setShaderColor(col.getRed() / 128F, col.getGreen() / 128F, col.getBlue() / 128F, 1);
				matrixStack.translate(x + width + 14F, y, 0);
				matrixStack.scale(0.5F, 0.5F, 1);
				
				blit(matrixStack, 0, 0, 219, 34, 15, 28);
				for (int i = 0; i < (labelWidth * 2) - (17 + 14); i++) {
					blit(matrixStack, 14 + i, 0, 184, 34, 2, 28);
				}
				blit(matrixStack, (int) ((labelWidth * 2) - 17), 0, 186, 34, 17, 28);
			}
			matrixStack.popPose();
			String label = shotlock == null ? "N/A" : "Max: "+shotlock.getMaxLocks();
			float centerX = (labelWidth / 2) - (minecraft.font.width(label) / 2);
			drawString(matrixStack, minecraft.font, label, (int) (x + width + centerX) + 14, y + 3, labelColour);
		}
		
	}

	@Override
	public void playDownSound(SoundManager soundHandler) {
		soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_in.get(), 1.0F, 1.0F));
	}
}
