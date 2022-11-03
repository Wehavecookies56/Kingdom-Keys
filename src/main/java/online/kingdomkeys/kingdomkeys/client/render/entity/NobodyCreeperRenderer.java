package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.NobodyCreeperModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.NobodyCreeperEntity;

public class NobodyCreeperRenderer extends MobRenderer<NobodyCreeperEntity, NobodyCreeperModel<NobodyCreeperEntity>> {

    private ResourceLocation texture, swordTexture, spearTexture;

    public NobodyCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new NobodyCreeperModel<>(context.bakeLayer(NobodyCreeperModel.LAYER_LOCATION)), 0.35F);
        this.texture = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/creeper.png");
        this.swordTexture = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/creeper_sword.png");
        this.spearTexture = new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/creeper_spear.png");
    }

    @Override
    public ResourceLocation getTextureLocation(NobodyCreeperEntity entity) {
        if(EntityHelper.getState(entity) == 0 || EntityHelper.getState(entity) == 3)
            return this.texture;
        else if(EntityHelper.getState(entity) == 1)
            return this.swordTexture;
        else if(EntityHelper.getState(entity) == 2)
            return this.spearTexture;
        return texture;
    }

    @Override
    public void render(NobodyCreeperEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        this.model.attackTime = this.getAttackAnim(entityIn, partialTicks);

        boolean shouldSit = entityIn.isPassenger() && (entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = entityIn.isBaby();
        float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entityIn.yHeadRotO, entityIn.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && entityIn.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entityIn.getVehicle();
            f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = Mth.wrapDegrees(f2);
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

        float f6 = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
        if (entityIn.getPose() == Pose.SLEEPING) {
            Direction direction = entityIn.getBedOrientation();
            if (direction != null) {
                float f4 = entityIn.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStackIn.translate((double)((float)(-direction.getStepX()) * f4), 0.0D, (double)((float)(-direction.getStepZ()) * f4));
            }
        }

        float f7 = this.getBob(entityIn, partialTicks);
        this.setupRotations(entityIn, matrixStackIn, f7, f, partialTicks);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entityIn, matrixStackIn, partialTicks);
        matrixStackIn.translate(0.0D, (double)-1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entityIn.isAlive()) {
            f8 = Mth.lerp(partialTicks, entityIn.animationSpeedOld, entityIn.animationSpeed);
            f5 = entityIn.animationPosition - entityIn.animationSpeed * (1.0F - partialTicks);
            if (entityIn.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(entityIn, f5, f8, partialTicks);
        this.model.setupAnim(entityIn, f5, f8, f7, f2, f6);
        boolean flag = this.isBodyVisible(entityIn);
        boolean flag1 = !flag && !entityIn.isInvisibleTo(Minecraft.getInstance().player);
        boolean flag2 = Minecraft.getInstance().shouldEntityAppearGlowing(entityIn);
        RenderType rendertype = this.getRenderType(entityIn, flag, flag1, flag2);
        if (rendertype != null) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(rendertype);
            int i = getOverlayCoords(entityIn, this.getWhiteOverlayProgress(entityIn, partialTicks));
            //this.entityModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
            if(EntityHelper.getState(entityIn) == 0 || EntityHelper.getState(entityIn) == 3)
            {
                model.RightArmDetail.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
                model.BodyLower.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
                model.LeftArmDetail.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
            }
            else if(EntityHelper.getState(entityIn) == 1)
                model.Sword_Handle1.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
            else if(EntityHelper.getState(entityIn) == 2)
                model.Spear_Handle.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        }

        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void scale(NobodyCreeperEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
