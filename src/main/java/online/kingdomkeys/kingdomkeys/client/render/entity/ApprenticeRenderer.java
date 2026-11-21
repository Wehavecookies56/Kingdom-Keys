package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ForetellerModel;
import online.kingdomkeys.kingdomkeys.entity.mob.ApprenticeEntity;

public class ApprenticeRenderer extends HumanoidMobRenderer<ApprenticeEntity, ForetellerModel<ApprenticeEntity>> {

	private static final ResourceLocation[] TEXTURES = new ResourceLocation[ApprenticeEntity.SKIN_COUNT];

	static {
		for (int i = 0; i < TEXTURES.length; i++) {
			TEXTURES[i] = KingdomKeys.rl("textures/entity/mob/apprentices/apprentice_" + i + ".png");
		}
	}

	public ApprenticeRenderer(EntityRendererProvider.Context context) {
		super(context, new ForetellerModel<>(context.bakeLayer(ForetellerModel.LAYER_LOCATION)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public void render(ApprenticeEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		boolean fighting = entity.isAggressive() && !entity.getMainHandItem().isEmpty();
		getModel().rightArmPose = fighting ? HumanoidModel.ArmPose.ITEM : HumanoidModel.ArmPose.EMPTY;

		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(ApprenticeEntity entity) {
		return TEXTURES[Mth.clamp(entity.getSkin(), 0, TEXTURES.length - 1)];
	}
}
