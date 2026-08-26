package online.kingdomkeys.kingdomkeys.client.render.shotlock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.shotlock.BaseShotlockShotEntity;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class VolleyShotlockShotEntityRenderer extends EntityRenderer<BaseShotlockShotEntity> {

	private static final int TRAIL_LENGTH = 6;
	private static final int OPENING_TRAIL_LENGTH = 2;
	private static final float TRAIL_WIDTH = 0.06F;

	private final CubeModel model;

	private final Map<Integer, TrailRenderer.Trail> trails = new HashMap<>();
	private final Map<Integer, Integer> lastTickPushed = new HashMap<>();

	public VolleyShotlockShotEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
        model = new CubeModel(context.bakeLayer(CubeModel.LAYER_LOCATION));
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(BaseShotlockShotEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		renderTrail(entity, partialTicks, matrixStackIn, bufferIn);

		ItemStack visualItem = entity.getVisualItem();
		if (!visualItem.isEmpty()) {
			matrixStackIn.pushPose();
			{
				matrixStackIn.translate(0, 0.05, 0);
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO)));
				matrixStackIn.mulPose(Axis.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO)));

				float spin = (entity.tickCount + partialTicks) * 25F;
				matrixStackIn.mulPose(Axis.ZP.rotationDegrees(spin));

				// Full bright (0xF000F0) so it reads as genuinely glowing rather than just lit normally.
				Minecraft.getInstance().getItemRenderer().renderStatic(visualItem, ItemDisplayContext.FIXED, 0xF000F0, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.level(), entity.getId());
			}
			matrixStackIn.popPose();
			super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			return;
		}

		matrixStackIn.pushPose();
    	{	
    		matrixStackIn.translate(0, 0.05, 0);
    		matrixStackIn.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO)));
    		matrixStackIn.mulPose(Axis.XN.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO)));
			
    		matrixStackIn.scale(0.3F, 0.3F, 0.3F);
    		Color color = new Color(entity.getColor());
    		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, color.getRGB());
     	}
     	matrixStackIn.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	private void renderTrail(BaseShotlockShotEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
		int id = entity.getId();

		TrailRenderer.Trail trail = trails.get(id);
		if (trail == null) {
			trail = new TrailRenderer.Trail(TRAIL_LENGTH);
			trails.put(id, trail);
			removeDeadShots(entity);
		}

		Integer lastTick = lastTickPushed.get(id);
		if (lastTick == null || lastTick != entity.tickCount) {
			lastTickPushed.put(id, entity.tickCount);
			trail.pushHead(entity.position());
		}

		Vec3 origin = entity.getPosition(partialTicks);
		Color color = new Color(entity.getColor());

		Vec3[] points = trail.interpolated(partialTicks);
		int visible = entity.isTrailReady() ? TRAIL_LENGTH : OPENING_TRAIL_LENGTH;
		if (visible < points.length) {
			points = Arrays.copyOf(points, visible);
		}

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();
		TrailRenderer.render(points, origin, pose, consumer, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, TRAIL_WIDTH);
	}

	private void removeDeadShots(BaseShotlockShotEntity current) {
		trails.keySet().removeIf(id -> current.level().getEntity(id) == null);
		lastTickPushed.keySet().removeIf(id -> !trails.containsKey(id));
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(BaseShotlockShotEntity entity) {
		return KingdomKeys.rl("textures/entity/models/cube.png");
	}
}