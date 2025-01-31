package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import java.util.function.Function;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities.ShieldCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.KKStyles;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
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

public class EpicKKWeapons {
    public static final Function<Item, CapabilityItem.Builder> CHAKRAM = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_CHAKRAM)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KK_CHAKRAM ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_CHAKRAM)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.CHAKRAM_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND, itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.IDLE, KKAnimations.CHAKRAM_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.IDLE, KKAnimations.CHAKRAM_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.WALK, KKAnimations.CHAKRAM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.WALK, KKAnimations.CHAKRAM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.RUN, KKAnimations.CHAKRAM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.RUN, KKAnimations.CHAKRAM_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);

    public static final Function<Item, CapabilityItem.Builder> ARROWGUN = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_ARROW_GUNS)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KK_ARROW_GUNS ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.LASER_BLAST.get())
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_ARROW_GUNS)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND, itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.IDLE, KKAnimations.XIGBAR_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.IDLE, KKAnimations.XIGBAR_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.WALK, KKAnimations.XIGBAR_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.WALK, KKAnimations.XIGBAR_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD);

    public static final Function<Item, CapabilityItem.Builder> CLAYMORE = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_CLAYMORE)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KK_CLAYMORE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_CLAYMORE)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, Animations.TRIDENT_AUTO1, Animations.LONGSWORD_AUTO3, Animations.GREATSWORD_DASH, Animations.TSUNAMI, Animations.GREATSWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.TRIDENT_AUTO1, Animations.LONGSWORD_AUTO3, Animations.GREATSWORD_DASH, Animations.TSUNAMI, Animations.GREATSWORD_AIR_SLASH)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.IDLE, KKAnimations.SAIX_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.WALK, KKAnimations.SAIX_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.RUN, KKAnimations.SAIX_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.IDLE, KKAnimations.SAIX_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.WALK, KKAnimations.SAIX_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.RUN, KKAnimations.SAIX_RUN);
  
    public static final Function<Item, CapabilityItem.Builder> ETHEREAL_BLADE = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_ETHEREAL_BLADE)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KK_ETHEREAL_BLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_ETHEREAL_BLADE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.IDLE, KKAnimations.XEMNAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.WALK, KKAnimations.XEMNAS_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.RUN, KKAnimations.XEMNAS_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.IDLE, KKAnimations.XEMNAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.WALK, KKAnimations.XEMNAS_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.CREATIVE_IDLE, KKAnimations.XEMNAS_FLY)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.CREATIVE_IDLE, KKAnimations.XEMNAS_FLY)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.CREATIVE_FLY, KKAnimations.XEMNAS_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.CREATIVE_FLY, KKAnimations.XEMNAS_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.RUN, KKAnimations.XEMNAS_RUN);

    public static final Function<Item, CapabilityItem.Builder> LANCE = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_LANCE)
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KK_LANCE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_LANCE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.IDLE, KKAnimations.XALDIN_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.WALK, KKAnimations.XALDIN_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.RUN, KKAnimations.XALDIN_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.IDLE, KKAnimations.XALDIN_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.WALK, KKAnimations.XALDIN_WALK)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.CREATIVE_IDLE, KKAnimations.XALDIN_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.CREATIVE_IDLE, KKAnimations.XALDIN_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND,LivingMotions.CREATIVE_FLY, KKAnimations.XALDIN_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.CREATIVE_FLY, KKAnimations.XALDIN_RUN)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND,LivingMotions.RUN, KKAnimations.XALDIN_RUN);


    public static final Function<Item, CapabilityItem.Builder> KEYBLADE = item ->
            WeaponCapability.builder()
                    .category(CapabilityItem.WeaponCategories.SWORD).styleProvider(playerpatch ->
                            switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm()) {
                                case Strings.Form_Valor -> KKStyles.VALOR;
                                case Strings.Form_Master -> KKStyles.MASTER;
                                case Strings.Form_Wisdom -> KKStyles.WISDOM;
                                case Strings.Form_Final -> KKStyles.FINAL;
                                default -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory()
                                        == CapabilityItem.WeaponCategories.SWORD ?
                                        switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getDualStyle()) {
                                            case KH2_ROXAS_DUAL -> KKStyles.KH2_ROXAS_DUAL;
                                            case DAYS_ROXAS_DUAL -> KKStyles.DAYS_ROXAS_DUAL;
                                        }
                                        :
                                        switch (ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getSingleStyle()) {
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

                    .livingMotionModifier(KKStyles.SORA, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.RIKU, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.ROXAS, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.TERRA, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.AQUA, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.VENTUS, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)



                    .livingMotionModifier(KKStyles.KH2_ROXAS_DUAL, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.DAYS_ROXAS_DUAL, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)

                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.IDLE, KKAnimations.WISDOM_IDLE)
                    .livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(KKStyles.FINAL, LivingMotions.IDLE, KKAnimations.FINAL_IDLE)

                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.WALK, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(KKStyles.FINAL, LivingMotions.WALK, KKAnimations.FINAL_IDLE)

                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.RUN, KKAnimations.WISDOM_RUN)
                    .livingMotionModifier(KKStyles.FINAL, LivingMotions.RUN, KKAnimations.FINAL_IDLE)

                    .newStyleCombo(KKStyles.VALOR, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO2, KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO3, KKAnimations.VALOR_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.WISDOM, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_FINISHER, KKAnimations.WISDOM_COMBO1, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(KKStyles.MASTER, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(KKStyles.FINAL, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)

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
                    .livingMotionModifier(KKStyles.WISDOM, LivingMotions.BLOCK, Animations.SWORD_GUARD)

                    .livingMotionModifier(KKStyles.KH2_ROXAS_DUAL, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.DAYS_ROXAS_DUAL, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.MASTER, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .livingMotionModifier(KKStyles.FINAL, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                    .passiveSkill(KKSkills.comboExtender.get());

    public static final Function<Item, CapabilityItem.Builder> KK_SHIELD = item ->
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

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_ETHEREAL_BLADE.toString().toLowerCase()), ETHEREAL_BLADE);
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_CHAKRAM.toString().toLowerCase()), CHAKRAM);
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_ARROW_GUNS.toString().toLowerCase()), ARROWGUN);
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_LANCE.toString().toLowerCase()), LANCE);
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_CLAYMORE.toString().toLowerCase()), CLAYMORE);
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_SHIELD.toString().toLowerCase()), KK_SHIELD);
        event.getTypeEntry().put(new ResourceLocation(KingdomKeys.MODID,EpicKKWeaponEnum.KK_KEYBLADE.toString().toLowerCase()), KEYBLADE);
    }

    public enum EpicKKWeaponEnum implements WeaponCategory {
        KK_ETHEREAL_BLADE, KK_ARROW_GUNS, KK_LANCE, KK_SHIELD, KK_AXE_SWORD, KK_LEXICON, KK_CLAYMORE, KK_CHAKRAM, KK_SITAR, KK_CARD, KK_SCYTHE,
        KK_KNIVES, KK_KEYBLADE;
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
