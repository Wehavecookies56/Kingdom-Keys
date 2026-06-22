package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
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
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.*;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomType extends JsonRegistryObject {

    private final boolean entranceHall;
    private final RoomSize size;
    private final RoomCategory category;
    @Nullable private final RoomEnemies enemies;
    @Nullable private final Color colour;
    @Nullable private final List<ResourceLocation> modifiers;
    @Nullable private final List<ResourceLocation> compatibleFloors;
    @Nullable private final ResourceLocation fixedRoom;
    @Nullable private final Holder<SoundEvent> music;
    private final int numberOfEnemies;
    private final int simultaneousEnemies;
    @Nullable private final TagKey<EntityType<?>> regularEnemies;
    @Nullable private final TagKey<EntityType<?>> strongEnemies;
    @Nullable private final ResourceLocation encounter;

    public static final Codec<RoomType> CODEC = RecordCodecBuilder.create(roomTypeInstance ->
        roomTypeInstance.group(
                StringRepresentable.fromEnum(RoomSize::values).fieldOf("size").forGetter(RoomType::getSize),
                StringRepresentable.fromEnum(RoomCategory::values).fieldOf("category").forGetter(RoomType::getCategory),
                StringRepresentable.fromEnum(RoomEnemies::values).optionalFieldOf("enemies").forGetter(o -> Optional.ofNullable(o.getEnemies())),
                Codec.BOOL.optionalFieldOf("entrance_hall").forGetter(o -> Optional.of(o.isEntranceHall())),
                Codecs.COLOR_CODEC_HEX.optionalFieldOf("colour").forGetter(o -> Optional.ofNullable(o.getColour())),
                ResourceLocation.CODEC.listOf().optionalFieldOf("modifiers").forGetter(o -> Optional.ofNullable(o.modifiers)),
                ResourceLocation.CODEC.listOf().optionalFieldOf("compatible").forGetter(o -> Optional.ofNullable(o.compatibleFloors)),
                ResourceLocation.CODEC.optionalFieldOf("fixed_room").forGetter(o -> Optional.ofNullable(o.fixedRoom)),
                SoundEvent.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music)),
                Codec.INT.optionalFieldOf("number_of_enemies").forGetter(o -> Optional.of(o.getNumberOfEnemies())),
                Codec.INT.optionalFieldOf("simultaneous_enemies").forGetter(o -> Optional.of(o.getSimultaneousEnemies())),
                TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("regular_enemies").forGetter(o -> Optional.ofNullable(o.getRegularEnemies())),
                TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("strong_enemies").forGetter(o -> Optional.ofNullable(o.getStrongEnemies())),
                ResourceLocation.CODEC.optionalFieldOf("encounter").forGetter(o -> Optional.ofNullable(o.encounter))
        ).apply(roomTypeInstance, RoomType::new)
    );


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomType(RoomSize size, RoomCategory category, Optional<RoomEnemies> enemies, Optional<Boolean> entranceHall, Optional<Color> colour, Optional<List<ResourceLocation>> modifiers, Optional<List<ResourceLocation>> compatibleFloors, Optional<ResourceLocation> fixedRoom, Optional<Holder<SoundEvent>> music, Optional<Integer> numberOfEnemies, Optional<Integer> simultaneousEnemies, Optional<TagKey<EntityType<?>>> regularEnemies, Optional<TagKey<EntityType<?>>> strongEnemies, Optional<ResourceLocation> encounter) {
        this.entranceHall = entranceHall.orElse(false);
        this.size = size;
        this.category = category;
        this.enemies = enemies.orElse(RoomEnemies.NONE);
        this.colour = colour.orElse(null);
        this.modifiers = modifiers.orElse(new ArrayList<>());
        this.compatibleFloors = compatibleFloors.orElse(new ArrayList<>());
        this.fixedRoom = fixedRoom.orElse(null);
        this.music = music.orElse(null);
        this.numberOfEnemies = numberOfEnemies.orElse(0);
        this.simultaneousEnemies = simultaneousEnemies.orElse(0);
        this.regularEnemies = regularEnemies.orElse(null);
        this.strongEnemies = strongEnemies.orElse(null);
        this.encounter = encounter.orElse(null);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
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

    public RoomSize getSize() {
        return size;
    }

    public RoomEnemies getEnemies() {
        return enemies;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public Color getColour() {
        return colour;
    }

    public List<RoomModifier> getModifiers() {
        return modifiers.stream().map(resourceLocation -> ModRoomModifiers.registry.get(resourceLocation)).toList();
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

    public SoundEvent getMusic() {
        if (music != null) {
            return music.value();
        } else {
            return null;
        }
    }

    public int getNumberOfEnemies() {
        return numberOfEnemies;
    }

    public int getSimultaneousEnemies() {
        return simultaneousEnemies;
    }

    public TagKey<EntityType<?>> getRegularEnemies() {
        return regularEnemies;
    }

    public TagKey<EntityType<?>> getStrongEnemies() {
        return strongEnemies;
    }
}
