package online.kingdomkeys.kingdomkeys.client.render.entity;

import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.BombModel;
import online.kingdomkeys.kingdomkeys.client.render.HeartlessEyesLayerRenderer;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MinuteBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.SkaterBombEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.StormBombEntity;
import org.joml.Matrix4f;

public class BombRenderer extends MobRenderer<BaseBombEntity, BombModel<BaseBombEntity>> {

    public BombRenderer(EntityRendererProvider.Context context) {
        super(context, new BombModel<>(context.bakeLayer(BombModel.LAYER_LOCATION)), 0.35F);
        this.addLayer(new HeartlessEyesLayerRenderer<>(this, KingdomKeys.rl("textures/entity/mob/bomb_eyes.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(BaseBombEntity entity) {
        return ClientUtils.variantTexture(entity.getTexture(), entity);
    }

    @Override
    protected void scale(BaseBombEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float scale = 1;
        if(entitylivingbaseIn instanceof MinuteBombEntity) {
            scale = 1;
        } else if(entitylivingbaseIn instanceof SkaterBombEntity) {
            scale = 1.15F;
        } else if(entitylivingbaseIn instanceof StormBombEntity) {
            scale = 1.3F;
        } else {
            scale = 1.5F;
        }
    	matrixStackIn.scale(scale, scale, scale);
    	super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }

    @Override
    public void render(BaseBombEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        if (entityIn.getState() == 1) {
            int timer = Math.max(entityIn.getTicks(), 0);
            String text = (int)Math.ceil(timer/20F) + "";
            matrixStackIn.pushPose();
            {
                matrixStackIn.translate(0, entityIn.getBbHeight() + 0.75D, 0);
                matrixStackIn.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));
                matrixStackIn.scale(-0.05F, -0.05F, -0.05F);
                Matrix4f matrix4f = matrixStackIn.last().pose();
                mc.font.drawInBatch(text, -mc.font.width(text) / 2, 0, 0xFFFFFF, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, 0, packedLightIn);
            }
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
