package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
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

	private float playerHealth;
	private long lastSystemTime;
	private float lastPlayerHealth;
	
	public HPGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		PlayerEntity player = minecraft.player;
		MatrixStack matrixStack = event.getMatrixStack();
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
			
			float i = (player.getHealth());
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

			matrixStack.push();
			{
				RenderSystem.enableBlend();
				matrixStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
				matrixStack.push();
				{
					matrixStack.translate((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
					matrixStack.scale(scale, scale, scale);
					drawHPBarBack(matrixStack, 0, 0, hpBarMaxWidth, scale, player);
				}
				matrixStack.pop();
	
				matrixStack.push();
				{
					matrixStack.translate((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					matrixStack.scale(scale, scale, scale);
					drawHPBarTop(matrixStack, 0, 0, hpBarWidth, scale, player);
				}
				matrixStack.pop();
				matrixStack.push(); // Red portion of the bar
				{
					matrixStack.translate((screenWidth - (hpBarWidth + missingHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					matrixStack.scale(scale, scale, scale);
					drawDamagedHPBarTop(matrixStack, 0, 0, missingHpBarWidth, scale, player);
				}
				matrixStack.pop();
				RenderSystem.disableBlend();
			}
			matrixStack.pop();
		}
	}

	public void drawHPBarBack(MatrixStack matrixStack, int posX, int posY, float width, float scale, PlayerEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			// Left
			matrixStack.push();
			{
				matrixStack.translate(scale * posX, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.pop();

			// Middle
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale, posY * scale, 0);
				matrixStack.scale(width, scale, 0);
				int v = Utils.isPlayerLowHP(player) ? 8 : 2;
				blit(matrixStack, 0, 0, v, 0, 1, 12);
			}
			matrixStack.pop();

			// Right
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.pop();
		}
		matrixStack.pop();

	}

	public void drawHPBarTop(MatrixStack matrixStack, int posX, int posY, float width, float scale, PlayerEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(matrixStack, 0, -1, 2, 12, 1, 8);
		}
		matrixStack.pop();

	}
	
	public void drawDamagedHPBarTop(MatrixStack matrixStack, int posX, int posY, float width, float scale, LivingEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(matrixStack,0, -1, 2, 22, 1, 8);
		}
		matrixStack.pop();
	}

}
