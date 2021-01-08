package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.CapabilityItemHandler;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class PedestalRenderer extends TileEntityRenderer<PedestalTileEntity> {

    private ItemRenderer renderItem;

    public PedestalRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(PedestalTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
	    this.renderItem = Minecraft.getInstance().getItemRenderer();

	    if (!tileEntityIn.isStationOfAwakeningMarker()) {
			tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(iih -> {
				if (!iih.getStackInSlot(0).isEmpty()) {
					renderItem(tileEntityIn, matrixStackIn, bufferIn, partialTicks, iih.getStackInSlot(0).getItem() instanceof KeychainItem ? new ItemStack(((KeychainItem) iih.getStackInSlot(0).getItem()).getKeyblade()) : iih.getStackInSlot(0));
				}
			});
		} else {
	    	if (!tileEntityIn.hide) {
				renderItem(tileEntityIn, matrixStackIn, bufferIn, partialTicks, tileEntityIn.getDisplayStack());
			}
		}
	}

	private void renderItem(PedestalTileEntity tileEntity, MatrixStack matrixStack, IRenderTypeBuffer buffer, float partialTicks, ItemStack toRender) {
		matrixStack.push();
		{
			RenderSystem.color4f(1, 1, 1, 1);
			float height, rotation;
			if (!tileEntity.isPaused()) {
				float lerpedTicks = tileEntity.previousTicks + (tileEntity.ticksExisted() - tileEntity.previousTicks) * partialTicks;
				height = tileEntity.getBaseHeight() + (0.1F * (float) Math.sin(tileEntity.getBobSpeed() * lerpedTicks));
				rotation = lerpedTicks * tileEntity.getRotationSpeed() % 360F;
				tileEntity.setCurrentTransforms(rotation, height);
			} else {
				height = tileEntity.getSavedHeight();
				rotation = tileEntity.getSavedRotation();
			}

			matrixStack.translate(0.5F, height, 0.5F);
			matrixStack.rotate(new Quaternion(new Vector3f(0, 1, 0), rotation, true));
			matrixStack.scale(tileEntity.getScale(), tileEntity.getScale(), tileEntity.getScale());
			renderItem.renderItem(toRender, TransformType.FIXED, 100, 655360, matrixStack, buffer);
		}
		matrixStack.pop();
	}
}
