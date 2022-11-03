package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DuskModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;

public class DuskRenderer extends MobRenderer<DuskEntity, DuskModel<DuskEntity>> {

    public DuskRenderer(EntityRendererProvider.Context context) {
        super(context, new DuskModel<>(context.bakeLayer(DuskModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(DuskEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/dusk.png");
    }

    @Override
    protected void scale(DuskEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
