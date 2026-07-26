package online.kingdomkeys.kingdomkeys.client.render.entity;

import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.client.render.HeartlessEyesLayerRenderer;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MegaShadowEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;

public class ShadowRenderer<Type extends ShadowEntity> extends MobRenderer<Type, ShadowModel<Type>> {

    public ShadowRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowModel<>(context.bakeLayer(ShadowModel.LAYER_LOCATION)), 0);
        model.CYCLES_PER_BLOCK = 1;
        this.addLayer(new HeartlessEyesLayerRenderer<>(this, KingdomKeys.rl("textures/entity/mob/shadow_eyes.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowEntity entity) {
        return ClientUtils.variantTexture(KingdomKeys.rl("textures/entity/mob/shadow.png"), entity);
    }

    @Override
    public void render(Type entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        {
			float anim = entityIn.prevShadowAnim + (entityIn.shadowAnim - entityIn.prevShadowAnim) * partialTicks;
	        float scaleY = 1F - anim * 0.99F;
	        float scaleXZ = 1F + anim * 0.7F;

	        matrixStackIn.scale(scaleXZ, scaleY, scaleXZ);

	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.popPose();
    }

	@Override
	protected void scale(Type entity, PoseStack matrixStackIn, float partialTickTime) {
		float scale = switch(entity){
			case GigaShadowEntity gigasShadow -> 4F;
			case MegaShadowEntity megaShadow -> 2.5F;
			case ShadowEntity shadow -> 1F;
		};

		matrixStackIn.scale(scale, scale, scale);
		super.scale(entity, matrixStackIn, partialTickTime);
	}
}
