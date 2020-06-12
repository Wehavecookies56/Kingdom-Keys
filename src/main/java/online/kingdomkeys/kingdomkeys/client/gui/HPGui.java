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
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class HPGui extends Screen {
	int hpBarWidth;
	int guiHeight = 10;

	int counter = 0;

	public HPGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		// if (!MainConfig.displayGUI())
		// return;
		PlayerEntity player = minecraft.player;
		// if (!player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			// if (!MainConfig.client.hud.EnableHeartsOnHUD)
			// event.setCanceled(true);
		}
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

			int screenWidth = minecraft.getMainWindow().getScaledWidth();
			int screenHeight = minecraft.getMainWindow().getScaledHeight();

			float scale = 1f;
			switch (minecraft.gameSettings.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
			}
			float scaleFactor = 1.5F;

			hpBarWidth = (int) (player.getHealth() * scaleFactor);
			int hpBarMaxWidth = (int) (player.getMaxHealth() * scaleFactor);

			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
				RenderSystem.scalef(scale, scale, scale);
				drawHPBarBack(0, 0, hpBarMaxWidth, scale);
			}
			RenderSystem.popMatrix();

			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
				RenderSystem.scalef(scale, scale, scale);
				drawHPBarTop(0, 0, (int) Math.ceil(hpBarWidth), scale, player);
			}
			RenderSystem.popMatrix();
		}
	}

	public void drawHPBarBack(int posX, int posY, int width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			// Left
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(scale * posX, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 0, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// Middle
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale, posY * scale, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 0, 1, 12);
			}
			RenderSystem.popMatrix();

			// Right
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale + width, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 3, 0, 2, 12);
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();

	}

	public void drawHPBarTop(int posX, int posY, int width, float scale, PlayerEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef((posX + 2) * scale, (posY + 2) * scale, 0);
			RenderSystem.scalef(width, scale, 0);
			int v = player.getHealth() >= player.getMaxHealth() / 4 ? 12 : 22;
			blit(0, -1, 2, v, 1, 8);
		}
		RenderSystem.popMatrix();

	}

}
