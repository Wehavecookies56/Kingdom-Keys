package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;

@OnlyIn(Dist.CLIENT)
public class FreezeLayerRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private static final ResourceLocation ICE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/ice.png");

	public FreezeLayerRenderer(RenderLayerParent<T, M> renderer, EntityModelSet entityModels) {
		super(renderer);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entitylivingbaseIn instanceof AbstractClientPlayer) {
			LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) entitylivingbaseIn);
			if (!((IDisabledAnimations) renderer).kingdom_Keys$isDisabled()) {
				renderEntity(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, ageInTicks);
			}
		} else {
			renderEntity(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, ageInTicks);
		}
	}

	public void renderEntity(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float ageInTicks) {
		if (GlobalData.get(entitylivingbaseIn) != null) {
			if (entitylivingbaseIn.hasEffect(ModMobEffects.FREEZE)) {
				if (entitylivingbaseIn.getEffect(ModMobEffects.FREEZE).getAmplifier() == 50) {
					PoseStack freshPose = new PoseStack();
					freshPose.pushPose();
					{
						float width = entitylivingbaseIn.getBbWidth() * 0.5F;
						float height = entitylivingbaseIn.getBbHeight() * 1F;

						Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;

						freshPose.translate(entitylivingbaseIn.getX() - camera.getPosition().x, entitylivingbaseIn.getY() - camera.getPosition().y, entitylivingbaseIn.getZ() - camera.getPosition().z);
						freshPose.mulPose(Axis.YN.rotationDegrees(entitylivingbaseIn.yBodyRot));

						freshPose.translate(-width, 0, -width);
						freshPose.scale(width * 2, height, width * 2);

						Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.ICE.defaultBlockState(), freshPose, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);

						VertexConsumer iceConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(ICE_TEXTURE));
						getParentModel().renderToBuffer(matrixStackIn, iceConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
					}
					freshPose.popPose();
				}
			}
		}
	}
}