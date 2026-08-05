package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.model.entity.DefenderModel;
import online.kingdomkeys.kingdomkeys.entity.mob.DefenderEntity;

public class DefenderRenderer<Type extends DefenderEntity> extends MobRenderer<Type, DefenderModel<Type>> {

	public DefenderRenderer(EntityRendererProvider.Context context) {
		super(context, new DefenderModel<>(context.bakeLayer(DefenderModel.LAYER_LOCATION)), 0.6F);
	}

	@Override
	protected void scale(Type entity, PoseStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(1.2F, 1.2F, 1.2F);
		super.scale(entity, matrixStackIn, partialTickTime);
	}

	@Override
	public ResourceLocation getTextureLocation(DefenderEntity entity) {
		return ClientUtils.variantTexture(entity.getTexture(), entity);
	}
}
