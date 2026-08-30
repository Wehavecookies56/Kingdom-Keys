package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.BaseArmorItem;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClothArmorOverlayRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static final ModelLayerLocation BASE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "outer");
	public static final ModelLayerLocation BASE_LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "leggings");

	public static final ModelLayerLocation OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "outer");
	public static final ModelLayerLocation LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "leggings");

	private static final float BASE_SIZE = 0.42F;
	private static final float OUTER_BASE_SIZE = 0.48F;

	private static final float BASE_LEGGINGS_SIZE = 0.30F;
	private static final float OUTER_LEGGINGS_SIZE = 0.36F;

	private static final Map<ResourceLocation, Boolean> EXISTING_OVERLAYS = new HashMap<>();


	public static ResourceLocation overlayTexture(ItemStack stack, EquipmentSlot slot) {
		if (!(stack.getItem() instanceof BaseArmorItem armor) || armor.getTextureName() == null) {
			return null;
		}

		String layer = slot == EquipmentSlot.LEGS ? "_layer_2_overlay.png" : "_layer_1_overlay.png";
		ResourceLocation texture = KingdomKeys.rl("textures/models/armor/" + armor.getTextureName() + layer);

		return EXISTING_OVERLAYS.computeIfAbsent(texture, rl -> Minecraft.getInstance().getResourceManager().getResource(rl).isPresent()) ? texture : null;
	}

	public static void clearCache() {
		EXISTING_OVERLAYS.clear();
		bakedFrom = null;
	}

	private final HumanoidArmorModel<T> outerModel;
	private final HumanoidArmorModel<T> leggingsModel;

	public ClothArmorOverlayRenderer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
		super(parent);
		this.outerModel = new HumanoidArmorModel<>(modelSet.bakeLayer(OUTER_LAYER));
		this.leggingsModel = new HumanoidArmorModel<>(modelSet.bakeLayer(LEGGINGS_LAYER));
	}

	private static LayerDefinition clothLayer(float size) {
		MeshDefinition mesh = HumanoidArmorModel.createBodyLayer(new CubeDeformation(size));
		PartDefinition root = mesh.getRoot();
		CubeDeformation legs = new CubeDeformation(size);

		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, legs), PartPose.offset(-1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, legs), PartPose.offset(1.9F, 12.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 32);
	}

	public static LayerDefinition createOuterLayer() {
		return clothLayer(OUTER_BASE_SIZE);
	}

	public static LayerDefinition createLeggingsLayer() {
		return clothLayer(OUTER_LEGGINGS_SIZE);
	}

	public static LayerDefinition createBaseOuterLayer() {
		return clothLayer(BASE_SIZE);
	}

	public static LayerDefinition createBaseLeggingsLayer() {
		return clothLayer(BASE_LEGGINGS_SIZE);
	}

	private static EntityModelSet bakedFrom;
	private static HumanoidArmorModel<LivingEntity> baseOuter, baseLeggings;

	public static HumanoidModel<?> baseModel(EquipmentSlot slot) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();

		if (models != bakedFrom) {
			bakedFrom = models;
			baseOuter = new HumanoidArmorModel<>(models.bakeLayer(BASE_OUTER_LAYER));
			baseLeggings = new HumanoidArmorModel<>(models.bakeLayer(BASE_LEGGINGS_LAYER));
		}

		return slot == EquipmentSlot.LEGS ? baseLeggings : baseOuter;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		renderChest(poseStack, buffer, packedLight, entity);
		renderLeggings(poseStack, buffer, packedLight, entity);
		renderBoots(poseStack, buffer, packedLight, entity);
	}

	private void renderChest(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
		ResourceLocation texture = overlayTexture(stack, EquipmentSlot.CHEST);

		if (texture == null) {
			return;
		}

		VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));

		outerModel.body.copyFrom(getParentModel().body);
		outerModel.rightArm.copyFrom(getParentModel().rightArm);
		outerModel.leftArm.copyFrom(getParentModel().leftArm);
		outerModel.body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		outerModel.rightArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		outerModel.leftArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
	}

	private void renderLeggings(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.LEGS);
		ResourceLocation texture = overlayTexture(stack, EquipmentSlot.LEGS);

		if (texture == null) {
			return;
		}

		VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
		leggingsModel.body.copyFrom(getParentModel().body);
		leggingsModel.rightLeg.copyFrom(getParentModel().rightLeg);
		leggingsModel.leftLeg.copyFrom(getParentModel().leftLeg);

		leggingsModel.body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		leggingsModel.rightLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		leggingsModel.leftLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
	}

	private void renderBoots(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.FEET);
		ResourceLocation texture = overlayTexture(stack, EquipmentSlot.FEET);

		if (texture == null) {
			return;
		}

		VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
		outerModel.rightLeg.copyFrom(getParentModel().rightLeg);
		outerModel.leftLeg.copyFrom(getParentModel().leftLeg);
		outerModel.rightLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		outerModel.leftLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
	}
}
