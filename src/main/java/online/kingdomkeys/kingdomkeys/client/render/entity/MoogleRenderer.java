package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MoogleModel;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

import javax.annotation.Nullable;
import java.awt.*;

public class MoogleRenderer extends MobRenderer<MoogleEntity, MoogleModel<MoogleEntity>> {

    public MoogleRenderer(EntityRendererProvider.Context context) {
        super(context, new MoogleModel<>(context.bakeLayer(MoogleModel.LAYER_LOCATION)), 0.35F);
    }

    @Override
    public void render(MoogleEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        this.model.pompomColor = entityIn.isFakeMoogle() ? MoogleEntity.NO_POMPOM_DYE : entityIn.getPompomColor();

        if (entityIn.isFakeMoogle()) {
            VertexConsumer builder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
            matrixStackIn.popPose();
            {

    	       	float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
	            float f7 = this.getBob(entityIn, partialTicks);
	            this.setupRotations(entityIn, matrixStackIn, f7, f, partialTicks, entityIn.getScale());
	            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
	            this.scale(entityIn, matrixStackIn, partialTicks);
	            matrixStackIn.translate(0.5D, -1.501F, -0.5D);
	            matrixStackIn.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().player.getYRot() + 180));
                Color colour = new Color(1F,1F,1F, entityIn.isFakeMoogle() ? 0.5F : 1F);
	            this.model.renderToBuffer(matrixStackIn, builder, packedLightIn, getOverlayCoords(entityIn, 0.0F), colour.getRGB());
            }
            matrixStackIn.pushPose();
        } else {
            matrixStackIn.pushPose();
            {
                float time = entityIn.tickCount + partialTicks;
                Vec3 vel = entityIn.getDeltaMovement();
                float speed = (float)Math.sqrt(vel.x * vel.x + vel.z * vel.z);
                float yawRad = (float)Math.toRadians(entityIn.getYRot());
                float moveFactor = Mth.clamp(speed * 20.0F, 0.0F, 1.0F);
                float idleFactor = 1.0F - moveFactor;
                float localSwayX = Mth.cos(time * 0.1F) * 0.05F * moveFactor;
                float localSwayZ = Mth.sin(time * 0.1F) * 0.02F * moveFactor;
                float sin = Mth.sin(yawRad);
                float cos = Mth.cos(yawRad);
                float worldX = localSwayX * cos - localSwayZ * sin;
                float worldZ = localSwayX * sin + localSwayZ * cos;
                float baseBob = Mth.sin(time * 0.1F) * 0.1F;
                float bob = baseBob * idleFactor;
                matrixStackIn.translate(worldX, bob, worldZ);
                float idleShadow = 0.20F;
                float moveShadow = 0.15F;
                float base = Mth.lerp(moveFactor, idleShadow, moveShadow);
                float shadowBob = Mth.sin(entityIn.tickCount * 0.1F) * 0.02F;
                shadowRadius = base + shadowBob * (1.0F - moveFactor);
                super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            }
            matrixStackIn.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MoogleEntity entity) {
        if (!entity.isFakeMoogle()) {
        	if(isOrg(entity)) {
        		return KingdomKeys.rl("textures/entity/mob/org_moogle.png");
        	}
            return KingdomKeys.rl("textures/entity/mob/moogle.png");
        } else {
            return KingdomKeys.rl("textures/entity/mob/fake_moogle.png");
        }
    }

    private boolean isOrg(MoogleEntity entity) {
    	if(entity.hasCustomName()) {
	    	String name = entity.getCustomName().getString().toLowerCase();
	    	return name.length() == 7 && name.contains("m") && name.chars().filter(c -> c == 'o').count() == 2 && name.contains("x") && name.contains("g") && name.contains("l") && name.contains("e");
    	}
    	return false;
	}
    
	@Nullable
    @Override //probably is called getRenderType or something
    protected RenderType getRenderType(MoogleEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return super.getRenderType(p_230496_1_, p_230496_2_, p_230496_3_, p_230496_4_);
    }
}
