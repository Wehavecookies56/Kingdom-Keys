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
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiBlockBase;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiEngineBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.*;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {
	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	private static final double MOVING_THRESHOLD_SQR = 0.0025D; // ~0.05 blocks/tick before particles kick in
	private static final Map<Integer, CachedShipMesh> MESH_CACHE = new HashMap<>();

	private record Exhaust(Vec3 pos, double spread, int flames) {}

	private static final class CachedShipMesh {
		final Map<RenderType, VertexBuffer> buffers;
		final List<Exhaust> engineLocalPositions;
		// Where each nozzle last bursted, in world space
		final Vec3[] lastExhaust;
		long lastBurstTick = Long.MIN_VALUE;
		final GummiStructure builtFrom;
		final boolean xEven;
		final boolean zEven;
		// Centre of the blocks actually placed, in the frame the roll is applied in
		final float rollPivotX, rollPivotY;

		CachedShipMesh(Map<RenderType, VertexBuffer> buffers, List<Exhaust> engineLocalPositions, GummiStructure builtFrom, boolean xEven, boolean zEven, float rollPivotX, float rollPivotY) {
			this.buffers = buffers;
			this.engineLocalPositions = engineLocalPositions;
			this.lastExhaust = new Vec3[engineLocalPositions.size()];
			this.builtFrom = builtFrom;
			this.xEven = xEven;
			this.zEven = zEven;
			this.rollPivotX = rollPivotX;
			this.rollPivotY = rollPivotY;
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

		boolean spawnParticles = entityIn.isThrusting() && entityIn.getFuel() > 0 && entityIn.getDeltaMovement().lengthSqr() >= MOVING_THRESHOLD_SQR;
		Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		matrixStackIn.pushPose();
		{
			int w = entityIn.structure.getWidth();
			int d = entityIn.structure.getDepth();
			float xRot = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
			matrixStackIn.mulPose(Axis.XP.rotationDegrees(-xRot));
			float roll = entityIn.getRoll(partialTicks);
			if (roll != 0F) {
				matrixStackIn.translate(cached.rollPivotX, cached.rollPivotY, 0);
				matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-roll));
				matrixStackIn.translate(-cached.rollPivotX, -cached.rollPivotY, 0);
			}
			matrixStackIn.translate(-w / 2.0, 0, -d / 2.0);

			if (spawnParticles) {
				emitExhaust(entityIn, cached, matrixStackIn, camPos);
			} else {
				java.util.Arrays.fill(cached.lastExhaust, null);
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

	private static final double VERNIER_SPREAD = 0.06D, THRUSTER_SPREAD = 0.2D, FLARE_SPREAD = 0.15D, BIG_SPREAD = 0.45D;

	// Multiblock engines burn from one nozzle, not from every block they are made of. Null for the parts that do not
	@Nullable
	private static Exhaust getExhaust(GummiEngineBlock engine, BlockState state, Vec3 center) {
		switch (engine.getPlacementType()) {
			case MULTIBLOCK2x2x2 -> {
				if (state.getValue(GummiBlockBase.X) != 0 || state.getValue(GummiBlockBase.Y) != 0 || state.getValue(GummiBlockBase.Z) != 0) {
					return null;
				}
				Direction facing = state.getValue(GummiBlockBase.HORIZONTAL_FACING);
				Vec3 back = step(facing).scale(-0.5D);
				Vec3 corner = step(Direction.UP).add(step(facing.getClockWise())).scale(0.5D);
				return new Exhaust(center.add(back).add(corner), BIG_SPREAD, 5);
			}
			case MULTIBLOCK1x1x3 -> {
				// Only the block at the nozzle end, the one furthest along facing
				if (state.getValue(GummiBlockBase.LENGTH) != 2) {
					return null;
				}
				Direction facing = state.getValue(GummiBlockBase.FACING);
				return new Exhaust(center.add(step(facing).scale(0.5D)), FLARE_SPREAD, 2);
			}
			default -> {
				if (state.is(ModBlocks.gummiThruster.get())) {
					return new Exhaust(center, THRUSTER_SPREAD, 3);
				}
				return new Exhaust(center, VERNIER_SPREAD, 1);
			}
		}
	}

	private static Vec3 step(Direction direction) {
		return new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ());
	}

	/** Walks the block grid exactly once (instead of every frame) to bake the ship's geometry into
	 * per-RenderType VertexBuffers, plus the local-space positions of every engine block (for the
	 * exhaust particles) so those don't need a full grid walk each frame either. */
	private CachedShipMesh buildCache(GummiShipEntity entityIn, int packedLight) {
		//KingdomKeys.LOGGER.info("Gummi ship mesh cache (re)built for entity {}", entityIn.getId());
		int w = entityIn.structure.getWidth();
		int h = entityIn.structure.getHeight();
		int d = entityIn.structure.getDepth();

		boolean[] even = Utils.isStructureEven(entityIn.structure);
		boolean xEven = even[0];
		boolean zEven = even[1];

		BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
		CapturingBufferSource capture = new CapturingBufferSource();
		List<Exhaust> engineLocalPositions = new ArrayList<>();

		PoseStack bakePose = new PoseStack();
		int minX = w, maxX = -1, minY = h, maxY = -1;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				for (int z = 0; z < d; z++) {
					BlockState state = entityIn.structure.getBlocks()[x][y][z];
					if (state == null || state.isAir()) continue;
					minX = Math.min(minX, x);
					maxX = Math.max(maxX, x);
					minY = Math.min(minY, y);
					maxY = Math.max(maxY, y);

					bakePose.pushPose();
					{
						float bx = xEven ? x + 0.5F : x;
						float by = y;
						float bz = zEven ? z - 0.5F : z;
						bakePose.translate(bx, by, bz);

						if (state.getBlock() instanceof GummiEngineBlock engine) {
							Exhaust exhaust = getExhaust(engine, state, new Vec3(bx + 0.5D, by + 0.5D, bz + 0.5D));
							if (exhaust != null) {
								engineLocalPositions.add(exhaust);
							}
						}

						RenderType renderType = ItemBlockRenderTypes.getRenderType(state, false);
						blockRenderer.renderSingleBlock(state, bakePose, capture, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
					}
					bakePose.popPose();
				}
			}
		}

		Map<RenderType, VertexBuffer> buffers = capture.upload();
		float pivotX = maxX < 0 ? 0 : (minX + maxX + 1) / 2F + (xEven ? 0.5F : 0F) - w / 2F;
		float pivotY = maxY < 0 ? h / 2F : (minY + maxY + 1) / 2F;
		return new CachedShipMesh(buffers, engineLocalPositions, entityIn.structure, xEven, zEven, pivotX, pivotY);
	}

	// Distance between bursts along the exhaust's path
	private static final double BURSTS_STEP = 0.3D;
	private static final int MAX_BURSTS = 16;

	private void emitExhaust(GummiShipEntity ship, CachedShipMesh cached, PoseStack poseStack, Vec3 camPos) {
		RandomSource random = ship.level().random;
		long tick = ship.level().getGameTime();
		boolean newTick = tick != cached.lastBurstTick;

		for (int e = 0; e < cached.engineLocalPositions.size(); e++) {
			Exhaust exhaust = cached.engineLocalPositions.get(e);

			poseStack.pushPose();
			poseStack.translate(exhaust.pos().x, exhaust.pos().y, exhaust.pos().z);
			Matrix4f m = poseStack.last().pose();
			Vec3 now = camPos.add(m.m30(), m.m31(), m.m32());
			poseStack.popPose();

			Vec3 last = cached.lastExhaust[e];
			double distance = last == null ? 0D : now.distanceTo(last);
			int bursts;

			if (last == null || distance > BURSTS_STEP * MAX_BURSTS * 2D) {
				bursts = newTick ? 1 : 0;
				last = now;
			} else if (distance >= BURSTS_STEP) {
				bursts = Math.min(MAX_BURSTS, (int) Math.ceil(distance / BURSTS_STEP));
			} else {
				bursts = newTick ? 1 : 0;
			}

			for (int p = 1; p <= bursts; p++) {
				Vec3 at = last.lerp(now, p / (double) bursts);
				for (int f = 0; f < exhaust.flames(); f++) {
					double s = exhaust.spread();
					ship.level().addParticle(ParticleTypes.FLAME, at.x + (random.nextDouble() - 0.5D) * 2D * s, at.y + (random.nextDouble() - 0.5D) * 2D * s, at.z + (random.nextDouble() - 0.5D) * 2D * s, 0, 0, 0);
				}
			}

			if (bursts > 0) {
				cached.lastExhaust[e] = now;
			}
		}

		cached.lastBurstTick = tick;
	}

	@Override
	public ResourceLocation getTextureLocation(GummiShipEntity entity) {
		return KingdomKeys.rl("textures/entity/models/gummi.png");
	}

}