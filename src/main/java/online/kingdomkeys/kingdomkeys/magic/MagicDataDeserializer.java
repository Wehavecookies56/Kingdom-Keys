package online.kingdomkeys.kingdomkeys.magic;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom deserializer for Keyblade Data json files located in
 * data/kingdomkeys/magics/ Str and Mag are integers Keychain can be null
 * therefore an invalid registry name will be treated as having no keychain A
 * keyblade with no keychain does not need the levels object Levels do not
 * require an ability Description can be empty
 */
public class MagicDataDeserializer implements JsonDeserializer<MagicData> {

	@Override
	public MagicData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		MagicData out = new MagicData();
		JsonObject jsonObject = json.getAsJsonObject();

		jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();
			int level = Integer.parseInt(entry.getKey());
			JsonObject jsonObject2 = entry.getValue().getAsJsonObject();
			jsonObject2.entrySet().forEach(entry2 -> {
				JsonElement element2 = entry2.getValue();

				switch (entry2.getKey()) {
				case "cost":
					out.setCost(level, element2.getAsInt());
					break;
				case "cooldown":
					out.setCooldown(level, element2.getAsInt());
					break;
				case "dmg_mult":
					out.setDmgMult(level, element2.getAsFloat());
					break;
				}
			});

		});
		return out;
	}
}
