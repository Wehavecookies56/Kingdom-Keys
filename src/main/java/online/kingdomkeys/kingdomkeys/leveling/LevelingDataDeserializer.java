package online.kingdomkeys.kingdomkeys.leveling;

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
		JsonObject jsonObject = json.getAsJsonObject();

		jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();
			int level = Integer.parseInt(entry.getKey());
			JsonObject jsonObject2 = entry.getValue().getAsJsonObject();
			jsonObject2.entrySet().forEach(entry2 -> {
				JsonElement element2 = entry2.getValue();

				switch (entry2.getKey()) {
				case "ap":
					out.setAP(level, element2.getAsInt());
					break;
				case "str":
					out.setStr(level, element2.getAsInt());
					break;
				case "mag":
					out.setMag(level, element2.getAsInt());
					break;
				case "def":
					out.setDef(level, element2.getAsInt());
					break;
				case "maxhp":
					out.setMaxHp(level, element2.getAsInt());
					break;
				case "maxmp":
					out.setMaxMp(level, element2.getAsInt());
					break;
				case "abilities":
					JsonArray abilities = entry2.getValue().getAsJsonArray();
					List<String> abilitiesArray = new ArrayList<String>();
					
					for(int i= 0; i < abilities.size(); i++) {
						abilitiesArray.add(abilities.get(i).getAsString());
					}
					out.setAbilities(level, abilitiesArray.toArray(new String[0]));
					break;
				case "shotlocks":
					JsonArray shotlocks = entry2.getValue().getAsJsonArray();
					List<String> shotlocksArray = new ArrayList<String>();
					
					for(int i= 0; i < shotlocks.size(); i++) {
						shotlocksArray.add(shotlocks.get(i).getAsString());
					}
					out.setShotlocks(level, shotlocksArray.toArray(new String[0]));
					break;
				case "spells":
					JsonArray spells = entry2.getValue().getAsJsonArray();
					List<String> spellsArray = new ArrayList<String>();
					
					for(int i= 0; i < spells.size(); i++) {
						spellsArray.add(spells.get(i).getAsString());
					}
					out.setSpells(level, spellsArray.toArray(new String[0]));
					break;
					
				case "max_accessories":
					out.setMaxAccessories(level, element2.getAsInt());
					break;
					
				case "max_armors":
					out.setMaxArmors(level, element2.getAsInt());
					break;
				}
			});

		});
		return out;
	}
}
