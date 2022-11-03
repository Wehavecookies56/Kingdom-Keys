package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MoogleProjectorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

public class MoogleProjectorRenderer implements BlockEntityRenderer<MoogleProjectorTileEntity> {

	public MoogleProjectorRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(MoogleProjectorTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        {
	        Minecraft mc = Minecraft.getInstance();
	        //mc.getRenderManager().renderEntityStatic(new MoogleEntity(ModEntities.TYPE_MOOGLE.get(), (World)mc.world), 0.5, 0.5, 0.5, 0, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
	        MoogleEntity fakeMoogle = new MoogleEntity(ModEntities.TYPE_MOOGLE.get(), (Level)mc.level);
	        fakeMoogle.setFakeMoogle(true);
	        EntityRenderer<MoogleEntity> moogleRenderer = (EntityRenderer<MoogleEntity>) mc.getEntityRenderDispatcher().getRenderer(fakeMoogle);
	        Vec3 vec3d = moogleRenderer.getRenderOffset(fakeMoogle, partialTicks);
	        matrixStackIn.translate(0.5 + vec3d.x(), 0.0 + vec3d.y(), 0.5 + vec3d.z());
        	RenderSystem.enableBlend();
        	{
                moogleRenderer.render(fakeMoogle, 0, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
        	}	        
        }
        matrixStackIn.popPose();
    }
}
