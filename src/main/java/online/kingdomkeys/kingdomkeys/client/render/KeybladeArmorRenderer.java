package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
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

	ResourceLocation texture, texture2;

	UXArmorModel<LivingEntity> uxTopSlim;
	UXArmorModel<LivingEntity> uxBotSlim;

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
		NonNullList<ItemStack> armor = NonNullList.create();
		int color = 0xFFFFFFFF;
		boolean glint = true;

		if (entitylivingbaseIn instanceof ArmorStand armorStand) {
            for (ItemStack itemStack : armorStand.getArmorSlots()) {
                armor.add(itemStack);
            }
			
		}
		if (entitylivingbaseIn instanceof Player player) {
			if (PlayerData.get(player) != null) {
				if (Minecraft.getInstance().player.getSkin().model().id().equals("slim")) {
					if (!armorModels.get(ModItems.ux_Helmet.get()).equals(uxTopSlim)) {
						armorModels.replace(ModItems.ux_Helmet.get(), uxTopSlim);
						armorModels.replace(ModItems.ux_Chestplate.get(), uxTopSlim);
						armorModels.replace(ModItems.ux_Leggings.get(), uxBotSlim);
						armorModels.replace(ModItems.ux_Boots.get(), uxTopSlim);
					}
				}

				PlayerData playerData = PlayerData.get(player);
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
				Item item = itemStack.getItem();
				String armorName = Utils.getItemRegistryName(item).getPath().substring(0, Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/" + armorName + "1.png");
				VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(texture), glint && itemStack.hasFoil());

				armorModelBoots.rightLeg.copyFrom(getParentModel().rightLeg);
				armorModelBoots.leftLeg.copyFrom(getParentModel().leftLeg);

				armorModelBoots.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
				armorModelBoots.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
			}
			itemStack = armor.get(1);
			if (itemStack.getItem() instanceof KeybladeArmorItem) {
				Item item = itemStack.getItem();
				String armorName = Utils.getItemRegistryName(item).getPath().substring(0, Utils.getItemRegistryName(item).getPath().indexOf("_"));
				texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/" + armorName + "2.png");
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
				Item item = itemStack.getItem();
				String armorName = Utils.getItemRegistryName(item).getPath().substring(0, Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/" + armorName + "1.png");
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
				Item item = itemStack.getItem();
				String armorName = Utils.getItemRegistryName(item).getPath().substring(0, Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/" + armorName + "1.png");
				VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(texture), glint && itemStack.hasFoil());
				armorModelHelmet.head.copyFrom(getParentModel().head);
				armorModelHelmet.head.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, color);
			}
		}
	}

	@Override
	protected ResourceLocation getTextureLocation(T pEntity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/" + "terra" + "1.png");
	}
}