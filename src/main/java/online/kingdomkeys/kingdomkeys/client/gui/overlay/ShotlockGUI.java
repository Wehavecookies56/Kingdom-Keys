package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class ShotlockGUI extends OverlayBase {
	float focusBarWidth;
	int guiWidth = 100;
	int guiHeight = 70;
	int noborderguiwidth = 98;
	int noborderguiheight = 68;
	IPlayerCapabilities playerData;

	@Override
	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		super.render(gui, poseStack, partialTick, width, height);
		if (ClientEvents.focusing) {
			GuiOverlayManager.enableOverlay(ForgeGui.CROSSHAIR_ELEMENT, false);
		} else {
			GuiOverlayManager.enableOverlay(ForgeGui.CROSSHAIR_ELEMENT, true);
		}

		Player player = minecraft.player;

		//minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		float rawScale = 1f;
		switch (minecraft.options.guiScale) {
			case Constants.SCALE_AUTO:
				rawScale = 0.85F;
				break;
		}
		float scaleX = rawScale * ModConfigs.focusXScale/100F;
		float scaleY = rawScale * ModConfigs.focusYScale/100F;
		
		playerData = ModCapabilities.getPlayer(player);
		if(playerData == null || playerData.getMaxFocus() <= 0)
			return;

		focusBarWidth = (int) (playerData.getFocus());
		poseStack.pushPose();
		{
			poseStack.pushPose();
			{

				RenderSystem.enableBlend();
				poseStack.translate(ModConfigs.focusXPos + 17, ModConfigs.focusYPos - 25, 0);

				poseStack.pushPose();// Focus Background
				{
					poseStack.translate((screenWidth - guiWidth * scaleX) - 20 * scaleX, (screenHeight - guiHeight * scaleY) - 7 * scaleY, 0);
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusBarBack(poseStack, 0, 0, guiWidth, 1);
				}
				poseStack.popPose();

				poseStack.pushPose();// Focus Cost Bar
				{
					poseStack.translate((screenWidth - guiWidth * scaleX) - 19 * scaleX, (screenHeight - (guiHeight) * scaleY) - 8 * scaleY, 0);
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusCostBarTop(poseStack, 0, 0, (float)(ClientEvents.focusGaugeTemp), playerData.getFocus(), 1);
				}
				poseStack.popPose();

				poseStack.pushPose();// Focus Bar
				{
					poseStack.translate((screenWidth - guiWidth * scaleX) - 19 * scaleX, (screenHeight - (guiHeight) * scaleY) - 8 * scaleY, 0);
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusBarTop(poseStack, 0, 0, (float)(ClientEvents.focusGaugeTemp), 1);
				}
				poseStack.popPose();
			}
			poseStack.popPose();

			if(ClientEvents.focusing) { //GUI itslef
				int guiWidth = 256;
				int guiHeight = 256;

				float focusScale = 400/100F;
				float size = 6;

				poseStack.pushPose();
				{
					poseStack.pushPose();
					{
						RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus.png"));
						poseStack.translate((screenWidth / 2) - (guiWidth / 2) * focusScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * focusScale / size - 0.5F, 0);
						poseStack.scale(focusScale / size, focusScale / size, focusScale / size);
						if(ClientEvents.focusGaugeTemp<= 0)
							RenderSystem.setShaderColor(1, 0, 0, 1);
						this.blit(poseStack, 0, 0, 0, 0, guiWidth, guiHeight);

						if(ClientEvents.focusGaugeTemp> 0) {
							RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus2.png"));
							double max = playerData.getFocus();
							double actual = ClientEvents.focusGaugeTemp;
							int topOffset = 25;
							int botOffset = 31;

							int realGuiHeight = (guiHeight-botOffset) - topOffset;
							int n = (int)(actual * realGuiHeight / max);
							blit(poseStack, 0, (guiHeight-botOffset)-n, 0, (guiHeight-botOffset ) - n, guiWidth, n);
						}
						RenderSystem.setShaderColor(1, 1, 1, 1);

					}
					poseStack.popPose();
				}
				poseStack.popPose();
			}
			RenderSystem.disableBlend();
		}
		poseStack.popPose();
	}

	public void drawFocusBarBack(PoseStack matrixStack, float posX, float posY, float width, float scale) {
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
		matrixStack.pushPose();
		{
			matrixStack.translate((posX) * scale, posY * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
		}
		matrixStack.popPose();
	}
	
	public void drawFocusCostBarTop(PoseStack matrixStack, float posX, float posY, float amount, double focus, float scale) {
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
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
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"));
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
