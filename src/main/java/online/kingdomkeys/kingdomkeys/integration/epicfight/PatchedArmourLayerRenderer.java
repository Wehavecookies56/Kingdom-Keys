package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorBaseModel;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeArmorItem;
import yesman.epicfight.api.client.event.types.render.PrepareModelEvent;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer.armorModels;

public class PatchedArmourLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>> extends PatchedLayer<E, T, M, KeybladeArmorRenderer<E, M>> {

	/** Armor slots in inventory order; {@code player.getInventory().armor} uses the same indices. */
	private static final List<EquipmentSlot> SLOTS = List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);

	/** Root children every {@link HumanoidModel} expects to find. */
	private static final List<String> ROOT_PARTS = List.of("head", "hat", "body", "right_arm", "left_arm", "right_leg", "left_leg");

	/** Which root parts actually belong to each armour slot. Mirrors {@code HumanoidArmorLayer#setPartVisibility}. */
	private static final Map<EquipmentSlot, Set<String>> SLOT_PARTS = Map.of(
			EquipmentSlot.FEET, Set.of("right_leg", "left_leg"),
			EquipmentSlot.LEGS, Set.of("body", "right_leg", "left_leg"),
			EquipmentSlot.CHEST, Set.of("body", "right_arm", "left_arm"),
			EquipmentSlot.HEAD, Set.of("head", "hat")
	);

	private static final Set<String> FIRST_PERSON_PARTS = Set.of("right_arm", "left_arm");

	/**
	 * Everything an armour item needs to draw, resolved once. Items are singletons, so an identity map
	 * gives us the cheapest possible per-frame lookup and no string keys to build.
	 */
	private static final Map<Item, ArmourPiece> PIECE_CACHE = new IdentityHashMap<>();

	/** Set by the mixin on Epic Fight's first person renderer. The camera check below is the real
	 *  authority, this only forces the reduced bake for renderers we know are first person. */
	private final boolean forceFirstPerson;

	public PatchedArmourLayerRenderer(boolean forceFirstPerson) {
		this.forceFirstPerson = forceFirstPerson;
	}

	/** Epic Fight rebuilds its meshes on resource reload; drop ours with them. */
	public static void clearModels(PrepareModelEvent meshBuildEvent) {
		PIECE_CACHE.values().forEach(ArmourPiece::destroy);
		PIECE_CACHE.clear();
	}

	@Override
	public void renderLayer(T t, E e, KeybladeArmorRenderer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, OpenMatrix4f[] poses, float bob, float netYawHead, float pitchHead, float partialTicks) {
		if (!(e instanceof Player player)) {
			return;
		}

		NonNullList<ItemStack> armor = player.getInventory().armor;
		if (!wearsKeybladeArmour(armor)) {
			return; // by far the most common case - leave before touching anything else
		}

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null) {
			return;
		}

		int color = playerData.getArmorColor();
		float red = ((color >> 16) & 0xff) / 255F;
		float green = ((color >> 8) & 0xff) / 255F;
		float blue = (color & 0xff) / 255F;

		boolean firstPerson = this.forceFirstPerson || isCameraEntityInFirstPerson(e);
		boolean rebake = ClientEngine.getInstance().isVanillaModelDebuggingMode();
		var armature = Armatures.BIPED.get();

		for (int i = 0; i < SLOTS.size(); i++) {
			ItemStack itemStack = armor.get(i);
			if (!(itemStack.getItem() instanceof KeybladeArmorItem item)) {
				continue;
			}

			EquipmentSlot slot = SLOTS.get(i);
			Set<String> parts = partsFor(slot, firstPerson);
			if (parts.isEmpty()) {
				continue; // nothing of this piece may be shown from where the camera is
			}

			ArmourPiece piece = PIECE_CACHE.get(item);
			if (piece == null) {
				piece = new ArmourPiece(item, slot);
				PIECE_CACHE.put(item, piece);
			}

			SkinnedMesh mesh = piece.mesh(firstPerson);
			if (mesh == null || rebake) {
				mesh = bake(player, itemStack, item, slot, parts, emRenderLayer);
				if (mesh == null) {
					continue;
				}
				piece.setMesh(firstPerson, mesh);
			}

			VertexConsumer buffer = multiBufferSource.getBuffer(piece.renderType());
			mesh.drawPosed(poseStack, buffer, Mesh.DrawingFunction.NEW_ENTITY, packedLightIn, red, green, blue, 1, OverlayTexture.NO_OVERLAY, armature, poses);
		}
	}

	private static boolean wearsKeybladeArmour(NonNullList<ItemStack> armor) {
		for (int i = 0; i < SLOTS.size(); i++) {
			if (armor.get(i).getItem() instanceof KeybladeArmorItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * True when {@code e} is the entity the camera is attached to and that camera is in first person.
	 * Anchoring on the camera rather than on which renderer instance we happen to be means the armour
	 * behaves correctly no matter which Epic Fight renderer picks up the layer, and it follows the
	 * player pressing F5 without needing a second registration.
	 */
	private static boolean isCameraEntityInFirstPerson(LivingEntity e) {
		Minecraft mc = Minecraft.getInstance();
		return mc.options.getCameraType().isFirstPerson() && mc.getCameraEntity() == e;
	}

	private static Set<String> partsFor(EquipmentSlot slot, boolean firstPerson) {
		if (!firstPerson) {
			return SLOT_PARTS.get(slot);
		}
		// Only the chest keeps anything in first person (the arms), so this never allocates.
		return slot == EquipmentSlot.CHEST ? FIRST_PERSON_PARTS : Set.of();
	}

	private SkinnedMesh bake(Player player, ItemStack itemStack, KeybladeArmorItem item, EquipmentSlot slot, Set<String> parts, KeybladeArmorRenderer<E, M> emRenderLayer) {
		ArmorBaseModel<LivingEntity> model = armorModels.get(item);
		if (model == null) {
			return null;
		}

		HumanoidModel<LivingEntity> humanoidModel = new HumanoidModel<>(filterRoot(model.root, parts));
		humanoidModel.setAllVisible(true);

		return HumanoidModelBaker.bakeArmor(player, itemStack, item, slot, emRenderLayer.getParentModel(), humanoidModel, emRenderLayer.getParentModel(), Meshes.BIPED.get());
	}

	/**
	 * Builds a root whose children are the real parts for {@code keep} and empty stand-ins for the
	 * rest, so the baker only ever sees the geometry that belongs to this slot. All seven children
	 * {@link HumanoidModel} looks up must exist, hence the placeholders.
	 */
	private static ModelPart filterRoot(ModelPart root, Set<String> keep) {
		Map<String, ModelPart> children = new HashMap<>();
		for (String name : ROOT_PARTS) {
			children.put(name, keep.contains(name) && root.hasChild(name) ? root.getChild(name) : emptyPart());
		}
		return new ModelPart(List.of(), children);
	}

	private static ModelPart emptyPart() {
		return new ModelPart(List.of(), Map.of());
	}

	public HumanoidMesh getModel(E e) {
		return ((AbstractClientPlayer) e).getSkin().model() == PlayerSkin.Model.WIDE ? Meshes.BIPED.get() : Meshes.ALEX.get();
	}

	/**
	 * One armour item's resolved render state. The two meshes differ in which body parts they contain,
	 * so first and third person can never share one; the render type is identical for both.
	 */
	private static final class ArmourPiece {

		private final RenderType renderType;
		private SkinnedMesh thirdPerson;
		private SkinnedMesh firstPerson;

		private ArmourPiece(KeybladeArmorItem item, EquipmentSlot slot) {
			// Resolving the texture walks the registry and builds strings, and the render type is not
			// cheap either - both happen exactly once per armour item.
			this.renderType = EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.armorCutoutNoCull(KeybladeArmorRenderer.getArmorTexture(item, slot == EquipmentSlot.LEGS)));
		}

		private RenderType renderType() {
			return this.renderType;
		}

		private SkinnedMesh mesh(boolean firstPerson) {
			return firstPerson ? this.firstPerson : this.thirdPerson;
		}

		private void setMesh(boolean firstPerson, SkinnedMesh mesh) {
			SkinnedMesh previous = mesh(firstPerson);
			if (previous != null && previous != mesh) {
				previous.destroy(); // debugging mode re-bakes constantly; don't leak the old one
			}
			if (firstPerson) {
				this.firstPerson = mesh;
			} else {
				this.thirdPerson = mesh;
			}
		}

		private void destroy() {
			if (this.thirdPerson != null) {
				this.thirdPerson.destroy();
			}
			if (this.firstPerson != null) {
				this.firstPerson.destroy();
			}
		}
	}
}
