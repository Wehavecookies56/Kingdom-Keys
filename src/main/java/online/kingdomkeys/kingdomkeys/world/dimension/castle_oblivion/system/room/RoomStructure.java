package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
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
    @Nullable private final ResourceLocation floor;
    //structure x and z dimensions ignoring y
    RoomDimensions dimensions;
    //whitelist specific rooms if empty no whitelist
    private final List<ResourceLocation> roomWhitelist;

    public static final Codec<RoomStructure> CODEC = RecordCodecBuilder.create(roomStructureInstance ->
        roomStructureInstance.group(
                Codec.STRING.fieldOf("structure").forGetter(RoomStructure::getPath),
                StringRepresentable.fromEnum(RoomSize::values).fieldOf("size").forGetter(RoomStructure::getSize),
                StringRepresentable.fromEnum(RoomCategory::values).listOf().fieldOf("categories").forGetter(RoomStructure::getCategories),
                ResourceLocation.CODEC.optionalFieldOf("floor").forGetter(o -> Optional.ofNullable(o.floor)),
                RoomDimensions.CODEC.fieldOf("dimensions").forGetter(RoomStructure::getDimensions),
                ResourceLocation.CODEC.listOf().optionalFieldOf("room_white_list").forGetter(o -> Optional.ofNullable(o.roomWhitelist))
        ).apply(roomStructureInstance, RoomStructure::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomStructure(String path, RoomSize size, List<RoomCategory> categories, Optional<ResourceLocation> floor, RoomDimensions dimensions, Optional<List<ResourceLocation>> roomWhitelist) {
        this.path = path;
        this.size = size;
        this.categories = categories;
        this.floor = floor.orElse(null);
        this.dimensions = dimensions;
        this.roomWhitelist = roomWhitelist.orElse(new ArrayList<>());
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

    public FloorType getFloor() {
        if (floor != null) {
            return ModFloorTypes.registry.get().getValue(floor);
        } else {
            return null;
        }
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
}
