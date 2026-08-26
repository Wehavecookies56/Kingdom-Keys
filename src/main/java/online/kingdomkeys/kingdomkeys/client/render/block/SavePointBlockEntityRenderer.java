package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.joml.Matrix4f;

public class SavePointBlockEntityRenderer implements BlockEntityRenderer<SavepointTileEntity> {
	private static final float[][] SAVEPOINT_COLORS = {{0.75F, 1.00F, 0.45F}, {0.70F, 1.00F, 0.45F}, {0.70F, 1.00F, 0.45F}, {0.60F, 1.00F, 0.45F}};

	private static final float[][] WARP_COLORS = {{0.75F, 1.00F, 1.00F}, {0.65F, 0.95F, 1.00F}, {0.55F, 0.90F, 1.00F}, {0.45F, 0.85F, 1.00F}};
	private static final float WIDTH = 0.05F;

	public SavePointBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(SavepointTileEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null) return;

		Vec3 center = Vec3.atCenterOf(be.getBlockPos()).add(0.5, 0.15, 0.5);
		Vec3 origin = Vec3.atCenterOf(be.getBlockPos());

		float[][] colors = be.getBlockState().getValue(SavePointBlock.TIER) == SavePointStorage.SavePointType.WARP ? WARP_COLORS : SAVEPOINT_COLORS;

		//Init
		if (be.particles[0] == null) {
			for (int i = 0; i < be.particles.length; i++) {
				SavePointParticle p = new SavePointParticle();

				p.angle = i * Math.PI;
				p.progress = i * Math.PI;

				p.radius = 0.8;
				p.rotationSpeed = 0.1;
				p.verticalSpeed = 0.05;

				be.particles[i] = p;
			}
		}

		long gameTime = mc.level.getGameTime();

		//Update
		if (gameTime != be.lastUpdateTick) {
			be.lastUpdateTick = gameTime;
			for (SavePointParticle p : be.particles) {
				p.angle += p.rotationSpeed;
				p.progress += p.verticalSpeed;

				Vec3 head = center.add(Math.cos(p.angle) * p.radius, (Math.sin(p.progress) + 1.0) * 0.35, Math.sin(p.angle) * p.radius);

				p.trail.pushHead(head);
			}
		}

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();

		for (SavePointParticle p : be.particles) {
			Vec3[] trail = p.trail.points.clone();

			if (trail[1] != null) {
				double renderAngle = p.angle + partialTicks * p.rotationSpeed;
				double renderProgress = p.progress + partialTicks * p.verticalSpeed;

				trail[0] = center.add(Math.cos(renderAngle) * p.radius, (Math.sin(renderProgress) + 1.0) * 0.5, Math.sin(renderAngle) * p.radius);
			}

			TrailRenderer.render(trail, origin, pose, consumer, colors, WIDTH);
		}
	}

	public static class SavePointParticle {

		public final TrailRenderer.Trail trail = new TrailRenderer.Trail(48);

		public double angle;
		public double radius;

		public double progress;

		public double rotationSpeed;
		public double verticalSpeed;
	}
}
