package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.skills.KKSkills;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.ex_cap.data.Moveset;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.registry.deferred.MovesetRegister;
import yesman.epicfight.registry.deferred.holders.DeferredMoveset;
import yesman.epicfight.skill.guard.GuardSkill;

public class KKMoveSets {
    public static final MovesetRegister MOVESETS = MovesetRegister.create(KingdomKeys.MODID);

    public static final DeferredMoveset FINAL_FORM_MOVESET = MOVESETS.registerMoveset("final_form", () -> Moveset.builder()
            .addComboAttacks(KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1, KKAnimations.FINAL_AUTO1,
                    Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            .addLivingMotionModifier(LivingMotions.IDLE, KKAnimations.FINAL_FORM_IDLE)
            .addLivingMotionModifier(LivingMotions.RUN, KKAnimations.FINAL_FORM_IDLE)
            .addLivingMotionModifier(LivingMotions.WALK, KKAnimations.FINAL_FORM_IDLE)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));

    public static final DeferredMoveset MASTER_FORM_MOVESET = MOVESETS.registerMoveset("master_form", () -> Moveset.builder()
            .addComboAttacks(Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3,
                    Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            .addLivingMotionModifier(LivingMotions.RUN, KKAnimations.MASTER_FORM_RUN)
            .addLivingMotionModifier(LivingMotions.WALK, KKAnimations.MASTER_FORM_WALK)
            .addLivingMotionModifier(LivingMotions.IDLE, KKAnimations.MASTER_FORM_IDLE)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));
    public static final DeferredMoveset WISDOM_FORM_MOVESET = MOVESETS.registerMoveset("wisdom_form", () -> Moveset.builder()
            .addComboAttacks(KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_COMBO1, KKAnimations.WISDOM_FINISHER,
                    Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            .addLivingMotionModifier(LivingMotions.RUN, KKAnimations.WISDOM_FORM_RUN)
            .addLivingMotionModifier(LivingMotions.WALK, KKAnimations.WISDOM_FORM_RUN)
            .addLivingMotionModifier(LivingMotions.IDLE, KKAnimations.WISDOM_FORM_IDLE)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));
    public static final DeferredMoveset VALOR_FORM_MOVESET = MOVESETS.registerMoveset("valor_form", () -> Moveset.builder()
            .addComboAttacks(KKAnimations.VALOR_AUTO1, KKAnimations.VALOR_AUTO2, KKAnimations.VALOR_AUTO3,
                                        Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            .addLivingMotionModifier(LivingMotions.IDLE, KKAnimations.VALOR_FORM_IDLE)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));

    public static final DeferredMoveset ANTI_FORM_MOVESET = MOVESETS.registerMoveset("anti_form", () -> Moveset.builder());


    public static final DeferredMoveset SORA_MOVESET = MOVESETS.registerMoveset("sora", () -> Moveset.builder()
            .addComboAttacks(KKAnimations.SORA_AUTO1, KKAnimations.SORA_AUTO2, KKAnimations.SORA_AUTO3, KKAnimations.SORA_FINISHER1,
                    Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            //.addLivingMotionModifier(LivingMotions.RUN, KKAnimations.SORA_RUN)
            .addLivingMotionModifier(LivingMotions.RUN, KKAnimations.ROXAS_RUN)
            .addLivingMotionModifier(LivingMotions.IDLE, KKAnimations.SORA_IDLE)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));

    public static final DeferredMoveset RIKU_MOVESET = MOVESETS.registerMoveset("riku", () -> Moveset.builder()
            .addComboAttacks(KKAnimations.SORA_AUTO1, KKAnimations.SORA_AUTO2, KKAnimations.SORA_AUTO3, KKAnimations.SORA_FINISHER1,
                    Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            .addLivingMotionModifier(LivingMotions.RUN, KKAnimations.ROXAS_RUN)
            .addLivingMotionModifier(LivingMotions.IDLE, KKAnimations.RIKU_IDLE)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));

    public static final DeferredMoveset CHAKRAM_MOVESET = MOVESETS.registerMoveset("chakram", () -> Moveset.builder()
            .addComboAttacks(KKAnimations.AXEL_AUTO1, Animations.DAGGER_DUAL_AUTO2, Animations.DAGGER_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4,
                    Animations.DAGGER_DUAL_DASH, Animations.DAGGER_DUAL_AIR_SLASH)
            .addLivingMotionModifier(LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
            .addMountAttacks(Animations.SWORD_MOUNT_ATTACK)
            .setPassiveSkill(KKSkills.comboExtender)
            .addGuardAnimations(GuardSkill.BlockType.GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.ADVANCED_GUARD, Animations.SWORD_DUAL_GUARD_HIT).addGuardAnimations(GuardSkill.BlockType.GUARD_BREAK, Animations.BIPED_COMMON_NEUTRALIZED));
}
