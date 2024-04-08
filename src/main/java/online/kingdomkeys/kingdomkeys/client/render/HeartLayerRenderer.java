package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;

@OnlyIn(Dist.CLIENT)
public class HeartLayerRenderer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/stop.png");

	public HeartLayerRenderer(RenderLayerParent<T, PlayerModel<T>> p_174540_, EntityModelSet p_174541_) {
		super(p_174540_);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entitylivingbaseIn instanceof AbstractClientPlayer) {
			LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) entitylivingbaseIn);
			if (!((IDisabledAnimations) renderer).isDisabled()) {
				renderEntity(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
			}
		} else {
			renderEntity(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
		}
	}

	public void renderEntity(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (ModCapabilities.getGlobal(entitylivingbaseIn) != null) {
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(entitylivingbaseIn);
			if (globalData.isKO()) {
				VertexConsumer buffer =
						bufferIn.getBuffer(Sheets.translucentCullBlockSheet());

				VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
				BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "entity/heart"));
				matrixStackIn.scale(0.005F, 0.005F, 0.005F);
				matrixStackIn.translate(0, 40, -150);
				matrixStackIn.mulPose(Axis.XN.rotationDegrees(90));
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(entitylivingbaseIn.tickCount*5));


				for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, RenderType.cutout())) {
					buffer.putBulkData(matrixStackIn.last(), quad, 1, 1, 1, 1, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
				}

			}
		}
	}
}