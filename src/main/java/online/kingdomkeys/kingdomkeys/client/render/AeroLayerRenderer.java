package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.math.Axis;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;

@OnlyIn(Dist.CLIENT)
public class AeroLayerRenderer<T extends LivingEntity> extends RenderLayer<T, PlayerModel<T>> {
	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident_riptide.png");
	public static final String BOX = "box";
	private final ModelPart box;

	public AeroLayerRenderer(RenderLayerParent<T, PlayerModel<T>> p_174540_, EntityModelSet p_174541_) {
		super(p_174540_);
		ModelPart modelpart = p_174541_.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
		this.box = modelpart.getChild("box");
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
			if (globalData.getAeroTicks() > 0) {
				VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

				for (int i = 1; i <= globalData.getAeroLevel() + 1; ++i) {
					matrixStackIn.pushPose();
					float f = ageInTicks * 20;
					if (i % 2 == 0)
						f *= -1;
					matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
					float scale = 1;
					switch (globalData.getAeroLevel()) {
					case 0:
						if (entitylivingbaseIn instanceof Player) {
							scale = 0.75F * i;
							matrixStackIn.scale(scale, scale * 1.2F, scale);
							matrixStackIn.translate(0.0D, (double) (-0.4F + 0.8F * (float) i), 0.0D);
						} else {
							scale = 0.35F * i;
							matrixStackIn.scale(scale, scale, scale);

						}
						break;
					case 1:
						if (entitylivingbaseIn instanceof Player) {
							scale = 0.85F * i;
							matrixStackIn.scale(scale, scale * 1F, scale);
							matrixStackIn.translate(0.0D, (double) (-0.8F + 0.8F * (float) i), 0.0D);
						} else {
							scale = 0.45F * i;
							matrixStackIn.scale(scale, scale, scale);
						}

						break;
					case 2:
						if (entitylivingbaseIn instanceof Player) {
							scale = 0.7F * i;
							matrixStackIn.scale(scale, scale * 0.6F, scale);
							matrixStackIn.translate(0.0D, (double) (-1.2F + 0.6F * (float) i), 0.0D);
						} else {
							scale = 0.55F * i;
							matrixStackIn.scale(scale, scale * 0.6F, scale);
						}
						break;

					}
					this.box.render(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY);
					matrixStackIn.popPose();
				}
			}
		}
	}
}