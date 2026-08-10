package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.model.entity.NeoshadowModel;
import online.kingdomkeys.kingdomkeys.client.render.HeartlessEyesLayerRenderer;
import online.kingdomkeys.kingdomkeys.entity.mob.NeoshadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.NovashadowEntity;

public class NeoshadowRenderer<Type extends NeoshadowEntity> extends MobRenderer<Type, NeoshadowModel<Type>> {

	public NeoshadowRenderer(EntityRendererProvider.Context context) {
		super(context, new NeoshadowModel<>(context.bakeLayer(NeoshadowModel.LAYER_LOCATION)), 0.4F);
		this.addLayer(new HeartlessEyesLayerRenderer<>(this, KingdomKeys.rl("textures/entity/mob/neoshadow_eyes.png")));
	}

	@Override
	public void render(Type entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			float anim = entity.prevShadowAnim + (entity.shadowAnim - entity.prevShadowAnim) * partialTicks;
			matrixStackIn.scale(1F + anim * 0.7F, 1F - anim * 0.99F, 1F + anim * 0.7F);

			super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.popPose();
	}

	@Override
	protected void scale(Type entity, PoseStack matrixStackIn, float partialTickTime) {
		float scale = entity instanceof NovashadowEntity ? 1.9F : 1.8F;
		matrixStackIn.scale(scale, scale, scale);
		super.scale(entity, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(NeoshadowEntity entity) {
		return ClientUtils.variantTexture(entity.getTexture(), entity);
	}
}
