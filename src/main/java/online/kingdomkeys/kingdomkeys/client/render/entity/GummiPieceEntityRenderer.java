package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.GummiPieceEntity;

public class GummiPieceEntityRenderer extends EntityRenderer<GummiPieceEntity> {

	public GummiPieceEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(GummiPieceEntity entity, float yaw, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int light) {
		matrix.pushPose();
		{
			// Leaves the hangar the size of a dropped block and is full size as it lands
			float scale = Mth.lerp(entity.getProgress(partialTicks), GummiPieceEntity.START_SCALE, 1F);

			matrix.scale(scale, scale, scale);
			matrix.translate(-0.5, -0.5, -0.5);
			ClientUtils.renderSingleBlock(entity.getState(), matrix, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, 1F);
		}
		matrix.popPose();

		super.render(entity, yaw, partialTicks, matrix, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(GummiPieceEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
