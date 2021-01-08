package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.SeedBulletEntity;

public class SeedBulletRenderer extends EntityRenderer<SeedBulletEntity> implements IRenderFactory<SeedBulletEntity> {

    int red = 96, green = 140, blue = 109, alpha = 255;
    private CubeModel model;

    public SeedBulletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        model = new CubeModel();
    }

    @Override
    public void render(SeedBulletEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    	matrixStackIn.push();
    	{	matrixStackIn.translate(0, 0.25, 0);
    		model.render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(getEntityTexture(entityIn))), packedLightIn, OverlayTexture.NO_OVERLAY, 0.6F, 1, 0.6F, 1F);
     	}
     	matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SeedBulletEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
    }

    @Override
    public EntityRenderer<? super SeedBulletEntity> createRenderFor(EntityRendererManager manager) {
        return new SeedBulletRenderer(manager);
    }
}
