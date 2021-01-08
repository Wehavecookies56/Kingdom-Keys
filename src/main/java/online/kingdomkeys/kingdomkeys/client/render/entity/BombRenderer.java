package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.BombModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.DarkballModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
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
        if (EntityHelper.getState(entityIn) == 1) {
            int timer = Math.max(entityIn.ticksToExplode, 0);
            String text = (int)Math.ceil(timer/20F) + "";
            matrixStackIn.push();
            matrixStackIn.translate(0, entityIn.getHeight() + 0.75D, 0);
            matrixStackIn.rotate(mc.getRenderManager().getCameraOrientation());
            matrixStackIn.scale(-0.05F, -0.05F, -0.05F);
            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
            mc.fontRenderer.renderString(text, -mc.fontRenderer.getStringWidth(text) / 2, 0, 0xFFFFFF, false, matrix4f, bufferIn, false, 0, packedLightIn);
            matrixStackIn.pop();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static class Factory implements IRenderFactory<BaseBombEntity> {
        @Override
        public EntityRenderer<? super BaseBombEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new BombRenderer(entityRendererManager);
        }
    }
}
