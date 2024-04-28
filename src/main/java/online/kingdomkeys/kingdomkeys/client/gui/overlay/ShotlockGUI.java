package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class ShotlockGUI extends OverlayBase {

	public static final ShotlockGUI INSTANCE = new ShotlockGUI();
	float focusBarWidth;
	int guiWidth = 100;
	int guiHeight = 70;
	int noborderguiwidth = 98;
	int noborderguiheight = 68;
	IPlayerCapabilities playerData;

	private ShotlockGUI() {
		super();
	}

	@SubscribeEvent
	public void renderOverlays(RenderGuiOverlayEvent.Pre event) {
		/*if (ClientEvents.focusing && event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
			event.setCanceled(true);
		}*/
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);

		Player player = minecraft.player;
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();

		float rawScale = 1f;
		switch (minecraft.options.guiScale().get()) {
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

		PoseStack poseStack = guiGraphics.pose();

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
					drawFocusBarBack(guiGraphics, 0, 0, guiWidth, 1);
				}
				poseStack.popPose();

				poseStack.pushPose();// Focus Cost Bar
				{
					poseStack.translate((screenWidth - guiWidth * scaleX) - 19 * scaleX, (screenHeight - (guiHeight) * scaleY) - 8 * scaleY, 0);
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusCostBarTop(guiGraphics, 0, 0, (float)(ClientEvents.focusGaugeTemp), playerData.getFocus(), 1);
				}
				poseStack.popPose();

				poseStack.pushPose();// Focus Bar
				{
					poseStack.translate((screenWidth - guiWidth * scaleX) - 19 * scaleX, (screenHeight - (guiHeight) * scaleY) - 8 * scaleY, 0);
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusBarTop(guiGraphics, 0, 0, (float)(ClientEvents.focusGaugeTemp), 1);
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
						poseStack.translate((screenWidth / 2) - (guiWidth / 2) * focusScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * focusScale / size - 0.5F, 0);
						poseStack.scale(focusScale / size, focusScale / size, focusScale / size);
						if(ClientEvents.focusGaugeTemp<= 0)
							RenderSystem.setShaderColor(1, 0, 0, 1);
						this.blit(guiGraphics, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus.png"), 0, 0, 0, 0, guiWidth, guiHeight);

						if(ClientEvents.focusGaugeTemp> 0) {
							double max = playerData.getFocus();
							double actual = ClientEvents.focusGaugeTemp;
							int topOffset = 25;
							int botOffset = 31;

							int realGuiHeight = (guiHeight-botOffset) - topOffset;
							int n = (int)(actual * realGuiHeight / max);
							blit(guiGraphics, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focus2.png"), 0, (guiHeight-botOffset)-n, 0, (guiHeight-botOffset ) - n, guiWidth, n);
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

	public void drawFocusBarBack(GuiGraphics gui, float posX, float posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX) * scale, posY * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"), 0, 0, 0, 0, guiWidth, guiHeight);
		}
		matrixStack.popPose();
	}
	
	public void drawFocusCostBarTop(GuiGraphics gui, float posX, float posY, float amount, double focus, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			//int w = (int) (amount * 100F / noborderguiwidth);
			int h = (int) (focus * noborderguiheight / 100);
			matrixStack.translate((posX) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"), 0, noborderguiheight-h, 0, 208 - h, noborderguiwidth, h);
		}
		matrixStack.popPose();
	}
	
	public void drawFocusBarTop(GuiGraphics gui, float posX, float posY, float amount, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			//int w = (int) (amount * 100F / noborderguiwidth);
			int h = (int) (amount * noborderguiheight / 100F);
			matrixStack.translate(posX * scale, (posY + 2) * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, new ResourceLocation(KingdomKeys.MODID, "textures/gui/focusbar.png"), 0, noborderguiheight-h, 0, 139 - h, noborderguiwidth, h);
		}
		matrixStack.popPose();
	}
}
