package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;

public class DarkballRenderer extends MobRenderer<DarkballEntity, DarkballModel<DarkballEntity>> {

    private final static ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/darkball.png");

    public DarkballRenderer(EntityRendererProvider.Context context) {
        super(context, new DarkballModel<>(context.bakeLayer(DarkballModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(DarkballEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(DarkballEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
