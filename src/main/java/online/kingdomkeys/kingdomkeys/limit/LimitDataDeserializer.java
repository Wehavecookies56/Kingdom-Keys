package online.kingdomkeys.kingdomkeys.limit;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom deserializer for Keyblade Data json files located in
 * data/kingdomkeys/magics/ Str and Mag are integers Keychain can be null
 * therefore an invalid registry name will be treated as having no keychain A
 * keyblade with no keychain does not need the levels object Levels do not
 * require an ability Description can be empty
 */
public class LimitDataDeserializer implements JsonDeserializer<LimitData> {

	@Override
	public LimitData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		LimitData out = new LimitData();
		JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();
			switch (entry.getKey()) {
			case "cost":
				out.setCost(element.getAsInt());
				break;
			case "cooldown":
				out.setCooldown(element.getAsInt());
				break;
			case "dmg_mult":
				out.setDmgMult(element.getAsFloat());
				break;
			}
        });
		return out;
	}
}
