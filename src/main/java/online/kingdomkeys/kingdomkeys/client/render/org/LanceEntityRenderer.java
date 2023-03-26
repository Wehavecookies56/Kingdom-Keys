package online.kingdomkeys.kingdomkeys.client.render.org;

import javax.annotation.Nullable;

import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceEntity;

@OnlyIn(Dist.CLIENT)
public class LanceEntityRenderer extends EntityRenderer<LanceEntity> {

	RandomSource rand = RandomSource.create();
	
	public LanceEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(LanceEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			String name = entity.getModel();
			
			VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "item/"+name));

			float scale = 0.02F;

			matrixStackIn.pushPose();
			{
				if(entity.tickCount > 1) {
					matrixStackIn.scale(scale, scale, scale);
	
					float a = 1;
					float rgb = 1;
					
					matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO)));
					if(entity.getRotationPoint() == 2)
						matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) + 270));
					else if(entity.getRotationPoint() == 0) {
						matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) + 90));
					}
					for (BakedQuad quad : model.getQuads(null, null, rand, ModelData.EMPTY, RenderType.translucent())) {
						buffer.putBulkData(matrixStackIn.last(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
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
	public ResourceLocation getTextureLocation(LanceEntity entity) {
		String name = entity.getModel().substring(entity.getModel().indexOf(KingdomKeys.MODID+".")+ KingdomKeys.MODID.length()+1);
		
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/"+name+".png");
	}

}
