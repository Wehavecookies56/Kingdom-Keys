package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.entity.organization.ClaymoreDropCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.ClaymoreItem;

public class ClaymoreDropCoreEntityRenderer extends EntityRenderer<ClaymoreDropCoreEntity> {
	private final ItemRenderer itemRenderer;

	public ClaymoreDropCoreEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(ClaymoreDropCoreEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (!(entity.getOwner() instanceof Player caster))
			return;

		ItemStack heldStack = caster.getMainHandItem();
		if (!(heldStack.getItem() instanceof ClaymoreItem))
			return; // only shows up if actually wielding a compatible claymore

		poseStack.pushPose();
		{
			float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
			float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
			poseStack.mulPose(Axis.YP.rotationDegrees(180F - yaw));
			poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

			if (!entity.isPlanted()) {
				float spin = (entity.tickCount + partialTicks) * 18F;
				poseStack.mulPose(Axis.ZP.rotationDegrees(spin));
			}

			itemRenderer.renderStatic(heldStack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());
		}
		poseStack.popPose();

		super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(ClaymoreDropCoreEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}