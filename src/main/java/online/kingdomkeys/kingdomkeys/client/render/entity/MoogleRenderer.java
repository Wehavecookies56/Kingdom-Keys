package online.kingdomkeys.kingdomkeys.client.render.entity;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MoogleModel;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

public class MoogleRenderer extends MobRenderer<MoogleEntity, MoogleModel<MoogleEntity>> {

    public MoogleRenderer(EntityRendererProvider.Context context) {
        super(context, new MoogleModel<>(context.bakeLayer(MoogleModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public void render(MoogleEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.isFakeMoogle()) {
            VertexConsumer builder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
            matrixStackIn.popPose();
            {
    	       	float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
	            float f7 = this.getBob(entityIn, partialTicks);
	            this.setupRotations(entityIn, matrixStackIn, f7, f, partialTicks);
	            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
	            this.scale(entityIn, matrixStackIn, partialTicks);
	            matrixStackIn.translate(0.5D, (double) -1.501F, -0.5D);
	            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(Minecraft.getInstance().player.getYRot() + 180));
	            this.model.renderToBuffer(matrixStackIn, builder, packedLightIn, getOverlayCoords(entityIn, 0.0F), 1.0F, 1.0F, 1.0F, entityIn.isFakeMoogle() ? 0.5F : 1.0F);
            }
            matrixStackIn.pushPose();
        } else {
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MoogleEntity entity) {
        if (!entity.isFakeMoogle()) {
            return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/moogle.png");
        } else {
            return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/fake_moogle.png");
        }
    }

    @Nullable
    @Override //probably is called getRenderType or something
    protected RenderType getRenderType(MoogleEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return super.getRenderType(p_230496_1_, p_230496_2_, p_230496_3_, p_230496_4_);
    }
}
