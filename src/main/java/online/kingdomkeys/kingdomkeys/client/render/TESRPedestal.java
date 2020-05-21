package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import online.kingdomkeys.kingdomkeys.container.PedestalInventory;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class TESRPedestal <T extends TileEntity> extends TileEntityRenderer<T> {

    private ItemRenderer renderItem;

    public TESRPedestal(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
	    this.renderItem = Minecraft.getInstance().getItemRenderer();

		matrixStackIn.push();
		{
			PedestalInventory inv = ((PedestalTileEntity)tileEntityIn).getInv();
			//System.out.println(inv.getStackInSlot(0));
			RenderSystem.color4f(1, 1, 1,1);
			matrixStackIn.translate(0.5, 1.25, 0.5);
		    renderItem.renderItem(new ItemStack(ModItems.ultimaWeaponKH3.get()), TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
		}
        matrixStackIn.pop();
	}
}
