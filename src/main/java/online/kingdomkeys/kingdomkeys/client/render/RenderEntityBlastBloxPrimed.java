package online.kingdomkeys.kingdomkeys.client.render;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.EntityBlastBloxPrimed;

/**
 * Mostly a copy of {@link net.minecraft.client.renderer.entity.TNTRenderer} with some small changes
 */
@OnlyIn(Dist.CLIENT)
public class RenderEntityBlastBloxPrimed extends EntityRenderer<EntityBlastBloxPrimed> {

    public static final Factory FACTORY = new RenderEntityBlastBloxPrimed.Factory();

    public RenderEntityBlastBloxPrimed(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityBlastBloxPrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockState blastBloxState = ModBlocks.blastBlox.getDefaultState();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)x, (float)y + 0.5f, (float)z);
        float f2;
        if ((float)entity.getFuse() - partialTicks + 1.0F < 10.0F) {
            f2 = 1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 10.0F;
            f2 = MathHelper.clamp(f2, 0.0F, 1.0F);
            f2 *= f2;
            f2 *= f2;
            float f1 = 1.0F + f2 * 0.3F;
            GlStateManager.scalef(f1, f1, f1);
        }

        f2 = (1.0F - ((float)entity.getFuse() - partialTicks + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
        brd.renderBlockBrightness(blastBloxState, entity.getBrightness());
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
            brd.renderBlockBrightness(blastBloxState, 1.0F);
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        } else if (entity.getFuse() / 5 % 2 == 0) {
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.polygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            brd.renderBlockBrightness(blastBloxState, 1.0F);
            GlStateManager.polygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBlastBloxPrimed entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityBlastBloxPrimed> {
        @Override
        public EntityRenderer<? super EntityBlastBloxPrimed> createRenderFor(EntityRendererManager entityRendererManager) {
            return new RenderEntityBlastBloxPrimed(entityRendererManager);
        }
    }
}
