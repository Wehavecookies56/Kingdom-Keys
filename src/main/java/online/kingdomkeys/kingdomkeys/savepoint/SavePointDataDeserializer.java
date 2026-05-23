package online.kingdomkeys.kingdomkeys.savepoint;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/savepoints/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class SavePointDataDeserializer implements JsonDeserializer<SavePointData> {

    @Override
    public SavePointData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Map<SavePointData.SavePointStat, ResourceLocation> materials = new EnumMap<>(SavePointData.SavePointStat.class);
        EnumSet<SavePointData.SavePointStat> restores = EnumSet.noneOf(SavePointData.SavePointStat.class);
        JsonObject materialsObj = obj.getAsJsonObject("materials");

        for(String key : materialsObj.keySet()) {
            SavePointData.SavePointStat stat = SavePointData.SavePointStat.valueOf(key);
            materials.put(stat, ResourceLocation.parse(materialsObj.get(key).getAsString()));
        }

        JsonArray restoreArray = obj.getAsJsonArray("restores");

        for(JsonElement element : restoreArray) {
            restores.add(SavePointData.SavePointStat.valueOf(element.getAsString()));
        }

        SavePointData data = new SavePointData(materials, restores);
        data.setName("a");
        return data;
    }
}