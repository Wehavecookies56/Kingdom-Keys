package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.model.entity.StopModel;

@OnlyIn(Dist.CLIENT)
public class StopLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID,"textures/entity/models/stop.png");

	ModelPart bb_main;
	StopModel<?> stopModel;
	
	public StopLayerRenderer(RenderLayerParent<T, M> entityRendererIn, EntityModelSet modelSet) {
		super(entityRendererIn);
	    stopModel = new StopModel<>(modelSet.bakeLayer(StopModel.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(ModCapabilities.getGlobal(entitylivingbaseIn) != null) {
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(entitylivingbaseIn);
			if(globalData.getStopModelTicks() > 0) {
				VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
		    	matrixStackIn.pushPose();
		    	matrixStackIn.translate(0, -1, 0);
		    	float scale = (10F-globalData.getStopModelTicks())/5F;
		    	System.out.println(scale);
		    	matrixStackIn.scale(scale*1.2F, scale, scale*1.2F);
		        this.stopModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
		        matrixStackIn.popPose();
			}
		}
	}
}