package online.kingdomkeys.kingdomkeys.client.render;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.model.armor.TerraShoulderModel;
import online.kingdomkeys.kingdomkeys.item.ShoulderArmorItem;

@OnlyIn(Dist.CLIENT)
public class ShoulderLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	TerraShoulderModel<?> shoulderArmorModel;
	ResourceLocation texture;
	
	public ShoulderLayerRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet) {
		super(entityRendererIn);
	    shoulderArmorModel = new TerraShoulderModel<>(modelSet.bakeLayer(TerraShoulderModel.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		this.shoulderArmorModel.leftArm.copyFrom(getParentModel().leftArm);
		ItemStack armor = ModCapabilities.getPlayer((Player) entitylivingbaseIn).getEquippedKBArmor(0);
		String armorName = armor != null && armor.getItem() instanceof ShoulderArmorItem shoulderArmor ? shoulderArmor.getTextureName() : "";
		if(armorName.equals(""))
			return;
		texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"_shoulder.png");
		VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);
		switch(armorName) {
		case "terra":
	        this.shoulderArmorModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
	        break;
		case "aqua":
			
			break;
		}
	}
}