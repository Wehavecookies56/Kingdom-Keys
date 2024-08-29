package online.kingdomkeys.kingdomkeys.util;

import net.neoforged.bus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.api.event.ChoiceEvent;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.ModItems;

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

    @SubscribeEvent
    public void equipSomething(EquipmentEvent event) {
        if (!event.getPlayer().level().isClientSide) {
            if (event.getNewStack() == null) {
                KingdomKeys.LOGGER.debug("Equipped/Unequipped a shotlock {}", ((EquipmentEvent.Shotlock) event).getNewShotlock().getRegistryName());
            } else {
                KingdomKeys.LOGGER.debug("{} was equipped now {} is", event.getPreviousStack(), event.getNewStack());
            }
        }
    }

    @SubscribeEvent
    public void equipKeychain(EquipmentEvent.Keychain event) {
        if (event.getKeychainSlot().equals(DriveForm.SYNCH_BLADE) && event.getPreviousStack().getItem() == ModItems.k111c.get()) {
            KingdomKeys.LOGGER.debug("Nope you must keep k111 in your synch blade slot now");
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void makeChoice(ChoiceEvent event) {
        KingdomKeys.LOGGER.debug("Chose {}, Sacrificed {}", event.getChoice(), event.getSacrifice());
    }

}
