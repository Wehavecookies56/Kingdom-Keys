package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.BombModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DarkballEntity;

public class BombRenderer extends MobRenderer<BaseBombEntity, BombModel<BaseBombEntity>> {

    public static final BombRenderer.Factory FACTORY = new BombRenderer.Factory();

    public BombRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BombModel<>(), 0.35F);
    }

    @Override
    public ResourceLocation getEntityTexture(BaseBombEntity entity) {
        return entity.getTexture();
    }

    @Override
    protected void preRenderCallback(BaseBombEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public void render(BaseBombEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        matrixStackIn.push();
        matrixStackIn.translate(entityIn.getPosX(), entityIn.getPosY() + 1, entityIn.getPosZ());
        matrixStackIn.rotate(mc.getRenderManager().getCameraOrientation());
        matrixStackIn.scale(-0.025F, -0.025F, -0.025F);
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        mc.fontRenderer.renderString(entityIn.ticksToExplode + "", -mc.fontRenderer.getStringWidth(entityIn.ticksToExplode + "") / 2, 0, 0x0099FF, false, matrix4f, bufferIn, false, 0, packedLightIn);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static class Factory implements IRenderFactory<BaseBombEntity> {
        @Override
        public EntityRenderer<? super BaseBombEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new BombRenderer(entityRendererManager);
        }
    }
}
