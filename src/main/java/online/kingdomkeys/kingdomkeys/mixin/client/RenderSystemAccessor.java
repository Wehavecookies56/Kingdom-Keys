package online.kingdomkeys.kingdomkeys.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The two directions entity lighting comes from, which vanilla sets but never hands back.
 *
 * <p>Needed by {@link online.kingdomkeys.kingdomkeys.client.render.LargeItemModels}, which draws
 * normals that were baked once in the model's own space and so has to turn the lights round into
 * that space instead.</p>
 */
@Mixin(RenderSystem.class)
public interface RenderSystemAccessor {

    @Accessor("shaderLightDirections")
    static Vector3f[] kingdomKeys$getShaderLightDirections() {
        throw new AssertionError();
    }
}
