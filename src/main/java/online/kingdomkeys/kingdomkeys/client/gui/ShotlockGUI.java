package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class ShotlockGUI extends Screen {
	float focusBarWidth;
	int guiWidth = 100;
	int guiHeight = 70;
	int noborderguiwidth = 98;
	int noborderguiheight = 68;
	IPlayerCapabilities playerData;

	public ShotlockGUI() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		PlayerEntity player = minecraft.player;
		MatrixStack matrixStack = event.getMatrixStack();

		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			//minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));

			int screenWidth = minecraft.getMainWindow().getScaledWidth();
			int screenHeight = minecraft.getMainWindow().getScaledHeight();

			float scale = 1f;
			switch (minecraft.gameSettings.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
			}
			float scaleFactor = 1F;
			playerData = ModCapabilities.getPlayer(player);
			if(playerData == null || playerData.getMaxFocus() <= 0)
				return;
			
			focusBarWidth = (int) (playerData.getFocus() * scaleFactor);
			matrixStack.push();
			{
				matrixStack.push();
				{
	
					RenderSystem.enableBlend();
					matrixStack.translate(ModConfigs.focusXPos + 30, ModConfigs.focusYPos - 16, 0);
	
					matrixStack.push();// Focus Background
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - 20*scale, (screenHeight - guiHeight * scale) - 7 * scale, 0);
						matrixStack.scale(scale, scale, scale);
						drawFocusBarBack(matrixStack, 0, 0, guiWidth, scale);
					}
					matrixStack.pop();
					
					matrixStack.push();// Focus Cost Bar
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - 19 * scale, (screenHeight - (guiHeight) * scale) - 8 * scale, 0);
						matrixStack.scale(scale, scale, scale);
						drawFocusCostBarTop(matrixStack, 0, 0, (float)(ClientEvents.focusGaugeTemp), playerData.getFocus(), scale);
					}
					matrixStack.pop();
					
					matrixStack.push();// Focus Bar
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - 19 * scale, (screenHeight - (guiHeight) * scale) - 8 * scale, 0);
						matrixStack.scale(scale, scale, scale);
						drawFocusBarTop(matrixStack, 0, 0, (float)(ClientEvents.focusGaugeTemp), scale);
					}
					matrixStack.pop();
				}
				matrixStack.pop();
				
				if(ClientEvents.focusing) { //GUI itslef
					int guiWidth = 256;
					int guiHeight = 256;

					float focusScale = 400/100F;
					float size = 6;

					matrixStack.push();
					{
						RenderSystem.pushMatrix();
						{
							minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus.png"));
							matrixStack.translate((screenWidth / 2) - (guiWidth / 2) * focusScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * focusScale / size - 0.5F, 0);
							matrixStack.scale(focusScale / size, focusScale / size, focusScale / size);
							if(ClientEvents.focusGaugeTemp<= 0)
								RenderSystem.color4f(1, 0, 0, 1);
							this.blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
							RenderSystem.color4f(1, 1, 1, 1);

						}
						RenderSystem.popMatrix();
					}
					matrixStack.pop();
				}
				RenderSystem.disableBlend();
			}
			matrixStack.pop();
		}
	}

	public void drawFocusBarBack(MatrixStack matrixStack, float posX, float posY, float width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.push();
		{
			matrixStack.translate((posX) * scale, posY * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
		}
		matrixStack.pop();
	}
	
	public void drawFocusCostBarTop(MatrixStack matrixStack, float posX, float posY, float amount, double focus, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.push();
		{
			//int w = (int) (amount * 100F / noborderguiwidth);
			int h = (int) (focus * noborderguiheight / 100);
			matrixStack.translate((posX) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, noborderguiheight-h, 0, 208 - h, noborderguiwidth, h);
		}
		matrixStack.pop();
	}
	
	public void drawFocusBarTop(MatrixStack matrixStack, float posX, float posY, float amount, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.push();
		{
			//int w = (int) (amount * 100F / noborderguiwidth);
			int h = (int) (amount * noborderguiheight / 100F);
			matrixStack.translate(posX * scale, (posY + 2) * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, noborderguiheight-h, 0, 139 - h, noborderguiwidth, h);
		}
		matrixStack.pop();
	}
}
