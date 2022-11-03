package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DirePlantModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DirePlantEntity;

public class DirePlantRenderer extends MobRenderer<DirePlantEntity, DirePlantModel<DirePlantEntity>> {

    public DirePlantRenderer(EntityRendererProvider.Context context) {
        super(context, new DirePlantModel<>(context.bakeLayer(DirePlantModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(DirePlantEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/dire_plant.png");
    }

    @Override
    protected void scale(DirePlantEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
