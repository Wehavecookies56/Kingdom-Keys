package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {

	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
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

	@Override
	public void render(GummiShipEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			CompoundTag data = entityIn.getDataManager();

			if(data != null && !data.isEmpty()){
				int w = entityIn.structure.getWidth();
				int h = entityIn.structure.getHeight();
				int d = entityIn.structure.getDepth();
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
				matrixStackIn.translate(-w / 2.0, 0, -d / 2.0);



				BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
				for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						for (int z = 0; z < d; z++) {
							BlockState state = entityIn.structure.getBlocks()[x][y][z];
							if (state == null || state.isAir())
                                continue;
                            boolean xEven = Utils.isStructureEven(entityIn.structure)[0];
                            boolean zEven = Utils.isStructureEven(entityIn.structure)[1];
							matrixStackIn.pushPose();
							{
								matrixStackIn.translate(xEven ? x+0.5F : x, y, zEven ? z-0.5F : z);
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

	@Override
	public ResourceLocation getTextureLocation(GummiShipEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/gummi.png");
	}

}
