package online.kingdomkeys.kingdomkeys.synthesis.shop;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/keyblades/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class ShopListDataDeserializer implements JsonDeserializer<ShopList> {

    @Override
    public ShopList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	ShopList out = new ShopList();

        JsonArray jsonArray = json.getAsJsonArray();
        //System.out.println(jsonArray);
        for(JsonElement e : jsonArray) {
        	ShopItem shopItem = new ShopItem();
        	JsonObject jsonObj = e.getAsJsonObject();
            boolean valid = jsonObj.get("item") != null && jsonObj.get("amount") != null;
			if (valid) {
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObj.get("item").getAsString()));
				shopItem.setResult(item, jsonObj.get("amount").getAsInt());
				shopItem.setTier(jsonObj.get("tier").getAsInt());
				shopItem.setCost(jsonObj.get("cost").getAsInt());
				out.addToList(shopItem);
		        KingdomKeys.LOGGER.info("OUTPUT: {}, TIER {}, QUANTITY: {}", shopItem.result, shopItem.tier, shopItem.amount);
			}
        }
        
       /* jsonObject.forEach(entry -> {
            JsonElement element = entry.getValue();
            switch (entry.getKey()) {//Check for the first level key
                    
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
        });*/
        return out;
    }
}
