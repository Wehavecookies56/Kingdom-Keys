package online.kingdomkeys.kingdomkeys.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The lightmap as the CPU last wrote it, one pixel per block and sky light pair.
 *
 * <p>Refreshed every frame before it is uploaded, so reading it gives exactly the colour the
 * shader is about to fetch for any given light level.</p>
 */
@Mixin(LightTexture.class)
public interface LightTextureAccessor {

    @Accessor("lightPixels")
    NativeImage kingdomKeys$getLightPixels();
}
