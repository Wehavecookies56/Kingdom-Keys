package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
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

        matrixStackIn.translate(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ());
        GlStateManager.disableTexture();
        matrixStackIn.rotate(new Quaternion(Vector3f.YP,entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks - 90.0F, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZP,entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks, true));
        matrixStackIn.scale(1, 0.5F, 0.5F);
        model.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntitySolid(null)), packedLightIn, 0, (float)this.red / 255, (float)this.green / 255, (float)this.blue / 255, (float)this.alpha / 255);
        GlStateManager.enableTexture();
        matrixStackIn.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SeedBulletEntity entity) {
        return null;
    }

    @Override
    public EntityRenderer<? super SeedBulletEntity> createRenderFor(EntityRendererManager manager) {
        return new SeedBulletRenderer(manager);
    }
}
