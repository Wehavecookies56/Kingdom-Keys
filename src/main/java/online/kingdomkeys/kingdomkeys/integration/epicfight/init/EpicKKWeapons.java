package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.EpicKKWeaponEnum;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import yesman.epicfight.EpicFight;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.registry.deferred.ItemPresetRegister;
import yesman.epicfight.registry.deferred.holders.DeferredWeapon;
import yesman.epicfight.registry.entries.EpicFightMovesets;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

public class EpicKKWeapons {

    public static final ItemPresetRegister WEAPONS = ItemPresetRegister.create(KingdomKeys.MODID);

    public static final DeferredWeapon KEYBLADE = WEAPONS.registerWeapon("kk_keyblade",
            () -> WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_KEYBLADE)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(KKCollider.KEYBLADE)
                    .setTierValues(0, 10d, 0.7, 0.3)
                    .addMoveset(KKStyles.SORA, KKMoveSets.SORA_MOVESET)
                    .addMoveset(KKStyles.RIKU, KKMoveSets.SORA_MOVESET)
                    .addMoveset(KKStyles.AQUA, KKMoveSets.SORA_MOVESET)
                    .addMoveset(KKStyles.ROXAS, KKMoveSets.SORA_MOVESET)
                    .addMoveset(KKStyles.VENTUS, KKMoveSets.SORA_MOVESET)
                    .addMoveset(KKStyles.TERRA, KKMoveSets.SORA_MOVESET)
                    .addMoveset(KKStyles.KH2_ROXAS_DUAL, EpicFightMovesets.SWORD_DUAL)
                    .addMoveset(KKStyles.DAYS_ROXAS_DUAL, EpicFightMovesets.SWORD_DUAL)
                    .addMoveset(KKStyles.VALOR_FORM, KKMoveSets.VALOR_FORM_MOVESET)
                    .addMoveset(KKStyles.WISDOM_FORM, KKMoveSets.WISDOM_FORM_MOVESET)
                    .addMoveset(KKStyles.MASTER_FORM, KKMoveSets.MASTER_FORM_MOVESET)
                    .addMoveset(KKStyles.FINAL_FORM, KKMoveSets.FINAL_FORM_MOVESET)
                    .addConditionals(KKProviderConditionals.SORA_STYLE, KKProviderConditionals.RIKU_STYLE,
                            KKProviderConditionals.AQUA_STYLE, KKProviderConditionals.ROXAS_STYLE,
                            KKProviderConditionals.VENTUS_STYLE, KKProviderConditionals.TERRA_STYLE,
                            KKProviderConditionals.MASTER_FORM_STYLE, KKProviderConditionals.VALOR_FORM_STYLE,
                            KKProviderConditionals.FINAL_FORM_STYLE, KKProviderConditionals.WISDOM_FORM_STYLE)
                    .addTag(EpicFight.identifier("kk_keyblade"))
                    .addTag(EpicFight.identifier("kk_weapon"))

    );


    public static final DeferredWeapon CHAKRAM = WEAPONS.registerWeapon("kk_chakram",
            () -> WeaponCapability.builder()
            .category(EpicKKWeaponEnum.KK_CHAKRAM)
            .hitSound(EpicFightSounds.BLADE_HIT)
            .collider(ColliderPreset.SWORD)
            .setTierValues(0, 10d, 0.7, 0.3)
            .addMoveset(CapabilityItem.Styles.ONE_HAND, KKMoveSets.CHAKRAM_MOVESET));
}
