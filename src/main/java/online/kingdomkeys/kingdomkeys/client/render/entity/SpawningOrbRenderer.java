package online.kingdomkeys.kingdomkeys.client.render.entity;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.SpawningOrbEntity;

public class SpawningOrbRenderer extends EntityRenderer<SpawningOrbEntity> {

    public SpawningOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
    }

    @Override
	public void render(SpawningOrbEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));

			matrixStackIn.pushPose();
			{

				float[] rgb = entity.getEntityType().equals(MobType.NOBODY.name()) ? new float[] { 0.6F, 0.6F, 0.6F, 0.9F } : new float[] { 0.2F, 0.1F, 0.3F, 0.9F };

				float ticks = entity.tickCount;
		        if(ticks < 10) //Growing
		        	matrixStackIn.scale(ticks*0.2f, ticks*0.2f, ticks*0.2f);
		        else if(ticks > 90) //Disappearing
		        	matrixStackIn.scale((100-ticks)*0.2f, (100-ticks)*0.2f, (100-ticks)*0.2f);
		        else //Static size
		        	matrixStackIn.scale(2.0f, 2.0f, 2.0f);
		        
	        	matrixStackIn.scale(1.0f, 0.5f, 1.0f);

				matrixStackIn.mulPose(Axis.YN.rotationDegrees(Minecraft.getInstance().player.getRotationVector().y));
				
				for (BakedQuad quad : model.getQuads(null, null, entity.level().random, ModelData.EMPTY, RenderType.translucent())) {
					buffer.putBulkData(matrixStackIn.last(), quad, rgb[0], rgb[1], rgb[2], rgb[3], 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
				}
				
			}
			matrixStackIn.popPose();


		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	private static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/portal.png");

    @Override
    public ResourceLocation getTextureLocation(SpawningOrbEntity entity) {
        return TEXTURE;
    }
}
