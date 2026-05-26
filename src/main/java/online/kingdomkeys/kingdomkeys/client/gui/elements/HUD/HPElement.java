package online.kingdomkeys.kingdomkeys.client.gui.elements.HUD;

import com.google.gson.JsonObject;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class HPElement extends HUDElement {

    public boolean showHearts = false;
    public int lowHPAlarm = 10;

    public HPElement(String name) {
        super(name);
    }

    @Override
    public JsonObject loadDefaultsFromJson() {
        //Load common and return object
        JsonObject json = super.loadDefaultsFromJson();
        if(json != null) {
            if (json.has("showHearts"))
                showHearts = json.get("showHearts").getAsBoolean();
            else
                showHearts = false;

            if (json.has("lowHPAlarm"))
                lowHPAlarm = json.get("lowHPAlarm").getAsInt();
            else
                lowHPAlarm = 10;

            ModConfigs.setShowHearts(showHearts);
            ModConfigs.setHPAlarm(lowHPAlarm);
        }
        return json;
    }
}
