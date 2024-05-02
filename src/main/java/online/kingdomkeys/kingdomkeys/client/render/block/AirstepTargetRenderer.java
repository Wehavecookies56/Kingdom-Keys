package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.block.AirStepTargetEntity;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;

public class AirstepTargetRenderer implements BlockEntityRenderer<AirStepTargetEntity> {

	public AirstepTargetRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(AirStepTargetEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        {
			/*if(tileEntityIn.getBlockPos().equals(ClientEvents.lockedAirStep)){
                ClientUtils.drawShotlockIndicator(ClientEvents.lockedAirStep,matrixStackIn,bufferIn,partialTicks);
			}*/
        }
        matrixStackIn.popPose();
    }
}
