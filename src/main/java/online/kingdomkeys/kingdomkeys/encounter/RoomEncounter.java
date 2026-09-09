package online.kingdomkeys.kingdomkeys.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
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
    List<ResourceLocation> requires;
    ResourceLocation grants;
    String info;

    public static final Codec<RoomEncounter> CODEC = RecordCodecBuilder.create(roomEncounterInstance ->
            roomEncounterInstance.group(
                Encounter.CODEC.fieldOf("encounter").forGetter(RoomEncounter::getEncounter),
                ItemStack.CODEC.listOf().optionalFieldOf("rewards", List.of()).forGetter(RoomEncounter::getRewards),
                SoundEvent.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music)),
                Codec.INT.optionalFieldOf("experience", 0).forGetter(RoomEncounter::getExperience),
                Codec.INT.optionalFieldOf("lux", 0).forGetter(RoomEncounter::getLux),
                Codec.INT.optionalFieldOf("arena_radius", DEFAULT_ARENA_RADIUS).forGetter(RoomEncounter::getArenaRadius),
                Codec.INT.optionalFieldOf("spawn_points", DEFAULT_SPAWN_POINTS).forGetter(RoomEncounter::getSpawnPoints),
                Codec.INT.optionalFieldOf("level", DYNAMIC_LEVEL).forGetter(RoomEncounter::getLevel),
                ResourceLocation.CODEC.listOf().optionalFieldOf("requires", List.of()).forGetter(RoomEncounter::getRequires),
                ResourceLocation.CODEC.optionalFieldOf("grants").forGetter(o -> Optional.ofNullable(o.grants)),
                Codec.STRING.optionalFieldOf("info").forGetter(o -> Optional.ofNullable(o.info))
            ).apply(roomEncounterInstance, RoomEncounter::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomEncounter(Encounter encounter, List<ItemStack> rewards, Optional<Holder<SoundEvent>> music, int experience, int lux, int arenaRadius, int spawnPoints, int level, List<ResourceLocation> requires, Optional<ResourceLocation> grants, Optional<String> info) {
        this.encounter = encounter;
        this.rewards = rewards;
        this.music = music.orElse(null);
        this.experience = experience;
        this.lux = lux;
        // Zero is a real answer: it means spawn on the spot rather than in a ring around it
        this.arenaRadius = Math.max(0, arenaRadius);
        this.spawnPoints = Math.max(1, spawnPoints);
        this.level = Math.max(DYNAMIC_LEVEL, level);
        this.requires = requires;
        this.grants = grants.orElse(null);
        this.info = info.orElse(null);
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

    public List<ResourceLocation> getRequires() {
        return requires;
    }

    public Optional<ResourceLocation> getGrants() {
        return Optional.ofNullable(grants);
    }

    public boolean canStart(PlayerData player) {
        return player != null && player.hasFlags(requires);
    }

    /**
    * Information plaque text at top-left
    * */
    public Optional<String> getInfo() {
        return Optional.ofNullable(info);
    }

    public String getTranslationKey() {
        return KingdomKeys.MODID+".encounter." + getRegistryName().getPath();
    }
}
