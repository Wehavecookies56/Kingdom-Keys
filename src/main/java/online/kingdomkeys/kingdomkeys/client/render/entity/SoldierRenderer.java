package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.SoldierModel;
import online.kingdomkeys.kingdomkeys.client.render.HeartlessEyesLayerRenderer;
import online.kingdomkeys.kingdomkeys.entity.mob.CommanderEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DesertorEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.SoldierEntity;

public class SoldierRenderer<Type extends SoldierEntity> extends MobRenderer<Type, SoldierModel<Type>> {

	public SoldierRenderer(EntityRendererProvider.Context context) {
        super(context, new SoldierModel<>(context.bakeLayer(SoldierModel.LAYER_LOCATION)), 0.5F);
		this.addLayer(new HeartlessEyesLayerRenderer<>(this, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/soldier_eyes.png")));
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
		if(entitylivingbaseIn instanceof CommanderEntity) {
			matrixStackIn.scale(1F, 1F, 1F);
		} else if(entitylivingbaseIn instanceof DesertorEntity) {
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
		} else {
			matrixStackIn.scale(0.6F, 0.6F, 0.6F);
		}
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(SoldierEntity entity) {
		return entity.getTexture();
	}

}
