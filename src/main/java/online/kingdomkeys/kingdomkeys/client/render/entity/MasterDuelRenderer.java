package online.kingdomkeys.kingdomkeys.client.render.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ForetellerModel;
import online.kingdomkeys.kingdomkeys.entity.mob.MasterDuelEntity;

public class MasterDuelRenderer extends HumanoidMobRenderer<MasterDuelEntity, ForetellerModel<MasterDuelEntity>> {
	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/mob/foreteller.png");

	public MasterDuelRenderer(EntityRendererProvider.Context context) {
		super(context, new ForetellerModel<>(context.bakeLayer(ForetellerModel.LAYER_LOCATION)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(MasterDuelEntity entity) {
		return TEXTURE;
	}
}
