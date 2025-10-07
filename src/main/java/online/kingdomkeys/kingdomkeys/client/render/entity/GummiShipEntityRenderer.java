package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;

import java.awt.*;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {

	int red = 96, green = 140, blue = 109, alpha = 255;
	//private GummiShipModel model;

	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		//model = new GummiShipModel();
	}

	@Override
	public void render(GummiShipEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			//matrixStackIn.translate(-2.5F, 0, -2.5);
			//matrixStackIn.scale(0.5F, 0.5F, 0.5F);
			//System.out.println(entityIn.getDataDataManager());
			CompoundTag data = entityIn.getDataManager();

			if(data != null && !data.isEmpty()){
				GummiShipEntity.GummiStructure struc = new GummiShipEntity.GummiStructure(7,7,7);
				struc.deserializeNBT(entityIn.level().registryAccess(),data);

				// Centrar la estructura en torno al origen de la entidad
				int w = entityIn.structure.width;
				int h = entityIn.structure.height;
				int d = entityIn.structure.depth;
				matrixStackIn.translate(-w / 2.0, 0, -d / 2.0);

				// Renderizador de bloques del cliente
				BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

				// Recorremos todos los bloques del array tridimensional
				for (int x = 0; x < w; x++) {
					for (int y = 0; y < h; y++) {
						for (int z = 0; z < d; z++) {
							BlockState state = entityIn.structure.blocks[x][y][z];
							if (state == null || state.isAir()) continue;
							matrixStackIn.pushPose();
							{
								matrixStackIn.translate(x, y, z);
								blockRenderer.renderSingleBlock(state, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
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
