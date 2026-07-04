package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import java.util.Arrays;

public class FloorTypeBuilder extends BuilderBase {

    public FloorTypeBuilder(ResourceLocation location, int critPathLength, ResourceKey<Biome> biomeColours) {
        super(location);
        root.addProperty("crit_path_length", critPathLength);
        root.addProperty("biome_colours", biomeColours.location().toString());
    }

    public FloorTypeBuilder bonusRooms(FloorType.CountChancePair bonusRooms) {
        JsonObject bonusRoomsObj = new JsonObject();
        bonusRoomsObj.addProperty("count", bonusRooms.count());
        bonusRoomsObj.addProperty("chance", bonusRooms.chance());
        root.add("bonus_rooms", bonusRoomsObj);
        return this;
    }

    public FloorTypeBuilder branches(FloorType.CountChancePair branches) {
        JsonObject branchesObj = new JsonObject();
        branchesObj.addProperty("count", branches.count());
        branchesObj.addProperty("chance", branches.chance());
        root.add("branches", branchesObj);
        return this;
    }

    public FloorTypeBuilder music(SoundEvent music) {
        root.addProperty("music", music.getLocation().toString());
        return this;
    }

    public FloorTypeBuilder roomBlacklist(ResourceLocation... rooms) {
        JsonArray blacklistArray = new JsonArray();
        Arrays.stream(rooms).forEach(roomType -> {
            blacklistArray.add(roomType.toString());
        });
        root.add("room_blacklist", blacklistArray);
        return this;
    }

    public FloorTypeBuilder startingRoom(ResourceLocation room) {
        root.addProperty("starting_room", room.toString());
        return this;
    }

    public FloorTypeBuilder modifiers(RoomModifier... modifiers) {
        JsonArray modifiersArray = new JsonArray();
        Arrays.stream(modifiers).forEach(modifier -> {
            modifiersArray.add(RoomModifier.CODEC.encodeStart(JsonOps.INSTANCE, modifier).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        });
        root.add("modifiers", modifiersArray);
        return this;
    }

    public FloorTypeBuilder regularEnemies(TagKey<EntityType<?>> tag) {
        root.addProperty("regular_enemies", "#" + tag.location());
        return this;
    }

    public FloorTypeBuilder stringEnemies(TagKey<EntityType<?>> tag) {
        root.addProperty("strong_enemies", "#" + tag.location());
        return this;
    }

    public FloorTypeBuilder useFogColour() {
        root.addProperty("use_fog_colour", true);
        return this;
    }
}
