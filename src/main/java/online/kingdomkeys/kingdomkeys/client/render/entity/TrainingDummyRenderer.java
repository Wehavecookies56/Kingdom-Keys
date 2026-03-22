package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.TrainingDummyModel;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;

public class TrainingDummyRenderer extends LivingEntityRenderer<TrainingDummyEntity, EntityModel<TrainingDummyEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/training_dummy.png");

    public TrainingDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new TrainingDummyModel<>(context.bakeLayer(TrainingDummyModel.LAYER_LOCATION)), 0);
    }

    @Override
    public void render(TrainingDummyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected boolean shouldShowName(TrainingDummyEntity entity) {
        return false;
    }

    @Override
    protected void scale(TrainingDummyEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float scale = 0.7F;
        matrixStackIn.scale(scale, scale, scale);
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public ResourceLocation getTextureLocation(TrainingDummyEntity entity) {
        return TEXTURE;
    }
}
