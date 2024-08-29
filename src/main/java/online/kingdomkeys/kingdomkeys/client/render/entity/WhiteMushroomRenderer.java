package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.WhiteMushroomModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.WhiteMushroomEntity;

//TODO should be able to make this a a generic human renderer for the rest of the members
public class WhiteMushroomRenderer<Type extends WhiteMushroomEntity> extends MobRenderer<Type, WhiteMushroomModel<Type>> {

	public WhiteMushroomRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiteMushroomModel<>(context.bakeLayer(WhiteMushroomModel.LAYER_LOCATION)), 0.5F);
	}

	int prevState = 0;
	int ticksDespawning = 0;

	@Override
	public void render(Type entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			if (EntityHelper.getState(entity) == -2) {
				if(prevState != -2){
					ticksDespawning = entity.tickCount;
				}
				VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
				BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/portal")));

				matrixStackIn.pushPose();
				{
					float[] rgb = { 0.2F, 0.1F, 0.3F, 0.9F };
					int offsetTicks = 25;
					int maxTicks = 20;

					float ticks = entity.tickCount - ticksDespawning - offsetTicks;
					if(ticks < 10) //Growing
						matrixStackIn.scale(ticks*0.2f, ticks*0.2f, ticks*0.2f);
					else if(ticks > maxTicks - 10 - offsetTicks) //Disappearing
						matrixStackIn.scale((maxTicks-ticks)*0.2f, (maxTicks-ticks)*0.2f, (maxTicks-ticks)*0.2f);
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
			super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.popPose();
		prevState = EntityHelper.getState(entity);
	}
	
	@Override
	protected void scale(Type entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(Type pEntity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/white_mushroom.png");
	}

}
