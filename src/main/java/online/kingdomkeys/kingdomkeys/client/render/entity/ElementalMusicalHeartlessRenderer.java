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
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    public static class Factory implements IRenderFactory<BaseElementalMusicalHeartlessEntity> {
        @Override
        public EntityRenderer<? super BaseElementalMusicalHeartlessEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new ElementalMusicalHeartlessRenderer(entityRendererManager);
        }
    }

}
