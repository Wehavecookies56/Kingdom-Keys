package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public final class BossDeathRays {

	private BossDeathRays() {}

	private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

	/** Fixed so the spikes sit still from frame to frame instead of crawling */
	private static final long SEED = 432L;

	private static final int SPIKES = 60;

	private static final float FADE = 0.2F;

	public static void render(PoseStack pose, MultiBufferSource buffer, float completion) {
		if (completion <= 0) {
			return;
		}

		pose.pushPose();
		// The body's middle
		pose.translate(0, 1, 0);

		rays(pose, buffer.getBuffer(RenderType.dragonRays()), completion);
		rays(pose, buffer.getBuffer(RenderType.dragonRaysDepth()), completion);

		pose.popPose();
	}

	private static void rays(PoseStack poseStack, VertexConsumer buffer, float completion) {
		poseStack.pushPose();

		float fade = Math.min(completion > 1 - FADE ? (completion - (1 - FADE)) / FADE : 0F, 1F);
		int centre = FastColor.ARGB32.colorFromFloat(1F - fade, 1F, 1F, 1F);
		int edge = 0xFFFFFF;

		RandomSource random = RandomSource.create(SEED);
		Vector3f origin = new Vector3f();
		Vector3f a = new Vector3f(), b = new Vector3f(), c = new Vector3f();
		Quaternionf spin = new Quaternionf();

		int spikes = Mth.floor((completion + completion * completion) / 2F * SPIKES);

		for (int i = 0; i < spikes; i++) {
			spin.rotationXYZ(
					random.nextFloat() * (float) (Math.PI * 2),
					random.nextFloat() * (float) (Math.PI * 2),
					random.nextFloat() * (float) (Math.PI * 2)
			).rotateXYZ(
					random.nextFloat() * (float) (Math.PI * 2),
					random.nextFloat() * (float) (Math.PI * 2),
					random.nextFloat() * (float) (Math.PI * 2) + completion * (float) (Math.PI / 2)
			);

			poseStack.mulPose(spin);

			float length = random.nextFloat() * 20F + 5F + fade * 10F;
			float width = random.nextFloat() * 2F + 1F + fade * 2F;

			a.set(-HALF_SQRT_3 * width, length, -0.5F * width);
			b.set(HALF_SQRT_3 * width, length, -0.5F * width);
			c.set(0F, length, width);

			PoseStack.Pose pose = poseStack.last();

			buffer.addVertex(pose, origin).setColor(centre);
			buffer.addVertex(pose, a).setColor(edge);
			buffer.addVertex(pose, b).setColor(edge);

			buffer.addVertex(pose, origin).setColor(centre);
			buffer.addVertex(pose, b).setColor(edge);
			buffer.addVertex(pose, c).setColor(edge);

			buffer.addVertex(pose, origin).setColor(centre);
			buffer.addVertex(pose, c).setColor(edge);
			buffer.addVertex(pose, a).setColor(edge);
		}

		poseStack.popPose();
	}
}
