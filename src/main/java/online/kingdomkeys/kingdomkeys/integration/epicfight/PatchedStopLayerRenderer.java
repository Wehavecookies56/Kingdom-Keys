package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.StopModel;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedStopLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, net.minecraft.client.renderer.entity.layers.RenderLayer<E, M>> {

    public static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/models/stop.png");

    private final StopModel<?> stopModel;

    public PatchedStopLayerRenderer() {
        EntityModelSet models = Minecraft.getInstance().getEntityModels();
        this.stopModel = new StopModel<>(models.bakeLayer(StopModel.LAYER_LOCATION));
    }

    @Override
    protected void renderLayer(T patch, E entity, @Nullable RenderLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
        GlobalData globalData = GlobalData.getClient(entity);
        if (globalData == null)
            return;

        if (globalData.getStopModelTicks() <= 0)
            return;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        poseStack.pushPose();
        {
            poseStack.translate(0.0D, 1.0D, 0.0D);
            poseStack.scale(1.0F, -1.0F, 1.0F);

            poseStack.translate(0, -1, 0);

            float scale = (10F - globalData.getStopModelTicks()) / 5F;
            poseStack.scale(scale * 1.2F, scale, scale * 1.2F);

            stopModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
        }
        poseStack.popPose();
    }
}