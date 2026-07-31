package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import yesman.epicfight.api.ex_cap.data.Moveset;
import yesman.epicfight.api.ex_cap.data.modifier.WeaponModifier;
import yesman.epicfight.registry.deferred.ModifierRegister;
import yesman.epicfight.registry.deferred.holders.DeferredModifier;
import yesman.epicfight.registry.deferred.holders.DeferredMoveset;
import yesman.epicfight.registry.entries.EpicFightItemCapabilityPresets;
import yesman.epicfight.registry.entries.EpicFightMovesets;
import yesman.epicfight.registry.entries.EpicFightProviderConditionals;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;

public class KKWeaponModifiers {
    public static final ModifierRegister WEAPON_MODIFIERS = ModifierRegister.create(KingdomKeys.MODID);

    public static final DeferredModifier FIST_MODIFIER = WEAPON_MODIFIERS.registerModifier("fist",
            () -> WeaponModifier.builder().target(EpicFightItemCapabilityPresets.FIST)
                    .addMovesetModifier(KKStyles.ANTI_FORM, EpicFightMovesets.SWORD_DUAL)
                    .addConditionalModifier(KKProviderConditionals.ANTI_FORM_STYLE)
                    .removeConditionalModifier(EpicFightProviderConditionals.DEFAULT_1H_WIELD_STYLE)
    );
}
