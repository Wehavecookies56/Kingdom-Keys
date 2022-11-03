package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MarluxiaModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;

//TODO should be able to make this a a generic human renderer for the rest of the members
public class MarluxiaRenderer extends HumanoidMobRenderer<MarluxiaEntity, MarluxiaModel<MarluxiaEntity>> {

	public MarluxiaRenderer(EntityRendererProvider.Context context) {
		super(context, new MarluxiaModel<>(context.bakeLayer(MarluxiaModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void render(MarluxiaEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			if (EntityHelper.getState(entityIn) == 1) {
				matrixStackIn.translate(0, 0.4, 0);
			} else if(EntityHelper.getState(entityIn) == 3) {
				matrixStackIn.translate(0, 1.5, 0);
			}
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(MarluxiaEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/marluxia.png");
	}

	@Override
	protected void scale(MarluxiaEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(1, 1, 1);
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

}
