package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.drops.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

public class LuxOrbRenderer extends EntityItemDropRenderer {
	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/lux_orb.png");

	private static final float BLOOM_SCALE = 2F;
	private static final int BLOOM_ALPHA = 60;
	private static final float PULSE_TICKS = 48.0F;

	public LuxOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(ItemDropEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		float value = entityIn.getValue() / 10F;
		value = Mth.clamp(Utils.map(value, 1, 35, 0.5F, 3), 0.5F, 3F);

		float age = entityIn.tickCount + partialTicks;
		float pulse = 0.85F + 0.15F * Mth.sin(age * (float) (Math.PI * 2) / PULSE_TICKS);

		matrixStackIn.pushPose();
		{
			matrixStackIn.scale(value, value, value);
			matrixStackIn.translate(0.0D, 0.3F, 0.0D);
			matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
			matrixStackIn.scale(1F, 1F, 1F);

			VertexConsumer buffer = bufferIn.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));

			// Bloom first and wider, so the star sits on top of its own light
			quad(buffer, matrixStackIn, BLOOM_SCALE * pulse, (int) (BLOOM_ALPHA * pulse), packedLightIn);
			quad(buffer, matrixStackIn, 1.0F, 255, packedLightIn);
		}
		matrixStackIn.popPose();
	}

	private static void quad(VertexConsumer buffer, PoseStack pose, float scale, int alpha, int packedLight) {
		pose.pushPose();
		{
			pose.scale(scale, scale, scale);
			Matrix4f matrix = pose.last().pose();
			vertex(buffer, matrix, -0.5F, -0.25F, alpha, 0F, 1F, packedLight);
			vertex(buffer, matrix, 0.5F, -0.25F, alpha, 1F, 1F, packedLight);
			vertex(buffer, matrix, 0.5F, 0.75F, alpha, 1F, 0F, packedLight);
			vertex(buffer, matrix, -0.5F, 0.75F, alpha, 0F, 0F, packedLight);
		}
		pose.popPose();
	}

	private static void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, int alpha, float u, float v, int packedLight) {
		buffer.addVertex(matrix, x, y, 0.0F).setColor(255, 255, 255, alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		return TEXTURE;
	}
}
