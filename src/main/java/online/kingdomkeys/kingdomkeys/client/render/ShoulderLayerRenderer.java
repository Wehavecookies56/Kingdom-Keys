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
import online.kingdomkeys.kingdomkeys.client.model.armor.AquaShoulderModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.EraqusShoulderModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.TerraShoulderModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.VentusShoulderModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.XehanortShoulderModel;
import online.kingdomkeys.kingdomkeys.item.ShoulderArmorItem;

@OnlyIn(Dist.CLIENT)
public class ShoulderLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	TerraShoulderModel<?> terraShoulderArmorModel;
	AquaShoulderModel<?> aquaShoulderArmorModel;
	VentusShoulderModel<?> ventusShoulderArmorModel;
	//NightmareVentusShoulderModel nightmareVentusShoulderArmorModel;
	EraqusShoulderModel<?> eraqusShoulderArmorModel;
	XehanortShoulderModel<?> xehanortShoulderArmorModel;

	ResourceLocation texture;
	boolean steve;
	
	public ShoulderLayerRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet, boolean steve) {
		super(entityRendererIn);
		this.steve = steve;
	    terraShoulderArmorModel = new TerraShoulderModel<>(modelSet.bakeLayer(TerraShoulderModel.LAYER_LOCATION));
	    aquaShoulderArmorModel = new AquaShoulderModel<>(modelSet.bakeLayer(AquaShoulderModel.LAYER_LOCATION));
	    ventusShoulderArmorModel = new VentusShoulderModel<>(modelSet.bakeLayer(VentusShoulderModel.LAYER_LOCATION));
	   // nightmareVentusShoulderArmorModel = new NightmareVentusShoulderModel<>(modelSet.bakeLayer(NightmareVentusShoulderModel.LAYER_LOCATION));
	    eraqusShoulderArmorModel = new EraqusShoulderModel<>(modelSet.bakeLayer(EraqusShoulderModel.LAYER_LOCATION));
	    xehanortShoulderArmorModel = new XehanortShoulderModel<>(modelSet.bakeLayer(XehanortShoulderModel.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		HumanoidModel model = null;
		if(entitylivingbaseIn instanceof Player player) {
			ItemStack armor = ModCapabilities.getPlayer(player).getEquippedKBArmor(0);
			String armorName = armor != null && armor.getItem() instanceof ShoulderArmorItem shoulderArmor ? shoulderArmor.getTextureName() : "";
			if(armorName.equals("") || !ItemStack.isSame(player.getInventory().getItem(38),ItemStack.EMPTY))
				return;

			texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"_shoulder.png");
			VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);

			switch(armorName) {
			case "terra":
				model = terraShoulderArmorModel;
		        break;
			case "aqua":
				model = aquaShoulderArmorModel;
				break;
			case "ventus":
				model = ventusShoulderArmorModel;
				break;
			case "nightmareventus":
				model = ventusShoulderArmorModel;
				break;
			case "eraqus":
				model = eraqusShoulderArmorModel;
				break;
			case "xehanort":
				model = xehanortShoulderArmorModel;
				break;
			}
		
			if(model != null) {
				model.leftArm.copyFrom(getParentModel().leftArm);
				if(steve)
					matrixStackIn.translate(0.06, 0, 0);
	        	model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
			}			
		}
	}
}