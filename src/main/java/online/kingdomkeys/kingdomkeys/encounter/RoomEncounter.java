package online.kingdomkeys.kingdomkeys.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;

import java.util.List;
import java.util.Optional;

public class RoomEncounter extends JsonRegistryObject {

    public static final int DEFAULT_ARENA_RADIUS = 7;
    public static final int DEFAULT_SPAWN_POINTS = 8;

    /** A level of zero means whoever runs the fight decides it, from whoever is fighting it. */
    public static final int DYNAMIC_LEVEL = 0;

    Encounter encounter;
    List<ItemStack> rewards;
    Holder<SoundEvent> music;
    int experience;
    int lux;
    int arenaRadius;
    int spawnPoints;
    int level;

    public static final Codec<RoomEncounter> CODEC = RecordCodecBuilder.create(roomEncounterInstance ->
            roomEncounterInstance.group(
                Encounter.CODEC.fieldOf("encounter").forGetter(RoomEncounter::getEncounter),
                ItemStack.CODEC.listOf().optionalFieldOf("rewards", List.of()).forGetter(RoomEncounter::getRewards),
                SoundEvent.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music)),
                Codec.INT.optionalFieldOf("experience", 0).forGetter(RoomEncounter::getExperience),
                Codec.INT.optionalFieldOf("lux", 0).forGetter(RoomEncounter::getLux),
                Codec.INT.optionalFieldOf("arena_radius", DEFAULT_ARENA_RADIUS).forGetter(RoomEncounter::getArenaRadius),
                Codec.INT.optionalFieldOf("spawn_points", DEFAULT_SPAWN_POINTS).forGetter(RoomEncounter::getSpawnPoints),
                Codec.INT.optionalFieldOf("level", DYNAMIC_LEVEL).forGetter(RoomEncounter::getLevel)
            ).apply(roomEncounterInstance, RoomEncounter::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomEncounter(Encounter encounter, List<ItemStack> rewards, Optional<Holder<SoundEvent>> music, int experience, int lux, int arenaRadius, int spawnPoints, int level) {
        this.encounter = encounter;
        this.rewards = rewards;
        this.music = music.orElse(null);
        this.experience = experience;
        this.lux = lux;
        this.arenaRadius = Math.max(2, arenaRadius);
        this.spawnPoints = Math.max(1, spawnPoints);
        this.level = Math.max(DYNAMIC_LEVEL, level);
    }

    public Encounter getEncounter() {
        return encounter;
    }

    @SuppressWarnings("unchecked")
    public EncounterHandler<Encounter, Encounter.State> getHandler() {
        return (EncounterHandler<Encounter, Encounter.State>) encounter.type().handler();
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public Optional<SoundEvent> getMusic() {
        return music == null ? Optional.empty() : Optional.of(music.value());
    }

    public int getExperience() {
        return experience;
    }

    public int getLux() {
        return lux;
    }

    public int getArenaRadius() {
        return arenaRadius;
    }

    public int getSpawnPoints() {
        return spawnPoints;
    }

    public int getLevel() {
        return level;
    }

    public boolean isDynamicLevel() {
        return level <= DYNAMIC_LEVEL;
    }

    public String getTranslationKey() {
        return KingdomKeys.MODID+".encounter." + getRegistryName().getPath();
    }
}
