package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.DragoonModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.DragoonEntity;

//TODO should be able to make this a a generic human renderer for the rest of the members
public class DragoonRenderer<Type extends DragoonEntity> extends MobRenderer<Type, DragoonModel<Type>> {

	public DragoonRenderer(EntityRendererProvider.Context context) {
        super(context, new DragoonModel<>(context.bakeLayer(DragoonModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void render(Type entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			if(EntityHelper.getState(entityIn) == 0) {
				
			} else if (EntityHelper.getState(entityIn) == 1) {
				matrixStackIn.translate(0, -0.4, 0);
			} else if(EntityHelper.getState(entityIn) == 2) {
				matrixStackIn.translate(0, -0.8, 0);
			}
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.popPose();
	}
	
	@Override
	protected void scale(Type entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
		if (EntityHelper.getState(entitylivingbaseIn) == 1) {
			matrixStackIn.scale(0.6F, 0.6F, 0.6F);
		} else {
			matrixStackIn.scale(0.8F, 0.8F, 0.8F);
		}
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(Type pEntity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/dragoon.png");
	}

}
