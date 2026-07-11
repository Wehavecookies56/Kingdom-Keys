package online.kingdomkeys.kingdomkeys.synthesis.shop;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;

import java.lang.reflect.Type;

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
		boolean setNames = false;
        for(JsonElement e : jsonArray) {
        	ShopItem shopItem = new ShopItem();
        	JsonObject jsonObj = e.getAsJsonObject();
			if (jsonObj.get("names") != null && !setNames) {
				ResourceLocation namesPath = ResourceLocation.parse(jsonObj.get("names").getAsString());
				if (NamesListRegistry.getInstance().containsKey(namesPath)) {
					out.setNames(namesPath);
				}
				setNames = true;
			} else {
				boolean valid = jsonObj.get("item") != null && jsonObj.get("amount") != null;
				if (valid) {
					Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(jsonObj.get("item").getAsString()));
					shopItem.setResult(item, jsonObj.get("amount").getAsInt());
					shopItem.setTier(jsonObj.get("tier").getAsInt());
					shopItem.setCost(jsonObj.get("cost").getAsInt());
					shopItem.setMatReq(jsonObj.has("mat_req") ? jsonObj.get("mat_req").getAsInt() : 0);
					shopItem.setRequireAll(jsonObj.has("condition") ? jsonObj.get("condition").getAsString().equals("all") : false);
					out.addToList(shopItem);
					KingdomKeys.LOGGER.info("OUTPUT: {}, TIER {}, QUANTITY: {}", shopItem.result, shopItem.tier, shopItem.amount);
				}
			}
        }
        return out;
    }
}
