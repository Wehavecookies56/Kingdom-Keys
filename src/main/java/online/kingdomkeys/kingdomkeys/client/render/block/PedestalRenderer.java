package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

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
					float height = 1.25F + (0.1F * (float)Math.sin(0.02F * tileEntityIn.ticksExisted()));
					matrixStackIn.translate(0.5, height, 0.5);
					float rotation = tileEntityIn.ticksExisted()  * 0.6F % 360F;
					matrixStackIn.rotate(new Quaternion(new Vector3f(0, 1, 0), rotation, true));
					renderItem.renderItem(iih.getStackInSlot(0), TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
				}
				matrixStackIn.pop();
			}
		});


	}
}
