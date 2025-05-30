package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO cleanup + comments
public class ShotlockGUI extends OverlayBase {

	public static final ShotlockGUI INSTANCE = new ShotlockGUI();
	float focusBarWidth;
	int guiWidth = 44;
	int guiHeight = 122;
	int noborderguiwidth = 42;
	int noborderguiheight = 120;
	PlayerData playerData;

	public ResourceLocation focusBar = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focusbar.png");
	private ShotlockGUI() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);

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
		
		playerData = PlayerData.get(player);
		if(playerData == null || playerData.getMaxFocus() <= 0)
			return;

		focusBarWidth = (int) (playerData.getFocus());

		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		{
			poseStack.pushPose();
			{

				RenderSystem.enableBlend();
				poseStack.translate(ModConfigs.focusXPos + 38, ModConfigs.focusYPos -10, 0);
				poseStack.translate((screenWidth - guiWidth * scaleX) - 20 * scaleX, (screenHeight - (guiHeight) * scaleY) - 8 * scaleY, 0);
				poseStack.mulPose(Axis.ZP.rotationDegrees(50));

				poseStack.pushPose();// Focus Background
				{
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusBarBack(guiGraphics, 0, 0, guiWidth, 1);
				}
				poseStack.popPose();

				poseStack.pushPose();// Focus Cost Bar
				{
					poseStack.scale(scaleX, scaleY, 1);
					drawFocusCostBarTop(guiGraphics, 0, 0, playerData.getFocus(), 1);
				}
				poseStack.popPose();

				poseStack.pushPose();// Focus Bar
				{
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
						Shotlock shotlock = Utils.getPlayerShotlock(minecraft.player);
						playerData = PlayerData.get(minecraft.player);
						if(playerData == null)
							return;

						poseStack.translate((screenWidth / 2) - (guiWidth / 2) * focusScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * focusScale / size - 0.5F, 0);
						poseStack.scale(focusScale / size, focusScale / size, focusScale / size);
						if(ClientEvents.focusGaugeTemp<= 0)
							RenderSystem.setShaderColor(1, 0, 0, 1);
						this.blit(guiGraphics, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus.png"), 0, 0, 0, 0, guiWidth, guiHeight);
						poseStack.pushPose();
						{
							poseStack.scale(2,2,2);
							this.drawString(guiGraphics, minecraft.font, playerData.getShotlockEnemies().size() + "/" + shotlock.getMaxLocks(), guiWidth/2, guiHeight / 4 - minecraft.font.lineHeight / 2, 0x88CC33);
						}
						poseStack.popPose();
						if(ClientEvents.focusGaugeTemp> 0) {
							double max = playerData.getFocus();
							double actual = ClientEvents.focusGaugeTemp;
							int topOffset = 25;
							int botOffset = 31;

							int realGuiHeight = (guiHeight-botOffset) - topOffset;
							int n = (int)(actual * realGuiHeight / max);
							blit(guiGraphics, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/focus2.png"), 0, (guiHeight-botOffset)-n, 0, (guiHeight-botOffset ) - n, guiWidth, n);
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
			blit(gui, focusBar, 0, 0, 0, 0, guiWidth, guiHeight);
		}
		matrixStack.popPose();
	}

	public void drawFocusCostBarTop(GuiGraphics gui, float posX, float posY, double focus, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			int h = (int) (focus * noborderguiheight / 100);
			matrixStack.translate((posX) * scale, posY * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, focusBar, 0, noborderguiheight-h+1, 88, 120 - h, noborderguiwidth+1, h);
		}
		matrixStack.popPose();
	}
	
	public void drawFocusBarTop(GuiGraphics gui, float posX, float posY, float amount, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			int h = (int) (amount * noborderguiheight / 100F);
			matrixStack.translate((posX) * scale, posY * scale, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, focusBar, 0, noborderguiheight-h+1, 44, 120 - h, noborderguiwidth+1, h);
		}
		matrixStack.popPose();
	}
}
