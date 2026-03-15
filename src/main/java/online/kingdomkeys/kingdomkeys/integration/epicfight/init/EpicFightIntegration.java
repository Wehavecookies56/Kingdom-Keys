package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.neoforged.bus.api.IEventBus;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.ComboExtender;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import yesman.epicfight.api.event.EpicFightEventHooks;

public class EpicFightIntegration {

    public static void initIntegration(IEventBus modEventBus) {
        modEventBus.addListener(KKAnimations::register);
        EpicFightEventHooks.Player.CAST_SKILL.registerEvent(ComboExtender::skillCastEvent);
        EpicKKWeapons.register();
        KKSkills.SKILLS.register(modEventBus);
        ComboExtender.DATA_KEYS.register(modEventBus);
    }

}
