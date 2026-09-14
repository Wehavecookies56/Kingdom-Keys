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
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;

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
		return RENDER_TYPES.computeIfAbsent(texture, rl -> EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.armorCutoutNoCull(rl)));
	}

	/*
	 * false = normal Epic Fight third person renderer
	 * true  = Epic Fight animated first person renderer
	 */
	private final boolean firstPerson;

	private record Meshes(SkinnedMesh chest, SkinnedMesh arm, SkinnedMesh waist, SkinnedMesh leg, SkinnedMesh boot) { }

	private Meshes org;
	private Meshes apprentice;

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

	private static void draw(SkinnedMesh mesh, ResourceLocation texture, int colour, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses) {
		if (mesh == null || texture == null) {
			return;
		}

		float r = (colour >> 16 & 0xFF) / 255F;
		float g = (colour >> 8 & 0xFF) / 255F;
		float b = (colour & 0xFF) / 255F;

		VertexConsumer vertexConsumer = buffer.getBuffer(renderType(texture));
		mesh.drawPosed(poseStack, vertexConsumer, Mesh.DrawingFunction.NEW_ENTITY, packedLight, r, g, b, 1.0F, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), poses);
	}

	@Override
	protected void renderLayer(T entityPatch, E entity, ClothArmorOverlayRenderer<E, M> vanillaLayer, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses, float bob, float yRot, float xRot, float partialTicks) {
		ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
		ItemStack leggings = entity.getItemBySlot(EquipmentSlot.LEGS);
		ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);

		if (!wearsCloth(chest, EquipmentSlot.CHEST) && !wearsCloth(leggings, EquipmentSlot.LEGS) && !wearsCloth(boots, EquipmentSlot.FEET)) {
			return;
		}

		ensureMeshes();

		/*
		 * CHEST
		 * Third person: body + both arms
		 * First person: arms only
		 */
		Meshes chestMeshes = familyOf(chest);
		paint(chest, EquipmentSlot.CHEST, firstPerson ? chestMeshes.arm() : chestMeshes.chest(), poseStack, buffer, packedLight, poses);

		/*
		 * LEGGINGS
		 * waist = torso shell
		 * legs  = leg shell, both from the leggings layer
		 */
		Meshes legMeshes = familyOf(leggings);

		if (!firstPerson) {
			paint(leggings, EquipmentSlot.LEGS, legMeshes.waist(), poseStack, buffer, packedLight, poses);
		}

		paint(leggings, EquipmentSlot.LEGS, legMeshes.leg(), poseStack, buffer, packedLight, poses);

		/*
		 * BOOTS Uses the outer leg geometry.
		 */
		paint(boots, EquipmentSlot.FEET, familyOf(boots).boot(), poseStack, buffer, packedLight, poses);
	}

	private static void paint(ItemStack stack, EquipmentSlot slot, SkinnedMesh mesh, PoseStack poseStack, MultiBufferSource buffer, int packedLight, OpenMatrix4f[] poses) {
		if (stack.getItem() instanceof UnionApprenticeArmorItem armor) {
			draw(mesh, ClothArmorOverlayRenderer.fixedTexture(stack, slot), 0xFFFFFF, poseStack, buffer, packedLight, poses);
			draw(mesh, ClothArmorOverlayRenderer.overlay1Texture(stack, slot), armor.getPrimaryColor(stack), poseStack, buffer, packedLight, poses);
			draw(mesh, ClothArmorOverlayRenderer.overlay2Texture(stack, slot), armor.getSecondaryColor(stack), poseStack, buffer, packedLight, poses);
			return;
		}

		draw(mesh, ClothArmorOverlayRenderer.overlayTexture(stack, slot), 0xFFFFFF, poseStack, buffer, packedLight, poses);
	}

	private static boolean wearsCloth(ItemStack stack, EquipmentSlot slot) {
		return stack.getItem() instanceof UnionApprenticeArmorItem ? ClothArmorOverlayRenderer.fixedTexture(stack, slot) != null : ClothArmorOverlayRenderer.overlayTexture(stack, slot) != null;
	}

	private Meshes familyOf(ItemStack stack) {
		return stack.getItem() instanceof UnionApprenticeArmorItem ? apprentice : org;
	}

	private void ensureMeshes() {
		if (org == null) {
			org = bake(ClothArmorOverlayRenderer.OUTER_LAYER, ClothArmorOverlayRenderer.LEGGINGS_LAYER);
		}

		if (apprentice == null) {
			apprentice = bake(ClothArmorOverlayRenderer.APPRENTICE_OUTER_LAYER, ClothArmorOverlayRenderer.APPRENTICE_LEGGINGS_LAYER);
		}
	}

	private static Meshes bake(ModelLayerLocation outer, ModelLayerLocation leggings) {
		return new Meshes(
				bakeMesh(outer, true, true, false),
				bakeMesh(outer, false, true, false),
				bakeMesh(leggings, true, false, false),
				bakeMesh(leggings, false, false, true),
				bakeMesh(outer, false, false, true)
		);
	}
}
