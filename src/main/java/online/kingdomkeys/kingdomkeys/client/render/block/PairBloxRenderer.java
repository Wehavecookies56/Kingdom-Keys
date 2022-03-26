package online.kingdomkeys.kingdomkeys.client.render.block;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.PairBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

/**
 * Mostly a copy of {@link net.minecraft.client.renderer.entity.TntRenderer}
 * with some small changes
 */
@OnlyIn(Dist.CLIENT)
public class PairBloxRenderer extends EntityRenderer<PairBloxEntity> {

	public PairBloxRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	@Override
	public void render(PairBloxEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLightIn) {
		BlockState blockstate = ModBlocks.pairBlox.get().defaultBlockState().setValue(PairBloxBlock.PAIR, pEntity.getPair());
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			pMatrixStack.pushPose();
			pMatrixStack.translate(0.0D, 0.5D, 0.0D);
			pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
			pMatrixStack.translate(-0.5D, -0.5D, 0.5D);
			pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, pMatrixStack, pBuffer, pPackedLightIn, OverlayTexture.NO_OVERLAY);
			pMatrixStack.popPose();
			super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLightIn);
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

	public static void renderWhiteSolidBlock(BlockState pBlockState, PoseStack pMatrixStack, MultiBufferSource pRenderTypeBuffer, int pCombinedLight, boolean pDoFullBright) {
		int i;
		if (pDoFullBright) {
			i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
		} else {
			i = OverlayTexture.NO_OVERLAY;
		}

		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(pBlockState, pMatrixStack, pRenderTypeBuffer, pCombinedLight, i);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(PairBloxEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
