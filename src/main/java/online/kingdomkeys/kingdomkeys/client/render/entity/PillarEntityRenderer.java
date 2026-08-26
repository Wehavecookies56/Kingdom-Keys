package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.entity.organization.PillarEntity;

public class PillarEntityRenderer extends EntityRenderer<PillarEntity> {

	private static final int SEGMENTS = 7;
	private static final float TOP_SCALE = 0.18F;

	public PillarEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(PillarEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		BlockState state = entity.getPillarBlockState();
		if (state.isAir())
			return;

		float height = entity.getPillarHeight();
		float radius = entity.getPillarRadius();
		float segmentHeight = height / SEGMENTS;

		BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
		RenderType renderType = ItemBlockRenderTypes.getRenderType(state, false);

		for (int i = 0; i < SEGMENTS; i++) {
			float t = i / (float) (SEGMENTS - 1); // 0 at the base, 1 at the very tip
			float scale = Mth.lerp(t, radius * 2F, radius * 2F * TOP_SCALE);

			poseStack.pushPose();
			// Each segment sits right on top of the last, growing thinner - shrinking around its own center rather than a corner, so the spike tapers evenly instead of leaning to one side.
			poseStack.translate(0.5D - scale / 2D, i * segmentHeight, 0.5D - scale / 2D);
			poseStack.scale(scale, segmentHeight * 1.05F, scale);

			blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);

			poseStack.popPose();
		}

		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(PillarEntity entity) {
		return net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;
	}
}
