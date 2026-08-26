package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.block.Blocks;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.Meshes.MeshAccessor;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedFreezeLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {
	private static final ResourceLocation ICE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/ice.png");

	public PatchedFreezeLayerRenderer() {
	}

	public static Meshes.MeshAccessor<? extends Mesh> getMesh(LivingEntity entity) {
		if (entity instanceof AbstractClientPlayer player) {
			return player.getSkin().model() == PlayerSkin.Model.WIDE ? Meshes.BIPED : Meshes.ALEX;
		}

		if (entity instanceof Zombie) return Meshes.BIPED;
		if (entity instanceof Skeleton) return Meshes.SKELETON;
		if (entity instanceof Creeper) return Meshes.CREEPER;
		if (entity instanceof EnderMan) return Meshes.ENDERMAN;
		if (entity instanceof Spider) return Meshes.SPIDER;
		if (entity instanceof IronGolem) return Meshes.IRON_GOLEM;
		if (entity instanceof Piglin) return Meshes.PIGLIN;
		if (entity instanceof Hoglin) return Meshes.HOGLIN;
		return null;
	}

	@Override
	protected void renderLayer(T entitypatch, E entity, @Nullable RenderLayer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
		if (entity.hasEffect(ModMobEffects.FREEZE) && entity.getEffect(ModMobEffects.FREEZE).getAmplifier() == 50) {
			PoseStack freshPose = new PoseStack();
			freshPose.pushPose();
			{
				float width = entity.getBbWidth() * 0.5F;
				float height = entity.getBbHeight() * 0.8F;

				Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;

				freshPose.translate(entity.getX() - camera.getPosition().x, entity.getY() - camera.getPosition().y, entity.getZ() - camera.getPosition().z);
				freshPose.mulPose(Axis.YN.rotationDegrees(entity.yBodyRot));

				freshPose.translate(-width, 0, -width);
				freshPose.scale(width * 2, height, width * 2);

				Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.ICE.defaultBlockState(), freshPose, buffer, packedLight, OverlayTexture.NO_OVERLAY);

				MeshAccessor<? extends Mesh> accessor = getMesh(entity);
				if (accessor != null) {
					Mesh mesh = accessor.get();
					mesh.draw(poseStack, buffer, EpicFightRenderTypes.entityTranslucent(ICE_TEXTURE), Mesh.DrawingFunction.NEW_ENTITY, packedLight, 1, 1, 1, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), poses);
				}
			}
			freshPose.popPose();
		}
	}
}