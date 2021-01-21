package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class MPGui extends Screen {
	int guiWidth = 173;
	int mpBarWidth;
	int guiHeight = 10;
	int noborderguiwidth = 171;
	IPlayerCapabilities playerData;
	int counter = 0;

	public MPGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		// if (!MainConfig.displayGUI() || !player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		PlayerEntity player = minecraft.player;

		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			// if (!MainConfig.client.hud.EnableHeartsOnHUD)
			// event.setCanceled(true);
		}
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));

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
			if(playerData == null || playerData.getMaxMP() <= 0)
				return;
			
			mpBarWidth = (int) (playerData.getMP() * scaleFactor);
			int mpBarMaxWidth = (int) (playerData.getMaxMP() * scaleFactor);

			RenderSystem.pushMatrix();
			{
				RenderSystem.enableBlend();

				RenderSystem.translatef(ModConfigs.mpXPos, ModConfigs.mpYPos, 0);
				RenderSystem.pushMatrix();// MP Background
				{
					RenderSystem.translatef((screenWidth - mpBarMaxWidth * scale) - 80 * scale, (screenHeight - guiHeight * scale) - 9 * scale, 0);
					RenderSystem.scalef(scale, scale / 1.3F, scale);
					drawMPBarBack(0, 0, mpBarMaxWidth, scale);
				}
	
				RenderSystem.popMatrix();// MP Bar
				{
					RenderSystem.pushMatrix();
					RenderSystem.translatef((screenWidth - ((int) mpBarWidth) * scale) - 80 * scale, (screenHeight - (guiHeight) * scale) - 9 * scale, 0);
					RenderSystem.scalef(scale, scale / 1.3F, scale);
					drawMPBarTop(0, 0, (int) Math.ceil(mpBarWidth), scale);
				}
				RenderSystem.popMatrix();
				RenderSystem.disableBlend();

			}
			RenderSystem.popMatrix();
		}
	}

	public void drawMPBarBack(int posX, int posY, int width, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
		RenderSystem.pushMatrix();
		{
			// Left Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(scale * posX, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 0, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// Background
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale, posY * scale, 0);
				RenderSystem.scalef(width, scale, 0);
				int v = playerData.getRecharge() ? 8 : 2;
				blit(0, 0, v, 0, 1, 12);

			}
			RenderSystem.popMatrix();

			// Right Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale + width, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 3, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// MP Icon
			RenderSystem.pushMatrix();
			{
				int v = playerData.getRecharge() ? 45 : 32;
				RenderSystem.translatef((posX + 2) * scale + width + 1, scale * posY, 0);
				RenderSystem.scalef(scale * 0.8F, scale, 1);
				blit(0, 0, 0, v, 23, 12);
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();

	}

	public void drawMPBarTop(int posX, int posY, int width, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef((posX + 2) * scale, (posY + 2) * scale, 0);
			RenderSystem.scalef(width, scale, 0);
			int v = playerData.getRecharge() ? 22 : 12;
			blit(0, 0, 2, v, 1, 8);
		}
		RenderSystem.popMatrix();

	}
}
