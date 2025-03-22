package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloorType extends JsonRegistryObject {

    private int critPathLength, bonusRoomsCount, bonusRoomsChance, branchCount, branchChance;
    private Color floorColour;
    @Nullable private ResourceLocation music;
    private List<ResourceLocation> roomBlacklist;
    @Nullable private ResourceLocation startingRoom;
    @Nullable private ResourceLocation fixedLayout;

    public FloorType(CompoundTag tag) {
        super(tag);
    }

    public FloorType(JsonElement element) {
        super(element);
    }

    public int getCritPathLength() {
        return critPathLength;
    }

    public int getBonusRoomCount() {
        return bonusRoomsCount;
    }

    public int getBonusRoomChance() {
        return bonusRoomsChance;
    }

    public int getBranchCount() {
        return branchCount;
    }

    public int getBranchChance() {
        return branchChance;
    }

    public Color getFloorColour() {
        return floorColour;
    }

    public List<RoomType> getRoomBlacklist() {
        return roomBlacklist.stream().map(resourceLocation -> ModRoomTypes.registry.get().getValue(resourceLocation)).toList();
    }

    @Nullable
    public RoomType getStartingRoom() {
        if (startingRoom != null) {
            return ModRoomTypes.registry.get().getValue(startingRoom);
        } else {
            return null;
        }
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("crit_path", critPathLength);
        tag.putInt("bonus_count", bonusRoomsCount);
        tag.putInt("bonus_chance", bonusRoomsChance);
        tag.putInt("branch_count", branchCount);
        tag.putInt("branch_chance", branchCount);
        tag.putInt("colour", floorColour.getRGB());
        CompoundTag blacklist = new CompoundTag();
        if (roomBlacklist != null) {
            roomBlacklist.forEach(roomType -> blacklist.putString(roomType.toString(), roomType.toString()));
        }
        tag.put("blacklist", blacklist);
        if (music != null) {
            tag.putString("music", music.toString());
        }
        if (startingRoom != null) {
            tag.putString("starting_room", startingRoom.toString());
        }
        if (fixedLayout != null) {
            tag.putString("fixed_layout", fixedLayout.toString());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        critPathLength = tag.getInt("crit_path");
        bonusRoomsCount = tag.getInt("bonus_count");
        bonusRoomsChance = tag.getInt("bonus_chance");
        branchCount = tag.getInt("branch_count");
        branchChance = tag.getInt("branch_chance");
        floorColour = new Color(tag.getInt("colour"));
        roomBlacklist = new ArrayList<>();
        tag.getCompound("blacklist").getAllKeys().forEach(s -> roomBlacklist.add(ResourceLocation.parse(s)));
        if (tag.contains("music")) {
            music = ResourceLocation.parse(tag.getString("music"));
        }
        if (tag.contains("starting_room")) {
            startingRoom = ResourceLocation.parse(tag.getString("starting_room"));
        }
        if (tag.contains("fixed_layout")) {
            fixedLayout = ResourceLocation.parse(tag.getString("fixed_layout"));
        }
    }

    @Override
    public void deserializeJson(JsonElement element) throws JsonParseException {
        JsonObject root = element.getAsJsonObject();
        if (!root.has("crit_path_length")) {
            throw new JsonParseException("Missing required element \"crit_path_length\"");
        }
        root.entrySet().forEach(entry -> {
            JsonElement entryElement = entry.getValue();
            switch (entry.getKey()) {
                case "crit_path_length" -> {
                    int length = entryElement.getAsInt();
                    if (length < 0 || length > 25) {
                        throw new JsonParseException("crit_path_length is out of range 0-25");
                    }
                    critPathLength = length;
                }
                case "bonus_rooms" -> {
                    JsonObject bonusRooms = entryElement.getAsJsonObject();
                    if (bonusRooms.has("count") && bonusRooms.has("chance")) {
                        bonusRoomsCount = bonusRooms.get("count").getAsInt();
                        bonusRoomsChance = bonusRooms.get("chance").getAsInt();
                    } else {
                        throw new JsonParseException("Missing either \"count\" or \"chance\" for bonus_rooms");
                    }
                }
                case "branches" -> {
                    JsonObject branches = entryElement.getAsJsonObject();
                    if (branches.has("count") && branches.has("chance")) {
                        branchCount = branches.get("count").getAsInt();
                        branchCount = branches.get("chance").getAsInt();
                    } else {
                        throw new JsonParseException("Missing either \"count\" or \"chance\" for branches");
                    }
                }
                case "colour" -> {
                    JsonArray colourArray = entryElement.getAsJsonArray();
                    if (colourArray.size() >= 3) { //ignore values after 3rd
                        int r = Math.max(0, Math.min(colourArray.get(0).getAsInt(), 255));
                        int g = Math.max(0, Math.min(colourArray.get(1).getAsInt(), 255));
                        int b = Math.max(0, Math.min(colourArray.get(2).getAsInt(), 255));
                        floorColour = new Color(r, g, b);
                    } else {
                        throw new JsonParseException("Colour should have 3 values");
                    }
                }
                case "music" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        if (BuiltInRegistries.SOUND_EVENT.containsKey(ResourceLocation.parse(s))) {
                            music = ResourceLocation.parse(s);
                        } else {
                            throw new JsonParseException("Supplied music does not exist");
                        }
                    }
                }
                case "black_list" -> {
                    JsonArray blackList = entryElement.getAsJsonArray();
                    roomBlacklist = new ArrayList<>();
                    blackList.forEach(roomTypeElement -> {
                        String s = roomTypeElement.getAsString();
                        roomBlacklist.add(ResourceLocation.parse(s));
                    });
                }
                case "starting_room" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        startingRoom = ResourceLocation.parse(s);
                    }
                }
                case "fixed_layout" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        fixedLayout = ResourceLocation.parse(s);
                    }
                }
            }
        });
        if (roomBlacklist == null) {
            roomBlacklist = new ArrayList<>();
        }
        if (floorColour == null) {
            floorColour = Color.black;
        }
    }

}
