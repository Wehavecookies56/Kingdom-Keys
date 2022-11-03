package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.AssassinModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.AssassinEntity;

public class AssassinRenderer extends MobRenderer<AssassinEntity, AssassinModel<AssassinEntity>> {

    public AssassinRenderer(EntityRendererProvider.Context context) {
        super(context, new AssassinModel<>(context.bakeLayer(AssassinModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public void render(AssassinEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    	matrixStackIn.pushPose();
        {
	    	if (EntityHelper.getState(entityIn) == 1) {
	    		matrixStackIn.translate(0, -1.5F, 0);
	    	} else if(EntityHelper.getState(entityIn) == 2){
	    		matrixStackIn.translate(0, 0.3F, 0);
	    		if(entityIn.tickCount % 10 == 0) {
	    			matrixStackIn.scale(1.2F, 1.2F, 1.2F);
	    		}
	    	}
	        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    	}
    	matrixStackIn.popPose();
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/assassin.png");

    @Override
    public ResourceLocation getTextureLocation(AssassinEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(AssassinEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(2,2,2);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
