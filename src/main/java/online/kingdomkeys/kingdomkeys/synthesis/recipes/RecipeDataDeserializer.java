package online.kingdomkeys.kingdomkeys.synthesis.recipes;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/keyblades/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class RecipeDataDeserializer implements JsonDeserializer<RecipeData> {

    @Override
    public RecipeData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	RecipeData out = new RecipeData();
        JsonObject jsonObject = json.getAsJsonObject();

        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            switch (entry.getKey()) {//Check for the first level key
                case "recipe":
                    Map<Material, Integer> recipe = new HashMap<>();//Create the recipe
                    JsonArray recipeArray = element.getAsJsonArray(); //Get the array
                    recipeArray.forEach(ingredient -> {//Iterate through all the ingredients
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
                    });
                    out.setMaterials(recipe);
                    break;                           
                    
                case "output":
                	 JsonObject outputObject = element.getAsJsonObject();
                     boolean valid = outputObject.get("item") != null && outputObject.get("quantity") != null;
                     if(valid) {
                    	 Item keychain = ForgeRegistries.ITEMS.getValue(new ResourceLocation(outputObject.get("item").getAsString()));
                         out.setResult(keychain, outputObject.get("quantity").getAsInt());
                         System.out.println("Loaded recipe for "+keychain.getName().getFormattedText());
                     }
                            	 
                   
                    break;
            }
        });
        KingdomKeys.LOGGER.info("KEYCHAIN: {}, LEVELS: {}, DESCRIPTION: {}", out.result,out.amount, out.materials);
        return out;
    }
}
