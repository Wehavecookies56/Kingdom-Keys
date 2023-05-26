package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowModel;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;

public class ShadowRenderer<Type extends ShadowEntity> extends MobRenderer<Type, ShadowModel<Type>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow.png");


    public ShadowRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowModel<>(context.bakeLayer(ShadowModel.LAYER_LOCATION)), 0.35F);
        model.CYCLES_PER_BLOCK = 1;
    }

    @Override
    public void render(Type entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        {	      
            matrixStackIn.scale(0.8F, 0.8F, 0.8F);

	    	/*if (EntityHelper.getState(entityIn) == 1) {
	            matrixStackIn.scale(1.5F, 0.01F, 1.5F);
	        }*/
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(Type entity) {
        return TEXTURE;
    }
}
