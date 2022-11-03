package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.model.entity.BombModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseBombEntity;

public class BombRenderer extends MobRenderer<BaseBombEntity, BombModel<BaseBombEntity>> {

    public BombRenderer(EntityRendererProvider.Context context) {
        super(context, new BombModel<>(context.bakeLayer(BombModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(BaseBombEntity entity) {
        return entity.getTexture();
    }

    @Override
    protected void scale(BaseBombEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    	matrixStackIn.scale(1F, 1F, 1F);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public void render(BaseBombEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        if (EntityHelper.getState(entityIn) == 1) {
            int timer = Math.max(entityIn.ticksToExplode, 0);
            String text = (int)Math.ceil(timer/20F) + "";
            matrixStackIn.pushPose();
            matrixStackIn.translate(0, entityIn.getBbHeight() + 0.75D, 0);
            matrixStackIn.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            matrixStackIn.scale(-0.05F, -0.05F, -0.05F);
            Matrix4f matrix4f = matrixStackIn.last().pose();
            mc.font.drawInBatch(text, -mc.font.width(text) / 2, 0, 0xFFFFFF, false, matrix4f, bufferIn, false, 0, packedLightIn);
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
