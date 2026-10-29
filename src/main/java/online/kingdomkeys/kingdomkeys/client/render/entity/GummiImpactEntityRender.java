package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiImpactEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GummiImpactEntityRender extends EntityRenderer<GummiImpactEntity> {

	private static final float FADE_IN = 2F, FADE_OUT = 6F;
	private static final float HEIGHT_FROM = 0.6F, HEIGHT_GROWTH = 0.1F;
	private static final float WAKE_FROM = 1.5F, WAKE_GROWTH = 0.15F;
	private static final int RING_SEGMENTS = 32, RING_TICKS = 10;
	private static final int[] RING_DELAYS = {0, 3};
	private static final int CREST = 0xBFF2FF, BODY = 0x338CF2, CORE = 0x73CCFF;

	// Cross-section of the wave from top to bottom. The edges trail behind the middle so it reads as a front
	private static final float[] HEIGHTS = {1F, 0.45F, 0.12F, 0F, -0.12F, -0.45F, -1F};
	private static final float[] SWEEPS = {0.6F, 0.12F, 0F, 0F, 0F, 0.12F, 0.6F};
	private static final float[] ALPHAS = {0F, 0.55F, 0.8F, 0.85F, 0.8F, 0.55F, 0F};
	private static final int[] COLORS = {BODY, CREST, CREST, CREST, CREST, CREST, BODY};
	private static final int MIDDLE = 3;

	public GummiImpactEntityRender(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

	@Override
	public boolean shouldRender(GummiImpactEntity entity, Frustum frustum, double camX, double camY, double camZ) {
		GummiImpactEntity partner = entity.otherPart();
		if (partner == null) {
			return super.shouldRender(entity, frustum, camX, camY, camZ);
		}
		// The wave spans both ends and the ship behind it, far wider than either entity
		AABB bounds = entity.getBoundingBox().minmax(partner.getBoundingBox()).minmax(new AABB(entity.origin(), entity.origin())).inflate(4D);
		return frustum.isVisible(bounds);
	}

	@Override
	public void render(GummiImpactEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		GummiImpactEntity partner = entity.otherPart();

		// Only the lower of the pair draws it, the same rule the damage goes by, or it would be drawn twice
		if (entity.tickCount < 1 || partner == null || partner.isRemoved() || entity.getId() > partner.getId()) {
			return;
		}

		float age = entity.tickCount + partialTicks;
		float remaining = entity.getMaxTicks() - age;
		if (remaining <= 0) {
			return;
		}
		float alpha = Math.min(Math.min(1F, age / FADE_IN), remaining / FADE_OUT);

		Vec3 from = entity.getPosition(partialTicks);
		Vec3 origin = entity.origin();
		Vec3[] arc = GummiImpactEntity.arc(from, partner.getPosition(partialTicks), origin);
		PoseStack.Pose pose = poseStack.last();

		drawWave(arc, origin, from, pose, bufferIn, age, alpha);
		drawRings(arc, origin, from, pose, bufferIn.getBuffer(RenderType.lightning()), age);
	}

	private void drawWave(Vec3[] arc, Vec3 origin, Vec3 renderOrigin, PoseStack.Pose pose, MultiBufferSource buffers, float age, float alpha) {
		int count = arc.length - 2;
		Vec3[][] sections = new Vec3[count][HEIGHTS.length];
		Vec3[] wakes = new Vec3[count];
		float height = HEIGHT_FROM + HEIGHT_GROWTH * age;
		float wake = WAKE_FROM + WAKE_GROWTH * age;

		for (int i = 0; i < count; i++) {
			Vec3 tangent = arc[i + 2].subtract(arc[i]).normalize();
			Vec3 out = arc[i + 1].subtract(origin);
			out = out.subtract(tangent.scale(out.dot(tangent)));
			out = out.lengthSqr() < 1E-6 ? new Vec3(0, 0, 1) : out.normalize();
			Vec3 up = tangent.cross(out).normalize();
			if (up.y < 0) {
				up = up.scale(-1);
			}

			// Tall in the middle, thinning out to the tips
			float taper = Math.max(0.08F, (float) Math.pow(Mth.sin(i / (float) (count - 1) * Mth.PI), 0.7F));
			float h = height * taper;
			Vec3 p = arc[i + 1].subtract(renderOrigin);

			for (int j = 0; j < HEIGHTS.length; j++) {
				sections[i][j] = p.add(up.scale(h * HEIGHTS[j])).subtract(out.scale(h * SWEEPS[j]));
			}
			wakes[i] = p.subtract(out.scale(wake * (0.4F + 0.6F * taper)));
		}

		VertexConsumer body = buffers.getBuffer(RenderType.debugQuads());
		for (int i = 0; i < count - 1; i++) {
			Vec3[] a = sections[i], b = sections[i + 1];
			for (int j = 0; j < HEIGHTS.length - 1; j++) {
				quad(body, pose, a[j], b[j], b[j + 1], a[j + 1], COLORS[j], alpha * ALPHAS[j], COLORS[j + 1], alpha * ALPHAS[j + 1]);
			}
			// The spray it leaves behind, fading out towards the ship
			quad(body, pose, a[MIDDLE], b[MIDDLE], wakes[i + 1], wakes[i], CREST, alpha * 0.45F, BODY, 0F);
		}

		VertexConsumer glow = buffers.getBuffer(RenderType.lightning());
		for (int i = 0; i < count - 1; i++) {
			Vec3[] a = sections[i], b = sections[i + 1];
			quad(glow, pose, a[MIDDLE - 1], b[MIDDLE - 1], b[MIDDLE], a[MIDDLE], CORE, 0F, CORE, alpha * 0.8F);
			quad(glow, pose, a[MIDDLE], b[MIDDLE], b[MIDDLE + 1], a[MIDDLE + 1], CORE, alpha * 0.8F, CORE, 0F);
		}
	}

	// Vapor rings bursting out of the ship as it breaks through, swept back like a cone
	private void drawRings(Vec3[] arc, Vec3 origin, Vec3 renderOrigin, PoseStack.Pose pose, VertexConsumer consumer, float age) {
		Vec3 forward = arc[arc.length / 2].subtract(origin);
		if (forward.lengthSqr() < 1E-6) {
			return;
		}
		forward = forward.normalize();
		Vec3 side = forward.cross(Math.abs(forward.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0)).normalize();
		Vec3 up = side.cross(forward);

		for (int delay : RING_DELAYS) {
			float a = age - delay;
			if (a <= 0 || a >= RING_TICKS) {
				continue;
			}
			float fade = 1F - a / RING_TICKS;
			float alpha = fade * fade * 0.7F;
			float radius = 1.5F + a * 0.8F, width = 0.3F + a * 0.1F;
			Vec3 center = origin.add(forward.scale(1F + a * 1.2F)).subtract(renderOrigin);
			Vec3 back = forward.scale(-width * 1.5F);

			Vec3 last = side;
			for (int s = 1; s <= RING_SEGMENTS; s++) {
				double angle = s * Math.PI * 2D / RING_SEGMENTS;
				Vec3 next = side.scale(Math.cos(angle)).add(up.scale(Math.sin(angle)));
				quad(consumer, pose, center.add(last.scale(radius)), center.add(next.scale(radius)), center.add(next.scale(radius + width)).add(back), center.add(last.scale(radius + width)).add(back), CREST, alpha, BODY, 0F);
				last = next;
			}
		}
	}

	// Front and back faces, as lightning culls
	private static void quad(VertexConsumer consumer, PoseStack.Pose pose, Vec3 a1, Vec3 b1, Vec3 b2, Vec3 a2, int rgb1, float alpha1, int rgb2, float alpha2) {
		int color1 = FastColor.ARGB32.color(Mth.floor(alpha1 * 255F), rgb1);
		int color2 = FastColor.ARGB32.color(Mth.floor(alpha2 * 255F), rgb2);

		consumer.addVertex(pose, a1.toVector3f()).setColor(color1);
		consumer.addVertex(pose, b1.toVector3f()).setColor(color1);
		consumer.addVertex(pose, b2.toVector3f()).setColor(color2);
		consumer.addVertex(pose, a2.toVector3f()).setColor(color2);
		consumer.addVertex(pose, a2.toVector3f()).setColor(color2);
		consumer.addVertex(pose, b2.toVector3f()).setColor(color2);
		consumer.addVertex(pose, b1.toVector3f()).setColor(color1);
		consumer.addVertex(pose, a1.toVector3f()).setColor(color1);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(GummiImpactEntity entity) {
		return KingdomKeys.rl("textures/entity/gummi_water.png");
	}
}
