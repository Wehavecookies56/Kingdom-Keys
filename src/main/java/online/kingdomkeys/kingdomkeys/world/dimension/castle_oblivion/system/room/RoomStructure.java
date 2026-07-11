package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//metadata for each nbt file for rooms
public class RoomStructure extends JsonRegistryObject {

    //structure path
    private final String path;
    //the size
    private final RoomSize size;
    //categories compatible with
    private final List<RoomCategory> categories;
    //floor compatible with, null if any
    private final boolean floorSpecificStructure;
    //structure x and z dimensions ignoring y //TODO remove and just get nbt structure dimensions
    RoomDimensions dimensions;
    //whitelist specific rooms if empty no whitelist
    private final List<ResourceLocation> roomWhitelist;
    //fallback room will not be generated when searching for compatible structures
    private final boolean fallback;

    public static final Codec<RoomStructure> CODEC = RecordCodecBuilder.create(roomStructureInstance ->
        roomStructureInstance.group(
                Codec.STRING.fieldOf("structure").forGetter(RoomStructure::getPath),
                StringRepresentable.fromEnum(RoomSize::values).fieldOf("size").forGetter(RoomStructure::getSize),
                StringRepresentable.fromEnum(RoomCategory::values).listOf().fieldOf("categories").forGetter(RoomStructure::getCategories),
                Codec.BOOL.optionalFieldOf("floor_specific_structure").forGetter(o -> Optional.of(o.floorSpecificStructure)),
                RoomDimensions.CODEC.fieldOf("dimensions").forGetter(RoomStructure::getDimensions),
                ResourceLocation.CODEC.listOf().optionalFieldOf("white_list").forGetter(o -> Optional.ofNullable(o.roomWhitelist)),
                Codec.BOOL.optionalFieldOf("fallback").forGetter(o -> Optional.of(o.fallback))
                ).apply(roomStructureInstance, RoomStructure::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomStructure(String path, RoomSize size, List<RoomCategory> categories, Optional<Boolean> floorSpecificStructure, RoomDimensions dimensions, Optional<List<ResourceLocation>> roomWhitelist, Optional<Boolean> fallback) {
        this.path = path;
        this.size = size;
        this.categories = categories;
        this.floorSpecificStructure = floorSpecificStructure.orElse(true);
        this.dimensions = dimensions;
        this.roomWhitelist = roomWhitelist.orElse(new ArrayList<>());
        this.fallback = fallback.orElse(false);
    }

    public record RoomDimensions(int width, int depth) {
        public static final Codec<RoomDimensions> CODEC = Codec.INT.listOf(2, 2).xmap(
                integers -> new RoomDimensions(integers.getFirst(), integers.getLast()),
                roomDimensions -> List.of(roomDimensions.width, roomDimensions.depth)
        );
    }

    public List<RoomType> getRoomWhitelist() {
        return roomWhitelist.stream().map(resourceLocation -> ModRoomTypes.registry.get().getValue(resourceLocation)).toList();
    }

    public boolean useFloorSpecificStructure() {
        return floorSpecificStructure;
    }

    public boolean isFallback() {
        return fallback;
    }

    public String getPath() {
        return path;
    }

    public RoomSize getSize() {
        return size;
    }

    public RoomDimensions getDimensions() {
        return dimensions;
    }

    public int getWidth() {
        return dimensions.width;
    }

    public int getDepth() {
        return dimensions.depth;
    }

    public List<RoomCategory> getCategories() {
        return categories;
    }

    public static final RoomDimensions S = new RoomDimensions(32, 32);
    public static final RoomDimensions M = new RoomDimensions(48, 48);
    public static final RoomDimensions L = new RoomDimensions(64, 64);

}
