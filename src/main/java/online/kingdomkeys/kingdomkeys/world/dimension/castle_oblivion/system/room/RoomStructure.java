package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.StringRepresentable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
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
    //whitelist specific rooms if empty no whitelist
    private final List<ResourceLocation> roomWhitelist;
    //fallback room will not be generated when searching for compatible structures
    private final boolean fallback;

    RoomDimensions dimensionsCache;

    public static final Codec<RoomStructure> CODEC = RecordCodecBuilder.create(roomStructureInstance ->
        roomStructureInstance.group(
                Codec.STRING.fieldOf("structure").forGetter(RoomStructure::getPath),
                StringRepresentable.fromEnum(RoomSize::values).fieldOf("size").forGetter(RoomStructure::getSize),
                StringRepresentable.fromEnum(RoomCategory::values).listOf().fieldOf("categories").forGetter(RoomStructure::getCategories),
                Codec.BOOL.optionalFieldOf("floor_specific_structure").forGetter(o -> Optional.of(o.floorSpecificStructure)),
                ResourceLocation.CODEC.listOf().optionalFieldOf("white_list").forGetter(o -> Optional.ofNullable(o.roomWhitelist)),
                Codec.BOOL.optionalFieldOf("fallback").forGetter(o -> Optional.of(o.fallback))
                ).apply(roomStructureInstance, RoomStructure::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private RoomStructure(String path, RoomSize size, List<RoomCategory> categories, Optional<Boolean> floorSpecificStructure, Optional<List<ResourceLocation>> roomWhitelist, Optional<Boolean> fallback) {
        this.path = path;
        this.size = size;
        this.categories = categories;
        this.floorSpecificStructure = floorSpecificStructure.orElse(true);
        this.roomWhitelist = roomWhitelist.orElse(new ArrayList<>());
        this.fallback = fallback.orElse(false);
    }

    public record RoomDimensions(int width, int height, int depth) {
        public static final Codec<RoomDimensions> CODEC = Codec.INT.listOf(3, 3).xmap(
                integers -> new RoomDimensions(integers.getFirst(), integers.get(1), integers.getLast()),
                roomDimensions -> List.of(roomDimensions.width, roomDimensions.height, roomDimensions.depth)
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

    public List<RoomCategory> getCategories() {
        return categories;
    }

    //gets structure file and caches structure dimensions if file exists
    public Optional<Resource> getStructureFile(ServerLevel level, FloorType floor) {
        String floorFolder = !this.useFloorSpecificStructure() ? "all" : floor.getRegistryName().getPath();
        ResourceLocation structureFile = ResourceLocation.fromNamespaceAndPath(floor.getRegistryName().getNamespace(), "structure/castle_oblivion/rooms/" + floorFolder + "/" + this.getPath() + ".nbt");
        Optional<Resource> out = level.getServer().getResourceManager().getResource(structureFile);
        if (out.isEmpty() && !floor.getRegistryName().getNamespace().equals(KingdomKeys.MODID)) {
            //try KK namespace as a fallback
            structureFile = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "structure/castle_oblivion/rooms/" + floorFolder + "/" + this.getPath() + ".nbt");
            out = level.getServer().getResourceManager().getResource(structureFile);
        }
        if (out.isPresent()) {
            try {
                CompoundTag main = NbtIo.readCompressed(out.get().open(), NbtAccounter.unlimitedHeap());
                ListTag size = main.getList("size", Tag.TAG_INT);
                dimensionsCache = new RoomStructure.RoomDimensions(size.getInt(0), size.getInt(1), size.getInt(2));
            } catch (IOException e) {
                KingdomKeys.LOGGER.error("Failed to read structure file", e.fillInStackTrace());
            }
        }
        return out;
    }

    public Optional<RoomDimensions> getDimensionsCache() {
        return Optional.ofNullable(dimensionsCache);
    }

}
