package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.HeartEntity;

@OnlyIn(Dist.CLIENT)
public class HeartEntityRenderer extends EntityRenderer<HeartEntity> {
	float rotation = 0;
	
	public HeartEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(HeartEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/heart")));

			matrixStackIn.pushPose();
			{

				float a = 1;
				float rgb = 1;

				float ticks = entity.tickCount;
				if (ticks < 10) // Growing
					matrixStackIn.scale(ticks * 0.0005f, ticks * 0.0005f, ticks * 0.0005f);
				else if (ticks > HeartEntity.MAX_TICKS - 10) // Disappearing
					matrixStackIn.scale((HeartEntity.MAX_TICKS - ticks) * 0.0005f, (HeartEntity.MAX_TICKS - ticks) * 0.0005f, (HeartEntity.MAX_TICKS - ticks) * 0.0005f);
				else // Static size
					matrixStackIn.scale(0.005f, 0.005f, 0.005f);
						        
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(ticks*20));
				
				for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, RenderType.cutout())) { //TODO totally made this up in the 1.19.3 port
					buffer.putBulkData(matrixStackIn.last(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
				}
				

			}
			matrixStackIn.popPose();


		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(HeartEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/heart.png");
	}
}
