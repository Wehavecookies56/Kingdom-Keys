package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO cleanup + comments
public class HPGui extends Screen {
	float hpBarWidth, missingHpBarWidth;
	int guiHeight = 10;

	int counter = 0;
	private int playerHealth;
	private long lastSystemTime;
	private int lastPlayerHealth;

	public HPGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		PlayerEntity player = minecraft.player;
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			if (!ModConfigs.hpShowHearts) {
				event.setCanceled(true);
			}
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

			hpBarWidth = (player.getHealth() * scaleFactor);
			int hpBarMaxWidth = (int) (player.getMaxHealth() * scaleFactor);

			int i = MathHelper.ceil(player.getHealth());
			long j = Util.milliTime();
			if (i < this.playerHealth && player.hurtResistantTime > 0) {
				this.lastSystemTime = j;
			} else if (i > this.playerHealth && player.hurtResistantTime > 0) {
				this.lastSystemTime = j;
			}

			if (j - this.lastSystemTime > 1000L || this.playerHealth < player.getHealth()) { // If 1 second since last attack has passed update variables
				this.playerHealth = i;
				this.lastPlayerHealth = i;
				this.lastSystemTime = j;
			}

			missingHpBarWidth = Math.max(((lastPlayerHealth - player.getHealth()) * scaleFactor),0);

			RenderSystem.pushMatrix();
			{
				RenderSystem.enableBlend();

				RenderSystem.translatef(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
				RenderSystem.pushMatrix(); // Bar Background
				{
					RenderSystem.translatef((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
					RenderSystem.scalef(scale, scale, scale);
					drawHPBarBack(0, 0, hpBarMaxWidth, scale, player);
				}
				RenderSystem.popMatrix();

				RenderSystem.pushMatrix(); // HP Bar
				{
					RenderSystem.translatef((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					RenderSystem.scalef(scale, scale, scale);
					drawHPBarTop(0, 0, hpBarWidth, scale, player);
				}
				RenderSystem.popMatrix();

				RenderSystem.pushMatrix(); // Red portion of the bar
				{
					RenderSystem.translatef((screenWidth - (hpBarWidth + missingHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					RenderSystem.scalef(scale, scale, scale);
					drawDamagedHPBarTop(0, 0, missingHpBarWidth, scale, player);
				}
				RenderSystem.popMatrix();
				RenderSystem.disableBlend();

			}
			RenderSystem.popMatrix();
		}
	}

	public void drawDamagedHPBarTop(int posX, int posY, float width, float scale, PlayerEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef((posX + 2) * scale, (posY + 2) * scale, 0);
			RenderSystem.scalef(width, scale, 0);

			blit(0, -1, 2, 22, 1, 8);
		}
		RenderSystem.popMatrix();
	}

	public void drawHPBarBack(int posX, int posY, float width, float scale, PlayerEntity player) {
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
				int v = Utils.isPlayerLowHP(player) ? 8 : 2;
				blit(0, 0, v, 0, 1, 12);
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


	public void drawHPBarTop(int posX, int posY, float width, float scale, PlayerEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef((posX + 2) * scale, (posY + 2) * scale, 0);
			RenderSystem.scalef(width, scale, 0);

			blit(0, -1, 2, 12, 1, 8);
		}
		RenderSystem.popMatrix();

	}

}
