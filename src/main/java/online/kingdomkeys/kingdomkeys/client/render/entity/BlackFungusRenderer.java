package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.WhiteMushroomModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BlackFungusEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.WhiteMushroomEntity;

//TODO should be able to make this a a generic human renderer for the rest of the members
public class BlackFungusRenderer<Type extends BlackFungusEntity> extends MobRenderer<Type, WhiteMushroomModel<Type>> {

	public BlackFungusRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiteMushroomModel<>(context.bakeLayer(WhiteMushroomModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public void render(Type entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.popPose();
	}
	
	@Override
	protected void scale(Type entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
		if(EntityHelper.getState(entitylivingbaseIn) == -5){
			matrixStackIn.scale(0.8F, 0.4F, 0.8F);
		} else {
			matrixStackIn.scale(0.6F, 0.6F, 0.6F);
		}
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(Type pEntity) {
		if(EntityHelper.getState(pEntity) == -4)
			return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/black_fungus_stone.png");
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/black_fungus.png");
	}

}
