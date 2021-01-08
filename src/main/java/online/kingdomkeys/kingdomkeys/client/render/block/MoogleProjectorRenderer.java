package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.client.render.entity.MoogleRenderer;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.MoogleProjectorTileEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

public class MoogleProjectorRenderer extends TileEntityRenderer<MoogleProjectorTileEntity> {
    public MoogleProjectorRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(MoogleProjectorTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        {
	        Minecraft mc = Minecraft.getInstance();
	        //mc.getRenderManager().renderEntityStatic(new MoogleEntity(ModEntities.TYPE_MOOGLE.get(), (World)mc.world), 0.5, 0.5, 0.5, 0, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
	        MoogleEntity fakeMoogle = new MoogleEntity(ModEntities.TYPE_MOOGLE.get(), (World)mc.world);
	        fakeMoogle.setFakeMoogle(true);
	        EntityRenderer<MoogleEntity> moogleRenderer = (EntityRenderer<MoogleEntity>) mc.getRenderManager().getRenderer(fakeMoogle);
	        Vector3d vec3d = moogleRenderer.getRenderOffset(fakeMoogle, partialTicks);
	        matrixStackIn.translate(0.5 + vec3d.getX(), 0.0 + vec3d.getY(), 0.5 + vec3d.getZ());
	        RenderSystem.enableAlphaTest();
	        {
	        	RenderSystem.enableBlend();
	        	{
	                moogleRenderer.render(fakeMoogle, 0, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
	        	}
	        }
        }
        matrixStackIn.pop();
    }
}
