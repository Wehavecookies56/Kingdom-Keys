package online.kingdomkeys.kingdomkeys.client.render.entity;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.client.model.entity.BombModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.ElementalMusicalHeartlessModel;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseElementalMusicalHeartlessEntity;

public class ElementalMusicalHeartlessRenderer extends MobRenderer<BaseElementalMusicalHeartlessEntity, ElementalMusicalHeartlessModel<BaseElementalMusicalHeartlessEntity>> { //my god that's a long one

    public static final ElementalMusicalHeartlessRenderer.Factory FACTORY = new ElementalMusicalHeartlessRenderer.Factory();
    static final double MAX = 200;

    public ElementalMusicalHeartlessRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ElementalMusicalHeartlessModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(BaseElementalMusicalHeartlessEntity entity) {
        return entity.getTexture();
    }

    @Override
    protected void preRenderCallback(BaseElementalMusicalHeartlessEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
        double pos = entitylivingbaseIn.ticksExisted % MAX / (MAX /2D);
        if(entitylivingbaseIn.ticksExisted % MAX < (MAX / 2)) {
        	matrixStackIn.translate(0, pos*0.6, 0);
        } else {
        	matrixStackIn.translate(0, (MAX - entitylivingbaseIn.ticksExisted % MAX) / (MAX / 2D) * 0.6, 0);
        }
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    public static class Factory implements IRenderFactory<BaseElementalMusicalHeartlessEntity> {
        @Override
        public EntityRenderer<? super BaseElementalMusicalHeartlessEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new ElementalMusicalHeartlessRenderer(entityRendererManager);
        }
    }

}
