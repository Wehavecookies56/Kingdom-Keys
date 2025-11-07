package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import online.kingdomkeys.kingdomkeys.entity.block.GummiCoreTileEntity;


    public class GummiCoreRenderer implements BlockEntityRenderer<GummiCoreTileEntity> {

        public GummiCoreRenderer(BlockEntityRendererProvider.Context context) {

        }

        @Override
        public void render(GummiCoreTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
            matrixStackIn.pushPose();
            {
                //TODO special effect maybe?
            }
            matrixStackIn.popPose();
        }
    }
