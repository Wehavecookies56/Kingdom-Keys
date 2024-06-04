package online.kingdomkeys.kingdomkeys.util;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.ability.AbilityEvent;

public class APITests {

    @SubscribeEvent
    public void equipAbility(AbilityEvent.Equip event) {
        if (!event.isClient()) {
            KingdomKeys.LOGGER.debug("Equipped an ability({}) serverside!", event.getAbility().getRegistryName());
        }
    }

    @SubscribeEvent
    public void unequipAbility(AbilityEvent.Unequip event) {
        if (event.getAbility().equals(ModAbilities.SCAN.get())) {
            KingdomKeys.LOGGER.debug("No you cannot unequip scan, sorry");
            event.setCanceled(true);
        }
    }

}
