package online.kingdomkeys.kingdomkeys.client.render.org;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeShotEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class LaserDomeShotEntityRenderer extends EntityRenderer<LaserDomeShotEntity> {

    private final CubeModel model;

    public LaserDomeShotEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new CubeModel(context.bakeLayer(CubeModel.LAYER_LOCATION));
    }

    @Override
    public void render(LaserDomeShotEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        {
            matrixStackIn.translate(0, 0.05, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO)));
            matrixStackIn.mulPose(Axis.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO)));
            if (entity.getDeltaMovement().equals(Vec3.ZERO)) {
                matrixStackIn.scale(0.3F, 0.3F, 0.3F);
            } else {
                matrixStackIn.scale(0.2F, 0.2F, 0.8F);
            }
            model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, 16724787);
        }
        matrixStackIn.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(LaserDomeShotEntity entity) {
        return KingdomKeys.rl("textures/entity/models/cube.png");
    }

}