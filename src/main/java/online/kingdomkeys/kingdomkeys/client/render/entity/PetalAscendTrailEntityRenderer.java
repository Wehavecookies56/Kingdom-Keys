package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.entity.organization.PetalAscendTrailEntity;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class PetalAscendTrailEntityRenderer extends EntityRenderer<PetalAscendTrailEntity> {

	private static final int DURATION_TICKS = 25;
	private static final float RISE_HEIGHT = 3.2F; // roughly how high the launch tends to carry someone
	private static final int TRAIL_LENGTH = 64;
	private static final float TRAIL_WIDTH = 0.06F;
	private static final float[][] TRAIL_COLORS = {{1.0F, 0.35F, 0.75F}, {0.15F, 0.05F, 0.1F}, {1.0F, 0.45F, 0.8F}, {0.15F, 0.05F, 0.1F}};

	private final Map<Integer, TrailRenderer.Trail> trails = new HashMap<>();

	public PetalAscendTrailEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(PetalAscendTrailEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		LivingEntity target = entity.getTarget();
		if (target == null)
			return;

		Vec3 basePos = target.getPosition(partialTicks);
		Vec3 entityPos = entity.getPosition(partialTicks); // matches what the incoming poseStack already assumes

		float tickProgress = Mth.clamp(entity.tickCount + partialTicks, 0F, DURATION_TICKS);
		float progress = tickProgress / DURATION_TICKS;
		float riseAmount = progress * RISE_HEIGHT;

		TrailRenderer.Trail trail = trails.computeIfAbsent(entity.getId(), id -> new TrailRenderer.Trail(TRAIL_LENGTH));
		Vec3 head = basePos.add(0, riseAmount, 0);
		trail.pushHead(head);

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();
		TrailRenderer.render(trail.points, entityPos, pose, consumer, TRAIL_COLORS, TRAIL_WIDTH);

		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(PetalAscendTrailEntity entity) {
		return net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;
	}
}
