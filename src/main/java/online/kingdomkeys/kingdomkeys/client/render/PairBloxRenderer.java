package online.kingdomkeys.kingdomkeys.client.render;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.PairBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

/**
 * Mostly a copy of {@link net.minecraft.client.renderer.entity.TNTRenderer}
 * with some small changes
 */
@OnlyIn(Dist.CLIENT)
public class PairBloxRenderer extends EntityRenderer<PairBloxEntity> {

	public static final Factory FACTORY = new PairBloxRenderer.Factory();

	public PairBloxRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(PairBloxEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {

		BlockState blockstate = ModBlocks.pairBlox.get().getDefaultState().with(PairBloxBlock.PAIR, entityIn.getPair());
		if (blockstate.getRenderType() == BlockRenderType.MODEL) {
			World world = entityIn.world;
			if (blockstate != world.getBlockState(new BlockPos(entityIn)) && blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
				matrixStackIn.push();
				BlockPos blockpos = new BlockPos(entityIn.getPosX(), entityIn.getBoundingBox().maxY, entityIn.getPosZ());
				matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
				for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.getBlockRenderTypes()) {
					if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
						net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
						blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, matrixStackIn, bufferIn.getBuffer(type), false, new Random(), blockstate.getPositionRandom(BlockPos.ZERO), OverlayTexture.NO_OVERLAY);
					}
				}
				net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
				matrixStackIn.pop();
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
	public ResourceLocation getEntityTexture(PairBloxEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

	public static class Factory implements IRenderFactory<PairBloxEntity> {
		@Override
		public EntityRenderer<? super PairBloxEntity> createRenderFor(EntityRendererManager entityRendererManager) {
			return new PairBloxRenderer(entityRendererManager);
		}
	}
}
