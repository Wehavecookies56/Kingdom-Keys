package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.armor.*;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeArmorItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class KeybladeArmorRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static final Map<Item, ArmorBaseModel<LivingEntity>> armorModels = new HashMap<>();

	/** Texture paths are derived from the registry name; resolve once instead of every frame. */
	private static final Map<Item, ResourceLocation> PRIMARY_TEXTURES = new HashMap<>();
	private static final Map<Item, ResourceLocation> SECONDARY_TEXTURES = new HashMap<>();

	/**
	 * The armour sheet for an item. Leggings use the "2" sheet, everything else the "1" sheet.
	 */
	public static ResourceLocation getArmorTexture(Item item, boolean secondary) {
		Map<Item, ResourceLocation> cache = secondary ? SECONDARY_TEXTURES : PRIMARY_TEXTURES;
		return cache.computeIfAbsent(item, key -> {
			String path = Utils.getItemRegistryName(key).getPath();
			String armorName = path.substring(0, path.indexOf("_"));
			return KingdomKeys.rl("textures/models/armor/" + armorName + (secondary ? "2" : "1") + ".png");
		});
	}

	ResourceLocation texture, texture2;

	UXArmorModel<LivingEntity> uxTopSlim;
	UXArmorModel<LivingEntity> uxBotSlim;

	/** The UX models are swapped for their slim variants at most once per session. */
	private static boolean swappedToSlim = false;

	public KeybladeArmorRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet) {
		super(entityRendererIn);

		VentusModel<LivingEntity> vTop = new VentusModel<>(modelSet.bakeLayer(VentusModel.LAYER_LOCATION_TOP));
		VentusModel<LivingEntity> vBot = new VentusModel<>(modelSet.bakeLayer(VentusModel.LAYER_LOCATION_BOTTOM));

		TerraModel<LivingEntity> tTop = new TerraModel<>(modelSet.bakeLayer(TerraModel.LAYER_LOCATION_TOP));
		TerraModel<LivingEntity> tBot = new TerraModel<>(modelSet.bakeLayer(TerraModel.LAYER_LOCATION_BOTTOM));

		AquaModel<LivingEntity> aTop = new AquaModel<>(modelSet.bakeLayer(AquaModel.LAYER_LOCATION_TOP));
		AquaModel<LivingEntity> aBot = new AquaModel<>(modelSet.bakeLayer(AquaModel.LAYER_LOCATION_BOTTOM));

		EraqusModel<LivingEntity> eTop = new EraqusModel<>(modelSet.bakeLayer(EraqusModel.LAYER_LOCATION_TOP));
		EraqusModel<LivingEntity> eBot = new EraqusModel<>(modelSet.bakeLayer(EraqusModel.LAYER_LOCATION_BOTTOM));

		XehanortModel<LivingEntity> xTop = new XehanortModel<>(modelSet.bakeLayer(XehanortModel.LAYER_LOCATION_TOP));
		XehanortModel<LivingEntity> xBot = new XehanortModel<>(modelSet.bakeLayer(XehanortModel.LAYER_LOCATION_BOTTOM));

		UXArmorModel<LivingEntity> uxTop = new UXArmorModel<>(modelSet.bakeLayer(UXArmorModel.LAYER_LOCATION_TOP));
		UXArmorModel<LivingEntity> uxBot = new UXArmorModel<>(modelSet.bakeLayer(UXArmorModel.LAYER_LOCATION_BOTTOM));

		uxTopSlim = new UXArmorModel<>(modelSet.bakeLayer(UXArmorModel.SLIM_LAYER_LOCATION_TOP));
		uxBotSlim = new UXArmorModel<>(modelSet.bakeLayer(UXArmorModel.SLIM_LAYER_LOCATION_BOTTOM));

		armorModels.put(ModItems.ux_Helmet.get(), uxTop);
		armorModels.put(ModItems.ux_Chestplate.get(), uxTop);
		armorModels.put(ModItems.ux_Leggings.get(), uxBot);
		armorModels.put(ModItems.ux_Boots.get(), uxTop);

		armorModels.put(ModItems.terra_Helmet.get(), tTop);
		armorModels.put(ModItems.terra_Chestplate.get(), tTop);
		armorModels.put(ModItems.terra_Leggings.get(), tBot);
		armorModels.put(ModItems.terra_Boots.get(), tTop);

		armorModels.put(ModItems.aqua_Helmet.get(), aTop);
		armorModels.put(ModItems.aqua_Chestplate.get(), aTop);
		armorModels.put(ModItems.aqua_Leggings.get(), aBot);
		armorModels.put(ModItems.aqua_Boots.get(), aTop);

		armorModels.put(ModItems.ventus_Helmet.get(), vTop);
		armorModels.put(ModItems.ventus_Chestplate.get(), vTop);
		armorModels.put(ModItems.ventus_Leggings.get(), vBot);
		armorModels.put(ModItems.ventus_Boots.get(), vTop);

		armorModels.put(ModItems.nightmareVentus_Helmet.get(), vTop);
		armorModels.put(ModItems.nightmareVentus_Chestplate.get(), vTop);
		armorModels.put(ModItems.nightmareVentus_Leggings.get(), vBot);
		armorModels.put(ModItems.nightmareVentus_Boots.get(), vTop);

		armorModels.put(ModItems.eraqus_Helmet.get(), eTop);
		armorModels.put(ModItems.eraqus_Chestplate.get(), eTop);
		armorModels.put(ModItems.eraqus_Leggings.get(), eBot);
		armorModels.put(ModItems.eraqus_Boots.get(), eTop);

		armorModels.put(ModItems.xehanort_Helmet.get(), xTop);
		armorModels.put(ModItems.xehanort_Chestplate.get(), xTop);
		armorModels.put(ModItems.xehanort_Leggings.get(), xBot);
		armorModels.put(ModItems.xehanort_Boots.get(), xTop);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		// Left null on purpose: allocating a list for every entity rendered, armoured or not, is a cost
		// paid thousands of times a second for nothing.
		NonNullList<ItemStack> armor = null;
		int color = 0xFFFFFFFF;
		boolean glint = true;

		if (entitylivingbaseIn instanceof ArmorStand armorStand) {
			armor = NonNullList.create();
            for (ItemStack itemStack : armorStand.getArmorSlots()) {
                armor.add(itemStack);
            }

		}
		if (entitylivingbaseIn instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData != null) {
				if (!swappedToSlim) {
					LocalPlayer localPlayer = Minecraft.getInstance().player;
					if (localPlayer != null && localPlayer.getSkin().model().id().equals("slim")) {
						armorModels.replace(ModItems.ux_Helmet.get(), uxTopSlim);
						armorModels.replace(ModItems.ux_Chestplate.get(), uxTopSlim);
						armorModels.replace(ModItems.ux_Leggings.get(), uxBotSlim);
						armorModels.replace(ModItems.ux_Boots.get(), uxTopSlim);
						swappedToSlim = true;
					}
				}

				//This transforms the RGB color from the player to ARGB so the glint can show
				color = (0xFF << 24) | (playerData.getArmorColor() & 0xFFFFFF);
				glint = playerData.getArmorGlint();

				armor = player.getInventory().armor;
			}

		}
		
		if(armor != null && !armor.isEmpty()) {
			ArmorBaseModel<LivingEntity> armorModelBoots = armorModels.get(armor.get(0).getItem());
			ArmorBaseModel<LivingEntity> armorModelLeggings = armorModels.get(armor.get(1).getItem());
			ArmorBaseModel<LivingEntity> armorModelChestplate = armorModels.get(armor.get(2).getItem());
			ArmorBaseModel<LivingEntity> armorModelHelmet = armorModels.get(armor.get(3).getItem());

			ItemStack itemStack = armor.get(0);
			if (itemStack.getItem() instanceof KeybladeArmorItem) {
				texture = getArmorTexture(itemStack.getItem(), false);
				VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(texture), glint && itemStack.hasFoil());

				armorModelBoots.rightLeg.copyFrom(getParentModel().rightLeg);
				armorModelBoots.leftLeg.copyFrom(getParentModel().leftLeg);

				armorModelBoots.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
				armorModelBoots.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
			}
			itemStack = armor.get(1);
			if (itemStack.getItem() instanceof KeybladeArmorItem) {
				texture = getArmorTexture(itemStack.getItem(), true);
				VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(texture), glint && itemStack.hasFoil());

				armorModelLeggings.body.copyFrom(getParentModel().body);
				armorModelLeggings.rightLeg.copyFrom(getParentModel().rightLeg);
				armorModelLeggings.leftLeg.copyFrom(getParentModel().leftLeg);

				armorModelLeggings.body.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
				armorModelLeggings.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
				armorModelLeggings.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
			}
			itemStack = armor.get(2);
			if (itemStack.getItem() instanceof KeybladeArmorItem) {
				texture = getArmorTexture(itemStack.getItem(), false);
				VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(texture), glint && itemStack.hasFoil());

				armorModelChestplate.body.copyFrom(getParentModel().body);
				armorModelChestplate.rightArm.copyFrom(getParentModel().rightArm);
				armorModelChestplate.leftArm.copyFrom(getParentModel().leftArm);

				armorModelChestplate.leftArm.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
				armorModelChestplate.rightArm.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
				armorModelChestplate.body.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
			}
			itemStack = armor.get(3);
			if (itemStack.getItem() instanceof KeybladeArmorItem) {
				texture = getArmorTexture(itemStack.getItem(), false);
				VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(texture), glint && itemStack.hasFoil());
				armorModelHelmet.head.copyFrom(getParentModel().head);
				armorModelHelmet.head.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
			}
		}
	}

	@Override
	protected ResourceLocation getTextureLocation(T pEntity) {
		return KingdomKeys.rl("textures/models/armor/" + "terra" + "1.png");
	}
}