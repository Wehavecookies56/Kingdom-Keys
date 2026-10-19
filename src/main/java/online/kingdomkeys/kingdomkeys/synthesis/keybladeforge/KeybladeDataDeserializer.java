package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom deserializer for Keyblade Data json files located in
 * data/kingdomkeys/keyblades/ Str and Mag are integers Keychain can be null
 * therefore an invalid registry name will be treated as having no keychain A
 * keyblade with no keychain does not need the levels object Levels do not
 * require an ability Description can be empty
 */
public class KeybladeDataDeserializer implements JsonDeserializer<KeybladeData> {

	@Override
	public KeybladeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		KeybladeData out = new KeybladeData();
		JsonObject jsonObject = json.getAsJsonObject();
		jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();

			switch (entry.getKey()) {
			case "ability", "abilities":
				out.setBaseAbilities(readAbilities(element, out));
				break;
			case "keychain":
				// Get item from the registry using the supplied resource location
				Item keychain = BuiltInRegistries.ITEM.get(KingdomKeys.rl(element.getAsString()));
				// Make sure the item is valid
				if (keychain != null) {
					out.setKeychain(keychain);
				}
				break;
			case "reach":
             	out.setReach(element.getAsFloat());
             	break;
			case "sound":
				out.setSound(element.getAsString());
				break;
			case "hit_particles":
				List<ResourceLocation> particles = new ArrayList<>();

				if (element.isJsonArray()) {
					element.getAsJsonArray().forEach(particle -> particles.add(ResourceLocation.parse(particle.getAsString())));
				} else if (!element.getAsString().isEmpty()) {
					particles.add(ResourceLocation.parse(element.getAsString()));
				}

				out.setHitParticles(particles);
				break;
			case "hit_particle_scale":
				out.setHitParticleScale(element.getAsFloat());
				break;
			case "base_stats":
				JsonObject statsObject = element.getAsJsonObject();
				statsObject.entrySet().forEach(statsEntry -> {
					JsonElement statsElement = statsEntry.getValue();
					switch (statsEntry.getKey()) {
					case "str":
						out.setBaseStrength(statsElement.getAsInt());
						break;
					case "mag":
						out.setBaseMagic(statsElement.getAsInt());
						break;
					}
				});
				break;
			case "levels":
				// A keyblade without a keychain doesn't have upgrades so only read levels if it does have one
				if (out.keychain != null) {
					List<KeybladeLevel> levels = getKeybladeLevels(json, element, out);
					out.setLevels(levels);
				}
				break;
			case "description":
				// Don't care if the string is empty or what it contains
				out.setDescription(element.getAsString());
				break;
			}
		});
		return out;
	}

	private static @NotNull List<KeybladeLevel> getKeybladeLevels(JsonElement json, JsonElement element, KeybladeData out) {
		List<KeybladeLevel> levels = new ArrayList<>();
		JsonArray levelsArray = element.getAsJsonArray();
		levelsArray.forEach(e -> {
			KeybladeLevel level = new KeybladeLevel();
			Map<Item, Integer> recipe = new LinkedHashMap<>();
			JsonObject levelObject = e.getAsJsonObject();
			levelObject.entrySet().forEach(levelEntry -> {
				JsonElement levelElement = levelEntry.getValue();
				switch (levelEntry.getKey()) {
				case "str":
					level.setStrength(levelElement.getAsInt());
					break;
				case "mag":
					level.setMagic(levelElement.getAsInt());
					break;
				case "recipe":
					JsonArray recipeArray = levelElement.getAsJsonArray();
					recipeArray.forEach(ingredient -> {
						JsonObject ingredientObject = ingredient.getAsJsonObject();
						Item m = null;
						int quantity = 0;
						boolean valid = ingredientObject.get("material") != null && ingredientObject.get("quantity") != null;
						if (valid) {
							m = BuiltInRegistries.ITEM.get(KingdomKeys.rl(ingredientObject.get("material").getAsString()));
							if (m == null) {
								throw new JsonParseException("Material supplied in recipe cannot be found in the registry" + json);
							} else {
								quantity = ingredientObject.get("quantity").getAsInt();
								recipe.put(m, quantity);
							}
						} else {
							throw new JsonParseException("Invalid recipe ingredient, missing material/quantity" + json);
						}
						level.setMaterials(recipe);
					});
					break;
				case "ability", "abilities":
					level.setAbilities(readAbilities(levelElement, out));
					break;
				}
			});
			levels.add(level);
		});
		return levels;
	}

	/** Takes either a single ability name or an array of them, leaving out empty and unknown ones */
	private static List<ResourceLocation> readAbilities(JsonElement element, KeybladeData out) {
		List<ResourceLocation> abilities = new ArrayList<>();
		List<JsonElement> names = new ArrayList<>();
		if (element.isJsonArray()) {
			element.getAsJsonArray().forEach(names::add);
		} else {
			names.add(element);
		}

		for (JsonElement nameElement : names) {
			String name = nameElement.getAsString();
			if (name.isEmpty()) {
				continue;
			}
			Ability ability = ModAbilities.registry.get(KingdomKeys.rl(name));
			if (ability != null) {
				abilities.add(ability.getRegistryName());
			} else {
				KingdomKeys.LOGGER.error("Ability " + name + " does not exist for keyblade " + out.keychain);
			}
		}
		return abilities;
	}
}
