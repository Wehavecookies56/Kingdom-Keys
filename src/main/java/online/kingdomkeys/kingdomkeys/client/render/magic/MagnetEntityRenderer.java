package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MagnetModel;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

@OnlyIn(Dist.CLIENT)
public class MagnetEntityRenderer extends EntityRenderer<ThrowableProjectile> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID,"textures/entity/models/magnet.png");
	MagnetModel magnetModel;

	public MagnetEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		magnetModel = new MagnetModel<>(context.bakeLayer(MagnetModel.LAYER_LOCATION));
	}

	float ticks = 0F;
	float prevRotationTicks = 0F;
	
	@Override
	public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{

			float rotation = prevRotationTicks + (ticks - prevRotationTicks) * partialTicks;

			float speed;
			float scale;
			//TODO interpolate somehow cuz I forgor how
			if(entity instanceof MagnetEntity) {
				speed = 3;
				scale = 2;
			} else if (entity instanceof MagneraEntity){
				speed = 5;
				scale = 3;
			} else {
				speed = 8;
				scale = 4;
			}
			//float r = 1, g = 0, b = 0;
			VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
			matrixStackIn.translate(0, 0.5, 0);
			
			matrixStackIn.scale(scale, scale, scale);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));

	        this.magnetModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
	        
			prevRotationTicks = ticks;
			ticks += speed;

	
		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		return TEXTURE;
	}

}
