package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MoogleModel;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

import javax.annotation.Nullable;

public class MoogleRenderer extends MobRenderer<MoogleEntity, MoogleModel<MoogleEntity>> {

    public static final MoogleRenderer.Factory FACTORY = new MoogleRenderer.Factory();

    public MoogleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MoogleModel<>(), 0.35F);
    }

    @Override
    public void render(MoogleEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isFakeMoogle()) {
            IVertexBuilder builder = bufferIn.getBuffer(this.entityModel.getRenderType(this.getEntityTexture(entityIn)));
            matrixStackIn.pop();
            float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
            float f7 = this.handleRotationFloat(entityIn, partialTicks);
            this.applyRotations(entityIn, matrixStackIn, f7, f, partialTicks);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(entityIn, matrixStackIn, partialTicks);
            matrixStackIn.translate(0.5D, (double) -1.501F, -0.5D);
            this.entityModel.render(matrixStackIn, builder, packedLightIn, getPackedOverlay(entityIn, 0.0F), 1.0F, 1.0F, 1.0F, entityIn.isFakeMoogle() ? 0.5F : 1.0F);
            matrixStackIn.push();
        } else {
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    @Override
    public ResourceLocation getEntityTexture(MoogleEntity entity) {
        if (!entity.isFakeMoogle()) {
            return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/moogle.png");
        } else {
            return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/fake_moogle.png");
        }
    }

    @Nullable
    @Override //probably is called getRenderType or something
    protected RenderType func_230042_a_(MoogleEntity entity, boolean p_230042_2_, boolean p_230042_3_) {
        return super.func_230042_a_(entity, p_230042_2_, p_230042_3_);
    }

    public static class Factory implements IRenderFactory<MoogleEntity> {
        @Override
        public EntityRenderer<? super MoogleEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new MoogleRenderer(entityRendererManager);
        }
    }
}
