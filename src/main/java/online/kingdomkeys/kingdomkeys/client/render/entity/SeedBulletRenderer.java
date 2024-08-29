package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.SeedBulletEntity;

import java.awt.*;

public class SeedBulletRenderer extends EntityRenderer<SeedBulletEntity> {

    int red = 96, green = 140, blue = 109, alpha = 255;
    private CubeModel model;

    public SeedBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new CubeModel(context.bakeLayer(CubeModel.LAYER_LOCATION));
    }

    @Override
    public void render(SeedBulletEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
    	matrixStackIn.pushPose();
    	{	matrixStackIn.translate(0, 0.25, 0);
    		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entityIn))), packedLightIn, OverlayTexture.NO_OVERLAY, new Color(0.6F * 255, 255, 0.6F * 255, 255).getRGB());
     	}
     	matrixStackIn.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SeedBulletEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/fire.png");
    }
}
