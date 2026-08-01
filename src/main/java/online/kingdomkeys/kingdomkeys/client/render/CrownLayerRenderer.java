package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.armor.CrownModel;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

@OnlyIn(Dist.CLIENT)
public class CrownLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	private final CrownModel model;

	public CrownLayerRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet) {
		super(entityRendererIn);
		this.model = new CrownModel(modelSet.bakeLayer(CrownModel.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(entitylivingbaseIn instanceof Player player) || player.isInvisible())
			return;

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null)
			return;

		String crown = playerData.getCrown();
		if (crown.isEmpty())
			return;

		ResourceLocation texture = KingdomKeys.rl("textures/models/crown/" + crown + ".png");
		VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(texture));

		matrixStackIn.pushPose();
		{
			getParentModel().head.translateAndRotate(matrixStackIn);

			float scale = 0.5F;
			matrixStackIn.scale(scale, scale, scale);

			matrixStackIn.translate(playerData.getCrownOffsetX() / 16F, -1.001F + playerData.getCrownOffsetY() / 16F, playerData.getCrownOffsetZ() / 16F);

			model.root.yRot = Mth.DEG_TO_RAD * playerData.getCrownRotationY();
			model.root.xRot = Mth.DEG_TO_RAD * playerData.getCrownRotationX();
			model.root.zRot = Mth.DEG_TO_RAD * playerData.getCrownRotationZ();

			model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		}
		matrixStackIn.popPose();
	}

	public CrownModel getCrownModel() {
		return model;
	}
}
