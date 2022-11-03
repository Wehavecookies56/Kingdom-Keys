package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;

public class GigaShadowRenderer extends MobRenderer<GigaShadowEntity, ShadowModel<GigaShadowEntity>> {

    public GigaShadowRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowModel<>(context.bakeLayer(ShadowModel.LAYER_LOCATION)), 1.5F);
        model.CYCLES_PER_BLOCK = 1;

    }

    @Override
    public ResourceLocation getTextureLocation(GigaShadowEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");
    }

    @Override
    public void render(GigaShadowEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    	matrixStackIn.pushPose();
        {
	       
	    	if (EntityHelper.getState(entityIn) == 1) {
	            matrixStackIn.scale(1.5F, 0.01F, 1.5F);
	        }
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.popPose();
    }

    @Override
    protected void scale(GigaShadowEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(4F, 4F, 4F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
