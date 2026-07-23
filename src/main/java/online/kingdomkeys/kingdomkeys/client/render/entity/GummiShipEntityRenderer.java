package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiEngineBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {
	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	int isXEven = -1, isZEven = -1;

	private static final RenderType CUSTOM_TINTED_GLASS2 = RenderType.create(
			"custom_tinted_glass",
			DefaultVertexFormat.BLOCK,
			VertexFormat.Mode.QUADS,
			2097152,
			true,
			true,
			RenderType.CompositeState.builder()
					.setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
					.setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setOutputState(RenderStateShard.MAIN_TARGET)
					.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
					.setCullState(RenderStateShard.CULL)
					.setLightmapState(RenderStateShard.LIGHTMAP)
					.setOverlayState(RenderStateShard.OVERLAY)
					.setWriteMaskState(RenderStateShard.COLOR_WRITE)
					.createCompositeState(true)
	);

	// Engine/thruster exhaust flash - simple vanilla flame particles spawned at each engine block's
	// real world position while the ship is moving. (A custom smooth-ribbon trail was tried here first,
	// re-using the same TrailRenderer the Savepoint uses, but the ribbon mesh kept degenerating into a
	// stray "stretches off to infinity" artifact that wasn't worth chasing further - plain particles are
	// far simpler and give a perfectly reasonable exhaust flash on their own.)
	private static final double MOVING_THRESHOLD_SQR = 0.0025D; // ~0.05 blocks/tick before particles kick in
	private static final Map<Integer, Long> LAST_PARTICLE_TICK = new HashMap<>();

	@Override
	public void render(GummiShipEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		boolean spawnParticles = entityIn.getDeltaMovement().lengthSqr() >= MOVING_THRESHOLD_SQR
				&& shouldSpawnParticlesThisTick(entityIn);
		Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

		matrixStackIn.pushPose();
		{
			CompoundTag data = entityIn.getDataManager();

			if(data != null && !data.isEmpty()){
				int w = entityIn.structure.getWidth();
				int h = entityIn.structure.getHeight();
				int d = entityIn.structure.getDepth();
				float xRot = Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot());
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(-xRot));
				matrixStackIn.translate(-w / 2.0, 0, -d / 2.0);

				boolean xEven, zEven;
				if(isXEven == -1 || isZEven == -1) {
					isXEven = Utils.isStructureEven(entityIn.structure)[0] ? 1 : 0;
					isZEven = Utils.isStructureEven(entityIn.structure)[1] ? 1 : 0;
				}

				xEven = isXEven == 1;
				zEven = isZEven == 1;

				BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
				for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						for (int z = 0; z < d; z++) {
							BlockState state = entityIn.structure.getBlocks()[x][y][z];
							if (state == null || state.isAir())
								continue;
							matrixStackIn.pushPose();
							{
								matrixStackIn.translate(xEven ? x+0.5F : x, y, zEven ? z-0.5F : z);

								if (spawnParticles && state.getBlock() instanceof GummiEngineBlock) {
									// Same block-center capture that worked fine for attaching the
									// (now reverted) trail: +0.5 on every axis to reach the center of
									// the block instead of its corner, then read the world position
									// straight off the already-fully-transformed matrix.
									matrixStackIn.pushPose();
									matrixStackIn.translate(0.5, 0.5, 0.5);
									Matrix4f m = matrixStackIn.last().pose();
									Vec3 worldPos = camPos.add(m.m30(), m.m31(), m.m32());
									entityIn.level().addParticle(ParticleTypes.FLAME, worldPos.x, worldPos.y, worldPos.z, 0, 0, 0);
									matrixStackIn.popPose();
								}

								RenderType renderType = ItemBlockRenderTypes.getRenderType(state, false);
								if(state.getBlock() instanceof TransparentBlock && Minecraft.getInstance().player.getVehicle() == entityIn && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON){
									renderType = CUSTOM_TINTED_GLASS2;
								}
								blockRenderer.renderSingleBlock(state, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
							}
							matrixStackIn.popPose();
						}
					}
				}
			}
		}
		matrixStackIn.popPose();
	}

	/** Caps particle spawning to once per game tick (render() runs every frame, which would otherwise
	 * spam far more particles than needed at high framerates). */
	private boolean shouldSpawnParticlesThisTick(GummiShipEntity entityIn) {
		long gameTime = entityIn.level().getGameTime();
		Long last = LAST_PARTICLE_TICK.get(entityIn.getId());
		if (last != null && last == gameTime) return false;
		LAST_PARTICLE_TICK.put(entityIn.getId(), gameTime);
		return true;
	}

	@Override
	public ResourceLocation getTextureLocation(GummiShipEntity entity) {
		return KingdomKeys.rl("textures/entity/models/gummi.png");
	}

}