package online.kingdomkeys.kingdomkeys.client.render.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.FaithBeamModel;

import javax.annotation.Nullable;

public class FaithBeamEntityRenderer extends EntityRenderer<ThrowableProjectile> {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/light_pillar.png");
	FaithBeamModel<Entity> lightBeamModel;

	public FaithBeamEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		lightBeamModel = new FaithBeamModel<>(context.bakeLayer(FaithBeamModel.LAYER_LOCATION));
	}

	@Override
	public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			float scale = 2F;
			matrixStackIn.pushPose();
			{
				matrixStackIn.scale(scale, 2.3F, scale);
				lightBeamModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutout(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFFAAFFFF);
			}
			matrixStackIn.popPose();

			//Outline
			matrixStackIn.pushPose();
			{
				scale = 2.3F;
				matrixStackIn.scale(scale, scale, scale);
				lightBeamModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFF00CCFF);
			}
			matrixStackIn.popPose();
		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		return TEXTURE;
	}
}
