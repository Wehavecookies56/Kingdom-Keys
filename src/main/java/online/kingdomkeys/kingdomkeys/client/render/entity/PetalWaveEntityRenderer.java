package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.entity.organization.PetalWaveEntity;
import org.joml.Matrix4f;

// A closed pink ring, rebuilt from the entity's current radius every frame rather than from a trail
// history - history would leave the older, tighter radii behind and draw a spiral instead of a
// circle.
public class PetalWaveEntityRenderer extends EntityRenderer<PetalWaveEntity> {

	// Enough that a ring reads as round rather than as a polygon at its widest.
	private static final int SEGMENTS = 72;
	private static final float RING_WIDTH = 0.11F;
	private static final float HEIGHT = 0.15F;
	// Ticks spent fading out at the end, so it doesn't just vanish.
	private static final float FADE_TICKS = 5F;
	// Top, right, bottom, left of the tube - the dark bands are what give it the petal look.
	private static final float[][] RING_COLORS = {{1.0F, 0.35F, 0.75F}, {0.2F, 0.05F, 0.12F}, {1.0F, 0.45F, 0.8F}, {0.2F, 0.05F, 0.12F}};

	public PetalWaveEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(PetalWaveEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		int duration = entity.getDuration();
		float age = Mth.clamp(entity.tickCount + partialTicks, 0F, duration);
		if (age >= duration) {
			return;
		}

		float remaining = duration - age;
		float alpha = remaining >= FADE_TICKS ? 1F : remaining / FADE_TICKS;
		float progress = age / duration;

		double radius = Mth.lerp(progress, entity.getRadiusStart(), entity.getRadiusEnd());

		Vec3 centre = entity.position();
		// Matches what the incoming poseStack already assumes.
		Vec3 renderOrigin = entity.getPosition(partialTicks);

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();

		drawRing(consumer, pose, centre, renderOrigin, radius, HEIGHT, alpha);

		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	private void drawRing(VertexConsumer consumer, Matrix4f pose, Vec3 centre, Vec3 renderOrigin, double radius, double y, float alpha) {
		// A few points past a full turn: the trail renderer skips the first and last couple of entries
		// when it builds the tube, so the overlap is what closes the loop seamlessly.
		Vec3[] ring = new Vec3[SEGMENTS + 4];
		for (int i = 0; i < ring.length; i++) {
			double angle = (Math.PI * 2 / SEGMENTS) * (i - 1);
			ring[i] = centre.add(Math.cos(angle) * radius, y, Math.sin(angle) * radius);
		}
		TrailRenderer.render(ring, renderOrigin, pose, consumer, RING_COLORS, RING_WIDTH, alpha);
	}

	@Override
	public ResourceLocation getTextureLocation(PetalWaveEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
