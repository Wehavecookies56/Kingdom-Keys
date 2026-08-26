package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MoogleProjectorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

public class MoogleProjectorRenderer implements BlockEntityRenderer<MoogleProjectorTileEntity> {

	private MoogleEntity fakeMoogle;

	public MoogleProjectorRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MoogleProjectorTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        {
	        Minecraft mc = Minecraft.getInstance();
	        MoogleEntity moogle = getFakeMoogle(mc, tileEntityIn);
	        EntityRenderer<MoogleEntity> moogleRenderer = (EntityRenderer<MoogleEntity>) mc.getEntityRenderDispatcher().getRenderer(moogle);
	        Vec3 vec3d = moogleRenderer.getRenderOffset(moogle, partialTicks);
	        matrixStackIn.translate(0.5 + vec3d.x(), 0.0 + vec3d.y(), 0.5 + vec3d.z());
        	RenderSystem.enableBlend();
        	{
                moogleRenderer.render(moogle, 0, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        	}	        
        }
        matrixStackIn.popPose();
    }

    private MoogleEntity getFakeMoogle(Minecraft mc, MoogleProjectorTileEntity projector) {
        if (fakeMoogle == null || fakeMoogle.level() != mc.level) {
            fakeMoogle = new MoogleEntity(ModEntities.TYPE_MOOGLE.get(), mc.level);
            fakeMoogle.setFakeMoogle(true);
        }

        BlockPos pos = projector.getBlockPos();
        fakeMoogle.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        return fakeMoogle;
    }
}
