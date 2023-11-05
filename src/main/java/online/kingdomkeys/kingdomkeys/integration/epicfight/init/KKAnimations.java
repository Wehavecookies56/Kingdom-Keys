package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.InteractionHand;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.SeparateClassToAvoidLoadingIssuesExtendedReach;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

public class KKAnimations {
    public static StaticAnimation CHAKRAM_AUTO1, ROXAS_AUTO1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO1, KK_SHIELD_AUTO2, KK_SHIELD_AUTO3, SORA_AUTO1,SORA_AUTO2, VALOR_IDLE, VALOR_AUTO1, VALOR_AUTO2,
            VALOR_AUTO3, MASTER_IDLE, WISDOM_IDLE, WISDOM_RUN, WISDOM_COMBO1, WISDOM_FINISHER, FINAL_IDLE, FINAL_AUTO1,
            SORA_SUMMON, DRIVE_SUMMON;


    private KKAnimations() {

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }

    private static void build() {
        List<Pair<Joint, Collider>> dualKeyblade =  List.of(Pair.of(Armatures.BIPED.toolR, KKCollider.KEYBLADE), Pair.of(Armatures.BIPED.toolL, KKCollider.KEYBLADE));
               DRIVE_SUMMON = new ActionAnimation(0.05F, "biped/living/sora_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE,true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 0.7F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.1f, (ep, animation, arr) ->
                        SeparateClassToAvoidLoadingIssuesExtendedReach.SummonKeyblade((PlayerPatch) ep), AnimationEvent.Side.BOTH));

        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", Armatures.BIPED);
        VALOR_AUTO1 = new BasicAttackAnimation(0.05F,  "biped/combat/valor_auto1", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND,dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 0.7F);
        VALOR_AUTO2 = new BasicAttackAnimation(0.05F,  "biped/combat/valor_auto2", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 0.7F);
        VALOR_AUTO3 = new BasicAttackAnimation(0.05F,  "biped/combat/valor_auto3", Armatures.BIPED,
        new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 0.7F);

        WISDOM_IDLE = new StaticAnimation(true, "biped/living/wisdom_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) ->0.7F);
        WISDOM_RUN = new StaticAnimation(true, "biped/living/wisdom_run", Armatures.BIPED);
        WISDOM_COMBO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.5F, KKCollider.NO, Armatures.BIPED.rootJoint, "biped/combat/wisdom_shoot", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 1.0F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.1f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.2f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.3f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH));
        WISDOM_FINISHER = new AttackAnimation(0.1F, 0.00F, 0.1f, 0.16F, 1.5F, KKCollider.NO, Armatures.BIPED.rootJoint, "biped/combat/wisdom_finisher", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 1.0F).addEvents(
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

        FINAL_IDLE = new StaticAnimation(true, "biped/living/final_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) ->.8f);
        FINAL_AUTO1 = new BasicAttackAnimation(0.16F,  "biped/combat/final_auto1", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> .8f) ;

        ROXAS_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/roxas_auto_1", Armatures.BIPED);
        ROXAS_IDLE = new StaticAnimation(true, "biped/living/sora_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) -> .6f);
        ROXAS_RUN = new StaticAnimation(true, "biped/living/roxas_run", Armatures.BIPED);


        SORA_SUMMON = new ActionAnimation(0.05F, "biped/living/sora_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE,true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(self, entitypatch, speed, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) ->
                        SeparateClassToAvoidLoadingIssuesExtendedReach.SummonKeyblade((PlayerPatch) ep), AnimationEvent.Side.BOTH));

        SORA_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.5F, 0.6F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/sora_auto1", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) ->.7f);
        SORA_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.5F, 0.6F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/sora_auto2", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, elapsedTime) ->.65f);

        KK_SHIELD_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_1", Armatures.BIPED);
        KK_SHIELD_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_2", Armatures.BIPED);
        KK_SHIELD_AUTO3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_3", Armatures.BIPED);

        CHAKRAM_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/chakram_auto_1", Armatures.BIPED);


    }
}
