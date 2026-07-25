package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiEngineBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

import java.util.*;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {
	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	int isXEven = -1, isZEven = -1;

	private static final double MOVING_THRESHOLD_SQR = 0.0025D; // ~0.05 blocks/tick before particles kick in
	private static final Map<Integer, Long> LAST_PARTICLE_TICK = new HashMap<>();
	private static final Map<Integer, CachedShipMesh> MESH_CACHE = new HashMap<>();

	private static final class CachedShipMesh {
		final Map<RenderType, VertexBuffer> buffers;
		final List<Vec3> engineLocalPositions;
		final GummiStructure builtFrom;
		final boolean xEven;
		final boolean zEven;

		CachedShipMesh(Map<RenderType, VertexBuffer> buffers, List<Vec3> engineLocalPositions, GummiStructure builtFrom, boolean xEven, boolean zEven) {
			this.buffers = buffers;
			this.engineLocalPositions = engineLocalPositions;
			this.builtFrom = builtFrom;
			this.xEven = xEven;
			this.zEven = zEven;
		}

		void close() {
			for (VertexBuffer vb : buffers.values()) vb.close();
		}
	}

	/** Routes BlockRenderDispatcher's output into our own per-RenderType BufferBuilders instead of the
	 * live screen buffer, so the exact same block-model rendering call can be reused to bake a cached
	 * mesh instead of drawing straight to the frame. */
	private static final class CapturingBufferSource implements MultiBufferSource {
		private final Map<RenderType, BufferBuilder> builders = new LinkedHashMap<>();
		private final List<ByteBufferBuilder> backing = new ArrayList<>();

		@Override
		public VertexConsumer getBuffer(RenderType renderType) {
			return builders.computeIfAbsent(renderType, rt -> {
				ByteBufferBuilder buf = new ByteBufferBuilder(rt.bufferSize());
				backing.add(buf);
				return new BufferBuilder(buf, rt.mode(), rt.format());
			});
		}

		Map<RenderType, VertexBuffer> upload() {
			Map<RenderType, VertexBuffer> result = new LinkedHashMap<>();
			for (Map.Entry<RenderType, BufferBuilder> entry : builders.entrySet()) {
				MeshData mesh = entry.getValue().build();
				if (mesh == null) continue;
				VertexBuffer vb = new VertexBuffer(VertexBuffer.Usage.STATIC);
				vb.bind();
				vb.upload(mesh);
				VertexBuffer.unbind();
				result.put(entry.getKey(), vb);
			}
			for (ByteBufferBuilder buf : backing) buf.close();
			return result;
		}
	}

	@Override
	public void render(GummiShipEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		CompoundTag data = entityIn.getDataManager();
		if (data == null || data.isEmpty()) return;

		CachedShipMesh cached = MESH_CACHE.get(entityIn.getId());
		if (cached == null || cached.builtFrom != entityIn.structure) {
			if (cached != null) cached.close();
			cached = buildCache(entityIn, packedLightIn);
			MESH_CACHE.put(entityIn.getId(), cached);
		}

		boolean spawnParticles = entityIn.getDeltaMovement().lengthSqr() >= MOVING_THRESHOLD_SQR
				&& shouldSpawnParticlesThisTick(entityIn);
		Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		matrixStackIn.pushPose();
		{
			int w = entityIn.structure.getWidth();
			int d = entityIn.structure.getDepth();
			float xRot = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
			matrixStackIn.mulPose(Axis.XP.rotationDegrees(-xRot));
			matrixStackIn.translate(-w / 2.0, 0, -d / 2.0);

			if (spawnParticles) {
				for (Vec3 local : cached.engineLocalPositions) {
					matrixStackIn.pushPose();
					matrixStackIn.translate(local.x, local.y, local.z);
					Matrix4f m = matrixStackIn.last().pose();
					Vec3 worldPos = camPos.add(m.m30(), m.m31(), m.m32());
					entityIn.level().addParticle(ParticleTypes.FLAME, worldPos.x, worldPos.y, worldPos.z, 0, 0, 0);
					matrixStackIn.popPose();
				}
			}

			Matrix4f pose = new Matrix4f(RenderSystem.getModelViewMatrix()).mul(matrixStackIn.last().pose());
			Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
			for (Map.Entry<RenderType, VertexBuffer> entry : cached.buffers.entrySet()) {
				RenderType renderType = entry.getKey();
				renderType.setupRenderState();
				VertexBuffer vb = entry.getValue();
				vb.bind();
				vb.drawWithShader(pose, projectionMatrix, RenderSystem.getShader());
				VertexBuffer.unbind();
				renderType.clearRenderState();
			}
		}
		matrixStackIn.popPose();
	}

	/** Walks the block grid exactly once (instead of every frame) to bake the ship's geometry into
	 * per-RenderType VertexBuffers, plus the local-space positions of every engine block (for the
	 * exhaust particles) so those don't need a full grid walk each frame either. */
	private CachedShipMesh buildCache(GummiShipEntity entityIn, int packedLight) {
		KingdomKeys.LOGGER.info("Gummi ship mesh cache (re)built for entity {}", entityIn.getId());
		int w = entityIn.structure.getWidth();
		int h = entityIn.structure.getHeight();
		int d = entityIn.structure.getDepth();

		boolean xEven, zEven;
		if (isXEven == -1 || isZEven == -1) {
			boolean[] even = Utils.isStructureEven(entityIn.structure);
			isXEven = even[0] ? 1 : 0;
			isZEven = even[1] ? 1 : 0;
		}
		xEven = isXEven == 1;
		zEven = isZEven == 1;

		BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
		CapturingBufferSource capture = new CapturingBufferSource();
		List<Vec3> engineLocalPositions = new ArrayList<>();

		PoseStack bakePose = new PoseStack();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				for (int z = 0; z < d; z++) {
					BlockState state = entityIn.structure.getBlocks()[x][y][z];
					if (state == null || state.isAir()) continue;

					bakePose.pushPose();
					{
						float bx = xEven ? x + 0.5F : x;
						float by = y;
						float bz = zEven ? z - 0.5F : z;
						bakePose.translate(bx, by, bz);

						if (state.getBlock() instanceof GummiEngineBlock) {
							engineLocalPositions.add(new Vec3(bx + 0.5D, by + 0.5D, bz + 0.5D));
						}

						RenderType renderType = ItemBlockRenderTypes.getRenderType(state, false);
						blockRenderer.renderSingleBlock(state, bakePose, capture, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
					}
					bakePose.popPose();
				}
			}
		}

		Map<RenderType, VertexBuffer> buffers = capture.upload();
		return new CachedShipMesh(buffers, engineLocalPositions, entityIn.structure, xEven, zEven);
	}

	private boolean shouldSpawnParticlesThisTick(GummiShipEntity entityIn) {
		long gameTime = entityIn.level().getGameTime();
		Long last = LAST_PARTICLE_TICK.get(entityIn.getId());
		if (last != null && last == gameTime)
			return false;
		LAST_PARTICLE_TICK.put(entityIn.getId(), gameTime);
		return true;
	}

	@Override
	public ResourceLocation getTextureLocation(GummiShipEntity entity) {
		return KingdomKeys.rl("textures/entity/models/gummi.png");
	}

}