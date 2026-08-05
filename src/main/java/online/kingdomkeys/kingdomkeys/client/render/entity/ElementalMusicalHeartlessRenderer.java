package online.kingdomkeys.kingdomkeys.client.render.entity;


import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.model.entity.ElementalMusicalHeartlessModel;
import online.kingdomkeys.kingdomkeys.client.render.HeartlessEyesLayerRenderer;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseElementalMusicalHeartlessEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.EmeraldBluesEntity;

public class ElementalMusicalHeartlessRenderer extends MobRenderer<BaseElementalMusicalHeartlessEntity, ElementalMusicalHeartlessModel<BaseElementalMusicalHeartlessEntity>> { //my god that's a long one

    public ElementalMusicalHeartlessRenderer(EntityRendererProvider.Context context) {
        super(context, new ElementalMusicalHeartlessModel<>(context.bakeLayer(ElementalMusicalHeartlessModel.LAYER_LOCATION)), 0.35F);
        this.addLayer(new HeartlessEyesLayerRenderer<>(this, KingdomKeys.rl("textures/entity/mob/musical_heartless_eyes.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(BaseElementalMusicalHeartlessEntity entity) {
        return ClientUtils.variantTexture(entity.getTexture(), entity);
    }

    @Override
    protected void scale(BaseElementalMusicalHeartlessEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
        if(entitylivingbaseIn instanceof EmeraldBluesEntity) {
            matrixStackIn.scale(1.1F, 1.1F, 1.1F);
        }
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

}
