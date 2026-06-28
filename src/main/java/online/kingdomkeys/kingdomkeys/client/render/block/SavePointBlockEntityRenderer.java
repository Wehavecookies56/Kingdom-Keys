package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import org.joml.Matrix4f;

public class SavePointBlockEntityRenderer implements BlockEntityRenderer<SavepointTileEntity> {

	private static final float WIDTH = 0.03F;

	public SavePointBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(SavepointTileEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null) return;

		Vec3 center = Vec3.atCenterOf(be.getBlockPos()).add(0.5, 0.15, 0.5);
		Vec3 origin = Vec3.atCenterOf(be.getBlockPos());

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

				Vec3 head = center.add(Math.cos(p.angle) * p.radius, (Math.sin(p.progress) + 1.0) * 0.5, Math.sin(p.angle) * p.radius);

				for (int i = p.trail.length - 1; i > 0; i--)
					p.trail[i] = p.trail[i - 1];

				p.trail[0] = head;
			}
		}

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();
		Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();

		for (SavePointParticle p : be.particles) {
			Vec3[] trail = p.trail.clone();

			if (trail[1] != null) {
				double renderAngle = p.angle + partialTicks * p.rotationSpeed;
				double renderProgress = p.progress + partialTicks * p.verticalSpeed;

				trail[0] = center.add(Math.cos(renderAngle) * p.radius, (Math.sin(renderProgress) + 1.0) * 0.5, Math.sin(renderAngle) * p.radius);
			}

			renderTrail(trail, origin, pose, consumer, camera);
		}
	}

	private void renderTrail(Vec3[] trail, Vec3 origin, Matrix4f pose, VertexConsumer consumer, Vec3 camera) {

		for (int i = 0; i < trail.length - 1; i++) {

			Vec3 p1 = trail[i];
			Vec3 p2 = trail[i + 1];

			if (p1 == null || p2 == null) continue;

			Vec3 dir = p2.subtract(p1).normalize();
			Vec3 view = camera.subtract(p1).normalize();

			Vec3 side = dir.cross(view);

			if (side.lengthSqr() < 1E-5) continue;

			side = side.normalize().scale(WIDTH);

			float alpha = 1F - i / (float) trail.length;

			drawQuad(consumer, pose, p1.subtract(origin), p2.subtract(origin), side, 0.3F, 1.0F, 0.45F, alpha);
		}
	}

	private void drawQuad(VertexConsumer buffer, Matrix4f pose, Vec3 p1, Vec3 p2, Vec3 offset, float r, float g, float b, float alpha) {
		Vec3 p1A = p1.add(offset);
		Vec3 p1B = p1.subtract(offset);

		Vec3 p2A = p2.add(offset);
		Vec3 p2B = p2.subtract(offset);

		buffer.addVertex(pose, (float) p1A.x, (float) p1A.y, (float) p1A.z).setColor(r, g, b, alpha).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) p2A.x, (float) p2A.y, (float) p2A.z).setColor(r, g, b, alpha).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) p2B.x, (float) p2B.y, (float) p2B.z).setColor(r, g, b, alpha).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) p1B.x, (float) p1B.y, (float) p1B.z).setColor(r, g, b, alpha).setNormal(0, 1, 0);
	}

	public static class SavePointParticle {

		public final Vec3[] trail = new Vec3[48];

		public double angle;
		public double radius;

		public double progress;

		public double rotationSpeed;
		public double verticalSpeed;
	}
}