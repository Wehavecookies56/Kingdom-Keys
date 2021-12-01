package online.kingdomkeys.kingdomkeys.client.render.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.PairBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

@OnlyIn(Dist.CLIENT)
public class PairBloxRenderer extends EntityRenderer<PairBloxEntity> {

	public PairBloxRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager);
		this.shadowRadius = 0.5F;
	}

	@Override
	public void render(PairBloxEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {

		BlockState blockstate = ModBlocks.pairBlox.get().defaultBlockState().setValue(PairBloxBlock.PAIR, entityIn.getPair());
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level world = entityIn.level;
			if (blockstate != world.getBlockState(new BlockPos(entityIn.position())) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				matrixStackIn.pushPose();
				BlockPos blockpos = new BlockPos(entityIn.getX(), entityIn.getBoundingBox().maxY, entityIn.getZ());
				matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
				BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
				for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.chunkBufferLayers()) {
					if (ItemBlockRenderTypes.canRenderInLayer(blockstate, type)) {
						net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
						blockrendererdispatcher.getModelRenderer().tesselateBlock(world, blockrendererdispatcher.getBlockModel(blockstate), blockstate, blockpos, matrixStackIn, bufferIn.getBuffer(type), false, new Random(), blockstate.getSeed(BlockPos.ZERO), OverlayTexture.NO_OVERLAY);
					}
				}
				net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
				matrixStackIn.popPose();
				super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			}
		}

		/*matrixStack.push();
		matrixStack.translate(0.0D, 0.5D, 0.0D);
		if (entity.getPair()) {
			matrixStack.scale(1, 1, 1);
		} else {
			matrixStack.scale(2, 2, 2);
		}
		matrixStack.pop();
		super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);*/
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(PairBloxEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
