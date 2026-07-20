package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities.ShieldCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.EpicKKWeaponEnum;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import yesman.epicfight.EpicFight;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.registry.deferred.ItemPresetRegister;
import yesman.epicfight.registry.deferred.holders.DeferredWeapon;
import yesman.epicfight.registry.entries.EpicFightMovesets;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.registry.entries.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;
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

    public static final Function<Item, WeaponCapability.Builder> CHAKRAM = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_CHAKRAM)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KK_CHAKRAM ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_CHAKRAM)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.AXEL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND, itemstack -> EpicFightSkills.EVISCERATE.get()).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH.get())
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);

/*
    public static final Function<Item, WeaponCapability.Builder> KEYBLADE = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                            switch (PlayerData.get((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                                case Strings.Form_Valor -> KKStyles.VALOR_FORM;
                                case Strings.Form_Master -> KKStyles.MASTER_FORM;
                                case Strings.Form_Wisdom -> KKStyles.WISDOM_FORM;
                                case Strings.Form_Final -> KKStyles.FINAL_FORM;
                                default -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory()
                                        == CapabilityItem.WeaponCategories.SWORD ?
                                        switch (PlayerData.get((Player) playerpatch.getOriginal()).getDualStyle()) {
                                            case KH2_ROXAS_DUAL -> KKStyles.KH2_ROXAS_DUAL;
                                            case DAYS_ROXAS_DUAL -> KKStyles.DAYS_ROXAS_DUAL;
                                        }
                                        :
                                        switch (PlayerData.get((Player) playerpatch.getOriginal()).getSingleStyle()) {
                                            case ROXAS -> KKStyles.ROXAS;
                                            case SORA -> KKStyles.SORA;
                                            case RIKU -> KKStyles.RIKU;
                                            case TERRA -> KKStyles.TERRA;
                                            case AQUA -> KKStyles.AQUA;
                                            case VENTUS -> KKStyles.VENTUS;
                                        };
                            })
                    .hitSound(EpicFightSounds.BLADE_HIT.get()).collider(KKCollider.KEYBLADE)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD)

                    .livingMotionModifier(KKStyles.SORA, LivingMotions.IDLE, KKAnimations.SORA_IDLE)
                    .livingMotionModifier(KKStyles.RIKU, LivingMotions.IDLE, KKAnimations.SORA_IDLE)
                    .livingMotionModifier(KKStyles.ROXAS, LivingMotions.IDLE, KKAnimations.SORA_IDLE)
                    .livingMotionModifier(KKStyles.TERRA, LivingMotions.IDLE, KKAnimations.SORA_IDLE)
                    .livingMotionModifier(KKStyles.AQUA, LivingMotions.IDLE, KKAnimations.SORA_IDLE)
                    .livingMotionModifier(KKStyles.VENTUS, LivingMotions.IDLE, KKAnimations.SORA_IDLE)

                    .livingMotionModifier(KKStyles.KH2_ROXAS_DUAL, LivingMotions.IDLE, KKAnimations.VALOR_FORM_IDLE)
                    .livingMotionModifier(KKStyles.DAYS_ROXAS_DUAL, LivingMotions.IDLE, KKAnimations.VALOR_FORM_IDLE)

                    .livingMotionModifier(KKStyles.VALOR_FORM, LivingMotions.IDLE, KKAnimations.VALOR_FORM_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM_FORM, LivingMotions.IDLE, KKAnimations.WISDOM_FORM_IDLE)
                    .livingMotionModifier(KKStyles.MASTER_FORM, LivingMotions.IDLE, KKAnimations.MASTER_FORM_IDLE)
                    .livingMotionModifier(KKStyles.FINAL_FORM, LivingMotions.IDLE, KKAnimations.FINAL_FORM_IDLE)

                    .livingMotionModifier(KKStyles.WISDOM_FORM, LivingMotions.WALK, KKAnimations.WISDOM_FORM_RUN)
                    .livingMotionModifier(KKStyles.MASTER_FORM, LivingMotions.WALK, KKAnimations.MASTER_FORM_WALK)
                    .livingMotionModifier(KKStyles.FINAL_FORM, LivingMotions.WALK, KKAnimations.FINAL_FORM_IDLE)

                    .livingMotionModifier(KKStyles.VALOR_FORM, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.WISDOM_FORM, LivingMotions.RUN, KKAnimations.WISDOM_FORM_RUN)
                    .livingMotionModifier(KKStyles.MASTER_FORM, LivingMotions.RUN, KKAnimations.MASTER_FORM_RUN)
                    .livingMotionModifier(KKStyles.FINAL_FORM, LivingMotions.RUN, KKAnimations.FINAL_FORM_IDLE)

                    .newStyleCombo(KKStyles.VALOR_FORM, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO2, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO3, KKAnimations.VALOR_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.WISDOM_FORM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(KKStyles.MASTER_FORM, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.FINAL_FORM, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)

                    .newStyleCombo(KKStyles.SORA, KKAnimations.SORA_AUTO1, KKAnimations.SORA_AUTO2, KKAnimations.SORA_AUTO3, KKAnimations.SORA_FINISHER1, KKAnimations.SORA_FINISHER1, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(KKStyles.VENTUS, Animations.SWORD_DUAL_AUTO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.AXE_AUTO1, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(KKStyles.RIKU, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.AXE_AUTO1, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(KKStyles.ROXAS, Animations.SWORD_DUAL_AUTO3, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.AXE_AUTO1, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(KKStyles.TERRA, Animations.SWORD_DUAL_AUTO2, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.AXE_AUTO1, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(KKStyles.AQUA, Animations.AXE_AUTO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.AXE_AUTO1, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)

                    .newStyleCombo(KKStyles.KH2_ROXAS_DUAL, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.DAYS_ROXAS_DUAL, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)

                    .livingMotionModifier(KKStyles.SORA, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(KKStyles.ROXAS, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(KKStyles.RIKU, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(KKStyles.TERRA, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(KKStyles.AQUA, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(KKStyles.VENTUS, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(KKStyles.WISDOM_FORM, LivingMotions.BLOCK, Animations.SWORD_GUARD)

                    .livingMotionModifier(KKStyles.KH2_ROXAS_DUAL, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.DAYS_ROXAS_DUAL, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR_FORM, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.MASTER_FORM, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.FINAL_FORM, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .passiveSkill(KKSkills.comboExtender.get());
    */
    public static final Function<Item, WeaponCapability.Builder> KK_SHIELD = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SHIELD)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .collider(ColliderPreset.SWORD)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK)
                    .weaponCombinationPredicator(entityPatch ->
                            EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_SHIELD)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.SWORD_AUTO1, KKAnimations.KK_SHIELD_AUTO2, Animations.DAGGER_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH).constructor(ShieldCapabilities::new);

    private EpicKKWeapons() {
    }

    public static void register() {
        EpicFightEventHooks.Registry.WEAPON_CAPABILITY_PRESET.registerEvent(event -> {
            event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, EpicKKWeaponEnum.KK_CHAKRAM.toString().toLowerCase()), CHAKRAM);
            event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, EpicKKWeaponEnum.KK_SHIELD.toString().toLowerCase()), KK_SHIELD);
            //event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, EpicKKWeaponEnum.KK_KEYBLADE.toString().toLowerCase()), KEYBLADE);
        }, KingdomKeys.MODID);
    }
}
