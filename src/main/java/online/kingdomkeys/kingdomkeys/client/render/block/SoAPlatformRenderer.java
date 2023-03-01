package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.block.SoAPlatformTileEntity;

public class SoAPlatformRenderer implements BlockEntityRenderer<SoAPlatformTileEntity> {


    public SoAPlatformRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(SoAPlatformTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.isMultiblockFormed()) {
            VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "block/station_of_awakening"));
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5,0.026,0.5);
            matrixStackIn.scale(1.12F, 0.975F, 1.12F);
            for (BakedQuad quad : model.getQuads(null, null, tileEntityIn.getLevel().getRandom(), EmptyModelData.INSTANCE)) {
                buffer.putBulkData(matrixStackIn.last(), quad, 1,1,1,1, combinedOverlayIn, combinedLightIn, true);
            }
            matrixStackIn.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen(SoAPlatformTileEntity te) {
        return te.isMultiblockFormed();
    }
}
