package online.kingdomkeys.kingdomkeys.magic;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

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

			switch (entry.getKey()) {
				case "cost" -> out.setCost(element.getAsInt());
				case "casttime" -> out.setCasttime(element.getAsInt());
				case "cooldown" -> out.setCooldown(element.getAsInt());
				case "dmg_mult" -> out.setDmgMult(element.getAsFloat());
				case "dmg_mult_max" -> out.setDmgMultMax(element.getAsFloat());
				case "magic_lock_on" -> out.setMagicLockon(element.getAsBoolean());
				case "max_exp" -> out.setMaxExp(element.getAsInt());
				case "max_lvl" -> out.setMaxLevel(element.getAsInt());
				case "next_tier" -> out.setNextTier(ResourceLocation.parse(element.getAsString()));
				case "magic_rc" -> out.setMagicRC(ResourceLocation.parse(element.getAsString()));
				case "spell_type" -> out.setSpellType(MagicData.SpellType.valueOf(element.getAsString().toUpperCase()));
			}
		});

		return out;
	}
}