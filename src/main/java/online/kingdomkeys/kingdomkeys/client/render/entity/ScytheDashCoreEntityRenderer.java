package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.entity.organization.ScytheDashCoreEntity;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class ScytheDashCoreEntityRenderer extends EntityRenderer<ScytheDashCoreEntity> {

	private static final float ORBIT_RADIUS_MULTIPLIER = 2.5F; // Trail radius
	private static final float ORBIT_SPEED = 50F; // negative = spins the other way
	private static final int TRAIL_LENGTH = 60;
	private static final float TRAIL_WIDTH = 0.05F;
	private static final float[][] TRAIL_COLORS = {{1.0F, 0.35F, 0.75F}, {1.0F, 0.45F, 0.8F}, {1.0F, 0.35F, 0.75F}, {1.0F, 0.55F, 0.85F}};

	private final ItemRenderer itemRenderer;
	private final Map<Integer, TrailRenderer.Trail> trails = new HashMap<>();

	public ScytheDashCoreEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(ScytheDashCoreEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		Player caster = entity.getCaster();
		if (caster == null)
			return;

		Vec3 casterPos = caster.getPosition(partialTicks);
		Vec3 entityPos = entity.getPosition(partialTicks); // matches what the incoming poseStack already assumes as its own local origin
		float halfHeight = caster.getBbHeight() / 2F;
		Vec3 center = entityPos.add(0, halfHeight, 0);

		// The ring spins in the vertical plane that contains the caster's own forward direction, so it wraps around them consistently no matter which way they're facing.
		Vec3 forward = caster.getLookAngle();
		Vec3 flatForward = new Vec3(forward.x, 0, forward.z);
		Vec3 side = flatForward.lengthSqr() > 1E-6 ? flatForward.normalize() : new Vec3(1, 0, 0);

		renderSpinningScythe(entity, caster, casterPos, partialTicks, poseStack, bufferSource, packedLight);
		renderOrbitRing(entity, center, entityPos, side, halfHeight * ORBIT_RADIUS_MULTIPLIER, partialTicks, poseStack, bufferSource);

		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	private void renderSpinningScythe(ScytheDashCoreEntity entity, Player caster, Vec3 casterPos, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		ItemStack stack = entity.getVisualItem();
		if (stack.isEmpty())
			return;

		float spin = (entity.tickCount + partialTicks) * ORBIT_SPEED;

		poseStack.pushPose();
		poseStack.translate(0, caster.getBbHeight() * 0.5, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(-caster.getYRot() + 90F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(spin));
		itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());
		poseStack.popPose();
	}

	private void renderOrbitRing(ScytheDashCoreEntity entity, Vec3 center, Vec3 origin, Vec3 side, float radius, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
		TrailRenderer.Trail trail = trails.computeIfAbsent(entity.getId(), id -> new TrailRenderer.Trail(TRAIL_LENGTH));

		double angleRad = Math.toRadians((entity.tickCount + partialTicks) * -ORBIT_SPEED);
		Vec3 head = center.add(side.scale(Math.cos(angleRad) * radius)).add(0, Math.sin(angleRad) * radius, 0);
		trail.pushHead(head);

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();
		TrailRenderer.render(trail.points, origin, pose, consumer, TRAIL_COLORS, TRAIL_WIDTH);
	}

	@Override
	public ResourceLocation getTextureLocation(ScytheDashCoreEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
