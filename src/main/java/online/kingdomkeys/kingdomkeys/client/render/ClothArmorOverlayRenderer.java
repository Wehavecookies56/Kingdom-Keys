package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
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
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClothArmorOverlayRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static final ModelLayerLocation BASE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "outer");
	public static final ModelLayerLocation BASE_LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "leggings");

	public static final ModelLayerLocation OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "outer");
	public static final ModelLayerLocation LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "leggings");

	public static final ModelLayerLocation APPRENTICE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_apprentice"), "outer");
	public static final ModelLayerLocation APPRENTICE_LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_apprentice"), "leggings");

	private static final float BASE_SIZE = 0.42F;
	private static final float OUTER_BASE_SIZE = 0.48F;

	private static final float BASE_LEGGINGS_SIZE = 0.30F;
	private static final float OUTER_LEGGINGS_SIZE = 0.36F;

	private static final float APPRENTICE_SIZE = 0.40F;
	private static final float APPRENTICE_LEGGINGS_SIZE = 0.28F;


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
	private final HumanoidArmorModel<T> apprenticeOuterModel;
	private final HumanoidArmorModel<T> apprenticeLeggingsModel;

	public ClothArmorOverlayRenderer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
		super(parent);
		this.outerModel = new HumanoidArmorModel<>(modelSet.bakeLayer(OUTER_LAYER));
		this.leggingsModel = new HumanoidArmorModel<>(modelSet.bakeLayer(LEGGINGS_LAYER));
		this.apprenticeOuterModel = new HumanoidArmorModel<>(modelSet.bakeLayer(APPRENTICE_OUTER_LAYER));
		this.apprenticeLeggingsModel = new HumanoidArmorModel<>(modelSet.bakeLayer(APPRENTICE_LEGGINGS_LAYER));
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

	// The apprentice's base and dyeable details intentionally share a size
	public static LayerDefinition createApprenticeOuterLayer() {
		return clothLayer(APPRENTICE_SIZE);
	}

	public static LayerDefinition createApprenticeLeggingsLayer() {
		return clothLayer(APPRENTICE_LEGGINGS_SIZE);
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

		HumanoidArmorModel<T> model = outerModel(stack);
		model.body.copyFrom(getParentModel().body);
		model.rightArm.copyFrom(getParentModel().rightArm);
		model.leftArm.copyFrom(getParentModel().leftArm);
		int color = overlayColor(stack);
		model.body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		model.rightArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		model.leftArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
	}

	private void renderLeggings(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.LEGS);
		ResourceLocation texture = overlayTexture(stack, EquipmentSlot.LEGS);

		if (texture == null) {
			return;
		}

		VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
		HumanoidArmorModel<T> model = leggingsModel(stack);
		model.body.copyFrom(getParentModel().body);
		model.rightLeg.copyFrom(getParentModel().rightLeg);
		model.leftLeg.copyFrom(getParentModel().leftLeg);

		int color = overlayColor(stack);
		model.body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		model.rightLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		model.leftLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
	}

	private void renderBoots(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.FEET);
		ResourceLocation texture = overlayTexture(stack, EquipmentSlot.FEET);

		if (texture == null) {
			return;
		}

		VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
		HumanoidArmorModel<T> model = outerModel(stack);
		model.rightLeg.copyFrom(getParentModel().rightLeg);
		model.leftLeg.copyFrom(getParentModel().leftLeg);
		int color = overlayColor(stack);
		model.rightLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		model.leftLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
	}

	private HumanoidArmorModel<T> outerModel(ItemStack stack) {
		return stack.getItem() instanceof UnionApprenticeArmorItem ? apprenticeOuterModel : outerModel;
	}

	private HumanoidArmorModel<T> leggingsModel(ItemStack stack) {
		return stack.getItem() instanceof UnionApprenticeArmorItem ? apprenticeLeggingsModel : leggingsModel;
	}

	private static int overlayColor(ItemStack stack) {
		if (stack.getItem() instanceof UnionApprenticeArmorItem apprenticeArmor) {
			return 0xFF000000 | apprenticeArmor.getDetailColor(stack);
		}

		return 0xFFFFFFFF;
	}
}
