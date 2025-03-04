package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Biome.class)
public class BiomeMixin {

    @Inject(method = "getBackgroundMusic", at = @At(value = "HEAD"), cancellable = true)
    public void replaceBiomeMusic(CallbackInfoReturnable<Optional<Music>> cir) {
        Holder<Biome> currentBiome = Minecraft.getInstance().level.getBiome(Minecraft.getInstance().player.blockPosition());
        if (currentBiome.isBound()) {
            if (currentBiome.is(new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_interior"))) {
                CastleOblivionCapabilities.ICastleOblivionInteriorCapability interiorData = ModCapabilities.getCastleOblivionInterior(Minecraft.getInstance().level);
                Floor floor = interiorData.getFloorAtPos(Minecraft.getInstance().level, Minecraft.getInstance().player.blockPosition());
                Room room = interiorData.getRoomAtPos(Minecraft.getInstance().level, Minecraft.getInstance().player.blockPosition());
                if (floor != null) {
                    SoundEvent music = null;
                    if (floor.getType().getMusic() != null) {
                        music = floor.getType().getMusic();
                    }
                    if (room != null && room.getType().getMusic() != null) {
                        music = room.getType().getMusic();
                    }
                    if (music != null) {
                        cir.setReturnValue(Optional.of(new Music(Holder.direct(music), 0, 0, true)));
                    } else {
                        cir.setReturnValue(Optional.of(new Music(Holder.direct(SoundEvents.MUSIC_GAME.get()), 12000, 24000, true)));
                    }
                }
            }
        }
    }

}
