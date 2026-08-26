package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedAeroLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {
	public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/trident_riptide.png");
	private static final ResourceLocation ICE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/ice.png");

	private final ModelPart box;

	public PatchedAeroLayerRenderer() {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();
		this.box = models.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK).getChild("box");
	}

	@Override
	protected void renderLayer(T entitypatch, E entity, @Nullable RenderLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
		if (entity.hasEffect(ModMobEffects.AERO)) {

			MobEffectInstance aero = entity.getEffect(ModMobEffects.AERO);
			VertexConsumer vertexConsumer = buffer.getBuffer(EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.entityCutoutNoCull(TEXTURE)));

			float age = entity.tickCount + partialTicks;

			for (int i = 1; i <= aero.getAmplifier() + 1; i++) {
				poseStack.pushPose();
				{
					float rot = age * 20F;
					if (i % 2 == 0) rot *= -1;

					poseStack.mulPose(Axis.YP.rotationDegrees(rot));

					float scale;
					poseStack.translate(0.0D, 1.0D, 0.0D);
					poseStack.scale(1.0F, -1.0F, 1.0F);
					switch (aero.getAmplifier()) {
						case 0:
							scale = 0.75F * i;
							poseStack.scale(scale, scale * 1.2F, scale);
							poseStack.translate(0.0D, -0.4F + 0.8F * i, 0.0D);
							break;

						case 1:
							scale = 0.85F * i;
							poseStack.scale(scale, scale, scale);
							poseStack.translate(0.0D, -0.8F + 0.8F * i, 0.0D);
							break;

						case 2:
							scale = 0.7F * i;
							poseStack.scale(scale, scale * 0.6F, scale);
							poseStack.translate(0.0D, -1.2F + 0.6F * i, 0.0D);
							break;
					}

					int color = -1;
					if (entity.hurtTime > 0)
						color = 0x64FFFF;

					this.box.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
				}
				poseStack.popPose();
			}
		}
	}

	public HumanoidMesh getModel(E e) {
		return ((AbstractClientPlayer) e).getSkin().model() == PlayerSkin.Model.WIDE ? Meshes.BIPED.get() : Meshes.ALEX.get();
	}

}