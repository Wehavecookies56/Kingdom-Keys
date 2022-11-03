package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.SoldierModel;
import online.kingdomkeys.kingdomkeys.entity.mob.SoldierEntity;

//TODO should be able to make this a a generic human renderer for the rest of the members
public class SoldierRenderer<Type extends SoldierEntity> extends MobRenderer<Type, SoldierModel<Type>> {

	public SoldierRenderer(EntityRendererProvider.Context context) {
        super(context, new SoldierModel<>(context.bakeLayer(SoldierModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void render(Type entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			//matrixStackIn.scale(0.7F, 0.7F, 0.7F);
			/*if (EntityHelper.getState(entityIn) == 1) {
				matrixStackIn.translate(0, 0.4, 0);
			} else if(EntityHelper.getState(entityIn) == 3) {
				matrixStackIn.translate(0, 1.5, 0);
			}*/
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.popPose();
	}
	
	@Override
	protected void scale(Type entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(Type pEntity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/soldier.png");
	}

}
