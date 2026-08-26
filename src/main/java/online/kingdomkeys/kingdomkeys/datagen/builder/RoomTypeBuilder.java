package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Codecs;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomCategory;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomSize;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import java.awt.*;
import java.util.Arrays;

public class RoomTypeBuilder extends BuilderBase {

    public RoomTypeBuilder(ResourceLocation location, RoomSize size, RoomCategory category) {
        super(location);
        root.addProperty("size", size.getSerializedName());
        root.addProperty("category", category.getSerializedName());
    }

    public RoomTypeBuilder enemies(RoomType.Enemies enemies) {
        root.add("enemies", RoomType.Enemies.CODEC.encodeStart(JsonOps.INSTANCE, enemies).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        return this;
    }

    public RoomTypeBuilder isEntranceHall() {
        root.addProperty("entrance_hall", true);
        return this;
    }

    public RoomTypeBuilder colour(Color color) {
        root.add("colour", Codecs.COLOR_CODEC_HEX.encodeStart(JsonOps.INSTANCE, color).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        return this;
    }

    public RoomTypeBuilder modifiers(RoomModifier... modifiers) {
        JsonArray modifiersArray = new JsonArray();
        Arrays.stream(modifiers).forEach(modifier -> {
            modifiersArray.add(RoomModifier.CODEC.encodeStart(JsonOps.INSTANCE, modifier).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        });
        root.add("modifiers", modifiersArray);
        return this;
    }

    public RoomTypeBuilder compatibleFloors(FloorType... floor) {
        JsonArray floorsArray = new JsonArray();
        Arrays.stream(floor).forEach(floorType -> {
            floorsArray.add(floorType.getRegistryName().toString());
        });
        root.add("compatible", floorsArray);
        return this;
    }

    public RoomTypeBuilder fixedRoom(ResourceLocation fixedRoom) {
        root.addProperty("fixed_room", fixedRoom.toString());
        return this;
    }

    public RoomTypeBuilder music(SoundEvent music) {
        root.addProperty("music", music.getLocation().toString());
        return this;
    }

    public RoomTypeBuilder encounter(ResourceLocation encounter) {
        root.addProperty("encounter", encounter.toString());
        return this;
    }

    public RoomTypeBuilder treasure(RoomType.Treasure treasure) {
        root.add("treasure", RoomType.Treasure.CODEC.encodeStart(JsonOps.INSTANCE, treasure).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        return this;
    }

}
