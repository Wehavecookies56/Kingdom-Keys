package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import online.kingdomkeys.kingdomkeys.client.render.ClothArmorOverlayRenderer;

import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;

public class PatchedClothArmorOverlayRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>> extends PatchedLayer<E, T, M, ClothArmorOverlayRenderer<E, M>> {
	private static final Map<ResourceLocation, RenderType> RENDER_TYPES = new HashMap<>();

	private static RenderType renderType(ResourceLocation texture) {
		return RENDER_TYPES.computeIfAbsent(texture,
				rl -> EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.armorCutoutNoCull(rl)));
	}

	/*
	 * false = normal Epic Fight third person renderer
	 * true  = Epic Fight animated first person renderer
	 */
	private final boolean firstPerson;

	/*
	 * Epic Fight cannot directly animate Minecraft ModelParts.
	 * These are the Epic Fight skinned-mesh versions of our
	 * inflated armor models.
	 */
	private SkinnedMesh chestMesh;
	private SkinnedMesh armMesh;
	private SkinnedMesh waistMesh;
	private SkinnedMesh legMesh;
	private SkinnedMesh bootMesh;

	public PatchedClothArmorOverlayRenderer(boolean firstPerson) {
		this.firstPerson = firstPerson;
	}

	/*
	 * Converts one of our Minecraft HumanoidArmorModels into an Epic Fight SkinnedMesh.
	 *
	 * body = render torso
	 * arms = render both arms
	 * legs = render both legs
	 */
	private static SkinnedMesh bakeMesh(ModelLayerLocation layer, boolean body, boolean arms, boolean legs) {
		HumanoidArmorModel<LivingEntity> model = new HumanoidArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(layer));

		model.setAllVisible(false);

		model.body.visible = body;

		model.rightArm.visible = arms;
		model.leftArm.visible = arms;

		model.rightLeg.visible = legs;
		model.leftLeg.visible = legs;

		return HumanoidModelBaker.VANILLA_TRANSFORMER.transformArmorModel(model);
	}

	private static void draw(SkinnedMesh mesh, RenderType renderType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses) {
		if (mesh == null) {
			return;
		}

		VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
		mesh.drawPosed(poseStack, vertexConsumer, Mesh.DrawingFunction.NEW_ENTITY, packedLight, 1.0F, 1.0F, 1.0F, 1.0F, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), poses);
	}

	@Override
	protected void renderLayer(T entityPatch, E entity, ClothArmorOverlayRenderer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
		ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
		ItemStack leggings = entity.getItemBySlot(EquipmentSlot.LEGS);
		ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);

		ResourceLocation chestTexture = ClothArmorOverlayRenderer.overlayTexture(chest, EquipmentSlot.CHEST);
		ResourceLocation leggingsTexture = ClothArmorOverlayRenderer.overlayTexture(leggings, EquipmentSlot.LEGS);
		ResourceLocation bootsTexture = ClothArmorOverlayRenderer.overlayTexture(boots, EquipmentSlot.FEET);

		if (chestTexture == null && leggingsTexture == null && bootsTexture == null) {
			return;
		}

		ensureMeshes();

		/*
		 * CHEST
		 * Third person: body + both arms
		 * First person: arms only
		 */
		if (chestTexture != null) {
			RenderType type = renderType(chestTexture);

			if (firstPerson) {
				draw(armMesh, type, poseStack, buffer, packedLight, poses);
			} else {
				draw(chestMesh, type, poseStack, buffer, packedLight, poses);
			}
		}

		/*
		 * LEGGINGS
		 * waist = torso shell
		 * legs  = leg shell, both from the leggings layer
		 */
		if (leggingsTexture != null) {
			RenderType type = renderType(leggingsTexture);

			if (!firstPerson) {
				draw(waistMesh, type, poseStack, buffer, packedLight, poses);
			}

			draw(legMesh, type, poseStack, buffer, packedLight, poses);
		}

		/*
		 * BOOTS Uses the outer leg geometry.
		 */
		if (bootsTexture != null) {
			draw(bootMesh, renderType(bootsTexture), poseStack, buffer, packedLight, poses);
		}
	}

	/*
	 * Build Epic Fight-compatible skinned meshes from the exact same model layers used by
	 * ClothArmorOverlayRenderer. Las piernas de las mallas salen de LEGGINGS_LAYER y las de las botas de
	 * OUTER_LAYER, igual que alli: si las dos salieran de la misma capa quedarian a la misma distancia del
	 * cuerpo y pelearian por el z-buffer.
	 */
	private void ensureMeshes() {
		if (chestMesh == null) {
			chestMesh = bakeMesh(ClothArmorOverlayRenderer.OUTER_LAYER, true, true, false);
		}

		if (armMesh == null) {
			armMesh = bakeMesh(ClothArmorOverlayRenderer.OUTER_LAYER, false, true, false);
		}

		if (waistMesh == null) {
			waistMesh = bakeMesh(ClothArmorOverlayRenderer.LEGGINGS_LAYER, true, false, false);
		}

		if (legMesh == null) {
			legMesh = bakeMesh(ClothArmorOverlayRenderer.LEGGINGS_LAYER, false, false, true);
		}

		if (bootMesh == null) {
			bootMesh = bakeMesh(ClothArmorOverlayRenderer.OUTER_LAYER, false, false, true);
		}
	}
}
