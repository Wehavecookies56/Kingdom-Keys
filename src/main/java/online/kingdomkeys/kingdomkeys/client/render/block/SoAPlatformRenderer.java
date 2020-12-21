package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;

import java.util.HashSet;

public class SoAPlatformRenderer extends TileEntityRenderer<SoAPlatformTileEntity> {


    public SoAPlatformRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SoAPlatformTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.isMultiblockFormed()) {
            IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentBlockType());
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "block/station_of_awakening"));
            matrixStackIn.push();
            matrixStackIn.translate(0.5,0,0.5);
            matrixStackIn.scale(1.12F, 1, 1.12F);
            for (BakedQuad quad : model.getQuads(null, null, tileEntityIn.getWorld().getRandom(), EmptyModelData.INSTANCE)) {
                buffer.addVertexData(matrixStackIn.getLast(), quad, 1,1,1,1, combinedOverlayIn, combinedLightIn, true);
            }
            matrixStackIn.pop();
        }
    }

    @Override
    public boolean isGlobalRenderer(SoAPlatformTileEntity te) {
        return te.isMultiblockFormed();
    }
}
