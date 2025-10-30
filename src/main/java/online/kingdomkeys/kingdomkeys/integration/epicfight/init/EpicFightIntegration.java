package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.ComboExtender;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;

public class EpicFightIntegration {

    public static void initIntegration(IEventBus modEventBus) {
        //EpicFightUtils.setBattleMode((player) -> EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode());
        modEventBus.addListener(KKAnimations::register);
        modEventBus.addListener(EpicKKWeapons::register);
        KKSkills.SKILLS.register(modEventBus);
        ComboExtender.DATA_KEYS.register(modEventBus);
    }

}
