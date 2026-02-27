package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

public class HPGui extends OverlayBase {

	public static final HPGui INSTANCE = new HPGui();

	private float displayedPlayerHP, realPlayerHP;
	private float displayedGummiHP, realGummiHP;

	private long playerDelayEnd = 0;
	private long gummiDelayEnd = 0;

    final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hpbar.png");

	private HPGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		if (player == null)
			return;

		PoseStack poseStack = guiGraphics.pose();

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();



        poseStack.pushPose();
        {
            poseStack.translate(screenWidth-ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
            poseStack.scale(0.2F,0.2F,0.2F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

			float health = minecraft.player.getHealth();
			float maxHealth = minecraft.player.getMaxHealth();
			float healthPercentage = health / maxHealth;

			int barWidth = 905;
			int barHeight = 241;
			int barX = 0;
			int barY = 0;
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_fill.png"));
			RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_mask.png"));
            ClientSetup.testShader.setSampler("Sampler0", 0);
			ClientSetup.testShader.setSampler("Sampler1", 1);
			ClientSetup.testShader.safeGetUniform("HealthPercentage").set(healthPercentage);
			ClientSetup.testShader.apply();
            RenderSystem.setShader(() -> ClientSetup.testShader);

            Matrix4f matrix = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
            buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
            buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
            buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
            BufferUploader.drawWithShader(buffer.buildOrThrow());

            RenderSystem.disableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
        }
        poseStack.popPose();





		float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);

		float scale = 1f;
		if (minecraft.options.guiScale().get() == Constants.SCALE_AUTO)
			scale = 0.85F;

		float scaleFactor = 1.5F * ModConfigs.hpXScale / 100F;
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		poseStack.pushPose();
		{
			poseStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);

			long now = net.minecraft.Util.getMillis();
			// Player
			float playerMaxHP = player.getMaxHealth();

			if (realPlayerHP == 0) {
				realPlayerHP = player.getHealth();
				displayedPlayerHP = player.getHealth();
			}

			float playerHP = player.getHealth();
			if (playerHP < realPlayerHP) {
				playerDelayEnd = now + 1000;
			}
			realPlayerHP = playerHP;

			if (now > playerDelayEnd) {
				displayedPlayerHP = Mth.lerp(0.05F * partialTick, displayedPlayerHP, realPlayerHP);
			}

			drawHPBars(guiGraphics, poseStack, screenWidth, screenHeight, scale, scaleFactor, displayedPlayerHP, realPlayerHP, playerMaxHP);

		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	public void drawHPBars(GuiGraphics gui, PoseStack poseStack, int screenWidth, int screenHeight, float scale, float scaleFactor, float displayedHP, float realHP, float maxHP) {
		float maxBarWidth = maxHP * scaleFactor;
		float realBarWidth = realHP * scaleFactor;
		float displayedBarWidth = displayedHP * scaleFactor;
		float missingWidth = Math.max(displayedBarWidth - realBarWidth, 0);

		// Background & outline
		poseStack.pushPose();
        int guiHeight = 10;
        {
			poseStack.translate((screenWidth - maxBarWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
			poseStack.scale(scale, scale, scale);
			drawHPBarBack(gui, 0, 0, maxBarWidth, scale, realHP, maxHP);
		}
		poseStack.popPose();

		// Green HP
		poseStack.pushPose();
		{
			poseStack.translate((screenWidth - realBarWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 1 * scale, 0);
			poseStack.scale(scale, scale, scale);
			drawHPBarTop(gui, 0, 0, realBarWidth, scale);
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
		poseStack.popPose();

		// Red
		if (missingWidth > 0.5F) {
			poseStack.pushPose();
			{
				poseStack.translate((screenWidth - (realBarWidth + missingWidth) * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 1 * scale, 0);
				poseStack.scale(scale, scale, scale);
				drawDamagedHPBarTop(gui, 0, 0, missingWidth, scale);
			}
			poseStack.popPose();
		}
	}

	public void drawHPBarBack(GuiGraphics gui, int posX, int posY, float width, float scale, float hp, float maxHP) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate(scale * posX, scale * posY, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, texture, 0, 0, 0, 0, 2, 12);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, posY * scale, 0);
			matrixStack.scale(width, scale, 0);
			int v = Utils.isLowHP(hp, maxHP) ? 8 : 2;
			blit(gui, texture, 0, 0, v, 0, 1, 12);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, texture, 0, 0, 3, 0, 2, 12);
		}
		matrixStack.popPose();
	}

	public void drawHPBarTop(GuiGraphics gui, int posX, int posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, texture, 0, -1, 2, 12, 1, 8);
		}
		matrixStack.popPose();
	}

	public void drawDamagedHPBarTop(GuiGraphics gui, int posX, int posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, texture, 0, -1, 2, 22, 1, 8);
		}
		matrixStack.popPose();
	}
}
