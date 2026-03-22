package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.BarrelModel;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.GigaShadowEntity;

public class TrainingDummyRenderer extends LivingEntityRenderer<TrainingDummyEntity, EntityModel<TrainingDummyEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/training_dummy.png");

    public TrainingDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new BarrelModel<>(context.bakeLayer(BarrelModel.LAYER_LOCATION)), 0);
    }

    @Override
    public void render(TrainingDummyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(TrainingDummyEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2F, 3F, 2F);
        matrixStackIn.translate(0F, 1.2F, 0F);
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public ResourceLocation getTextureLocation(TrainingDummyEntity entity) {
        return TEXTURE;
    }
}
