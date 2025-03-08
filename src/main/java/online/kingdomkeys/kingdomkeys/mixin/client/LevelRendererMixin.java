package online.kingdomkeys.kingdomkeys.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.render.KingdomHeartsSkyRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow protected abstract boolean doesMobEffectBlockSky(Camera pCamera);

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Nullable private ClientLevel level;

    @Inject(method = "renderSky", at = @At(value = "TAIL"))
    public void renderKH(PoseStack pPoseStack, Matrix4f pProjectionMatrix, float pPartialTick, Camera pCamera, boolean pIsFoggy, Runnable pSkyFogSetup, CallbackInfo ci) {
        if (!pIsFoggy) {
            FogType fogtype = pCamera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !this.doesMobEffectBlockSky(pCamera)) {
                if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL) {
                    KingdomHeartsSkyRenderer.renderSky(level, pPoseStack, pProjectionMatrix, pPartialTick, pCamera, pSkyFogSetup);
                }
            }
        }
    }

}
