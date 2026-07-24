package online.kingdomkeys.kingdomkeys.shotlock;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ShotlockDataDeserializer implements JsonDeserializer<ShotlockData> {
	@Override
	public ShotlockData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		ShotlockData out = new ShotlockData();
		JsonObject jsonObject = json.getAsJsonObject();
		jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();
			switch (entry.getKey()) {
			case "cooldown":
				out.setCooldown(element.getAsInt());
				break;
			case "cooldown_max":
				out.setCooldownMax(element.getAsInt());
				break;
			case "max":
				out.setMax(element.getAsInt());
				break;
			case "dmg_mult":
				out.setDmgMult(element.getAsFloat());
				break;
			case "dmg_mult_max":
				out.setDmgMultMax(element.getAsFloat());
				break;
			case "max_exp":
				out.setMaxExp(element.getAsInt());
				break;
			case "max_level":
				out.setMaxLevel(element.getAsInt());
				break;
			case "element":
				out.setElement(element.getAsString());
				break;
			}
		});
		return out;
	}
}
