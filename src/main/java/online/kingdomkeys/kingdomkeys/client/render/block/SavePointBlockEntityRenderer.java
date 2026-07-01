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

				for (int i = p.trail.length - 1; i > 0; i--)
					p.trail[i] = p.trail[i - 1];

				p.trail[0] = head;
			}
		}

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();

		for (SavePointParticle p : be.particles) {
			Vec3[] trail = p.trail.clone();

			if (trail[1] != null) {
				double renderAngle = p.angle + partialTicks * p.rotationSpeed;
				double renderProgress = p.progress + partialTicks * p.verticalSpeed;

				trail[0] = center.add(Math.cos(renderAngle) * p.radius, (Math.sin(renderProgress) + 1.0) * 0.5, Math.sin(renderAngle) * p.radius);
			}

			renderTrail(trail, origin, pose, consumer, colors);
		}
	}

	private void renderTrail(Vec3[] trail, Vec3 origin, Matrix4f pose, VertexConsumer consumer, float[][] colors) {
		int count = trail.length;

		Vec3[] p0 = new Vec3[count];
		Vec3[] p1 = new Vec3[count];
		Vec3[] p2 = new Vec3[count];
		Vec3[] p3 = new Vec3[count];

		for (int i = 1; i < count - 1; i++) {
			Vec3 prev = trail[i - 1];
			Vec3 curr = trail[i];
			Vec3 next = trail[i + 1];

			if (prev == null || curr == null || next == null) continue;

			Vec3 prev2 = (i >= 2) ? trail[i - 2] : prev;
			Vec3 next2 = (i + 2 < count) ? trail[i + 2] : next;

			Vec3 shortDir = next.subtract(prev).normalize();
			Vec3 dir = shortDir;

			if (prev2 != null && next2 != null) {
				Vec3 longDir = next2.subtract(prev2).normalize();
				dir = shortDir.scale(0.35).add(longDir.scale(0.65));

				if (dir.lengthSqr() > 1E-5)
					dir = dir.normalize();
				else
					dir = shortDir;
			}

			Vec3 upRef = Math.abs(dir.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);

			Vec3 side = dir.cross(upRef).normalize().scale(WIDTH);
			Vec3 up = side.cross(dir).normalize().scale(WIDTH);

			p0[i] = curr.add(side).add(up).subtract(origin);
			p1[i] = curr.subtract(side).add(up).subtract(origin);
			p2[i] = curr.subtract(side).subtract(up).subtract(origin);
			p3[i] = curr.add(side).subtract(up).subtract(origin);
		}

		for (int i = 1; i < count - 2; i++) {
			if (p0[i] == null || p0[i + 1] == null) continue;

			float a1 = 1F - i / (float) count;
			float a2 = 1F - (i + 1) / (float) count;

			drawQuad(consumer, pose, p0[i], p1[i], p0[i + 1], p1[i + 1], colors[0][0], colors[0][1], colors[0][2], a1, a2);
			drawQuad(consumer, pose, p1[i], p2[i], p1[i + 1], p2[i + 1], colors[1][0], colors[1][1], colors[1][2], a1, a2);
			drawQuad(consumer, pose, p2[i], p3[i], p2[i + 1], p3[i + 1], colors[2][0], colors[2][1], colors[2][2], a1, a2);
			drawQuad(consumer, pose, p3[i], p0[i], p3[i + 1], p0[i + 1], colors[3][0], colors[3][1], colors[3][2], a1, a2);
		}
	}

	private void drawQuad(VertexConsumer buffer, Matrix4f pose, Vec3 left1, Vec3 right1, Vec3 left2, Vec3 right2, float r, float g, float b, float alpha1, float alpha2) {
		buffer.addVertex(pose, (float) left1.x, (float) left1.y, (float) left1.z).setColor(r, g, b, alpha1).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) left2.x, (float) left2.y, (float) left2.z).setColor(r, g, b, alpha2).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) right2.x, (float) right2.y, (float) right2.z).setColor(r, g, b, alpha2).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) right1.x, (float) right1.y, (float) right1.z).setColor(r, g, b, alpha1).setNormal(0, 1, 0);
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