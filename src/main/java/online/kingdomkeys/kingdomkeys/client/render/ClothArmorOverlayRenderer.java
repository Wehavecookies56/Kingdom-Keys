package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.item.BaseArmorItem;
import online.kingdomkeys.kingdomkeys.item.UnionApprenticeArmorItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClothArmorOverlayRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static final ModelLayerLocation BASE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "outer");
	public static final ModelLayerLocation BASE_LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "leggings");

	public static final ModelLayerLocation OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "outer");
	public static final ModelLayerLocation LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "leggings");

	public static final ModelLayerLocation APPRENTICE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_apprentice"), "outer");
	public static final ModelLayerLocation APPRENTICE_LEGGINGS_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_apprentice"), "leggings");

	public static final ModelLayerLocation SLIM_BASE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_base"), "outer_slim");
	public static final ModelLayerLocation SLIM_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_overlay"), "outer_slim");
	public static final ModelLayerLocation SLIM_APPRENTICE_OUTER_LAYER = new ModelLayerLocation(KingdomKeys.rl("cloth_apprentice"), "outer_slim");

	private static final float BASE_SIZE = 0.42F;
	private static final float OUTER_BASE_SIZE = 0.48F;

	private static final float BASE_LEGGINGS_SIZE = 0.30F;
	private static final float OUTER_LEGGINGS_SIZE = 0.36F;

	private static final float APPRENTICE_SIZE = 0.40F;
	private static final float APPRENTICE_LEGGINGS_SIZE = 0.28F;

	// With the skin's outer layer hidden underneath, clothes no longer need to clear it
	private static final float TIGHTEN = 0.20F;

	public enum Fit {
		NORMAL(""),
		TIGHT("_tight"),
		// The waist stays clear of the skin's jacket, which only hides when something is worn on the chest
		TIGHT_LEGS("_tight_legs");

		private final String suffix;

		Fit(String suffix) {
			this.suffix = suffix;
		}

		public ModelLayerLocation of(ModelLayerLocation layer) {
			return this == NORMAL ? layer : new ModelLayerLocation(layer.getModel(), layer.getLayer() + suffix);
		}
	}

	private static final Map<ResourceLocation, Boolean> EXISTING = new HashMap<>();
	private static EntityModelSet bakedFrom;
	private static final Map<String, HumanoidArmorModel<LivingEntity>> BASE_MODELS = new HashMap<>();
	private final EntityModelSet modelSet;
	private final Map<ModelLayerLocation, HumanoidArmorModel<T>> models = new HashMap<>();

	public ClothArmorOverlayRenderer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
		super(parent);
		this.modelSet = modelSet;
	}

	private HumanoidArmorModel<T> model(ModelLayerLocation layer, Fit fit) {
		return models.computeIfAbsent(fit.of(layer), location -> new HumanoidArmorModel<>(modelSet.bakeLayer(location)));
	}

	public static void registerLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> register) {
		registerFits(register, BASE_OUTER_LAYER, BASE_SIZE, false);
		registerFits(register, BASE_LEGGINGS_LAYER, BASE_LEGGINGS_SIZE, false);
		registerFits(register, OUTER_LAYER, OUTER_BASE_SIZE, false);
		registerFits(register, LEGGINGS_LAYER, OUTER_LEGGINGS_SIZE, false);
		registerFits(register, APPRENTICE_OUTER_LAYER, APPRENTICE_SIZE, false);
		registerFits(register, APPRENTICE_LEGGINGS_LAYER, APPRENTICE_LEGGINGS_SIZE, false);
		registerFits(register, SLIM_BASE_OUTER_LAYER, BASE_SIZE, true);
		registerFits(register, SLIM_OUTER_LAYER, OUTER_BASE_SIZE, true);
		registerFits(register, SLIM_APPRENTICE_OUTER_LAYER, APPRENTICE_SIZE, true);
	}

	private static void registerFits(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> register, ModelLayerLocation layer, float size, boolean slim) {
		register.accept(Fit.NORMAL.of(layer), () -> clothLayer(size, size, slim));
		register.accept(Fit.TIGHT.of(layer), () -> clothLayer(size - TIGHTEN, size - TIGHTEN, slim));
		register.accept(Fit.TIGHT_LEGS.of(layer), () -> clothLayer(size, size - TIGHTEN, slim));
	}

	// Only players get their skin's outer layer hidden under clothes, and not when Epic Fight draws them
	public static boolean fitsTight(LivingEntity entity) {
		return ModConfigs.hideSkinLayerUnderArmor && !KingdomKeys.efmLoaded && entity instanceof AbstractClientPlayer;
	}

	public static boolean worn(LivingEntity entity, EquipmentSlot slot) {
		ItemStack stack = entity.getItemBySlot(slot);
		return !stack.isEmpty() && !(stack.getItem() instanceof ElytraItem);
	}

	private static Fit fit(LivingEntity entity, EquipmentSlot slot) {
		if (!fitsTight(entity)) {
			return Fit.NORMAL;
		}
		return slot == EquipmentSlot.LEGS && !worn(entity, EquipmentSlot.CHEST) ? Fit.TIGHT_LEGS : Fit.TIGHT;
	}

	public static boolean isSlim(LivingEntity entity) {
		if (entity instanceof AbstractClientPlayer player) {
			return player.getSkin().model() == PlayerSkin.Model.SLIM;
		}
		return entity instanceof ForetellerEntity foreteller && foreteller.isSlim();
	}

	private static boolean textureExists(ResourceLocation texture) {
		return EXISTING.computeIfAbsent(texture, rl -> Minecraft.getInstance().getResourceManager().getResource(rl).isPresent());
	}

	public static void clearCache() {
		EXISTING.clear();
		BASE_MODELS.clear();
		bakedFrom = null;
	}

	public static ResourceLocation fixedTexture(ItemStack stack, EquipmentSlot slot) {
		if (!(stack.getItem() instanceof UnionApprenticeArmorItem apprentice)) {
			return null;
		}
		String name = apprentice.getTextureName(stack);
		String layer = slot == EquipmentSlot.LEGS ? "layer_2.png" : "layer_1.png";
		ResourceLocation texture = KingdomKeys.rl("textures/models/armor/" + name + layer);
		return textureExists(texture) ? texture : null;
	}

	public static ResourceLocation overlay1Texture(ItemStack stack, EquipmentSlot slot) {
		if (!(stack.getItem() instanceof UnionApprenticeArmorItem apprentice)) {
			return null;
		}
		String name = apprentice.getTextureName(stack);
		String layer = slot == EquipmentSlot.LEGS ? "layer_2_overlay1.png" : "layer_1_overlay1.png";
		ResourceLocation texture = KingdomKeys.rl("textures/models/armor/" + name + layer);
		return textureExists(texture) ? texture : null;
	}

	public static ResourceLocation overlay2Texture(ItemStack stack, EquipmentSlot slot) {
		if (!(stack.getItem() instanceof UnionApprenticeArmorItem apprentice)) {
			return null;
		}
		String name = apprentice.getTextureName(stack);
		String layer = slot == EquipmentSlot.LEGS ? "layer_2_overlay2.png" : "layer_1_overlay2.png";
		ResourceLocation texture = KingdomKeys.rl("textures/models/armor/" + name + layer);
		return textureExists(texture) ? texture : null;
	}

	public static ResourceLocation overlayTexture(ItemStack stack, EquipmentSlot slot) {
		if (!(stack.getItem() instanceof BaseArmorItem armor) || armor.getTextureName() == null) {
			return null;
		}
		if (stack.getItem() instanceof UnionApprenticeArmorItem) {
			// Apprentice uses overlay1/overlay2 instead
			return null;
		}
		String layer = slot == EquipmentSlot.LEGS ? "_layer_2_overlay.png" : "_layer_1_overlay.png";
		ResourceLocation texture = KingdomKeys.rl("textures/models/armor/" + armor.getTextureName() + layer);
		return textureExists(texture) ? texture : null;
	}

	private static LayerDefinition clothLayer(float size, float legSize, boolean slim) {
		MeshDefinition mesh = HumanoidArmorModel.createBodyLayer(new CubeDeformation(size));
		PartDefinition root = mesh.getRoot();
		CubeDeformation legs = new CubeDeformation(legSize);

		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, legs), PartPose.offset(-1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, legs), PartPose.offset(1.9F, 12.0F, 0.0F));

		if (slim) {
			CubeDeformation arms = new CubeDeformation(size - 0.5F, size, size);
			root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, arms), PartPose.offset(-5.0F, 2.5F, 0.0F));
			root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, arms), PartPose.offset(5.0F, 2.5F, 0.0F));
		}
		return LayerDefinition.create(mesh, 64, 32);
	}

	public static HumanoidModel<?> baseModel(LivingEntity entity, EquipmentSlot slot) {
		boolean slim = isSlim(entity) && slot != EquipmentSlot.LEGS;
		ModelLayerLocation layer = slot == EquipmentSlot.LEGS ? BASE_LEGGINGS_LAYER : slim ? SLIM_BASE_OUTER_LAYER : BASE_OUTER_LAYER;
		return baseModel(fit(entity, slot).of(layer), ownFirstPersonBody(entity));
	}

	private static HumanoidModel<?> baseModel(ModelLayerLocation layer, boolean noTorso) {
		EntityModelSet models = Minecraft.getInstance().getEntityModels();

		if (models != bakedFrom) {
			bakedFrom = models;
			BASE_MODELS.clear();
		}

		return BASE_MODELS.computeIfAbsent(layer + (noTorso ? "#no_torso" : ""), key -> {
			ModelPart root = models.bakeLayer(layer);
			return new HumanoidArmorModel<>(noTorso ? withoutTorso(root) : root);
		});
	}

	private static ModelPart withoutTorso(ModelPart root) {
		Map<String, ModelPart> children = new HashMap<>();

		for (String name : new String[]{"head", "hat", "body", "right_arm", "left_arm", "right_leg", "left_leg"}) {
			children.put(name, name.equals("body") ? new ModelPart(List.of(), Map.of()) : root.getChild(name));
		}

		return new ModelPart(List.of(), children);
	}

	public static boolean ownFirstPersonBody(LivingEntity entity) {
		Minecraft mc = Minecraft.getInstance();
		return !ClientUtils.renderingEntityInGui && mc.options.getCameraType().isFirstPerson() && mc.getCameraEntity() == entity;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		renderChest(poseStack, buffer, packedLight, entity);
		renderLeggings(poseStack, buffer, packedLight, entity);
		renderBoots(poseStack, buffer, packedLight, entity);
	}

	private void renderChest(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
		if (stack.isEmpty()) {
			return;
		}

		HumanoidArmorModel<T> model = outerModel(stack, entity);
		model.body.copyFrom(getParentModel().body);
		model.rightArm.copyFrom(getParentModel().rightArm);
		model.leftArm.copyFrom(getParentModel().leftArm);

		if (stack.getItem() instanceof UnionApprenticeArmorItem) {
			renderApprenticeParts(poseStack, buffer, packedLight, stack, EquipmentSlot.CHEST, model, true, true, false);
		} else {
			renderNormalOverlay(poseStack, buffer, packedLight, stack, EquipmentSlot.CHEST, model, true, true, false);
		}
	}

	private void renderLeggings(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.LEGS);
		if (stack.isEmpty()) {
			return;
		}

		HumanoidArmorModel<T> model = leggingsModel(stack, entity);
		model.body.copyFrom(getParentModel().body);
		model.rightLeg.copyFrom(getParentModel().rightLeg);
		model.leftLeg.copyFrom(getParentModel().leftLeg);

		if (stack.getItem() instanceof UnionApprenticeArmorItem) {
			renderApprenticeParts(poseStack, buffer, packedLight, stack, EquipmentSlot.LEGS, model, true, false, true);
		} else {
			renderNormalOverlay(poseStack, buffer, packedLight, stack, EquipmentSlot.LEGS, model, true, false, true);
		}
	}

	private void renderBoots(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.FEET);
		if (stack.isEmpty()) {
			return;
		}

		HumanoidArmorModel<T> model = outerModel(stack, entity);
		model.rightLeg.copyFrom(getParentModel().rightLeg);
		model.leftLeg.copyFrom(getParentModel().leftLeg);

		if (stack.getItem() instanceof UnionApprenticeArmorItem) {
			renderApprenticeParts(poseStack, buffer, packedLight, stack, EquipmentSlot.FEET, model, false, false, true);
		} else {
			renderNormalOverlay(poseStack, buffer, packedLight, stack, EquipmentSlot.FEET, model, false, false, true);
		}
	}

	private void renderApprenticeParts(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack stack, EquipmentSlot slot, HumanoidArmorModel<T> model, boolean body, boolean arms, boolean legs) {

		UnionApprenticeArmorItem armor = (UnionApprenticeArmorItem) stack.getItem();
		int primary = 0xFF000000 | armor.getPrimaryColor(stack);
		int secondary = 0xFF000000 | armor.getSecondaryColor(stack);

		drawParts(poseStack, buffer, packedLight, model, fixedTexture(stack, slot), 0xFFFFFFFF, body, arms, legs);
		drawParts(poseStack, buffer, packedLight, model, overlay1Texture(stack, slot), primary, body, arms, legs);
		drawParts(poseStack, buffer, packedLight, model, overlay2Texture(stack, slot), secondary, body, arms, legs);
	}

	private void renderNormalOverlay(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack stack, EquipmentSlot slot, HumanoidArmorModel<T> model, boolean body, boolean arms, boolean legs) {
		drawParts(poseStack, buffer, packedLight, model, overlayTexture(stack, slot), 0xFFFFFFFF, body, arms, legs);
	}

	private void drawParts(PoseStack poseStack, MultiBufferSource buffer, int packedLight, HumanoidArmorModel<T> model, ResourceLocation texture, int color, boolean body, boolean arms, boolean legs) {
		if (texture == null) {
			return;
		}
		VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
		if (body) {
			model.body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		}
		if (arms) {
			model.rightArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
			model.leftArm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		}
		if (legs) {
			model.rightLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
			model.leftLeg.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		}
	}

	private HumanoidArmorModel<T> outerModel(ItemStack stack, T entity) {
		boolean slim = isSlim(entity);
		ModelLayerLocation layer = stack.getItem() instanceof UnionApprenticeArmorItem ? (slim ? SLIM_APPRENTICE_OUTER_LAYER : APPRENTICE_OUTER_LAYER) : (slim ? SLIM_OUTER_LAYER : OUTER_LAYER);
		return model(layer, fit(entity, EquipmentSlot.CHEST));
	}

	private HumanoidArmorModel<T> leggingsModel(ItemStack stack, T entity) {
		ModelLayerLocation layer = stack.getItem() instanceof UnionApprenticeArmorItem ? APPRENTICE_LEGGINGS_LAYER : LEGGINGS_LAYER;
		return model(layer, fit(entity, EquipmentSlot.LEGS));
	}
}
