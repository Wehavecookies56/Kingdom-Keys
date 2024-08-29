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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MagnetModel;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

@OnlyIn(Dist.CLIENT)
public class MagnetEntityRenderer extends EntityRenderer<ThrowableProjectile> {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"textures/entity/models/magnet.png");
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

			float speed = 0;
			float scale = 0;
			int maxTicks = 0;

			if(entity instanceof MagnetEntity magnet) {
				speed = 3;
				scale = 2;
				maxTicks = magnet.getMaxTicks();
			} else if (entity instanceof MagneraEntity magnera){
				speed = 5;
				scale = 3;
				maxTicks = magnera.getMaxTicks();

			} else if (entity instanceof MagnegaEntity magnega){
				speed = 8;
				scale = 4;
				maxTicks = magnega.getMaxTicks();
			}
			
			if(entity.tickCount < scale * 10) {
				scale = entity.tickCount / 10F;
			}
			if(entity.tickCount > maxTicks - scale * 10) {
				scale = (maxTicks - entity.tickCount) / 10F;
			}
			
			
			
			//float r = 1, g = 0, b = 0;
			VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
			matrixStackIn.translate(0, 1, 0);
			
			matrixStackIn.scale(scale, scale, scale);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));

	        this.magnetModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);
	        
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
