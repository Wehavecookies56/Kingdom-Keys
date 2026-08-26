package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.render.CrownLayerRenderer;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedCrownLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>>
		extends PatchedLayer<E, T, M, CrownLayerRenderer<E, M>> {

	@Override
	protected void renderLayer(T entitypatch, E entity, @Nullable CrownLayerRenderer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
		if (!(entity instanceof Player player) || player.isInvisible())
			return;

		Minecraft mc = Minecraft.getInstance();
		if (player == mc.player && mc.options.getCameraType().isFirstPerson())
			return;

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null)
			return;

		String variant = playerData.getCrown();
		if (variant.isEmpty())
			return;

		int headBone = Armatures.BIPED.get().searchJointByName("Head").getId();

		ResourceLocation texture = KingdomKeys.rl("textures/models/crown/" + variant + ".png");
		VertexConsumer consumer = buffer.getBuffer(net.minecraft.client.renderer.RenderType.entityCutoutNoCull(texture));

		poseStack.pushPose();
		{
			poseStack.mulPose(OpenMatrix4f.exportToMojangMatrix(poses[headBone]));
			poseStack.mulPose(Axis.XP.rotationDegrees(180F));

			float scale = 0.5F;
			poseStack.scale(scale, scale, scale);
			poseStack.translate(-playerData.getCrownOffsetX() / 16F, -1.001F + playerData.getCrownOffsetY() / 16F, -playerData.getCrownOffsetZ() / 16F);

			vanillaLayer.getCrownModel().root.yRot = Mth.DEG_TO_RAD * playerData.getCrownRotationY();
			vanillaLayer.getCrownModel().root.xRot = -Mth.DEG_TO_RAD * playerData.getCrownRotationX();
			vanillaLayer.getCrownModel().root.zRot = -Mth.DEG_TO_RAD * playerData.getCrownRotationZ();
			vanillaLayer.getCrownModel().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

		}
		poseStack.popPose();
	}
}
