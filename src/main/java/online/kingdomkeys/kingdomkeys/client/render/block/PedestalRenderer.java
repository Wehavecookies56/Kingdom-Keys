package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
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

	    tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(iih -> {
			if (!iih.getStackInSlot(0).isEmpty()) {
				matrixStackIn.push();
				{
					RenderSystem.color4f(1, 1, 1, 1);
					float height, rotation;
					if (!tileEntityIn.isPaused()) {
						float lerpedTicks = tileEntityIn.previousTicks + (tileEntityIn.ticksExisted() - tileEntityIn.previousTicks) * partialTicks;
						height = tileEntityIn.getBaseHeight() + (0.1F * (float) Math.sin(tileEntityIn.getBobSpeed() * lerpedTicks));
						rotation = lerpedTicks * tileEntityIn.getRotationSpeed() % 360F;
						tileEntityIn.setCurrentTransforms(rotation, height);
					} else {
						height = tileEntityIn.getSavedHeight();
						rotation = tileEntityIn.getSavedRotation();
					}

					matrixStackIn.translate(0.5F, height, 0.5F);
					matrixStackIn.rotate(new Quaternion(new Vector3f(0, 1, 0), rotation, true));
					matrixStackIn.scale(tileEntityIn.getScale(), tileEntityIn.getScale(), tileEntityIn.getScale());
					ItemStack item = iih.getStackInSlot(0).getItem() instanceof KeychainItem ? new ItemStack( ((KeychainItem)iih.getStackInSlot(0).getItem()).getKeyblade() ) : iih.getStackInSlot(0);
					renderItem.renderItem(item, TransformType.FIXED, 100, 655360, matrixStackIn, bufferIn);
				}
				matrixStackIn.pop();
			}
		});


	}
}
