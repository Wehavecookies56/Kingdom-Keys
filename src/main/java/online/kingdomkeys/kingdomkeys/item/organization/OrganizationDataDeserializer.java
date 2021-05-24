package online.kingdomkeys.kingdomkeys.item.organization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Custom deserializer for Keyblade Data json files located in data/kingdomkeys/keyblades/
 * Str and Mag are integers
 * Keychain can be null therefore an invalid registry name will be treated as having no keychain
 * A keyblade with no keychain does not need the levels object
 * Levels do not require an ability
 * Description can be empty
 */
public class OrganizationDataDeserializer implements JsonDeserializer<OrganizationData> {

    @Override
    public OrganizationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        OrganizationData out = new OrganizationData();
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            switch (entry.getKey()) {
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
                case "description":
                    //Don't care if the string is empty or what it contains
                    out.setDescription(element.getAsString());
                    break;
            }
        });
      //  KingdomKeys.LOGGER.info("DESCRIPTION: {}", out.description);
        return out;
    }
}
