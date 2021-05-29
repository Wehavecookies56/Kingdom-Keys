package online.kingdomkeys.kingdomkeys.driveform;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/driveforms/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class DriveFormDataDeserializer implements JsonDeserializer<DriveFormData> {

    @Override
    public DriveFormData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DriveFormData out = new DriveFormData();
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();
			switch (entry.getKey()) {
			case "cost":
				out.setCost(element.getAsInt());
				break;
			case "ap":
				out.setAP(element.getAsInt());
				break;
			case "str_mult":
				out.setStrMult(element.getAsFloat());
				break;
			case "mag_mult":
				out.setMagMult(element.getAsFloat());
				break;
			case "speed_mult":
				out.setSpeedMult(element.getAsFloat());
				break;
			case "level_up":
				JsonArray costs = element.getAsJsonArray();
				List<Integer> levelsArray = new ArrayList<Integer>();
				
				for(int i= 0; i < costs.size(); i++) {
					levelsArray.add(costs.get(i).getAsInt());
				}
				out.setLevelUp(levelsArray.stream().mapToInt(x->x).toArray());
				break;
			}
        });
      //  KingdomKeys.LOGGER.info("DESCRIPTION: {}", out.description);
        return out;
    }
}
