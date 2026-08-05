package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.model.entity.AirSoldierModel;
import online.kingdomkeys.kingdomkeys.entity.mob.AirSoldierEntity;

public class AirSoldierRenderer<Type extends AirSoldierEntity> extends MobRenderer<Type, AirSoldierModel<Type>> {

	public AirSoldierRenderer(EntityRendererProvider.Context context) {
		super(context, new AirSoldierModel<>(context.bakeLayer(AirSoldierModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	protected void scale(Type entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(0.7F, 0.7F, 0.7F);
		super.scale(entity, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(AirSoldierEntity entity) {
		return ClientUtils.variantTexture(entity.getTexture(), entity);
	}
}
