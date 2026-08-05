package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class TrailRenderer {

	/** A rolling history of positions for one trail strand. */
	public static class Trail {
		public final Vec3[] points;
		private final Vec3[] prevPoints;

		public Trail(int length) {
			points = new Vec3[length];
			prevPoints = new Vec3[length];
		}

		/** Shifts the whole history back one slot and inserts a new head at index 0. Call once per tick. */
		public void pushHead(Vec3 head) {
			System.arraycopy(points, 0, prevPoints, 0, points.length);
			for (int i = points.length - 1; i > 0; i--) {
				points[i] = points[i - 1];
			}
			points[0] = head;
		}

		/**
		 * Blends the WHOLE trail shape between last tick's state and the current one, not just the head -
		 * needed because {@link #pushHead(Vec3)} only runs once per tick (20/s) while render can run
		 * every frame (60+/s): interpolating only the head and leaving the rest of the tail "frozen"
		 * between ticks makes the trail visibly kink/jitter right behind the head. This smooths the
		 * entire shape the same way vanilla interpolates an entity's whole position between ticks.
		 */
		public Vec3[] interpolated(float partialTicks) {
			Vec3[] result = new Vec3[points.length];
			for (int i = 0; i < points.length; i++) {
				Vec3 curr = points[i];
				if (curr == null) continue;
				Vec3 prev = prevPoints[i];
				if (prev == null) {
					result[i] = curr;
				} else {
					result[i] = new Vec3(Mth.lerp(partialTicks, prev.x, curr.x), Mth.lerp(partialTicks, prev.y, curr.y), Mth.lerp(partialTicks, prev.z, curr.z));
				}
			}
			return result;
		}
	}

	/** Simplest overload: the same RGB color across the whole tube's cross-section. */
	public static void render(Vec3[] trail, Vec3 origin, Matrix4f pose, VertexConsumer consumer, float r, float g, float b, float width) {
		float[][] colors = {{r, g, b}, {r, g, b}, {r, g, b}, {r, g, b}};
		render(trail, origin, pose, consumer, colors, width);
	}

	/**
	 * Full version: a separate RGB per side of the tube (in order: top, right, bottom, left), like the
	 * Savepoint's slightly-different-shade bands around its sparks.
	 *
	 * @param trail  rolling history of world-space points, most recent (the "head") at index 0.
	 * @param origin any world-space point to render relative to (usually the effect's own anchor, e.g.
	 *               the savepoint block's position or the entity's interpolated position) - keeps the
	 *               actual GPU vertex coordinates small/stable regardless of how far the effect is from
	 *               the world origin.
	 * @param pose    the current PoseStack's matrix, expected to already put (0,0,0) at {@code origin}.
	 * @param width   half-width of the tube's cross-section.
	 */
	public static void render(Vec3[] trail, Vec3 origin, Matrix4f pose, VertexConsumer consumer, float[][] colors, float width) {
		render(trail, origin, pose, consumer, colors, width, -1F);
	}

	/**
	 * As above, but {@code alpha} >= 0 draws the whole strand at that one opacity instead of fading it
	 * out along its length. Needed for closed shapes - on a ring the usual tail-off just reads as the
	 * circle being broken open on one side.
	 */
	public static void render(Vec3[] trail, Vec3 origin, Matrix4f pose, VertexConsumer consumer, float[][] colors, float width, float alpha) {
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

			Vec3 side = dir.cross(upRef).normalize().scale(width);
			Vec3 up = side.cross(dir).normalize().scale(width);

			p0[i] = curr.add(side).add(up).subtract(origin);
			p1[i] = curr.subtract(side).add(up).subtract(origin);
			p2[i] = curr.subtract(side).subtract(up).subtract(origin);
			p3[i] = curr.add(side).subtract(up).subtract(origin);
		}

		for (int i = 1; i < count - 2; i++) {
			if (p0[i] == null || p0[i + 1] == null) continue;

			float a1 = alpha >= 0 ? alpha : 1F - i / (float) count;
			float a2 = alpha >= 0 ? alpha : 1F - (i + 1) / (float) count;

			drawQuad(consumer, pose, p0[i], p1[i], p0[i + 1], p1[i + 1], colors[0][0], colors[0][1], colors[0][2], a1, a2);
			drawQuad(consumer, pose, p1[i], p2[i], p1[i + 1], p2[i + 1], colors[1][0], colors[1][1], colors[1][2], a1, a2);
			drawQuad(consumer, pose, p2[i], p3[i], p2[i + 1], p3[i + 1], colors[2][0], colors[2][1], colors[2][2], a1, a2);
			drawQuad(consumer, pose, p3[i], p0[i], p3[i + 1], p0[i + 1], colors[3][0], colors[3][1], colors[3][2], a1, a2);
		}
	}

	private static void drawQuad(VertexConsumer buffer, Matrix4f pose, Vec3 left1, Vec3 right1, Vec3 left2, Vec3 right2, float r, float g, float b, float alpha1, float alpha2) {
		buffer.addVertex(pose, (float) left1.x, (float) left1.y, (float) left1.z).setColor(r, g, b, alpha1).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) left2.x, (float) left2.y, (float) left2.z).setColor(r, g, b, alpha2).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) right2.x, (float) right2.y, (float) right2.z).setColor(r, g, b, alpha2).setNormal(0, 1, 0);
		buffer.addVertex(pose, (float) right1.x, (float) right1.y, (float) right1.z).setColor(r, g, b, alpha1).setNormal(0, 1, 0);
	}
}