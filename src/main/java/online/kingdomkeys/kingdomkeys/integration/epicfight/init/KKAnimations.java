package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.config.EpicFightOptions;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class KKAnimations {
    public static StaticAnimation CHAKRAM_AUTO1, ROXAS_AUTO1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO1, KK_SHIELD_AUTO2, KK_SHIELD_AUTO3, SORA_AUTO1, SORA_AUTO2, SORA_AUTO3, SORA_FINISHER1, VALOR_IDLE, VALOR_AUTO1, VALOR_AUTO2,
            VALOR_AUTO3, MASTER_IDLE, WISDOM_IDLE, WISDOM_RUN, WISDOM_COMBO1, WISDOM_FINISHER, FINAL_IDLE, FINAL_AUTO1,
            SORA_SUMMON, DRIVE_SUMMON,
            CHAKRAM_IDLE, CHAKRAM_RUN, AXEL_SUMMON, XEMNAS_IDLE, XEMNAS_WALK, XEMNAS_RUN, XEMNAS_FLY, XEMNAS_SUMMON, XIGBAR_IDLE, XIGBAR_WALK, XIGBAR_SUMMON, XALDIN_IDLE, XALDIN_WALK, XALDIN_RUN, XALDIN_SUMMON, SAIX_IDLE, SAIX_WALK, SAIX_RUN, SAIX_SUMMON;

    public static Map<OrgMember, StaticAnimation> orgMap = new HashMap<>();
    public static Map<SingleChoices, StaticAnimation> singleKeybladeMap = new HashMap<>();
    public static Map<DualChoices, StaticAnimation> dualKeybladeMap = new HashMap<>();


    private KKAnimations() {

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);


    }

    public static void initSummonMap() {
        orgMap.put(OrgMember.AXEL, KKAnimations.AXEL_SUMMON);
        orgMap.put(OrgMember.DEMYX, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.LARXENE, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.LEXAEUS, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.ROXAS, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.SAIX, KKAnimations.SAIX_SUMMON);
        orgMap.put(OrgMember.XALDIN, KKAnimations.XALDIN_SUMMON);
        orgMap.put(OrgMember.XEMNAS, KKAnimations.XEMNAS_SUMMON);
        orgMap.put(OrgMember.XIGBAR, KKAnimations.XIGBAR_SUMMON);
        orgMap.put(OrgMember.LUXORD, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.ZEXION, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.VEXEN, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.MARLUXIA, KKAnimations.SORA_SUMMON);

        singleKeybladeMap.put(SingleChoices.SORA, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.AQUA, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.ROXAS, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.RIKU, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.TERRA, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.VENTUS, KKAnimations.SORA_SUMMON);


        dualKeybladeMap.put(DualChoices.KH2_ROXAS_DUAL, KKAnimations.SORA_SUMMON);
        dualKeybladeMap.put(DualChoices.DAYS_ROXAS_DUAL, KKAnimations.SORA_SUMMON);

    }

    private static void build() {
        AttackAnimation.JointColliderPair[] dualKeyblade = new AttackAnimation.JointColliderPair[]{AttackAnimation.JointColliderPair.of(Armatures.BIPED.toolR, KKCollider.KEYBLADE), AttackAnimation.JointColliderPair.of(Armatures.BIPED.toolL, KKCollider.KEYBLADE)};
        DRIVE_SUMMON = new ActionAnimation(0.05F, "biped/living/drive_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.1f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch<?>) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade(new ResourceLocation(ModCapabilities.getPlayer((Player) ep.getOriginal()).getActiveDriveForm())));
                }, AnimationEvent.Side.BOTH));

        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", Armatures.BIPED);
        VALOR_AUTO1 = new BasicAttackAnimation(0.05F, "biped/combat/valor_auto1", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F);
        VALOR_AUTO2 = new BasicAttackAnimation(0.05F, "biped/combat/valor_auto2", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F);
        VALOR_AUTO3 = new BasicAttackAnimation(0.05F, "biped/combat/valor_auto3", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F);

        WISDOM_IDLE = new StaticAnimation(true, "biped/living/wisdom_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F);
        WISDOM_RUN = new StaticAnimation(true, "biped/living/wisdom_run", Armatures.BIPED);
        WISDOM_COMBO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.5F, KKCollider.NO, Armatures.BIPED.rootJoint, "biped/combat/wisdom_shoot", Armatures.BIPED) {
            @Override
            public boolean shouldPlayerMove(LocalPlayerPatch playerpatch) {
                return true;
            }

            @Override
            public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
                super.end(entitypatch, nextAnimation, isEnd);

                if (!isEnd && !nextAnimation.isMainFrameAnimation() && entitypatch.isLogicalClient()) {
                    float playbackSpeed = EpicFightOptions.A_TICK * this.getPlaySpeed(entitypatch, nextAnimation);
                    entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
                }
            }
        }.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 1.0F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.1f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.2f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.TimeStampedEvent.create(.3f, (ep, animation, arr) ->
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH)).addState(EntityState.MOVEMENT_LOCKED, false);
        WISDOM_FINISHER = new AttackAnimation(0.1F, 0.00F, 0.1f, 0.16F, 1.5F, KKCollider.NO, Armatures.BIPED.rootJoint, "biped/combat/wisdom_finisher", Armatures.BIPED) {

            @Override
            public boolean shouldPlayerMove(LocalPlayerPatch playerpatch) {
                return true;
            }

            @Override
            public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
                super.end(entitypatch, nextAnimation, isEnd);

                if (!isEnd && !nextAnimation.isMainFrameAnimation() && entitypatch.isLogicalClient()) {
                    float playbackSpeed = EpicFightOptions.A_TICK * this.getPlaySpeed(entitypatch, nextAnimation);
                    entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
                }
            }
        }.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 1.0F).addEvents(
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
                                WisdomProjectile.shoot(ep, Armatures.BIPED.toolR), AnimationEvent.Side.BOTH)).addState(EntityState.MOVEMENT_LOCKED, false);

        MASTER_IDLE = new StaticAnimation(true, "biped/living/master_idle", Armatures.BIPED);

        FINAL_IDLE = new StaticAnimation(true, "biped/living/final_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .8f);
        FINAL_AUTO1 = new BasicAttackAnimation(0.01F, "biped/combat/final_auto1", Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .8f);

        ROXAS_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/roxas_auto_1", Armatures.BIPED);
        ROXAS_IDLE = new StaticAnimation(true, "biped/living/sora_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f);
        ROXAS_RUN = new StaticAnimation(true, "biped/living/roxas_run", Armatures.BIPED);


        SORA_SUMMON = new ActionAnimation(0.05F, "biped/living/sora_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade());
                }, AnimationEvent.Side.BOTH));

        SORA_AUTO1 = new BasicAttackAnimation(-0.85F, 0.05F, 0.39F, 0.4F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/sora_auto1", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.9f);
        SORA_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.39F, 0.4F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/sora_auto2", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .9f);
        SORA_AUTO3 = new BasicAttackAnimation(0.16F, 0.05F, 0.5F, 0.6F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/sora_auto3", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .9f);
        SORA_FINISHER1 = new BasicAttackAnimation(-0.85F, 0.05F, 0.59F, 0.6F, KKCollider.KEYBLADE, Armatures.BIPED.toolR, "biped/combat/sora_finisher1", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .9f);

        KK_SHIELD_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_1", Armatures.BIPED);
        KK_SHIELD_AUTO2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_2", Armatures.BIPED);
        KK_SHIELD_AUTO3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/kk_shield_auto_3", Armatures.BIPED);

        CHAKRAM_AUTO1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR, "biped/combat/chakram_auto_1", Armatures.BIPED);
        CHAKRAM_IDLE = new StaticAnimation(true, "biped/living/axel_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f);
        CHAKRAM_RUN = new StaticAnimation(true, "biped/living/axel_run", Armatures.BIPED);

        XIGBAR_WALK = new StaticAnimation(true, "biped/living/xigbar_walk", Armatures.BIPED);
        XIGBAR_IDLE = new StaticAnimation(true, "biped/living/xigbar_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f);

        SAIX_IDLE = new StaticAnimation(true, "biped/living/saix_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f);
        SAIX_WALK = new StaticAnimation(true, "biped/living/saix_walk", Armatures.BIPED);
        SAIX_RUN = new StaticAnimation(true, "biped/living/saix_run", Armatures.BIPED);

        XEMNAS_IDLE = new StaticAnimation(true, "biped/living/xemnas_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f);
        XEMNAS_WALK = new StaticAnimation(true, "biped/living/xemnas_walk", Armatures.BIPED);
        XEMNAS_RUN = new StaticAnimation(true, "biped/living/xemnas_run", Armatures.BIPED);
        XEMNAS_FLY = new StaticAnimation(true, "biped/living/xemnas_fly", Armatures.BIPED);

        XALDIN_IDLE = new StaticAnimation(true, "biped/living/xaldin_idle", Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f);
        XALDIN_WALK = new StaticAnimation(true, "biped/living/xaldin_walk", Armatures.BIPED);
        XALDIN_RUN = new StaticAnimation(true, "biped/living/xaldin_run", Armatures.BIPED);

        XEMNAS_SUMMON = new ActionAnimation(0.05F, "biped/living/xemnas_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade());
                }, AnimationEvent.Side.BOTH));
        XIGBAR_SUMMON = new ActionAnimation(0.05F, "biped/living/xigbar_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade());
                }, AnimationEvent.Side.BOTH));
        XALDIN_SUMMON = new ActionAnimation(0.05F, "biped/living/xaldin_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade());
                }, AnimationEvent.Side.BOTH));
        SAIX_SUMMON = new ActionAnimation(0.05F, "biped/living/saix_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade());
                }, AnimationEvent.Side.BOTH));

        AXEL_SUMMON = new ActionAnimation(0.05F, "biped/living/axel_summon", Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.TimeStampedEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide && ((PlayerPatch) ep).isBattleMode())
                        PacketHandler.sendToServer(new CSSummonKeyblade());
                }, AnimationEvent.Side.BOTH));

        initSummonMap();
    }
}
