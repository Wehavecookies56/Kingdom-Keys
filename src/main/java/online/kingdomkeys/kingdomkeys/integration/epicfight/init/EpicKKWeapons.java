package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities.KKWeaponCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities.ShieldCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.Function;

public class EpicKKWeapons {
    public static final Function<Item, CapabilityItem.Builder> CHAKRAM = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.CHAKRAM)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.CHAKRAM ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.CHAKRAM)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.CHAKRAM_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND, itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD).constructor(KKWeaponCapabilities::new);

    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_ROXAS = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                            switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                                case Strings.Form_Valor -> KKStyles.VALOR;
                                case Strings.Form_Master -> KKStyles.MASTER;
                                case Strings.Form_Wisdom -> KKStyles.WISDOM;
                                case Strings.Form_Final -> CapabilityItem.Styles.TWO_HAND;
                                default -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory()
                                        == CapabilityItem.WeaponCategories.SWORD ? CapabilityItem.Styles.TWO_HAND :
                                        CapabilityItem.Styles.ONE_HAND;
                            })
                    .hitSound(EpicFightSounds.BLADE_HIT).collider(KKCollider.KEYBLADE)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.IDLE, KKAnimations.WISDOM_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.WALK, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.RUN, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO2, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.MASTER, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.WISDOM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_COMBO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);

    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_SORA = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                            switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                                case Strings.Form_Valor -> KKStyles.VALOR;
                                case Strings.Form_Master -> KKStyles.MASTER;
                                case Strings.Form_Wisdom -> KKStyles.WISDOM;
                                case Strings.Form_Final -> CapabilityItem.Styles.TWO_HAND;
                                default -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory()
                                        == CapabilityItem.WeaponCategories.SWORD ? CapabilityItem.Styles.TWO_HAND :
                                        CapabilityItem.Styles.ONE_HAND;
                            })
                    .hitSound(EpicFightSounds.BLADE_HIT).collider(KKCollider.KEYBLADE)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.IDLE, KKAnimations.WISDOM_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.WALK, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.RUN, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO2, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.MASTER, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.WISDOM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_COMBO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);

    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_RIKU = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                            switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                                case Strings.Form_Valor -> KKStyles.VALOR;
                                case Strings.Form_Master -> KKStyles.MASTER;
                                case Strings.Form_Wisdom -> KKStyles.WISDOM;
                                case Strings.Form_Final -> CapabilityItem.Styles.TWO_HAND;
                                default -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory()
                                        == CapabilityItem.WeaponCategories.SWORD ? CapabilityItem.Styles.TWO_HAND :
                                        CapabilityItem.Styles.ONE_HAND;
                            })
                    .hitSound(EpicFightSounds.BLADE_HIT).collider(KKCollider.KEYBLADE)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.IDLE, KKAnimations.WISDOM_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.WALK, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.RUN, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO2, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.MASTER, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.WISDOM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_COMBO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);


    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_TERRA = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                            switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                                case Strings.Form_Valor -> KKStyles.VALOR;
                                case Strings.Form_Master -> KKStyles.MASTER;
                                case Strings.Form_Wisdom -> KKStyles.WISDOM;
                                case Strings.Form_Final -> CapabilityItem.Styles.TWO_HAND;
                                default -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory()
                                        == CapabilityItem.WeaponCategories.SWORD ? CapabilityItem.Styles.TWO_HAND :
                                        CapabilityItem.Styles.ONE_HAND;
                            })
                    .hitSound(EpicFightSounds.BLADE_HIT).collider(KKCollider.KEYBLADE)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == CapabilityItem.WeaponCategories.SWORD)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.IDLE, KKAnimations.WISDOM_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.WALK, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.RUN, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO2, KKAnimations.VALOR_AUTO1, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.MASTER, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.WISDOM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_COMBO1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);

    public static final Function<Item, CapabilityItem.Builder> KK_SHIELD = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SHIELD)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK)
                    .weaponCombinationPredicator(entityPatch ->
                            EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_SHIELD)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.SWORD_AUTO1, KKAnimations.KK_SHIELD_AUTO2, Animations.DAGGER_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH).constructor(ShieldCapabilities::new);

    private EpicKKWeapons() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(EpicKKWeaponEnum.CHAKRAM.toString().toLowerCase(), CHAKRAM);
        event.getTypeEntry().put(EpicKKWeaponEnum.KK_SHIELD.toString().toLowerCase(), KK_SHIELD);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_roxas", KEYBLADE_ROXAS);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_sora", KEYBLADE_SORA);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_riku", KEYBLADE_RIKU);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_terra", KEYBLADE_TERRA);
    }

    public enum EpicKKWeaponEnum implements WeaponCategory {
        ETHEREAL_BLADE, ARROW_GUNS, LANCE, KK_SHIELD, AXE_SWORD, LEXICON, CLAYMORE, CHAKRAM, SITAR, CARD, SCYTHE,
        KNIVES, KEYBLADE;
        private final int id;

        EpicKKWeaponEnum() {
            this.id = WeaponCategory.ENUM_MANAGER.assign(this);
        }

        @Override
        public int universalOrdinal() {
            return id;
        }
    }
}
