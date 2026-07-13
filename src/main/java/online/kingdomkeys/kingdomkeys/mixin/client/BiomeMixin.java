package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.EncounterInstance;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import org.jetbrains.annotations.Nullable;
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
            if (currentBiome.is(KingdomKeys.rl("castle_oblivion_interior"))) {
                CastleOblivionData.InteriorData.getClient(Minecraft.getInstance().level).ifPresent(interiorData -> {
                    if (!interiorData.getFloors().isEmpty()) {
                        Floor floor = interiorData.getFloorAtPos(Minecraft.getInstance().player.blockPosition());
                        Room room = interiorData.getRoomAtPos(Minecraft.getInstance().player.blockPosition());
                        if (floor != null) {
                            SoundEvent music = getSoundEvent(floor, room);
                            if (music != null) {
                                cir.setReturnValue(Optional.of(new Music(Holder.direct(music), 0, 0, true)));
                            } else {
                                cir.setReturnValue(Optional.of(new Music(Holder.direct(SoundEvents.MUSIC_GAME.value()), 12000, 24000, true)));
                            }
                        }
                    }
                });
            }
        }
    }

    private static @Nullable SoundEvent getSoundEvent(Floor floor, Room room) {
        SoundEvent music = null;
        if (floor.getType().getMusic() != null) {
            music = floor.getType().getMusic();
        }
        if (room != null) {
            if (room.getType().getMusic() != null) {
                music = room.getType().getMusic();
            }
            if (room.getEncounter().isPresent()) {
                EncounterInstance encounterInstance = room.getEncounter().get();
                if (!encounterInstance.isComplete()) {
                    if (encounterInstance.getEncounter().getMusic().isPresent()) {
                        music = encounterInstance.getEncounter().getMusic().get();
                    }
                }
            }
        }
        return music;
    }
}
