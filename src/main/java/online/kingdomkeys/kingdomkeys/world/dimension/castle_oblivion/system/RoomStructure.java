package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Size2i;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

//metadata for each nbt file for rooms
public class RoomStructure {

    //structure path
    String path;

    //the size
    RoomProperties.RoomSize size;

    //categories compatible with
    List<RoomProperties.RoomCategory> categories;

    //floor compatible with, null if any
    FloorType floor;

    //structure x and z dimensions ignoring y
    Size2i physicalSize;

    //whitelist specific rooms if empty no whitelist
    List<Supplier<RoomType>> roomWhitelist;

    ResourceLocation registryName;

    public RoomStructure(String path, FloorType floor, RoomProperties.RoomSize size, List<RoomProperties.RoomCategory> categories, Size2i physicalSize, Supplier<RoomType>... roomWhitelist) {
        this.path = path;
        this.size = size;
        this.categories = categories;
        this.floor = floor;
        this.physicalSize = physicalSize;
        this.roomWhitelist = Arrays.stream(roomWhitelist).toList();
    }

    public List<RoomType> getRoomWhitelist() {
        return roomWhitelist.stream().map(Supplier::get).toList();
    }

    public RoomStructure setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Nullable
    public ResourceLocation getRegistryName() {
        return registryName;
    }

}
