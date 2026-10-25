package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.HeartlessLightEntity;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class HeartlessLightRenderer extends EntityRenderer<HeartlessLightEntity> {

	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/heartless_light.png");
	private static final float LIGHT_SIZE = 0.6F, BLOOM_SIZE = 1.6F;
	private static final int BLOOM_ALPHA = 90;
	private static final float PULSE_TICKS = 40F;
	// u, v of each corner, which also give its position: v grows downwards
	private static final float[][] CORNERS = {{0, 1}, {1, 1}, {1, 0}, {0, 0}};

	public HeartlessLightRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(HeartlessLightEntity entity, float entityYaw, float partialTicks, PoseStack pose, MultiBufferSource buffers, int packedLight) {
		float ticks = entity.tickCount + partialTicks;
		float grow = Math.min(ticks, 10F) / 10F;
		float pulse = 0.85F + 0.15F * Mth.sin(ticks * (float) (Math.PI * 2) / PULSE_TICKS);

		pose.pushPose();
		{
			pose.translate(0D, 0.5D, 0D);
			pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
			pose.mulPose(Axis.YP.rotationDegrees(180F));
			VertexConsumer buffer = buffers.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
			quad(buffer, pose, BLOOM_SIZE * pulse * grow, (int) (BLOOM_ALPHA * pulse));
			quad(buffer, pose, LIGHT_SIZE * grow, 255);
		}
		pose.popPose();
		super.render(entity, entityYaw, partialTicks, pose, buffers, packedLight);
	}

	private static void quad(VertexConsumer buffer, PoseStack pose, float size, int alpha) {
		Matrix4f matrix = pose.last().pose();
		for (float[] uv : CORNERS) {
			buffer.addVertex(matrix, (uv[0] - 0.5F) * size, (0.5F - uv[1]) * size, 0F).setColor(255, 255, 255, alpha).setUv(uv[0], uv[1]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0F, 1F, 0F);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(HeartlessLightEntity entity) {
		return TEXTURE;
	}
}
