package online.kingdomkeys.kingdomkeys.client.render.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.model.entity.ElementalMusicalHeartlessModel;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseElementalMusicalHeartlessEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.EmeraldBluesEntity;

public class ElementalMusicalHeartlessRenderer extends MobRenderer<BaseElementalMusicalHeartlessEntity, ElementalMusicalHeartlessModel<BaseElementalMusicalHeartlessEntity>> { //my god that's a long one

    static final double MAX = 200;

    public ElementalMusicalHeartlessRenderer(EntityRendererProvider.Context context) {
        super(context, new ElementalMusicalHeartlessModel<>(context.bakeLayer(ElementalMusicalHeartlessModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseElementalMusicalHeartlessEntity entity) {
        return entity.getTexture();
    }

    @Override
    protected void scale(BaseElementalMusicalHeartlessEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
        double pos = entitylivingbaseIn.tickCount % MAX / (MAX /2D);
        if(entitylivingbaseIn.tickCount % MAX < (MAX / 2)) {
        	matrixStackIn.translate(0, pos*0.6, 0);
        } else {
        	matrixStackIn.translate(0, (MAX - entitylivingbaseIn.tickCount % MAX) / (MAX / 2D) * 0.6, 0);
        }
        if(entitylivingbaseIn instanceof EmeraldBluesEntity) {
            matrixStackIn.scale(1.1F, 1.1F, 1.1F);
        }
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

}
