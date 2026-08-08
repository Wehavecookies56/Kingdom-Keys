package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorld;
import org.joml.Matrix4f;

// Draws the world's texture as a flat billboard that always faces the camera
public class WorldMarkerEntityRenderer extends EntityRenderer<WorldMarkerEntity> {

	private static final ResourceLocation MISSING = KingdomKeys.rl("textures/worldmap/missing.png");
	private static final int FULL_BRIGHT = 0xF000F0;

	public WorldMarkerEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(WorldMarkerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		GummiWorld world = entity.getWorld();
		if (world == null) {
			return;
		}

		float half = world.scale() * 0.5F;

		poseStack.pushPose();
		{
			poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
			VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
			Matrix4f matrix = poseStack.last().pose();
			vertex(consumer, matrix, -half, -half, 0F, 0F, 1F);
			vertex(consumer, matrix, half, -half, 0F, 1F, 1F);
			vertex(consumer, matrix, half, half, 0F, 1F, 0F);
			vertex(consumer, matrix, -half, half, 0F, 0F, 0F);
		}
		poseStack.popPose();

		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(WorldMarkerEntity entity) {
		GummiWorld world = entity.getWorld();
		return world == null ? MISSING : world.texture();
	}

	@Override
	public boolean shouldRender(WorldMarkerEntity entity, Frustum frustum, double x, double y, double z) {
		return entity.getWorld() != null;
	}

	private static void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float z, float u, float v) {
		buffer.addVertex(matrix, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(FULL_BRIGHT).setNormal(0F, 0F, 1F);
	}
}
