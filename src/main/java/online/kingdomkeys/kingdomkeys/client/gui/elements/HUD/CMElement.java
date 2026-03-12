package online.kingdomkeys.kingdomkeys.client.gui.elements.HUD;

import com.google.gson.JsonObject;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class CMElement extends HUDElement {

    public boolean classicColors = false;
    public int selectedXOffset = 5;
    public int submenuXOffset = 130;
    public boolean headerTitle = true;
    public int textXOffset = 0;

    public CMElement(String name) {
        super(name);
    }

    @Override
    public JsonObject loadDefaultsFromJson() {
        //Load common and return object
        JsonObject json = super.loadDefaultsFromJson();
        if(json != null) {
            if (json.has("classicColors"))
                classicColors = json.get("classicColors").getAsBoolean();
            else
                classicColors = false;

            if (json.has("selectedXOffset"))
                selectedXOffset = json.get("selectedXOffset").getAsInt();
            else
                selectedXOffset = 5;

            if (json.has("submenuXOffset"))
                submenuXOffset = json.get("submenuXOffset").getAsInt();
            else
                submenuXOffset = 100;

            if (json.has("headerTitle"))
                headerTitle = json.get("headerTitle").getAsBoolean();
            else
                headerTitle = true;

            if (json.has("textXOffset"))
                textXOffset = json.get("textXOffset").getAsInt();
            else
                textXOffset = 0;


           ModConfigs.setCmClassicColors(classicColors);
           ModConfigs.setCmSelectedXOffset(selectedXOffset);
           ModConfigs.setCmSubXOffset(submenuXOffset);
           ModConfigs.setCmHeaderTextVisible(headerTitle);
           ModConfigs.setCmTextXOffset(textXOffset);
        }
        return json;
    }
}
