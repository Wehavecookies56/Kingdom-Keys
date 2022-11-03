package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ShadowGlobModel;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowGlobEntity;

public class ShadowGlobRenderer extends MobRenderer<ShadowGlobEntity, ShadowGlobModel<ShadowGlobEntity>> {

    public ShadowGlobRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowGlobModel<>(context.bakeLayer(ShadowGlobModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowGlobEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/shadow_glob.png");
    }

    @Override
    protected void scale(ShadowGlobEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
