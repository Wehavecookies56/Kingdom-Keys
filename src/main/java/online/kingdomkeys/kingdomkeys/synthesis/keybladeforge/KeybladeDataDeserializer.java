package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/keyblades/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class KeybladeDataDeserializer implements JsonDeserializer<KeybladeData> {

    //TODO read the ability array

    @Override
    public KeybladeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        KeybladeData out = new KeybladeData();
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            switch (entry.getKey()) {
                case "keychain":
                    //Get item from the registry using the supplied resource location
                    Item keychain = ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString()));
                    //Make sure the item is valid
                    if (keychain != null) {
                        out.setKeychain(keychain);
                    }
                    break;
                case "base_stats":
                    JsonObject statsObject = element.getAsJsonObject();
                    statsObject.entrySet().forEach(statsEntry -> {
                        JsonElement statsElement = statsEntry.getValue();
                        switch(statsEntry.getKey()) {
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
                    //A keyblade without a keychain doesn't have upgrades so only read levels if it does have one
                    if (out.keychain != null) {
                        List<KeybladeLevel> levels = new ArrayList<>();
                        JsonArray levelsArray = element.getAsJsonArray();
                        levelsArray.forEach(e -> {
                            KeybladeLevel level = new KeybladeLevel();
                            Map<Material, Integer> recipe = new HashMap<>();
                            JsonObject levelObject = e.getAsJsonObject();
                            levelObject.entrySet().forEach(levelEntry -> {
                                JsonElement levelElement = levelEntry.getValue();
                                switch(levelEntry.getKey()) {
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
                                            Material m = null;
                                            int quantity = 0;
                                            boolean valid = ingredientObject.get("material") != null && ingredientObject.get("quantity") != null;
                                            if (valid) {
                                                m = GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation(ingredientObject.get("material").getAsString()));
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
                                    //Optional value currently just a string that does nothing //TODO abilities, use string as ability ResourceLocation, maybe support multiple
                                    case "ability":
                                        level.setAbility(levelElement.getAsString());
                                        break;
                                }
                            });
                            levels.add(level);
                        });
                        out.setLevels(levels);
                    }
                    break;
                case "description":
                    //Don't care if the string is empty or what it contains
                    out.setDescription(element.getAsString());
                    break;
            }
        });
        KingdomKeys.LOGGER.info("KEYCHAIN: {}, LEVELS: {}, DESCRIPTION: {}", out.keychain, out.levels, out.description);
        return out;
    }
}
