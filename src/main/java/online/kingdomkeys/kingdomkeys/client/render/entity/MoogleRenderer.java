package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MoogleModel;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

public class MoogleRenderer extends MobRenderer<MoogleEntity, MoogleModel<MoogleEntity>> {

    public static final MoogleRenderer.Factory FACTORY = new MoogleRenderer.Factory();

    public MoogleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MoogleModel<>(), 0.35F);
    }

    @Override
    public void render(MoogleEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(MoogleEntity entity) {
        if (!entity.isFakeMoogle()) {
            return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/moogle.png");
        } else {
            return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/fake_moogle.png");
        }
    }

    public static class Factory implements IRenderFactory<MoogleEntity> {
        @Override
        public EntityRenderer<? super MoogleEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new MoogleRenderer(entityRendererManager);
        }
    }
}
