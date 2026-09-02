package online.kingdomkeys.kingdomkeys.ability;

import com.google.gson.*;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;

import java.lang.reflect.Type;

public class AbilityDataDeserializer implements JsonDeserializer<AbilityData> {

	@Override
	public AbilityData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		AbilityData out = new AbilityData();
		JsonObject jsonObject = json.getAsJsonObject();

		jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();

			switch (entry.getKey()) {
				case "ap_cost" -> out.setAPCost(element.getAsInt());
				case "order" -> out.setOrder(element.getAsInt());
				case "exclusion_group" -> out.setExclusionGroup(element.isJsonNull() ? null : element.getAsString());
				case "type" -> readType(out, element.getAsString());
			}
		});

		return out;
	}

	private void readType(AbilityData out, String name) {
		try {
			out.setType(AbilityType.valueOf(name.trim().toUpperCase()));
		} catch (IllegalArgumentException unknown) {
			// null means the ability keeps its registered type rather than breaking the menu
			KingdomKeys.LOGGER.warn("Unknown ability type {} in ability data, keeping the registered one", name.trim());
		}
	}
}
