package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/keyblades/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class RecipeDataDeserializer implements JsonDeserializer<Recipe> {

    @Override
    public Recipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	Recipe out = new Recipe();
        JsonObject jsonObject = json.getAsJsonObject();

        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            switch (entry.getKey()) {//Check for the first level key
                case "ingredients":
                    Map<Material, Integer> recipe = new HashMap<>();//Create the recipe
                    JsonArray recipeArray = element.getAsJsonArray(); //Get the array
                    recipeArray.forEach(ingredient -> {//Iterate through all the ingredients
                        JsonObject ingredientObject = ingredient.getAsJsonObject();
                        Material m = null;
                        int quantity = 0;
                        boolean valid = ingredientObject.get("material") != null && ingredientObject.get("quantity") != null;
						if (valid) {
                            m = ModMaterials.registry.get().getValue(new ResourceLocation(ingredientObject.get("material").getAsString()));
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
                    
                case "cost":
                	out.setCost(element.getAsInt());
                	break;
                
                case "output":
                	 JsonObject outputObject = element.getAsJsonObject();
                     boolean valid = outputObject.get("item") != null && outputObject.get("quantity") != null;
                     if(valid) {
                    	 Item keychain = ForgeRegistries.ITEMS.getValue(new ResourceLocation(outputObject.get("item").getAsString()));
                         out.setResult(keychain, outputObject.get("quantity").getAsInt());
                         out.setType(outputObject.get("type").getAsString());
                     }
                    break;
                    
                case "tier":
                	out.setTier(element.getAsInt());
                	break;
            }
        });
        //KingdomKeys.LOGGER.info("OUTPUT: {}, TYPE {}, QUANTITY: {}, INGREDIENTS: {}", out.result, out.type, out.amount, out.materials);
        return out;
    }
}
