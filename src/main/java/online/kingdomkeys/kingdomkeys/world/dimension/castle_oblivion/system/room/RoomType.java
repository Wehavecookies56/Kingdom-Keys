package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoomType extends JsonRegistryObject {

    private boolean entranceHall = false;
    private RoomSize size;
    private RoomCategory category;
    private RoomEnemies enemies;
    private Color colour;
    private List<ResourceLocation> modifiers;
    private List<ResourceLocation> compatibleFloors;
    private ResourceLocation fixedRoom;
    private ResourceLocation music;

    public RoomType(CompoundTag tag) {
        super(tag);
    }

    public RoomType(JsonElement element) {
        super(element);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public String getTranslationKey() {
        return "room." + registryName.getPath();
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
        //TODO registry
        return null;
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

    public RoomStructure getFixedRoom() {
        if (fixedRoom != null) {
            return ModRoomStructures.registry.get().getValue(fixedRoom);
        } else {
            return null;
        }
    }

    public SoundEvent getMusic() {
        if (music != null) {
            return ForgeRegistries.SOUND_EVENTS.getValue(music);
        } else {
            return null;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("entrance_hall", entranceHall);
        tag.putInt("size", size.ordinal());
        tag.putInt("category", category.ordinal());

        CompoundTag compatibleFloors = new CompoundTag();
        if (this.compatibleFloors != null) {
            this.compatibleFloors.forEach(resourceLocation -> compatibleFloors.putString(resourceLocation.toString(), resourceLocation.toString()));
        }
        tag.put("compatible_floors", compatibleFloors);
        CompoundTag modifiers = new CompoundTag();
        if (this.modifiers != null) {
            this.modifiers.forEach(resourceLocation -> modifiers.putString(resourceLocation.toString(), resourceLocation.toString()));
        }
        tag.put("modifiers", modifiers);

        if (fixedRoom != null) {
            tag.putString("fixed_room", fixedRoom.toString());
        }
        if (enemies != null) {
            tag.putInt("enemies", enemies.ordinal());
        }
        if (music != null) {
            tag.putString("music", music.toString());
        }
        if (colour != null) {
            tag.putInt("colour", colour.getRGB());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        entranceHall = tag.getBoolean("entrance_hall");
        size = RoomSize.values()[tag.getInt("size")];
        category = RoomCategory.values()[tag.getInt("category")];

        CompoundTag compatibleFloors = tag.getCompound("compatible_floors");
        this.compatibleFloors = new ArrayList<>();
        compatibleFloors.getAllKeys().forEach(s -> {
            this.compatibleFloors.add(new ResourceLocation(s));
        });
        CompoundTag modifiers = tag.getCompound("modifiers");
        this.modifiers = new ArrayList<>();
        modifiers.getAllKeys().forEach(s -> {
            this.modifiers.add(new ResourceLocation(s));
        });

        if (tag.contains("fixed_room")) {
            fixedRoom = new ResourceLocation(tag.getString("fixed_room"));
        }
        if (tag.contains("enemies")) {
            enemies = RoomEnemies.values()[(tag.getInt("enemies"))];
        }
        if (tag.contains("music")) {
            music = new ResourceLocation(tag.getString("music"));
        }
        if (tag.contains("colour")) {
            colour = new Color(tag.getInt("colour"));
        }
    }

    @Override
    public void deserializeJson(JsonElement element) throws JsonParseException {
        JsonObject root = getJsonObject(element);
        root.entrySet().forEach(entry -> {
            JsonElement entryElement = entry.getValue();
            switch (entry.getKey()) {
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
                case "category" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        try {
                            category = RoomCategory.valueOf(s.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new JsonParseException("Invalid category, valid values are: ENEMY, STATUS, BOUNTY, SPECIAL, ANY");
                        }
                    } else {
                        throw new JsonParseException("Category should not be empty");
                    }
                }
                case "entrance_hall" -> {
                    entranceHall = entryElement.getAsBoolean();
                }
                case "colour" -> {
                    JsonArray colourArray = entryElement.getAsJsonArray();
                    if (colourArray.size() >= 3) { //ignore values after 3rd
                        int r = Math.max(0, Math.min(colourArray.get(0).getAsInt(), 255));
                        int g = Math.max(0, Math.min(colourArray.get(1).getAsInt(), 255));
                        int b = Math.max(0, Math.min(colourArray.get(2).getAsInt(), 255));
                        colour = new Color(r, g, b);
                    } else {
                        throw new JsonParseException("Colour should have 3 values");
                    }
                }
                case "enemies" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        try {
                            enemies = RoomEnemies.valueOf(s.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            throw new JsonParseException("Invalid enemies, valid values are: NONE, S, M, L");
                        }
                    } else {
                        throw new JsonParseException("Category should not be empty");
                    }
                }
                case "compatible_floors" -> {
                    JsonArray floors = entryElement.getAsJsonArray();
                    if (!floors.isEmpty()) {
                        compatibleFloors = new ArrayList<>();
                        floors.forEach(ftentry -> {
                            String floorType = ftentry.getAsString();
                            compatibleFloors.add(new ResourceLocation(floorType));
                        });
                    }
                }
                case "modifiers" -> {
                    JsonArray modifiers = entryElement.getAsJsonArray();
                    if (!modifiers.isEmpty()) {
                        this.modifiers = new ArrayList<>();
                        modifiers.forEach(mentry -> {
                            String modifier = mentry.getAsString();
                            //TODO registry
                            this.modifiers.add(new ResourceLocation(modifier));
                        });
                    }
                }
                case "fixed_room" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        fixedRoom = new ResourceLocation(s);
                    }
                }
                case "music" -> {
                    String s = entryElement.getAsString();
                    if (!s.isEmpty()) {
                        if (ForgeRegistries.SOUND_EVENTS.containsKey(new ResourceLocation(s))) {
                            music = new ResourceLocation(s);
                        } else {
                            throw new JsonParseException("Supplied music does not exist");
                        }
                    }
                }
            }
        });
        if (enemies == null) {
            enemies = RoomEnemies.NONE;
        }
        if (modifiers == null) {
            modifiers = new ArrayList<>();
        }
        if (compatibleFloors == null) {
            compatibleFloors = new ArrayList<>();
        }
    }

    private static @NotNull JsonObject getJsonObject(JsonElement element) {
        JsonObject root = element.getAsJsonObject();
        if (!root.has("size")) {
            throw new JsonParseException("Missing required element \"size\"");
        }
        if (!root.has("category")) {
            throw new JsonParseException("Missing required element \"category\"");
        }
        return root;
    }
}
