package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;

public class KKAnimations {
    public static StaticAnimation TEST, CHAKRAM_AUTO1, ROXAS_AUTO1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO1, KK_SHIELD_AUTO2, KK_SHIELD_AUTO3, KH1_SORA_COMBO1, VALOR_IDLE, VALOR_AUTO1, VALOR_AUTO2,
            VALOR_AUTO3, MASTER_IDLE, WISDOM_IDLE, WISDOM_RUN, WISDOM_COMBO1, WISDOM_FINISHER;


    private KKAnimations() {

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }

    private static void build() {
        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", Armatures.BIPED);
        VALOR_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.4F, 0.7F, ColliderPreset.DUAL_SWORD, Armatures.BIPED.torso, "biped/combat/valor_auto1", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 0.7F);
        VALOR_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.7F, 0.9F, ColliderPreset.DUAL_SWORD, Armatures.BIPED.torso, "biped/combat/valor_auto2", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 0.7F);
        VALOR_AUTO3 = new BasicAttackAnimation(0.16F, 0.1F, 0.8F, 1.0F, ColliderPreset.DUAL_SWORD, Armatures.BIPED.torso, "biped/combat/valor_auto3", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 0.7F);
        WISDOM_IDLE = new StaticAnimation(true, "biped/living/wisdom_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 0.7F);
        WISDOM_RUN = new StaticAnimation(true, "biped/living/wisdom_run", Armatures.BIPED);
        WISDOM_COMBO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.5F, KKCollider.NO, Armatures.BIPED.rootJoint, "biped/combat/wisdom_shoot", Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 1.0F).addEvents(
                        AnimationEvent.TimeStampedEvent.create(.1f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.2f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.3f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH));
        WISDOM_FINISHER = new AttackAnimation(0.1F, 0.00F, 0.1f, 0.16F, 1.5F, KKCollider.NO, Armatures.BIPED.rootJoint, "biped/combat/wisdom_finisher", Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 1.0F).addEvents(
                        AnimationEvent.TimeStampedEvent.create(.1f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.2f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.3f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.4f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.55f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.7f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.75f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.8f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH));

        MASTER_IDLE = new StaticAnimation(true, "biped/living/master_idle", Armatures.BIPED);


        ROXAS_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/roxas_auto_1", Armatures.BIPED);
        ROXAS_IDLE = new StaticAnimation(true, "biped/living/roxas_idle", Armatures.BIPED);
        ROXAS_RUN = new StaticAnimation(true, "biped/living/roxas_run", Armatures.BIPED);

        KH1_SORA_COMBO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.5F, 0.7F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/kh1_sora_auto_1", Armatures.BIPED);

        KK_SHIELD_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_1", Armatures.BIPED);
        KK_SHIELD_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_2", Armatures.BIPED);
        KK_SHIELD_AUTO3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_3", Armatures.BIPED);

        CHAKRAM_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/chakram_auto_1", Armatures.BIPED);

        TEST = new StaticAnimation(true, "biped/living/test", Armatures.BIPED);
    }
}
