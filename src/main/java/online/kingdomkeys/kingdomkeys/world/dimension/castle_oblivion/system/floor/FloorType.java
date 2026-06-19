package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Codecs;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloorType extends JsonRegistryObject {

    private final int critPathLength;
    private final CountChancePair bonusRooms, branches;
    private final Color floorColour;
    @Nullable private final ResourceLocation music;
    private final List<ResourceLocation> roomBlacklist;
    @Nullable private final ResourceLocation startingRoom;
    @Nullable private final ResourceLocation fixedLayout;
    private final List<ResourceLocation> globalModifiers;
    @Nullable private final TagKey<EntityType<?>> regularEnemies;
    @Nullable private final TagKey<EntityType<?>> strongEnemies;

    public static final Codec<FloorType> CODEC = RecordCodecBuilder.create(floorTypeInstance ->
        floorTypeInstance.group(
                Codec.INT.fieldOf("crit_path_length").forGetter(FloorType::getCritPathLength),
                Codecs.COLOR_CODEC_HEX.fieldOf("colour").forGetter(FloorType::getFloorColour),
                CountChancePair.CODEC.optionalFieldOf("bonus_rooms").forGetter(o -> Optional.ofNullable(o.getBonusRooms())),
                CountChancePair.CODEC.optionalFieldOf("branches").forGetter(o -> Optional.ofNullable(o.getBranches())),
                ResourceLocation.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music)),
                ResourceLocation.CODEC.listOf().optionalFieldOf("room_blacklist").forGetter(o -> Optional.ofNullable(o.roomBlacklist)),
                ResourceLocation.CODEC.optionalFieldOf("starting_room").forGetter(o -> Optional.ofNullable(o.startingRoom)),
                ResourceLocation.CODEC.optionalFieldOf("fixed_layout").forGetter(o -> Optional.ofNullable(o.fixedLayout)),
                ResourceLocation.CODEC.listOf().optionalFieldOf("modifiers").forGetter(o -> Optional.ofNullable(o.globalModifiers)),
                TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("regular_enemies").forGetter(o -> Optional.ofNullable(o.getRegularEnemies())),
                TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("strong_enemies").forGetter(o -> Optional.ofNullable(o.getStrongEnemies()))
                ).apply(floorTypeInstance, FloorType::new)
    );

    public record CountChancePair(int count, int chance) {
        public static final Codec<CountChancePair> CODEC = RecordCodecBuilder.create(countChancePairInstance ->
                countChancePairInstance.group(
                    Codec.INT.fieldOf("count").forGetter(CountChancePair::count),
                    Codec.INT.fieldOf("chance").forGetter(CountChancePair::chance)
                ).apply(countChancePairInstance, CountChancePair::new)
        );
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public FloorType(int critPathLength, Color floorColour, Optional<CountChancePair> bonusRooms, Optional<CountChancePair> branches, Optional<ResourceLocation> music, Optional<List<ResourceLocation>> roomBlacklist, Optional<ResourceLocation> startingRoom, Optional<ResourceLocation> fixedLayout, Optional<List<ResourceLocation>> globalModifiers, Optional<TagKey<EntityType<?>>> regularEnemies, Optional<TagKey<EntityType<?>>> strongEnemies) {
        this.critPathLength = critPathLength;
        this.floorColour = floorColour;
        this.bonusRooms = bonusRooms.orElse(new CountChancePair(0, 0));
        this.branches = branches.orElse(new CountChancePair(0, 0));
        this.music = music.orElse(null);
        this.roomBlacklist = roomBlacklist.orElse(new ArrayList<>());
        this.startingRoom = startingRoom.orElse(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "unknown_room"));
        this.fixedLayout = fixedLayout.orElse(null);
        this.globalModifiers = globalModifiers.orElse(new ArrayList<>());
        this.regularEnemies = regularEnemies.orElse(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "castle_oblivion/regular_enemies")));
        this.strongEnemies = strongEnemies.orElse(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "castle_oblivion/strong_enemies")));
    }

    public int getCritPathLength() {
        return critPathLength;
    }

    public CountChancePair getBonusRooms() {
        return bonusRooms;
    }

    public CountChancePair getBranches() {
        return branches;
    }

    public Color getFloorColour() {
        return floorColour;
    }

    public List<RoomType> getRoomBlacklist() {
        return roomBlacklist.stream().map(resourceLocation -> ModRoomTypes.registry.get().getValue(resourceLocation)).toList();
    }

    public List<RoomModifier> getGlobalModifiers() {
        return globalModifiers.stream().map(resourceLocation -> ModRoomModifiers.registry.get(resourceLocation)).toList();
    }

    public TagKey<EntityType<?>> getRegularEnemies() {
        return regularEnemies;
    }

    public TagKey<EntityType<?>> getStrongEnemies() {
        return strongEnemies;
    }

    public RoomType getStartingRoom() {
        return ModRoomTypes.registry.get().getValue(startingRoom);
    }

    @Nullable
    public SoundEvent getMusic() {
        if (music != null) {
            return BuiltInRegistries.SOUND_EVENT.get(music);
        } else {
            return null;
        }
    }

    @Nullable
    public ResourceLocation getFixedLayout() {
        return fixedLayout;
    }

}
