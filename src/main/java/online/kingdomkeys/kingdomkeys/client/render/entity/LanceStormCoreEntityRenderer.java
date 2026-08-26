package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceStormCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.LanceItem;

public class LanceStormCoreEntityRenderer extends EntityRenderer<LanceStormCoreEntity> {
	private final ItemRenderer itemRenderer;

	public LanceStormCoreEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(LanceStormCoreEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (!(entity.getOwner() instanceof Player caster))
			return;

		ItemStack heldStack = caster.getMainHandItem();
		if (!(heldStack.getItem() instanceof LanceItem))
			return;

		float yaw = entity.getFrozenYaw();
		float pitch = entity.getFrozenPitch();
		float yawRad = (float) Math.toRadians(yaw);
		float pitchRad = (float) Math.toRadians(pitch);
		Vec3 forward = new Vec3(-Math.sin(yawRad) * Math.cos(pitchRad), -Math.sin(pitchRad), Math.cos(yawRad) * Math.cos(pitchRad));
		Vec3 upRef = Math.abs(forward.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
		Vec3 right = forward.cross(upRef).normalize();
		Vec3 up = right.cross(forward).normalize();

		float tickProgress = entity.tickCount + partialTicks;
		float travelled;
		if (tickProgress <= LanceStormCoreEntity.TELEGRAPH_TICKS) {
			travelled = 0F;
		} else {
			float thrustTick = tickProgress - LanceStormCoreEntity.TELEGRAPH_TICKS;
			float progress = Mth.clamp(thrustTick / LanceStormCoreEntity.THRUST_TICKS, 0F, 1F);
			travelled = progress * LanceStormCoreEntity.THRUST_DISTANCE;
		}

		float renderYaw = 180F - yaw;
		float renderPitch = -pitch;

		for (int i = 0; i < LanceStormCoreEntity.LANCE_COUNT; i++) {
			double angle = (2 * Math.PI / LanceStormCoreEntity.LANCE_COUNT) * i;
			Vec3 ringOffset = right.scale(Math.cos(angle) * LanceStormCoreEntity.HEX_RADIUS).add(up.scale(Math.sin(angle) * LanceStormCoreEntity.HEX_RADIUS));
			Vec3 point = entity.position().add(forward.scale(travelled)).add(ringOffset);
			Vec3 relative = point.subtract(entity.position());

			poseStack.pushPose();
			poseStack.translate(relative.x, relative.y, relative.z);
			poseStack.mulPose(Axis.YP.rotationDegrees(renderYaw));
			poseStack.mulPose(Axis.XP.rotationDegrees(renderPitch));
			poseStack.mulPose(Axis.XP.rotationDegrees(90F)); // stand the lance up along its own length

			itemRenderer.renderStatic(heldStack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId() + i);

			poseStack.popPose();
		}

		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(LanceStormCoreEntity entity) {
		return net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;
	}
}