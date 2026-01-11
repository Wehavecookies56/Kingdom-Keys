package online.kingdomkeys.kingdomkeys.synthesis.shop.sell;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.lang.reflect.Type;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/keyblades/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class SellListDataDeserializer implements JsonDeserializer<SellList> {

    @Override
    public SellList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SellList out = new SellList();

        JsonArray jsonArray = json.getAsJsonArray();

        for(JsonElement e : jsonArray) {
        	SellItem shopItem = new SellItem();
        	JsonObject jsonObj = e.getAsJsonObject();
            boolean valid = jsonObj.get("item") != null && jsonObj.get("amount") != null;
            if (valid) {
                Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(jsonObj.get("item").getAsString()));
                shopItem.setResult(item);
                shopItem.setPrice(jsonObj.get("price").getAsInt());
                out.addToList(shopItem);
                KingdomKeys.LOGGER.info("OUTPUT: {}, PRICE {}", shopItem.result, shopItem.price);
            }

        }
        return out;
    }
}
