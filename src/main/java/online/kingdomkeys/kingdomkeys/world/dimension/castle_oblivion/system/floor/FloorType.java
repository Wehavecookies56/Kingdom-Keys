package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloorType extends JsonRegistryObject {

    private final int critPathLength;
    private final CountChancePair bonusRooms, branches;
    private final Holder<Biome> floorColour;
    @Nullable private final Holder<SoundEvent> music;
    private final List<ResourceLocation> roomBlacklist;
    private final ResourceLocation startingRoom;
    @Nullable private final ResourceLocation fixedLayout;
    private final List<RoomModifier> globalModifiers;
    private final TagKey<EntityType<?>> regularEnemies;
    private final TagKey<EntityType<?>> strongEnemies;
    private final boolean useFogColour;

    public static final Codec<FloorType> CODEC = RecordCodecBuilder.create(floorTypeInstance ->
        floorTypeInstance.group(
                Codec.INT.fieldOf("crit_path_length").forGetter(FloorType::getCritPathLength),
                Biome.CODEC.fieldOf("biome_colours").forGetter(FloorType::getFloorColour),
                CountChancePair.CODEC.optionalFieldOf("bonus_rooms", new CountChancePair(0, 0)).forGetter(FloorType::getBonusRooms),
                CountChancePair.CODEC.optionalFieldOf("branches", new CountChancePair(0, 0)).forGetter(FloorType::getBranches),
                SoundEvent.CODEC.optionalFieldOf("music").forGetter(o -> Optional.ofNullable(o.music)),
                ResourceLocation.CODEC.listOf().optionalFieldOf("room_blacklist", new ArrayList<>()).forGetter(o -> o.roomBlacklist),
                ResourceLocation.CODEC.optionalFieldOf("starting_room", KingdomKeys.rl("unknown_room")).forGetter(o -> o.startingRoom),
                ResourceLocation.CODEC.optionalFieldOf("fixed_layout").forGetter(o -> Optional.ofNullable(o.fixedLayout)),
                RoomModifier.CODEC.listOf().optionalFieldOf("modifiers", new ArrayList<>()).forGetter(o -> o.globalModifiers),
                TagKey.hashedCodec(Registries.ENTITY_TYPE).optionalFieldOf("regular_enemies", ModTags.CO_REGULAR_ENEMIES).forGetter(FloorType::getRegularEnemies),
                TagKey.hashedCodec(Registries.ENTITY_TYPE).optionalFieldOf("strong_enemies", ModTags.CO_STRONG_ENEMIES).forGetter(FloorType::getStrongEnemies),
                Codec.BOOL.optionalFieldOf("use_fog_colour", false).forGetter(FloorType::useFogColour)
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
    public FloorType(int critPathLength, Holder<Biome> floorColour, CountChancePair bonusRooms, CountChancePair branches, Optional<Holder<SoundEvent>> music, List<ResourceLocation> roomBlacklist, ResourceLocation startingRoom, Optional<ResourceLocation> fixedLayout, List<RoomModifier> globalModifiers, TagKey<EntityType<?>> regularEnemies, TagKey<EntityType<?>> strongEnemies, boolean useFogColour) {
        this.critPathLength = critPathLength;
        this.floorColour = floorColour;
        this.bonusRooms = bonusRooms;
        this.branches = branches;
        this.music = music.orElse(null);
        this.roomBlacklist = roomBlacklist;
        this.startingRoom = startingRoom;
        this.fixedLayout = fixedLayout.orElse(null);
        this.globalModifiers = globalModifiers;
        this.regularEnemies = regularEnemies;
        this.strongEnemies = strongEnemies;
        this.useFogColour = useFogColour;
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

    public Holder<Biome> getFloorColour() {
        return floorColour;
    }

    public List<RoomType> getRoomBlacklist() {
        return roomBlacklist.stream().map(resourceLocation -> ModRoomTypes.registry.get().getValue(resourceLocation)).toList();
    }

    public List<RoomModifier> getGlobalModifiers() {
        return globalModifiers;
    }

    public boolean useFogColour() {
        return useFogColour;
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
            return music.value();
        } else {
            return null;
        }
    }

    @Nullable
    public ResourceLocation getFixedLayout() {
        return fixedLayout;
    }

}
