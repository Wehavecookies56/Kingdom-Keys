package online.kingdomkeys.kingdomkeys.client.render.org;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;

@OnlyIn(Dist.CLIENT)
public class ChakramEntityRenderer extends EntityRenderer<ChakramEntity> {

	public static final Factory FACTORY = new ChakramEntityRenderer.Factory();
	Random rand = new Random();
	float rotation = 0;
	
	public ChakramEntityRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(ChakramEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			String name = entity.getModel();
			
			VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "item/"+name));

			float scale = 0.03F; // (1.0f + poweringState) + (0.6f + poweringState) * progress0;

			matrixStackIn.pushPose();
			{
				matrixStackIn.scale(scale, scale, scale);
				matrixStackIn.translate(0, 10, 0);

				float a = 1;// MathHelper.clamp(1 - progress1, 0, 1);
				float rgb = 1;// MathHelper.clamp(progress1, 0, 1);
				
				if(entity.getRotationPoint() == 0) {
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.getYRot()O + (entity.getYRot() - entity.getYRot()O)));
					matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90));
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.getXRot()O + (entity.getXRot() - entity.getXRot()O)));
					matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(rotation));
				}
				
				if(entity.getRotationPoint() == 1) {
					
				}
				
				if(entity.getRotationPoint() == 2) {
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.getYRot()O + (entity.getYRot() - entity.getYRot()O)));
					matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90));
					matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(entity.getXRot()O + (entity.getXRot() - entity.getXRot()O)));
					matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(rotation));
				}

				
				rotation+=20;

				if(entity.tickCount > 1) {
					for (BakedQuad quad : model.getQuads(null, null, rand, EmptyModelData.INSTANCE)) {
						buffer.addVertexData(matrixStackIn.last(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
					}
				}

				matrixStackIn.popPose();
			}

		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ChakramEntity entity) {
		String name = entity.getModel().substring(entity.getModel().indexOf(KingdomKeys.MODID+".")+ KingdomKeys.MODID.length()+1);
		
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/"+name+".png");
	}

	public static class Factory implements IRenderFactory<ChakramEntity> {
		@Override
		public EntityRenderer<? super ChakramEntity> createRenderFor(EntityRenderDispatcher manager) {
			return new ChakramEntityRenderer(manager);
		}
	}
}
