package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.LightPortalEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class LightPortalEntityRenderer extends EntityRenderer<LightPortalEntity> {

	private static final ModelResourceLocation MODEL = ModelResourceLocation.standalone(KingdomKeys.rl("entity/portal"));

	private static final float FULL_SIZE = 2.0F;

	private static final float[] TINT = { 0.55F, 0.85F, 1.0F, 0.9F };

	public LightPortalEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

	@Override
	public void render(LightPortalEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		float ticks = entity.tickCount + partialTicks;
		float scale = Math.min(1F, ticks / LightPortalEntity.OPENING) * FULL_SIZE;

		if (scale <= 0F) {
			return;
		}

		matrixStackIn.pushPose();
		{
			VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			BakedModel model = Minecraft.getInstance().getModelManager().getModel(MODEL);

			matrixStackIn.scale(scale, scale, scale);
			matrixStackIn.mulPose(Axis.YN.rotationDegrees(Minecraft.getInstance().player.getRotationVector().y));

			for (BakedQuad quad : model.getQuads(null, null, entity.level().random, ModelData.EMPTY, RenderType.translucent())) {
				buffer.putBulkData(matrixStackIn.last(), quad, TINT[0], TINT[1], TINT[2], TINT[3], 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
			}
		}
		matrixStackIn.popPose();

		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(LightPortalEntity entity) {
		return KingdomKeys.rl("textures/entity/models/cube.png");
	}
}
