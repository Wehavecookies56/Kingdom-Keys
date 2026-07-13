package online.kingdomkeys.kingdomkeys.client.render.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MagnetModel;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MagnetEntityRenderer extends EntityRenderer<ThrowableProjectile> {
	public static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/models/magnet.png");
	MagnetModel magnetModel;

	public MagnetEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		magnetModel = new MagnetModel<>(context.bakeLayer(MagnetModel.LAYER_LOCATION));
	}

	@Override
	public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{

			float rotation = (entity.tickCount + partialTicks) * 20;
			float scale = 0;
			int maxTicks = 0;

			switch (entity) {
				case MagnetEntity magnet -> {
					scale = 2;
					maxTicks = magnet.getMaxTicks();
				}
				case MagneraEntity magnera -> {
					scale = 3;
					maxTicks = magnera.getMaxTicks();
				}
				case MagnegaEntity magnega -> {
					scale = 4;
					maxTicks = magnega.getMaxTicks();
				}
				default -> {
				}
			}

			if (entity.tickCount < scale * 10) {
				scale = entity.tickCount / 10F;
			}
			if (entity.tickCount > maxTicks - scale * 10) {
				scale = (maxTicks - entity.tickCount) / 10F;
			}

			VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
			matrixStackIn.translate(0, 1, 0);

			matrixStackIn.scale(scale, scale, scale);
			if(!Minecraft.getInstance().isPaused()) {
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
			}

			this.magnetModel.renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

			renderMagnetElectricity(entity, matrixStackIn, bufferIn, partialTicks, 0.8F);
		}
		matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	private void renderMagnetElectricity(ThrowableProjectile entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, float radius) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.lines());
		poseStack.pushPose();
		PoseStack.Pose pose = poseStack.last();

		float rotation = (entity.tickCount + partialTicks) * 8F;

		int points = 24;
		for (int i = 0; i < points; i++) {
			float angle1 = (float) (Math.PI * 2 * i / points);
			float angle2 = (float) (Math.PI * 2 * (i + 1) / points);

			angle1 += Math.toRadians(rotation);
			angle2 += Math.toRadians(rotation);

			float x1 = (float) Math.cos(angle1) * radius;
			float z1 = (float) Math.sin(angle1) * radius;

			float x2 = (float) Math.cos(angle2) * radius;
			float z2 = (float) Math.sin(angle2) * radius;

			lightningLine(consumer, pose, x1, 0, z1, x2, 0, z2);
		}

		poseStack.popPose();
	}

	private void lightningLine(VertexConsumer consumer, PoseStack.Pose pose, float x1, float y1, float z1, float x2, float y2, float z2) {
		float px = x1;
		float py = y1;
		float pz = z1;

		int segments = 5;

		for (int i = 1; i <= segments; i++) {

			float t = i / (float) segments;

			float nx = x1 + (x2 - x1) * t;
			float ny = y1 + (y2 - y1) * t;
			float nz = z1 + (z2 - z1) * t;

			if (i < segments) {
				nx += (float) ((Math.random() - 0.5) * 0.08);
				ny += (float) ((Math.random() - 0.5) * 0.2);
				nz += (float) ((Math.random() - 0.5) * 0.08);
			}

			consumer.addVertex(pose, px, py, pz).setColor(230, 140, 255, 255).setNormal(pose, 0, 1, 0);
			consumer.addVertex(pose, nx, ny, nz).setColor(120, 220, 255, 255).setNormal(pose, 0, 1, 0);

			px = nx;
			py = ny;
			pz = nz;
		}
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		return TEXTURE;
	}

}
