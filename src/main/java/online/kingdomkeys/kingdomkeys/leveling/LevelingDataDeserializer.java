package online.kingdomkeys.kingdomkeys.leveling;

import com.google.gson.*;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom deserializer for Keyblade Data json files located in
 * data/kingdomkeys/leveling/ Str and Mag are integers Keychain can be null
 * therefore an invalid registry name will be treated as having no keychain A
 * keyblade with no keychain does not need the levels object Levels do not
 * require an ability Description can be empty
 */
public class LevelingDataDeserializer implements JsonDeserializer<LevelingData> {

	@Override
	public LevelingData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		LevelingData out = new LevelingData();
		JsonObject root = json.getAsJsonObject();

		if(!root.has("version"))
			KingdomKeys.LOGGER.warn("No version found in one of the leveling paths data, you might want to add it: "+json.toString().substring(0,15));

		//Get version
		int version = root.has("version") ? root.get("version").getAsInt() : 0;
		out.setVersion(version);

		//This should allow for old datapacks to still be compatible
		JsonObject levelsObj = root.has("levels") ? root.getAsJsonObject("levels") : root;

		//Levels
		for (Map.Entry<String,JsonElement> entry : levelsObj.entrySet()) {
			int level = Integer.parseInt(entry.getKey());
			JsonObject levelData = entry.getValue().getAsJsonObject();

			for (Map.Entry<String,JsonElement> entry2 : levelData.entrySet()) {
				JsonElement element = entry2.getValue();

				switch (entry2.getKey()) {
					case "ap":
						out.setAP(level, element.getAsInt());
						break;
					case "str":
						out.setStr(level, element.getAsInt());
						break;
					case "mag":
						out.setMag(level, element.getAsInt());
						break;
					case "def":
						out.setDef(level, element.getAsInt());
						break;
					case "maxhp":
						out.setMaxHp(level, element.getAsInt());
						break;
					case "maxmp":
						out.setMaxMp(level, element.getAsInt());
						break;

					case "abilities":
						out.setAbilities(level, toStringArray(element.getAsJsonArray()));
						break;

					case "shotlocks":
						out.setShotlocks(level, toStringArray(element.getAsJsonArray()));
						break;

					case "spells":
						out.setSpells(level, toStringArray(element.getAsJsonArray()));
						break;

					case "max_accessories":
						out.setMaxAccessories(level, element.getAsInt());
						break;

					case "max_armors":
						out.setMaxArmors(level, element.getAsInt());
						break;
				}
			}
		}

		return out;
	}

	private String[] toStringArray(JsonArray array) {
		String[] out = new String[array.size()];
		for (int i = 0; i < array.size(); i++) {
			out[i] = array.get(i).getAsString();
		}
		return out;
	}
}
