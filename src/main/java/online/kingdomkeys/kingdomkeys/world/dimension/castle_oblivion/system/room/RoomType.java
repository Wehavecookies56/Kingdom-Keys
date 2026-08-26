package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.util.Codecs;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomEncounters;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomType extends JsonRegistryObject {

    private final boolean entranceHall;
    @NotNull private final RoomSize size;
    @NotNull private final RoomCategory category;
    @NotNull private final Enemies enemies;
    @Nullable private final Color colour;
    @NotNull private final List<RoomModifier> modifiers;
    @NotNull private final List<ResourceLocation> compatibleFloors;
    @Nullable private final ResourceLocation fixedRoom;
    @Nullable private final Holder<SoundEvent> music;
    @Nullable private final ResourceLocation encounter;
    @Nullable private final Treasure treasure;

    public static final Codec<RoomType> CODEC = RecordCodecBuilder.create(roomTypeInstance ->
        roomTypeInstance.group(
                StringRepresentable.fromEnum(RoomSize::values).fieldOf("size").forGetter(RoomType::getSize),
                StringRepresentable.fromEnum(RoomCategory::values).fieldOf("category").forGetter(RoomType::getCategory),
                Enemies.CODEC.optionalFieldOf("enemies", new Enemies(RoomEnemies.NONE, 0, 0)).forGetter(RoomType::getEnemiesProperties),
                Codec.BOOL.optionalFieldOf("entrance_hall", false).forGetter(RoomType::isEntranceHall),
                Codecs.COLOR_CODEC_HEX.optionalFieldOf("colour").forGetter(o -> Optional.ofNullable(o.getColour())),
                RoomModifier.CODEC.listOf().optionalFieldOf("modifiers", new ArrayList<>()).forGetter(o -> o.modifiers),
                ResourceLocation.CODEC.listOf().optionalFieldOf("compatible", new ArrayList<>()).forGetter(o -> o.compatibleFloors),
                ResourceLocation.CODEC.optionalFieldOf("fixed_room").forGetter(o -> Optional.ofNullable(o.fixedRoom)),
                SoundEvent.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music)),
                ResourceLocation.CODEC.optionalFieldOf("encounter").forGetter(o -> Optional.ofNullable(o.encounter)),
                Treasure.CODEC.optionalFieldOf("treasure").forGetter(o -> Optional.ofNullable(o.treasure))
        ).apply(roomTypeInstance, RoomType::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomType(@NotNull RoomSize size, @NotNull RoomCategory category, @NotNull Enemies enemies, boolean entranceHall, Optional<Color> colour, @NotNull List<RoomModifier> modifiers, @NotNull List<ResourceLocation> compatibleFloors, Optional<ResourceLocation> fixedRoom, Optional<Holder<SoundEvent>> music, Optional<ResourceLocation> encounter, Optional<Treasure> treasure) {
        this.entranceHall = entranceHall;
        this.size = size;
        this.category = category;
        this.enemies = enemies;
        this.colour = colour.orElse(null);
        this.modifiers = modifiers;
        this.compatibleFloors = compatibleFloors;
        this.fixedRoom = fixedRoom.orElse(null);
        this.music = music.orElse(null);
        this.encounter = encounter.orElse(null);
        this.treasure = treasure.orElse(null);
    }

    public String getTranslationKey() {
        return "room." + registryName.getPath();
    }

    public MutableComponent getName(RoomData room) {
        return isEntranceHall() ? Component.translatable(getTranslationKey(), room.getParentID() + 1) : Component.translatable(getTranslationKey());
    }

    public boolean isEntranceHall() {
        return entranceHall;
    }

    public @NotNull RoomSize getSize() {
        return size;
    }

    public Enemies getEnemiesProperties() {
        return enemies;
    }

    public RoomEnemies getEnemies() {
        return enemies.roomEnemies;
    }

    public @NotNull RoomCategory getCategory() {
        return category;
    }

    public @Nullable Color getColour() {
        return colour;
    }

    public @NotNull List<RoomModifier> getModifiers() {
        return modifiers;
    }

    public boolean isFloorCompatible(FloorType floor) {
        if (compatibleFloors.isEmpty()) {
            return true;
        } else {
            return compatibleFloors.contains(floor.getRegistryName());
        }
    }

    public List<FloorType> getCompatibleFloors() {
        return compatibleFloors.stream().map(resourceLocation -> ModFloorTypes.registry.get().getValue(resourceLocation)).toList();
    }

    public Optional<RoomEncounter> getEncounter() {
        return Optional.ofNullable(ModRoomEncounters.registry.get().getValue(encounter));
    }

    public Optional<RoomStructure> getFixedRoom() {
        return Optional.ofNullable(ModRoomStructures.registry.get().getValue(fixedRoom));
    }

    public Optional<Treasure> getTreasure() {
        return Optional.ofNullable(treasure);
    }

    public SoundEvent getMusic() {
        if (music != null) {
            return music.value();
        } else {
            return null;
        }
    }

    public int getNumberOfEnemies() {
        return enemies.numberOfEnemies;
    }

    public int getSimultaneousEnemies() {
        return enemies.simultaneousEnemies;
    }

    public TagKey<EntityType<?>> getRegularEnemies() {
        return enemies.regularEnemies;
    }

    public TagKey<EntityType<?>> getStrongEnemies() {
        return enemies.strongEnemies;
    }

    public record Treasure(ResourceLocation lootTable, int count, int trappedCount, TagKey<EntityType<?>> trappedEntities) {
        public static final Codec<Treasure> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("treasure").forGetter(Treasure::lootTable),
                        Codec.INT.fieldOf("count").forGetter(Treasure::count),
                        Codec.INT.optionalFieldOf("trapped_count", 0).forGetter(Treasure::trappedCount),
                        TagKey.hashedCodec(Registries.ENTITY_TYPE).optionalFieldOf("enemy").forGetter(o -> Optional.ofNullable(o.trappedEntities))
                ).apply(instance, Treasure::new)
        );

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public Treasure(ResourceLocation lootTable, int count, int trappedCount, Optional<TagKey<EntityType<?>>> trappedEntities) {
            this(lootTable, count, trappedCount, trappedEntities.orElse(null));
        }

        public Treasure(ResourceLocation lootTable, int count) {
            this(lootTable, count, 0, (TagKey<EntityType<?>>) null);
        }

    }

    public record Enemies(RoomEnemies roomEnemies, int numberOfEnemies, int simultaneousEnemies, TagKey<EntityType<?>> regularEnemies, TagKey<EntityType<?>> strongEnemies) {

        public static final Codec<Enemies> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        StringRepresentable.fromEnum(RoomEnemies::values).fieldOf("type").forGetter(Enemies::roomEnemies),
                        Codec.INT.fieldOf("number_of_enemies").forGetter(Enemies::numberOfEnemies),
                        Codec.INT.fieldOf("simultaneous_enemies").forGetter(Enemies::simultaneousEnemies),
                        TagKey.hashedCodec(Registries.ENTITY_TYPE).optionalFieldOf("regular_enemies").forGetter(o -> Optional.ofNullable(o.regularEnemies)),
                        TagKey.hashedCodec(Registries.ENTITY_TYPE).optionalFieldOf("strong_enemies").forGetter(o -> Optional.ofNullable(o.strongEnemies))
                ).apply(instance, Enemies::new)
        );


        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public Enemies(RoomEnemies roomEnemies, int numberOfEnemies, int simultaneousEnemies, Optional<TagKey<EntityType<?>>> regularEnemies, Optional<TagKey<EntityType<?>>> strongEnemies) {
            this(roomEnemies, numberOfEnemies, simultaneousEnemies, regularEnemies.orElse(null), strongEnemies.orElse(null));
        }

        public Enemies(RoomEnemies roomEnemies, int numberOfEnemies, int simultaneousEnemies) {
            this(roomEnemies, numberOfEnemies, simultaneousEnemies, (TagKey<EntityType<?>>) null, null);
        }
    }
}
