package online.kingdomkeys.kingdomkeys.client.render.entity;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

@OnlyIn(Dist.CLIENT)
public class OrgPortalEntityRenderer extends EntityRenderer<OrgPortalEntity> {

	Random rand = new Random();
	
	public OrgPortalEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(OrgPortalEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));

			matrixStackIn.pushPose();
			{

				float a = 0.9F;
				float rgb = 1;

				float ticks = entity.tickCount;
		        if(ticks < 10) //Growing
		        	matrixStackIn.scale(ticks*0.2f, ticks*0.2f, ticks*0.2f);
		        else if(ticks > 90) //Disappearing
		        	matrixStackIn.scale((100-ticks)*0.2f, (100-ticks)*0.2f, (100-ticks)*0.2f);
		        else //Static size
		        	matrixStackIn.scale(2.0f, 2.0f, 2.0f);
		        
				matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(Minecraft.getInstance().player.getRotationVector().y));
				
				for (BakedQuad quad : model.getQuads(null, null, rand, EmptyModelData.INSTANCE)) {
					buffer.addVertexData(matrixStackIn.last(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
				}
				

			}
			matrixStackIn.popPose();


		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(OrgPortalEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

}
