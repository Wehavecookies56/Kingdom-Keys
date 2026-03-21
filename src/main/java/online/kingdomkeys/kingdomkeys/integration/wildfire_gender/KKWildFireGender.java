package online.kingdomkeys.kingdomkeys.integration.wildfire_gender;

import com.wildfire.api.IGenderArmor;
import com.wildfire.api.WildfireAPI;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class KKWildFireGender {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(WildfireAPI.GENDER_ARMOR_CAPABILITY, (IStackHelper, context) -> new IGenderArmor() {
                @Override
                public boolean alwaysHidesBreasts() {
                    return true;
                }
            }, ModItems.terra_Chestplate.get(), ModItems.aqua_Chestplate.get(), ModItems.ventus_Chestplate.get(), ModItems.eraqus_Chestplate.get(), ModItems.ux_Chestplate.get(), ModItems.nightmareVentus_Chestplate.get(), ModItems.xehanort_Chestplate.get());
    }

}
