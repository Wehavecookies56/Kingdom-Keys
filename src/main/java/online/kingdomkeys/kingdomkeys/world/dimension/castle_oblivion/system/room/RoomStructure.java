package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

//metadata for each nbt file for rooms
public class RoomStructure extends JsonRegistryObject {

    //structure path
    private String path;
    //the size
    private RoomSize size;
    //categories compatible with
    private List<RoomCategory> categories;
    //floor compatible with, null if any
    @Nullable private ResourceLocation floor;
    //structure x and z dimensions ignoring y
    private int width, depth;
    //whitelist specific rooms if empty no whitelist
    private List<ResourceLocation> roomWhitelist;

    public RoomStructure(CompoundTag tag) {
        super(tag);
    }

    public RoomStructure(JsonElement element) {
        super(element);
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

    public int getWidth() {
        return width;
    }

    public int getDepth() {
        return depth;
    }

    public List<RoomCategory> getCategories() {
        return categories;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putString("structure", path);
        tag.putInt("size", size.ordinal());
        tag.putInt("width", width);
        tag.putInt("depth", depth);
        CompoundTag categories = new CompoundTag();
        this.categories.forEach(roomCategory -> {
            categories.putInt(roomCategory.name(), roomCategory.ordinal());
        });
        tag.put("categories", categories);
        if (floor != null) {
            tag.putString("floor", floor.toString());
        }
        CompoundTag whiteList = new CompoundTag();
        if (roomWhitelist != null) {
            this.roomWhitelist.forEach(resourceLocation -> whiteList.putString(resourceLocation.toString(), resourceLocation.toString()));
        }
        tag.put("white_list", whiteList);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        path = tag.getString("structure");
        size = RoomSize.values()[tag.getInt("size")];
        width = tag.getInt("width");
        depth = tag.getInt("depth");
        categories = new ArrayList<>();
        CompoundTag cats = tag.getCompound("categories");
        cats.getAllKeys().forEach(s -> categories.add(RoomCategory.values()[cats.getInt(s)]));
        if (tag.contains("floor")) {
            floor = ResourceLocation.parse(tag.getString("floor"));
        }
        roomWhitelist = new ArrayList<>();
        tag.getCompound("white_list").getAllKeys().forEach(s -> roomWhitelist.add(ResourceLocation.parse(s)));
    }

    @Override
    public void deserializeJson(JsonElement element) throws JsonParseException {
        JsonObject root = getJsonObject(element);
        root.entrySet().forEach(entry -> {
            JsonElement entryElement = entry.getValue();
            switch (entry.getKey()) {
                case "structure" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        path = s;
                    } else {
                        throw new JsonParseException("Structure should not be empty");
                    }
                }
                case "size" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        try {
                            size = RoomSize.valueOf(s.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new JsonParseException("Invalid size, valid values are: S, M, L, SPECIAL");
                        }
                    } else {
                        throw new JsonParseException("Size should not be empty");
                    }
                }
                case "dimensions" -> {
                    JsonArray dimensions = entryElement.getAsJsonArray();
                    if (dimensions.size() >= 2) { //ignore values after 2nd
                        width = dimensions.get(0).getAsInt();
                        depth = dimensions.get(1).getAsInt();
                    } else {
                        throw new JsonParseException("Dimensions should have 2 values");
                    }
                }
                case "categories" -> {
                    JsonArray categories = entryElement.getAsJsonArray();
                    if (!categories.isEmpty()) {
                        this.categories = new ArrayList<>();
                        categories.forEach(cat -> {
                            String s = cat.getAsString();
                            if (!s.isEmpty()) {
                                try {
                                    this.categories.add(RoomCategory.valueOf(s.toUpperCase()));
                                } catch (IllegalArgumentException e) {
                                    throw new JsonParseException("Invalid category, valid values are: ENEMY, STATUS, BOUNTY, SPECIAL, ANY");
                                }
                            } else {
                                throw new JsonParseException("Category should not be empty");
                            }
                        });
                    } else {
                        throw new JsonParseException("Categories should not be empty");
                    }
                }
                case "floor" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        floor = ResourceLocation.parse(s);
                    }
                }
                case "white_list" -> {
                    JsonArray whitelist = entryElement.getAsJsonArray();
                    if (!whitelist.isEmpty()) {
                        roomWhitelist = new ArrayList<>();
                        whitelist.forEach(wlentry -> {
                            String roomtype = wlentry.getAsString();
                            roomWhitelist.add(ResourceLocation.parse(roomtype));
                        });
                    }
                }
            }
            if (roomWhitelist == null) {
                roomWhitelist = new ArrayList<>();
            }
        });
    }

    private static @Nonnull JsonObject getJsonObject(JsonElement element) {
        JsonObject root = element.getAsJsonObject();
        if (!root.has("structure")) {
            throw new JsonParseException("Missing required element \"structure\"");
        }
        if (!root.has("size")) {
            throw new JsonParseException("Missing required element \"size\"");
        }
        if (!root.has("dimensions")) {
            throw new JsonParseException("Missing required element \"dimensions\"");
        }
        if (!root.has("categories")) {
            throw new JsonParseException("Missing required element \"categories\"");
        }
        return root;
    }
}
