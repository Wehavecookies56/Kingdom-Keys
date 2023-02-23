package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
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
                    .styleProvider(playerpatch -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.CHAKRAM? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.CHAKRAM)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.CHAKRAM_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, KKAnimations.CHAKRAM_AUTO_1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND, itemstack-> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);

    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_ROXAS = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KEYBLADE)
                    .styleProvider(playerpatch -> {

                        if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor))
                            return KKStyles.VALOR;
                        else
                            return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;            })
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    //.livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND,itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);

    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_SORA_KH1 = item ->
        WeaponCapability.builder()
                .category(EpicKKWeaponEnum.KEYBLADE)
                .styleProvider(playerpatch -> {

                    if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor))
                        return KKStyles.VALOR;
                    //else if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Master))
                    //    return KKStyles.MASTER;
                     else
                        return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;            })
                .hitSound(EpicFightSounds.BLADE_HIT)
                .collider(ColliderPreset.SWORD)
                .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                //.livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                .newStyleCombo(KKStyles.VALOR, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND,itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);


    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_SORA_KH2 = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KEYBLADE)
                    .styleProvider(playerpatch -> {

                        if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor))
                            return KKStyles.VALOR;
                        else
                            return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;                    })
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    //.livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND,itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);



    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_SORA_KH3 = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KEYBLADE)
                    .styleProvider(playerpatch -> {

                        if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor))
                            return KKStyles.VALOR;
                        else
                            return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;})
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    //.livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND,itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);



    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_RIKU = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KEYBLADE)
                    .styleProvider(playerpatch -> {

                        if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor))
                            return KKStyles.VALOR;
                        else
                            return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;})
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    //.livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND,itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);

    public static final Function<Item, CapabilityItem.Builder> KEYBLADE_TERRA = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KEYBLADE)
                    .styleProvider(playerpatch -> {
                        if ((Player) playerpatch.getOriginal() != null && ModCapabilities.getPlayer((Player) playerpatch.getOriginal()).getActiveDriveForm().equals(Strings.Form_Valor))
                            return KKStyles.VALOR;
                        else
                            return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE ? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND;
                    })
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.SWORD)
                    .weaponCombinationPredicator(entityPatch -> EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KEYBLADE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, KKAnimations.ROXAS_IDLE)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.IDLE, KKAnimations.VALOR_IDLE)
                    //.livingMotionModifier(KKStyles.MASTER, LivingMotions.IDLE, KKAnimations.MASTER_IDLE)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN).livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .livingMotionModifier(KKStyles.VALOR, LivingMotions.RUN, KKAnimations.ROXAS_RUN)
                    .newStyleCombo(KKStyles.VALOR, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KH1_SORA_AUTO_1, Animations.DAGGER_AUTO2, Animations.DAGGER_AUTO3, Animations.DAGGER_DUAL_DASH, Animations.DAGGER_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_COMBO1, Animations.SWORD_DUAL_COMBO2, Animations.SWORD_DUAL_COMBO3, Animations.SWORD_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK).innateSkill(CapabilityItem.Styles.ONE_HAND,itemstack -> EpicFightSkills.EVISCERATE).innateSkill(CapabilityItem.Styles.TWO_HAND, itemstack -> EpicFightSkills.BLADE_RUSH);

    public static final Function<Item, CapabilityItem.Builder> KK_SHIELD = item ->
            WeaponCapability.builder()
                    .category(EpicKKWeaponEnum.KK_SHIELD)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.DAGGER)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, KKAnimations.ROXAS_IDLE)
                    .weaponCombinationPredicator(entityPatch ->
                            EpicFightCapabilities.getItemStackCapability(entityPatch.getOriginal().getOffhandItem()).getWeaponCategory() == EpicKKWeaponEnum.KK_SHIELD)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, KKAnimations.KK_SHIELD_AUTO_1, KKAnimations.KK_SHIELD_AUTO_2, Animations.DAGGER_AUTO3, Animations.SWORD_DASH, Animations.DAGGER_AIR_SLASH);

    private EpicKKWeapons() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(EpicKKWeaponEnum.CHAKRAM.toString().toLowerCase(), CHAKRAM);
        event.getTypeEntry().put(EpicKKWeaponEnum.KK_SHIELD.toString().toLowerCase(), KK_SHIELD);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_roxas", KEYBLADE_ROXAS);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_sora_kh1", KEYBLADE_SORA_KH1);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_sora_kh2", KEYBLADE_SORA_KH2);
        event.getTypeEntry().put(EpicKKWeaponEnum.KEYBLADE.toString().toLowerCase() + "_sora_kh3", KEYBLADE_SORA_KH3);
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
