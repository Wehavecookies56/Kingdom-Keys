package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.NobodyCreeperModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.NobodyCreeperEntity;

public class NobodyCreeperRenderer extends MobRenderer<NobodyCreeperEntity, NobodyCreeperModel<NobodyCreeperEntity>> {

    private ResourceLocation texture, swordTexture, spearTexture;
    public static final NobodyCreeperRenderer.Factory FACTORY = new NobodyCreeperRenderer.Factory();

    public NobodyCreeperRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new NobodyCreeperModel<>(), 0.35F);
        this.texture = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/creeper.png");
        this.swordTexture = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/creeper_sword.png");
        this.spearTexture = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/creeper_spear.png");
    }

    @Override
    public ResourceLocation getEntityTexture(NobodyCreeperEntity entity) {
        if(EntityHelper.getState(entity) == 0 || EntityHelper.getState(entity) == 3)
            return this.texture;
        else if(EntityHelper.getState(entity) == 1)
            return this.swordTexture;
        else if(EntityHelper.getState(entity) == 2)
            return this.spearTexture;
        return texture;
    }

    @Override
    public void render(NobodyCreeperEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        this.entityModel.swingProgress = this.getSwingProgress(entityIn, partialTicks);

        boolean shouldSit = entityIn.isPassenger() && (entityIn.getRidingEntity() != null && entityIn.getRidingEntity().shouldRiderSit());
        this.entityModel.isSitting = shouldSit;
        this.entityModel.isChild = entityIn.isChild();
        float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRenderYawOffset, entityIn.renderYawOffset);
        float f1 = MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYawHead, entityIn.rotationYawHead);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getRidingEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entityIn.getRidingEntity();
            f = MathHelper.interpolateAngle(partialTicks, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
            f2 = f1 - f;
            float f3 = MathHelper.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
        if (entityIn.getPose() == Pose.SLEEPING) {
            Direction direction = entityIn.getBedDirection();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStackIn.translate((double)((float)(-direction.getXOffset()) * f4), 0.0D, (double)((float)(-direction.getZOffset()) * f4));
            }
        }

        float f7 = this.handleRotationFloat(entityIn, partialTicks);
        this.applyRotations(entityIn, matrixStackIn, f7, f, partialTicks);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.preRenderCallback(entityIn, matrixStackIn, partialTicks);
        matrixStackIn.translate(0.0D, (double)-1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = MathHelper.lerp(partialTicks, entityIn.prevLimbSwingAmount, entityIn.limbSwingAmount);
            f5 = entityIn.limbSwing - entityIn.limbSwingAmount * (1.0F - partialTicks);
            if (entityIn.isChild()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.entityModel.setLivingAnimations(entityIn, f5, f8, partialTicks);
        this.entityModel.setRotationAngles(entityIn, f5, f8, f7, f2, f6);
        boolean flag = this.isVisible(entityIn);
        boolean flag1 = !flag && !entityIn.isInvisibleToPlayer(Minecraft.getInstance().player);
        RenderType rendertype = this.func_230042_a_(entityIn, flag, flag1);
        if (rendertype != null) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getPackedOverlay(entityIn, this.getOverlayProgress(entityIn, partialTicks));
            //this.entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
            if(EntityHelper.getState(entityIn) == 0 || EntityHelper.getState(entityIn) == 3)
            {
                entityModel.RightArmDetail.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
                entityModel.BodyLower.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
                entityModel.LeftArmDetail.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
            }
            else if(EntityHelper.getState(entityIn) == 1)
                entityModel.Sword_Handle1.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
            else if(EntityHelper.getState(entityIn) == 2)
                entityModel.Spear_Handle.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        }

        matrixStackIn.pop();
    }

    @Override
    protected void preRenderCallback(NobodyCreeperEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
        super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
    
    public static class Factory implements IRenderFactory<NobodyCreeperEntity> {
        @Override
        public EntityRenderer<? super NobodyCreeperEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new NobodyCreeperRenderer(entityRendererManager);
        }
    }
}
