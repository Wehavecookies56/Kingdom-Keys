package online.kingdomkeys.kingdomkeys.client.render;

import java.awt.Color;

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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.model.armor.UXArmorModel;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@OnlyIn(Dist.CLIENT)
public class UXArmorRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	UXArmorModel<LivingEntity> armorModel;

	ResourceLocation texture,texture2;
	boolean steve;
	
	public UXArmorRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet, boolean steve) {
		super(entityRendererIn);
		this.steve = steve;
		armorModel = new UXArmorModel<>(modelSet.bakeLayer(UXArmorModel.LAYER_LOCATION_TOP));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entitylivingbaseIn instanceof Player player) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			int color = playerData.getArmorColor();
			float red = ((color >> 16) & 0xff) / 255F;
			float green = ((color >> 8) & 0xff) / 255F;
			float blue = (color & 0xff) / 255F;
			
			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/ux1.png");
			texture2 = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/ux2.png");

			VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);

			NonNullList<ItemStack> armor = player.getInventory().armor;
			armorModel.head.copyFrom(getParentModel().head);
			armorModel.body.copyFrom(getParentModel().body);
			armorModel.rightArm.copyFrom(getParentModel().rightArm);
			armorModel.leftArm.copyFrom(getParentModel().leftArm);
			armorModel.rightLeg.copyFrom(getParentModel().rightLeg);
			armorModel.leftLeg.copyFrom(getParentModel().leftLeg);

			if(steve)
				matrixStackIn.translate(0.06, 0, 0);

	    	if(armor.get(0).getItem() == ModItems.ux_Boots.get()) {
				armorModel.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				armorModel.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    	}
	    	
	    	if(armor.get(2).getItem() == ModItems.ux_Chestplate.get()) {
	    		armorModel.leftArm.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    		armorModel.rightArm.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    		armorModel.body.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    	}
	    	if(armor.get(3).getItem() == ModItems.ux_Helmet.get()) {
	    		armorModel.head.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
	    	}

	    	vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture2), false, false);
			if(armor.get(1).getItem() == ModItems.ux_Leggings.get()) {
				armorModel.body.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				armorModel.leftLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				armorModel.rightLeg.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY,red,green,blue,1);
				
	    	}
		}
	}
}