package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorld;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorldLoader;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

// We need a custom renderer so the entity tracking limit is not what holds us off
@OnlyIn(Dist.CLIENT)
public class WorldMapRenderer {
	// Fun fact, TIL (15/8/26) I learned you can write down large numbers with underscores :)
	private static final float FOG_START = 1_000_000F;
	private static final float FOG_END = 2_000_000F;

	private static final ResourceLocation UNKNOWN = KingdomKeys.rl("textures/worldmap/missing.png");

	private static Matrix4f frameModelView;
	private static Matrix4f frameProjection;
	private static Vec3 frameCamera;

	@Nullable
	public static Matrix4f modelViewMatrix() {
		return frameModelView;
	}

	@Nullable
	public static Matrix4f projectionMatrix() {
		return frameProjection;
	}

	@Nullable
	public static Vec3 cameraPosition() {
		return frameCamera;
	}

	public static void forget() {
		frameModelView = null;
		frameProjection = null;
		frameCamera = null;
	}

	@SubscribeEvent
	public void render(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		PoseStack pose = event.getPoseStack();

		if (pose == null || mc.level == null || !mc.level.dimension().equals(ModDimensions.OCEAN_BETWEEN) || GummiWorldLoader.all().isEmpty()) {
			forget();
			return;
		}

		Vec3 camera = event.getCamera().getPosition();

		frameModelView = new Matrix4f(event.getModelViewMatrix());
		frameProjection = new Matrix4f(event.getProjectionMatrix());
		frameCamera = camera;
		MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

		float fogStart = RenderSystem.getShaderFogStart();
		float fogEnd = RenderSystem.getShaderFogEnd();
		RenderSystem.setShaderFogStart(FOG_START);
		RenderSystem.setShaderFogEnd(FOG_END);

		PlayerData playerData = mc.player == null ? null : PlayerData.get(mc.player);

		try {
			for (GummiWorld world : GummiWorldLoader.all().values()) {
				boolean known = playerData != null && playerData.knowsWorld(world);
				draw(world, mc, pose, buffer, camera, known);
			}
		} finally {
			// Whatever happens above, the fog has to go back: leaving it off would be felt everywhere
			RenderSystem.setShaderFogStart(fogStart);
			RenderSystem.setShaderFogEnd(fogEnd);
		}
	}

	private void draw(GummiWorld world, Minecraft mc, PoseStack pose, MultiBufferSource.BufferSource buffer, Vec3 camera, boolean known) {
		Vec3 at = world.worldmapPosition();
		float half = world.scale() * 0.5F;

		// A locked world has the ? texture
		ResourceLocation texture = known ? world.texture() : UNKNOWN;

		pose.pushPose();
		{
			pose.translate(at.x - camera.x, at.y - camera.y, at.z - camera.z);
			pose.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

			RenderType type = RenderType.entityCutoutNoCull(texture);
			VertexConsumer consumer = buffer.getBuffer(type);
			Matrix4f matrix = pose.last().pose();

			vertex(consumer, matrix, -half, -half, 0F, 1F);
			vertex(consumer, matrix, half, -half, 1F, 1F);
			vertex(consumer, matrix, half, half, 1F, 0F);
			vertex(consumer, matrix, -half, half, 0F, 0F);

			buffer.endBatch(type);
		}
		pose.popPose();
	}

	private static void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y, float u, float v) {
		buffer.addVertex(matrix, x, y, 0F).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0F, 0F, 1F);
	}
}
