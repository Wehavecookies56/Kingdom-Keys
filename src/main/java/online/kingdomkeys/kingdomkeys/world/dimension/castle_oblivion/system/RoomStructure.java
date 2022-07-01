package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Size2i;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//metadata for each nbt file for rooms
public class RoomStructure implements IForgeRegistryEntry<RoomStructure> {

    //structure path
    String path;

    //the size
    RoomProperties.RoomSize size;

    //categories compatible with
    List<RoomProperties.RoomCategory> categories;

    //floor compatible with "all" for any floor
    String floor;

    //structure x and z dimensions ignoring y
    Size2i physicalSize;

    ResourceLocation registryName;

    public RoomStructure(String path, String floor, RoomProperties.RoomSize size, List<RoomProperties.RoomCategory> categories, Size2i physicalSize) {
        this.path = path;
        this.size = size;
        this.categories = categories;
        this.floor = floor;
        this.physicalSize = physicalSize;
    }

    @Override
    public RoomStructure setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<RoomStructure> getRegistryType() {
        return RoomStructure.class;
    }
}
