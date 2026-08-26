package online.kingdomkeys.kingdomkeys.client.render.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.magic.IceBarrageControllerEntity;

public class IceBarrageEntityRenderer extends EntityRenderer<IceBarrageControllerEntity> {

	public IceBarrageEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

	@Override
	public void render(IceBarrageControllerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

		poseStack.pushPose();
		{
			BlockRenderDispatcher renderer = Minecraft.getInstance().getBlockRenderer();
			BlockState iceState = Blocks.PACKED_ICE.defaultBlockState();

			renderPiece(renderer, iceState, poseStack, buffer, LightTexture.FULL_BRIGHT, 0F, 0F, 0F, 1.2F, 0.15F, 1.2F, 0F);

			renderPiece(renderer, iceState, poseStack, buffer, LightTexture.FULL_BRIGHT, 0F, 0F, -0.9F, 0.8F, 0.12F, 0.8F, 15F);
			renderPiece(renderer, iceState, poseStack, buffer, LightTexture.FULL_BRIGHT, 0F, 0F, 0.9F, 0.8F, 0.12F, 0.8F, -15F);
			renderPiece(renderer, iceState, poseStack, buffer, LightTexture.FULL_BRIGHT, 0.9F, 0F, 0F, 0.8F, 0.12F, 0.8F, 15F);
			renderPiece(renderer, iceState, poseStack, buffer, LightTexture.FULL_BRIGHT, -0.9F, 0F, 0F, 0.8F, 0.12F, 0.8F, -15F);
		}
		poseStack.popPose();
	}

	private void renderPiece(BlockRenderDispatcher renderer, BlockState state, PoseStack poseStack, MultiBufferSource buffer, int light, float x, float y, float z, float sx, float sy, float sz, float rotation) {
		poseStack.pushPose();
		{
			poseStack.translate(x, y, z);
			poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
			poseStack.scale(sx, sy, sz);

			renderer.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
		}
		poseStack.popPose();
	}

	@Override
	public net.minecraft.resources.ResourceLocation getTextureLocation(IceBarrageControllerEntity entity) {
		return null;
	}
}