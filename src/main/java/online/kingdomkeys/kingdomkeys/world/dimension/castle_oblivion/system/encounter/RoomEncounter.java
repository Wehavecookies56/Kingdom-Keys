package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;

import java.util.List;
import java.util.Optional;

public class RoomEncounter extends JsonRegistryObject {
    Encounter encounter;
    List<ItemStack> rewards;
    Holder<SoundEvent> music;

    public static final Codec<RoomEncounter> CODEC = RecordCodecBuilder.create(roomEncounterInstance ->
            roomEncounterInstance.group(
                Encounter.CODEC.fieldOf("encounter").forGetter(RoomEncounter::getEncounter),
                ItemStack.CODEC.listOf().fieldOf("rewards").forGetter(RoomEncounter::getRewards),
                SoundEvent.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music))
            ).apply(roomEncounterInstance, RoomEncounter::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomEncounter(Encounter encounter, List<ItemStack> rewards, Optional<Holder<SoundEvent>> music) {
        this.encounter = encounter;
        this.rewards = rewards;
        this.music = music.orElse(null);
    }

    public Encounter getEncounter() {
        return encounter;
    }

    @SuppressWarnings("unchecked")
    public EncounterHandler<Encounter, EncounterState> getHandler() {
        return (EncounterHandler<Encounter, EncounterState>) encounter.type().handler();
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }
}
