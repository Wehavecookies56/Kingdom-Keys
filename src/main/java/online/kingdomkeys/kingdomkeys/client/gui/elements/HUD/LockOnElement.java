package online.kingdomkeys.kingdomkeys.client.gui.elements.HUD;

import com.google.gson.JsonObject;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

public class LockOnElement extends HUDElement {

    public int lockOnIconScale = 75;
    public int lockOnIconRotationSpeed = 16;
    public int hpPerBar = 40;

    public LockOnElement(String name) {
        super(name);
    }

    @Override
    public JsonObject loadDefaultsFromJson() {
        //Load common and return object
        JsonObject json = super.loadDefaultsFromJson();
        if(json != null) {
            if (json.has("lockOnIconScale"))
                lockOnIconScale = json.get("lockOnIconScale").getAsInt();
            else
                lockOnIconScale = 75;

            if (json.has("lockOnIconRotationSpeed"))
                lockOnIconRotationSpeed = json.get("lockOnIconRotationSpeed").getAsInt();
            else
                lockOnIconRotationSpeed = 16;

            if (json.has("hpPerBar"))
                hpPerBar = json.get("hpPerBar").getAsInt();
            else
                hpPerBar = 40;

            ModConfigs.setLockOnIconScale(lockOnIconScale);
            ModConfigs.setLockOnIconRotation(lockOnIconRotationSpeed);
            ModConfigs.setLockOnHpPerBar(hpPerBar);
        }
        return json;
    }
}
