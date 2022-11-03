package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO cleanup + comments
public class HPGui extends OverlayBase {
	float hpBarWidth, missingHpBarWidth;
	int guiHeight = 10;

	private float playerHealth;
	private long lastSystemTime;
	private float lastPlayerHealth;

	@Override
	public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		super.render(gui, poseStack, partialTick, width, height);
		Player player = minecraft.player;
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		float scale = 1f;
		switch (minecraft.options.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
		}
		float scaleFactor = 1.5F * ModConfigs.hpXScale/100F;

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

		poseStack.pushPose();
		{
//			poseStack.scale(scale, 1, 1);

			RenderSystem.enableBlend();
			poseStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
			poseStack.pushPose();
			{
				poseStack.translate((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
				poseStack.scale(scale, scale, scale);
				drawHPBarBack(poseStack, 0, 0, hpBarMaxWidth, scale, player);
			}
			poseStack.popPose();

			poseStack.pushPose();
			{
				poseStack.translate((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
				poseStack.scale(scale, scale, scale);
				drawHPBarTop(poseStack, 0, 0, hpBarWidth, scale, player);
			}
			poseStack.popPose();
			poseStack.pushPose(); // Red portion of the bar
			{
				poseStack.translate((screenWidth - (hpBarWidth + missingHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
				poseStack.scale(scale, scale, scale);
				drawDamagedHPBarTop(poseStack, 0, 0, missingHpBarWidth, scale, player);
			}
			poseStack.popPose();
			RenderSystem.disableBlend();
		}
		poseStack.popPose();
	}

	public void drawHPBarBack(PoseStack matrixStack, int posX, int posY, float width, float scale, Player player) {
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
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
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(matrixStack, 0, -1, 2, 12, 1, 8);
		}
		matrixStack.popPose();

	}
	
	public void drawDamagedHPBarTop(PoseStack matrixStack, int posX, int posY, float width, float scale, LivingEntity player) {
		RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(matrixStack,0, -1, 2, 22, 1, 8);
		}
		matrixStack.popPose();
	}

}
