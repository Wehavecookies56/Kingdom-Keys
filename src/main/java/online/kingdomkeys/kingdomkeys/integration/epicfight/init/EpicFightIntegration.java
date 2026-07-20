package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.neoforged.bus.api.IEventBus;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.EpicKKWeaponEnum;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.ComboExtender;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

public class EpicFightIntegration {

    public static void initIntegration(IEventBus modEventBus) {
        WeaponCategory.ENUM_MANAGER.registerEnumCls(KingdomKeys.MODID, EpicKKWeaponEnum.class);
        modEventBus.addListener(KKAnimations::register);
        EpicFightEventHooks.Player.CAST_SKILL.registerEvent(ComboExtender::skillCastEvent);
        EpicKKWeapons.register();
        KKSkills.SKILLS.register(modEventBus);
        ComboExtender.DATA_KEYS.register(modEventBus);
        KKMoveSets.MOVESETS.register(modEventBus);
        KKProviderConditionals.CONDITIONALS.register(modEventBus);
        EpicKKWeapons.WEAPONS.register(modEventBus);
    }

}
