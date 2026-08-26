package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomCategory;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomSize;

import java.util.Arrays;
import java.util.List;

public class RoomStructureBuilder extends BuilderBase {

    public RoomStructureBuilder(ResourceLocation location, String structure, RoomSize size, List<RoomCategory> categories) {
        super(location);
        root.addProperty("structure", structure);
        root.addProperty("size", size.getSerializedName());
        JsonArray categoriesArray = new JsonArray();
        categories.forEach(category -> {
            categoriesArray.add(category.getSerializedName());
        });
        root.add("categories", categoriesArray);
    }

    public RoomStructureBuilder notFloorSpecific() {
        root.addProperty("floor_specific_structure", false);
        return this;
    }

    public RoomStructureBuilder roomWhitelist(ResourceLocation... rooms) {
        JsonArray whitelistArray = new JsonArray();
        Arrays.stream(rooms).forEach(roomType -> {
            whitelistArray.add(roomType.toString());
        });
        root.add("white_list", whitelistArray);
        return this;
    }

    public RoomStructureBuilder fallback() {
        root.addProperty("fallback", true);
        return this;
    }
}
