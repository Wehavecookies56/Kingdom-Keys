package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import org.joml.Matrix4f;

public class HPGui extends OverlayBase {

	public static final HPGui INSTANCE = new HPGui();

	private float displayedPlayerHP, realPlayerHP;
	private float displayedGummiHP, realGummiHP;

	private long playerDelayEnd = 0;
	private long gummiDelayEnd = 0;

    final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hpbar.png");

    int barWidth = 908;
    int barHeight = 244;
    int barX = 0;
    int barY = 0;

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

		float health = minecraft.player.getHealth();
		float maxHealth = minecraft.player.getMaxHealth();
		float maxMaxHealth = 180; //maybe config value or something?
		float healthPercentage = health / maxMaxHealth;
		float maxHealthPercentage = maxHealth / maxMaxHealth;
        float maxHealthPercentageOutline = (maxHealth+1) / (maxMaxHealth);

        if (realPlayerHP == 0) {
            realPlayerHP = health;
            displayedPlayerHP = health;
        }

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        long now = net.minecraft.Util.getMillis();

        if (health < realPlayerHP) {
            playerDelayEnd = now + 1000;
        }
        realPlayerHP = health;

        if (now > playerDelayEnd) {
            displayedPlayerHP = Mth.lerp(0.05F * partialTick, displayedPlayerHP, realPlayerHP);
        }

        float displayedPercentage = displayedPlayerHP / maxMaxHealth;

        float scaleX = 0.2F;
        float scaleY = 0.2F;
        poseStack.pushPose();
        {
            poseStack.translate(screenWidth-15, screenHeight-5, 0);
            poseStack.translate(-barWidth * scaleX, -barHeight * scaleY, 0);
            poseStack.scale(scaleX, scaleY, 1);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            drawHPOutline(poseStack, maxHealthPercentageOutline);
			drawHPBackground(poseStack, maxHealthPercentage);
            drawHPBar(poseStack, healthPercentage);
            drawRedHP(poseStack, healthPercentage, displayedPercentage);

            RenderSystem.disableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
        }
        poseStack.popPose();
	}



    private void drawHPBackground(PoseStack poseStack, float maxHealthPercentage) {
        RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_background.png"));
        RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_mask.png"));
        ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
        ClientSetup.gummiHPShader.setSampler("Sampler1", 1);
        ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(maxHealthPercentage);
        ClientSetup.gummiHPShader.safeGetUniform("Colour").set(1F, 1F, 1F, 1F);
        ClientSetup.gummiHPShader.apply();
        RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
        buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
        buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
        buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private void drawHPBar(PoseStack poseStack, float healthPercentage) {
        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        if (minecraft.player.level().getLevelData().isHardcore())
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_fill_h.png"));
        else
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_fill.png"));

        RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_mask.png"));
        ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
        ClientSetup.gummiHPShader.setSampler("Sampler1", 1);
        ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(healthPercentage);
        ClientSetup.gummiHPShader.safeGetUniform("RedStart").set(0f);
        ClientSetup.gummiHPShader.safeGetUniform("RedEnd").set(0f);

        if(minecraft.player.hasEffect(MobEffects.POISON))
            ClientSetup.gummiHPShader.safeGetUniform("Colour").set(0.8F, 0.6F, 0F, 1F);
        else if(minecraft.player.hasEffect(MobEffects.WITHER))
            ClientSetup.gummiHPShader.safeGetUniform("Colour").set(0.2F, 0.1F, 0.1F, 1F);
        else if(minecraft.player.hasEffect(ModMobEffects.FREEZE))
            ClientSetup.gummiHPShader.safeGetUniform("Colour").set(0.3F, 0.8F, 1F, 1F);
        else
            ClientSetup.gummiHPShader.safeGetUniform("Colour").set(0.86F, 1F, 0F, 1F);

        ClientSetup.gummiHPShader.apply();
        RenderSystem.setShader(() -> ClientSetup.gummiHPShader);
        buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
        buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
        buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
        buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private void drawRedHP(PoseStack poseStack, float healthPercentage, float displayedPercentage) {
        float damagedPercentage = displayedPercentage - healthPercentage;

        if (damagedPercentage < 0)
            damagedPercentage = 0;

        if (damagedPercentage > 0.001F) {
            float redEnd = displayedPercentage;

            Matrix4f matrix = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShaderTexture(0,
                    ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_fill.png")
            );

            RenderSystem.setShaderTexture(1,
                    ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_mask.png")
            );

            ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
            ClientSetup.gummiHPShader.setSampler("Sampler1", 1);

            ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(0F);
            ClientSetup.gummiHPShader.safeGetUniform("RedStart").set(healthPercentage);
            ClientSetup.gummiHPShader.safeGetUniform("RedEnd").set(redEnd);

            ClientSetup.gummiHPShader.safeGetUniform("Colour").set(1F, 0F, 0F, 1F);

            ClientSetup.gummiHPShader.apply();
            RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

            buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
            buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
            buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
            buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);

            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }
    }

    private void drawHPOutline(PoseStack poseStack, float maxHealthPercentage) {
        poseStack.pushPose();

        RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_outline.png"));
        RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_mask.png"));
        ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
        ClientSetup.gummiHPShader.setSampler("Sampler1", 1);
        ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(maxHealthPercentage);
        ClientSetup.gummiHPShader.safeGetUniform("Colour").set(1F, 1F, 1F, 1F);
        ClientSetup.gummiHPShader.apply();
        RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float scale = 1.05F;


        poseStack.scale(scale, scale, 1);

        poseStack.translate(-36,-4,0);

        buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
        buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
        buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
        buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
        poseStack.popPose();
    }
}

