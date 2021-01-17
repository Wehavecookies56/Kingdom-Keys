package online.kingdomkeys.kingdomkeys.client.render.magic;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.BlizzardModel;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderBoltEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ThunderBoltEntityRenderer extends EntityRenderer<ThunderBoltEntity> {

    public static final Factory FACTORY = new ThunderBoltEntityRenderer.Factory();

    public ThunderBoltEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.25F;
    }

    @Override
    public void render(ThunderBoltEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ThunderBoltEntity entity) {
        return null;
    }

    public static class Factory implements IRenderFactory<ThunderBoltEntity> {
        @Override
        public EntityRenderer<? super ThunderBoltEntity> createRenderFor(EntityRendererManager manager) {
            return new ThunderBoltEntityRenderer(manager);
        }
    }
}
