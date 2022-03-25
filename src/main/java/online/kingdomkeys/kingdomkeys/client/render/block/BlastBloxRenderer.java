package online.kingdomkeys.kingdomkeys.client.render.block;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;

/**
 * Mostly a copy of {@link net.minecraft.client.renderer.entity.TntRenderer} with some small changes
 */
@OnlyIn(Dist.CLIENT)
public class BlastBloxRenderer extends EntityRenderer<BlastBloxEntity> {

    public BlastBloxRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(BlastBloxEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, 0.5D, 0.0D);
        if ((float)entity.getFuse() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float f1 = 1.0F + f * 0.3F;
            matrixStack.scale(f1, f1, f1);
        }
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5D, -0.5D, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        renderFlash(ModBlocks.blastBlox.get().defaultBlockState(), matrixStack, buffer, packedLight, entity.getFuse() / 5 % 2 == 0);
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    public static void renderFlash(BlockState blockStateIn, PoseStack matrixStackIn, MultiBufferSource renderTypeBuffer, int combinedLight, boolean doFullBright) {
        int i;
        if (doFullBright) {
            i = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
        } else {
            i = OverlayTexture.NO_OVERLAY;
        }

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockStateIn, matrixStackIn, renderTypeBuffer, combinedLight, i);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(BlastBloxEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
