package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {

	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

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
				//matrixStackIn.mulPose(Axis.XP.rotationDegrees(-(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()))));
				matrixStackIn.translate(-w / 2.0, 0, -d / 2.0);

				BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
				for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						for (int z = 0; z < d; z++) {
							BlockState state = entityIn.structure.getBlocks()[x][y][z];
							if (state == null || state.isAir()) continue;
							matrixStackIn.pushPose();
							{
								matrixStackIn.translate(x, y, z);
								blockRenderer.renderSingleBlock(state, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
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
