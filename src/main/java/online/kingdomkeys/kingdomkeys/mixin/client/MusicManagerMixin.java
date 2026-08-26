package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MusicManager.class)
public class MusicManagerMixin {

    @Inject(method = "isPlayingMusic", at = @At("HEAD"), cancellable = true)
    public void isPlayingUnderWaterMusicCOOverride(Music selector, CallbackInfoReturnable<Boolean> cir) {
        if (selector == Musics.UNDER_WATER) {
            if (Minecraft.getInstance().level != null) {
                if (CastleOblivionHandler.isInterior(Minecraft.getInstance().level.dimension())) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

}
