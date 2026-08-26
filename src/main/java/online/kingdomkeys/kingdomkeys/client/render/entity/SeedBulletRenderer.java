package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.mob.SeedBulletEntity;

public class SeedBulletRenderer extends EntityRenderer<SeedBulletEntity> {
	private final CubeModel model;

    public SeedBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new CubeModel(context.bakeLayer(CubeModel.LAYER_LOCATION));
    }

    @Override
    public void render(SeedBulletEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    	matrixStackIn.pushPose();
    	{
			matrixStackIn.translate(0, 0.25, 0);
		    model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entityIn))), packedLightIn, OverlayTexture.NO_OVERLAY, 10092441);
     	}
     	matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SeedBulletEntity entity) {
		return KingdomKeys.rl("textures/entity/models/cube.png");
    }
}
