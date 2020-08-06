package online.kingdomkeys.kingdomkeys.synthesis.recipes;

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
public class RecipeDataDeserializer implements JsonDeserializer<RecipeData> {

    //TODO read the ability array

    @Override
    public RecipeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	RecipeData out = new RecipeData();
        JsonObject jsonObject = json.getAsJsonObject();

        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            switch (entry.getKey()) {
                case "synthesis":
                    //A keyblade without a keychain doesn't have upgrades so only read levels if it does have one
                    Map<Material, Integer> recipe = new HashMap<>();
                    JsonObject synthesisObject = element.getAsJsonObject();
                    
                    synthesisObject.entrySet().forEach(synthesisEntry -> {
                        JsonElement synthesisElement = synthesisEntry.getValue();
                        switch(synthesisEntry.getKey()) {
                            case "recipe":
                                JsonArray recipeArray = synthesisElement.getAsJsonArray();
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
                                    out.setMaterials(recipe);
                                });
                                break;                           
                        }
                    });
                    
                case "result":
                    Item keychain = ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString()));
                    out.setResult(keychain);
                    break;
            }
        });
        //KingdomKeys.LOGGER.info("KEYCHAIN: {}, LEVELS: {}, DESCRIPTION: {}", out.keychain, out.levels, out.description);
        return out;
    }
}
