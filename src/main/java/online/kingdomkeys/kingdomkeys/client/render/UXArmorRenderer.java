package online.kingdomkeys.kingdomkeys.client.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.client.model.armor.AquaModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.EraqusModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.TerraModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.UXArmorModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.VentusModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.XehanortModel;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.util.Utils;

@OnlyIn(Dist.CLIENT)
public class UXArmorRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static final Map<Item, HumanoidModel<LivingEntity>> armorModels = new HashMap<>();

	ResourceLocation texture,texture2;
	boolean steve;
	
	public UXArmorRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet, boolean steve) {
		super(entityRendererIn);
		this.steve = steve;
		
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
		
		armorModels.put(ModItems.ux_Helmet.get(), uxTop);
		armorModels.put(ModItems.ux_Chestplate.get(), uxTop);
		armorModels.put(ModItems.ux_Leggings.get(), uxBot);
		armorModels.put(ModItems.ux_Boots.get(), uxTop);
		
		//armorModel = new UXArmorModel<>(modelSet.bakeLayer(UXArmorModel.LAYER_LOCATION_TOP));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entitylivingbaseIn instanceof Player player) {
			
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			int color = playerData.getArmorColor();
			float red = ((color >> 16) & 0xff) / 255F;
			float green = ((color >> 8) & 0xff) / 255F;
			float blue = (color & 0xff) / 255F;
			
			NonNullList<ItemStack> armor = player.getInventory().armor;

			HumanoidModel<LivingEntity> armorModelBoots = armorModels.get(armor.get(0).getItem());
			HumanoidModel<LivingEntity> armorModelLeggings = armorModels.get(armor.get(1).getItem());
			HumanoidModel<LivingEntity> armorModelChestplate = armorModels.get(armor.get(2).getItem());
			HumanoidModel<LivingEntity> armorModelHelmet = armorModels.get(armor.get(3).getItem());

			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/ux1.png");
			texture2 = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/ux2.png");

			VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);

			if(steve)
				matrixStackIn.translate(0.06, 0, 0);

	    	//if(armor.get(0).getItem() == ModItems.ux_Boots.get()) {
	    		Item item = armor.get(0).getItem();
				String armorName = Utils.getItemRegistryName(item).getPath().substring(0,Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"1.png");
	    		vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);
	    		
	    		armorModelBoots.rightLeg.copyFrom(getParentModel().rightLeg);
				armorModelBoots.leftLeg.copyFrom(getParentModel().leftLeg);

	    		armorModelBoots.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    		armorModelBoots.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    	//}
	    	
	    	//if(armor.get(2).getItem() == ModItems.ux_Chestplate.get()) {
	    		item = armor.get(2).getItem();
				armorName = Utils.getItemRegistryName(item).getPath().substring(0,Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"1.png");
	    		vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);

	    		armorModelChestplate.body.copyFrom(getParentModel().body);
				armorModelChestplate.rightArm.copyFrom(getParentModel().rightArm);
				armorModelChestplate.leftArm.copyFrom(getParentModel().leftArm);

	    		armorModelChestplate.leftArm.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    		armorModelChestplate.rightArm.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    		armorModelChestplate.body.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    	//}
	    	//if(armor.get(3).getItem() == ModItems.ux_Helmet.get()) {
	    		item = armor.get(3).getItem();
				armorName = Utils.getItemRegistryName(item).getPath().substring(0,Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"1.png");
	    		vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);

	    		armorModelHelmet.head.copyFrom(getParentModel().head);
	    		armorModelHelmet.head.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    	//}
	    	
	    	//Change texture
	    	//vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture2), false, false);
			//if(armor.get(1).getItem() == ModItems.ux_Leggings.get()) {
	    		item = armor.get(1).getItem();
				armorName = Utils.getItemRegistryName(item).getPath().substring(0,Utils.getItemRegistryName(item).getPath().indexOf("_"));

				texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"2.png");
	    		vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);

				armorModelLeggings.body.copyFrom(getParentModel().body);
				armorModelLeggings.rightLeg.copyFrom(getParentModel().rightLeg);
				armorModelLeggings.leftLeg.copyFrom(getParentModel().leftLeg);

				armorModelLeggings.body.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				armorModelLeggings.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				armorModelLeggings.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				
	    	//}
		}
	}
}