package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
		super(new TranslatableComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		Player player = minecraft.player;
		PoseStack matrixStack = event.getMatrixStack();

		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			//minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));

			int screenWidth = minecraft.getWindow().getGuiScaledWidth();
			int screenHeight = minecraft.getWindow().getGuiScaledHeight();

			float scale = 1f;
			switch (minecraft.options.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
			}
			float scaleFactor = 1F;
			playerData = ModCapabilities.getPlayer(player);
			if(playerData == null || playerData.getMaxFocus() <= 0)
				return;
			
			focusBarWidth = (int) (playerData.getFocus() * scaleFactor);
			matrixStack.pushPose();
			{
				matrixStack.pushPose();
				{
	
					RenderSystem.enableBlend();
					matrixStack.translate(ModConfigs.focusXPos + 30, ModConfigs.focusYPos - 16, 0);
	
					matrixStack.pushPose();// Focus Background
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - 20*scale, (screenHeight - guiHeight * scale) - 7 * scale, 0);
						matrixStack.scale(scale, scale, scale);
						drawFocusBarBack(matrixStack, 0, 0, guiWidth, scale);
					}
					matrixStack.popPose();
					
					matrixStack.pushPose();// Focus Cost Bar
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - 19 * scale, (screenHeight - (guiHeight) * scale) - 8 * scale, 0);
						matrixStack.scale(scale, scale, scale);
						drawFocusCostBarTop(matrixStack, 0, 0, (float)(ClientEvents.focusGaugeTemp), playerData.getFocus(), scale);
					}
					matrixStack.popPose();
					
					matrixStack.pushPose();// Focus Bar
					{
						matrixStack.translate((screenWidth - guiWidth * scale) - 19 * scale, (screenHeight - (guiHeight) * scale) - 8 * scale, 0);
						matrixStack.scale(scale, scale, scale);
						drawFocusBarTop(matrixStack, 0, 0, (float)(ClientEvents.focusGaugeTemp), scale);
					}
					matrixStack.popPose();
				}
				matrixStack.popPose();
				
				if(ClientEvents.focusing) { //GUI itslef
					int guiWidth = 256;
					int guiHeight = 256;

					float focusScale = 400/100F;
					float size = 6;

					matrixStack.pushPose();
					{
						RenderSystem.pushMatrix();
						{
							minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus.png"));
							matrixStack.translate((screenWidth / 2) - (guiWidth / 2) * focusScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * focusScale / size - 0.5F, 0);
							matrixStack.scale(focusScale / size, focusScale / size, focusScale / size);
							if(ClientEvents.focusGaugeTemp<= 0)
								RenderSystem.color4f(1, 0, 0, 1);
							this.blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
							
							if(ClientEvents.focusGaugeTemp> 0) {
								minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus2.png"));
								double max = playerData.getFocus();
								double actual = ClientEvents.focusGaugeTemp;
								int topOffset = 25;
								int botOffset = 31;
								
								int realGuiHeight = (guiHeight-botOffset) - topOffset;
								int n = (int)(actual * realGuiHeight / max);
								blit(matrixStack, 0, (guiHeight-botOffset)-n, 0, (guiHeight-botOffset ) - n, guiWidth, n);
							}
							RenderSystem.color4f(1, 1, 1, 1);

						}
						RenderSystem.popMatrix();
					}
					matrixStack.popPose();
				}
				RenderSystem.disableBlend();
			}
			matrixStack.popPose();
		}
	}

	public void drawFocusBarBack(PoseStack matrixStack, float posX, float posY, float width, float scale) {
		minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.pushPose();
		{
			matrixStack.translate((posX) * scale, posY * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
		}
		matrixStack.popPose();
	}
	
	public void drawFocusCostBarTop(PoseStack matrixStack, float posX, float posY, float amount, double focus, float scale) {
		minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.pushPose();
		{
			//int w = (int) (amount * 100F / noborderguiwidth);
			int h = (int) (focus * noborderguiheight / 100);
			matrixStack.translate((posX) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, noborderguiheight-h, 0, 208 - h, noborderguiwidth, h);
		}
		matrixStack.popPose();
	}
	
	public void drawFocusBarTop(PoseStack matrixStack, float posX, float posY, float amount, float scale) {
		minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.pushPose();
		{
			//int w = (int) (amount * 100F / noborderguiwidth);
			int h = (int) (amount * noborderguiheight / 100F);
			matrixStack.translate(posX * scale, (posY + 2) * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, noborderguiheight-h, 0, 139 - h, noborderguiwidth, h);
		}
		matrixStack.popPose();
	}
}
