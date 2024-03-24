package online.kingdomkeys.kingdomkeys.client.render;

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
import online.kingdomkeys.kingdomkeys.item.PauldronItem;

@OnlyIn(Dist.CLIENT)
public class ShoulderLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

	public static Map<String, HumanoidModel<LivingEntity>> models = new HashMap<>();

	ResourceLocation texture;
	boolean steve;
	
	public ShoulderLayerRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet, boolean steve) {
		super(entityRendererIn);
		this.steve = steve;
		models.put("terra", new TerraShoulderModel<>(modelSet.bakeLayer(TerraShoulderModel.LAYER_LOCATION)));
	    models.put("aqua", new AquaShoulderModel<>(modelSet.bakeLayer(AquaShoulderModel.LAYER_LOCATION)));
	    models.put("ventus", new VentusShoulderModel<>(modelSet.bakeLayer(VentusShoulderModel.LAYER_LOCATION)));
		models.put("nightmareventus", new VentusShoulderModel<>(modelSet.bakeLayer(VentusShoulderModel.LAYER_LOCATION)));
	    models.put("eraqus", new EraqusShoulderModel<>(modelSet.bakeLayer(EraqusShoulderModel.LAYER_LOCATION)));
	    models.put("xehanort", new XehanortShoulderModel<>(modelSet.bakeLayer(XehanortShoulderModel.LAYER_LOCATION)));	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		HumanoidModel<LivingEntity> model = null;
		if(entitylivingbaseIn instanceof Player player) {
			if (ModCapabilities.getPlayer(player) != null) {
				ItemStack armor = ModCapabilities.getPlayer(player).getEquippedKBArmor(0);
				String armorName = armor != null && armor.getItem() instanceof PauldronItem shoulderArmor ? shoulderArmor.getTextureName() : "";
				if (armorName.equals("") || !ItemStack.isSameItem(player.getInventory().getItem(38), ItemStack.EMPTY))
					return;

				texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/" + armorName + "_shoulder.png");
				VertexConsumer vertexconsumer = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(texture), false, false);
				model = models.get(armorName);

				if (model != null) {
					model.leftArm.copyFrom(getParentModel().leftArm);
					matrixStackIn.pushPose();
					if (steve)
						matrixStackIn.translate(0.06, 0, 0);
					model.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
					matrixStackIn.popPose();
				}
			}
		}
	}
}