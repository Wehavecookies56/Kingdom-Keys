package online.kingdomkeys.kingdomkeys.client.gui.elements.HUD;

import com.google.gson.JsonObject;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class PartyElement extends HUDElement {

    public int distance = 10;

    public PartyElement(String name) {
        super(name);
    }

    @Override
    public JsonObject loadDefaultsFromJson() {
        //Load common and return object
        JsonObject json = super.loadDefaultsFromJson();
        if(json != null) {
            if (json.has("distance"))
                distance = json.get("distance").getAsInt();
            else
                distance = 70;

            ModConfigs.setPartyYDistance(distance);
        }
        return json;
    }
}
