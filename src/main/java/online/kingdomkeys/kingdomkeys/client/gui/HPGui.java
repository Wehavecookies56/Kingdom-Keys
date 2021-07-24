package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
		super(new TranslatableComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		Player player = minecraft.player;
		PoseStack matrixStack = event.getMatrixStack();
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			if (!ModConfigs.hpShowHearts) {
				event.setCanceled(true);
			}
		}
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
			int screenWidth = minecraft.getWindow().getGuiScaledWidth();
			int screenHeight = minecraft.getWindow().getGuiScaledHeight();

			float scale = 1f;
			switch (minecraft.options.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
			}
			float scaleFactor = 1.5F;

			hpBarWidth = (player.getHealth() * scaleFactor);
			int hpBarMaxWidth = (int) (player.getMaxHealth() * scaleFactor);
			
			float i = (player.getHealth());
			long j = Util.getMillis();
			if (i < this.playerHealth && player.invulnerableTime > 0) {
				this.lastSystemTime = j;
			} else if (i > this.playerHealth && player.invulnerableTime > 0) {
				this.lastSystemTime = j;
			}

			if (j - this.lastSystemTime > 1000L || this.playerHealth < player.getHealth()) { // If 1 second since last attack has passed update variables
				this.playerHealth = i;
				this.lastPlayerHealth = i;
				this.lastSystemTime = j;
			}

			missingHpBarWidth = Math.max(((lastPlayerHealth - player.getHealth()) * scaleFactor),0);

			matrixStack.pushPose();
			{
				RenderSystem.enableBlend();
				matrixStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
				matrixStack.pushPose();
				{
					matrixStack.translate((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
					matrixStack.scale(scale, scale, scale);
					drawHPBarBack(matrixStack, 0, 0, hpBarMaxWidth, scale, player);
				}
				matrixStack.popPose();
	
				matrixStack.pushPose();
				{
					matrixStack.translate((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					matrixStack.scale(scale, scale, scale);
					drawHPBarTop(matrixStack, 0, 0, hpBarWidth, scale, player);
				}
				matrixStack.popPose();
				matrixStack.pushPose(); // Red portion of the bar
				{
					matrixStack.translate((screenWidth - (hpBarWidth + missingHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					matrixStack.scale(scale, scale, scale);
					drawDamagedHPBarTop(matrixStack, 0, 0, missingHpBarWidth, scale, player);
				}
				matrixStack.popPose();
				RenderSystem.disableBlend();
			}
			matrixStack.popPose();
		}
	}

	public void drawHPBarBack(PoseStack matrixStack, int posX, int posY, float width, float scale, Player player) {
		minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.pushPose();
		{
			// Left
			matrixStack.pushPose();
			{
				matrixStack.translate(scale * posX, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.popPose();

			// Middle
			matrixStack.pushPose();
			{
				matrixStack.translate((posX + 2) * scale, posY * scale, 0);
				matrixStack.scale(width, scale, 0);
				int v = Utils.isPlayerLowHP(player) ? 8 : 2;
				blit(matrixStack, 0, 0, v, 0, 1, 12);
			}
			matrixStack.popPose();

			// Right
			matrixStack.pushPose();
			{
				matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();

	}

	public void drawHPBarTop(PoseStack matrixStack, int posX, int posY, float width, float scale, Player player) {
		minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(matrixStack, 0, -1, 2, 12, 1, 8);
		}
		matrixStack.popPose();

	}
	
	public void drawDamagedHPBarTop(PoseStack matrixStack, int posX, int posY, float width, float scale, LivingEntity player) {
		minecraft.textureManager.bindForSetup(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(matrixStack,0, -1, 2, 22, 1, 8);
		}
		matrixStack.popPose();
	}

}
