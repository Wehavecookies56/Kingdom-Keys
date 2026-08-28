package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightUtils;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightSharedConstants;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;

public class KKAnimations {

    public static AnimationManager.AnimationAccessor<? extends StaticAnimation>
        SORA_IDLE, RIKU_IDLE, RIKU_WALK, RIKU_RUN, AQUA_IDLE, VALOR_FORM_IDLE, WISDOM_FORM_IDLE, MASTER_FORM_IDLE, FINAL_FORM_IDLE, ANTI_FORM_IDLE, AXEL_IDLE, DEMYX_IDLE, LARXENE_IDLE,
            LEXAEUS_IDLE, LUXORD_IDLE, MARLUXIA_IDLE, ROXAS_IDLE, SAIX_IDLE, VEXEN_IDLE, XALDIN_IDLE, XEMNAS_IDLE, XIGBAR_IDLE, ZEXION_IDLE;
    public static AnimationManager.AnimationAccessor<? extends StaticAnimation>
        ROXAS_RUN, WISDOM_FORM_RUN, MASTER_FORM_WALK, MASTER_FORM_RUN, ANTI_FORM_WALK, ANTI_FORM_RUN, XEMNAS_WALK, XEMNAS_RUN, XEMNAS_FLY, XIGBAR_WALK, XALDIN_WALK,
            XALDIN_RUN, VEXEN_WALK, LEXAEUS_WALK, LEXAEUS_RUN, ZEXION_WALK, ZEXION_RUN,
            SAIX_WALK, SAIX_RUN, AXEL_RUN, DEMYX_WALK, DEMYX_RUN, LUXORD_WALK, LUXORD_RUN, MARLUXIA_WALK, MARLUXIA_RUN,
            LARXENE_WALK, LARXENE_RUN;

    public static AnimationManager.AnimationAccessor<ComboAttackAnimation>
            VALOR_AUTO1, VALOR_AUTO2, VALOR_AUTO3,
            WISDOM_COMBO1,
            MASTER_AUTO1,
            FINAL_AUTO1,
            ROXAS_AUTO1,
            SORA_AUTO1, SORA_AUTO2, SORA_AUTO3, SORA_FINISHER1, RIKU_AUTO1,
            KK_SHIELD_AUTO1, KK_SHIELD_AUTO2, KK_SHIELD_AUTO3,
            AXEL_AUTO1;

    public static AnimationManager.AnimationAccessor<ActionAnimation>
            INDIRECT_CAST, PROJECTILE_CAST,
            DRIVE_SUMMON, SORA_SUMMON, XEMNAS_SUMMON, XIGBAR_SUMMON, XALDIN_SUMMON, VEXEN_SUMMON, LEXAEUS_SUMMON,
                ZEXION_SUMMON, SAIX_SUMMON, AXEL_SUMMON, DEMYX_SUMMON, LUXORD_SUMMON, MARLUXIA_SUMMON, LARXENE_SUMMON;

    public static AnimationManager.AnimationAccessor<AttackAnimation>
            WISDOM_FINISHER;

    public static Map<OrgMember, AnimationManager.AnimationAccessor<?>> orgMap = new HashMap<>();
    public static Map<SingleChoices, AnimationManager.AnimationAccessor<?>> singleKeybladeMap = new HashMap<>();
    public static Map<DualChoices, AnimationManager.AnimationAccessor<?>> dualKeybladeMap = new HashMap<>();

    public static Map<?, ?> spellFireMap = new HashMap<>();
    public static Map<?, ?> spellBlizzardMap = new HashMap<>();
    public static Map<?, ?> spellThunderMap = new HashMap<>();


    private KKAnimations() {

    }

    public static void register(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(KingdomKeys.MODID, KKAnimations::build);
    }

    public static void initSummonMap() {
        orgMap.put(OrgMember.AXEL, KKAnimations.AXEL_SUMMON);
        orgMap.put(OrgMember.DEMYX, KKAnimations.DEMYX_SUMMON);
        orgMap.put(OrgMember.LARXENE, KKAnimations.LARXENE_SUMMON);
        orgMap.put(OrgMember.LEXAEUS, KKAnimations.LEXAEUS_SUMMON);
        orgMap.put(OrgMember.ROXAS, KKAnimations.SORA_SUMMON);
        orgMap.put(OrgMember.SAIX, KKAnimations.SAIX_SUMMON);
        orgMap.put(OrgMember.XALDIN, KKAnimations.XALDIN_SUMMON);
        orgMap.put(OrgMember.XEMNAS, KKAnimations.XEMNAS_SUMMON);
        orgMap.put(OrgMember.XIGBAR, KKAnimations.XIGBAR_SUMMON);
        orgMap.put(OrgMember.LUXORD, KKAnimations.LUXORD_SUMMON);
        orgMap.put(OrgMember.ZEXION, KKAnimations.ZEXION_SUMMON);
        orgMap.put(OrgMember.VEXEN, KKAnimations.VEXEN_SUMMON);
        orgMap.put(OrgMember.MARLUXIA, KKAnimations.MARLUXIA_SUMMON);

        singleKeybladeMap.put(SingleChoices.SORA, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.AQUA, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.ROXAS, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.RIKU, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.TERRA, KKAnimations.SORA_SUMMON);
        singleKeybladeMap.put(SingleChoices.VENTUS, KKAnimations.SORA_SUMMON);


        dualKeybladeMap.put(DualChoices.KH2_ROXAS_DUAL, KKAnimations.SORA_SUMMON);
        dualKeybladeMap.put(DualChoices.DAYS_ROXAS_DUAL, KKAnimations.SORA_SUMMON);

    }

    public static void initSpellFireMap() {

    }


    private static void build(AnimationManager.AnimationBuilder builder) {
        AttackAnimation.JointColliderPair[] dualKeyblade = new AttackAnimation.JointColliderPair[]{AttackAnimation.JointColliderPair.of(Armatures.BIPED.get().toolR, KKCollider.KEYBLADE), AttackAnimation.JointColliderPair.of(Armatures.BIPED.get().toolL, KKCollider.KEYBLADE)};
        DRIVE_SUMMON = builder.nextAccessor("biped/living/forms/drive_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F)
                .addEvents(AnimationEvent.InTimeEvent.create(.1f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide) {
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade(PlayerData.get((Player) ep.getOriginal()).getActiveDriveForm()));
                        }
                    }
                }, AnimationEvent.Side.BOTH)));

        VALOR_FORM_IDLE = builder.nextAccessor("biped/living/forms/valor_form/valor_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        VALOR_AUTO1 = builder.nextAccessor("biped/combat/forms/valor_form/valor_auto1", animationAccessor -> new ComboAttackAnimation(0.05F, animationAccessor, Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F));
        VALOR_AUTO2 = builder.nextAccessor("biped/combat/forms/valor_form/valor_auto2", animationAccessor -> new ComboAttackAnimation(0.05F, animationAccessor, Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F));
        VALOR_AUTO3 = builder.nextAccessor("biped/combat/forms/valor_form/valor_auto3", animationAccessor -> new ComboAttackAnimation(0.05F, animationAccessor, Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F));

        WISDOM_FORM_IDLE = builder.nextAccessor("biped/living/forms/wisdom_form/wisdom_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.7F));
        WISDOM_FORM_RUN = builder.nextAccessor("biped/living/forms/wisdom_form/wisdom_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        WISDOM_COMBO1 = builder.nextAccessor("biped/combat/forms/wisdom_form/wisdom_shoot", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.16F, 0.5F, KKCollider.NO, Armatures.BIPED.get().rootJoint, animationAccessor, Armatures.BIPED) {
            @Override
            public boolean shouldPlayerMove(LocalPlayerPatch playerpatch) {
                return true;
            }

            @Override
            public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd) {
                super.end(entitypatch, nextAnimation, isEnd);

                if (!isEnd && !nextAnimation.get().isMainFrameAnimation() && entitypatch.isLogicalClient()) {
                    float playbackSpeed = EpicFightSharedConstants.A_TICK * this.getPlaySpeed(entitypatch, nextAnimation.get());
                    entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
                }
            }
        }.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 1.0F)
                .addEvents(AnimationEvent.InTimeEvent.create(.1f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.2f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.3f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH)).addState(EntityState.MOVEMENT_LOCKED, false));
        WISDOM_FINISHER = builder.nextAccessor("biped/combat/forms/wisdom_form/wisdom_finisher", animationAccessor -> new AttackAnimation(0.1F, 0.00F, 0.1f, 0.16F, 1.5F, KKCollider.NO, Armatures.BIPED.get().rootJoint, animationAccessor, Armatures.BIPED) {

            @Override
            public boolean shouldPlayerMove(LocalPlayerPatch playerpatch) {
                return true;
            }

            @Override
            public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd) {
                super.end(entitypatch, nextAnimation, isEnd);

                if (!isEnd && !nextAnimation.get().isMainFrameAnimation() && entitypatch.isLogicalClient()) {
                    float playbackSpeed = EpicFightSharedConstants.A_TICK * this.getPlaySpeed(entitypatch, nextAnimation.get());
                    entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
                }
            }
        }.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 1.0F).addEvents(
                        AnimationEvent.InTimeEvent.create(.1f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.2f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.3f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.4f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.55f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.7f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.75f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH),
                        AnimationEvent.InTimeEvent.create(.8f, (ep, animation, arr) -> WisdomProjectile.shoot(ep, Armatures.BIPED.get().toolR), AnimationEvent.Side.BOTH)).addState(EntityState.MOVEMENT_LOCKED, false));

        MASTER_FORM_IDLE = builder.nextAccessor("biped/living/forms/master_form/master_form_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        MASTER_FORM_WALK = builder.nextAccessor("biped/living/forms/master_form/master_form_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 1.4F));
        MASTER_FORM_RUN = builder.nextAccessor("biped/living/forms/master_form/master_form_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 1.5F));


        ANTI_FORM_IDLE = builder.nextAccessor("biped/living/forms/anti_form/anti_form_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) ->  1.25F));
        ANTI_FORM_WALK = builder.nextAccessor("biped/living/forms/anti_form/anti_form_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) ->  4.3F));
        ANTI_FORM_RUN = builder.nextAccessor("biped/living/forms/anti_form/anti_form_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) ->  2F));

        FINAL_FORM_IDLE = builder.nextAccessor("biped/living/forms/final_form/final_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .8f));
        FINAL_AUTO1 = builder.nextAccessor("biped/combat/forms/final_form/final_auto1", animationAccessor -> new ComboAttackAnimation(0.01F, animationAccessor, Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.25F, 0.25F, 0.35F, 0.75F, Float.MAX_VALUE, false, InteractionHand.MAIN_HAND, dualKeyblade))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .8f));

        ROXAS_AUTO1 = builder.nextAccessor("biped/combat/roxas_auto_1", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED));
        ROXAS_IDLE = builder.nextAccessor("biped/living/organization/roxas/roxas_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        ROXAS_RUN = builder.nextAccessor("biped/living/organization/roxas/roxas_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));


        SORA_IDLE = builder.nextAccessor("biped/living/single_keyblade/sora/sora_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        RIKU_IDLE = builder.nextAccessor("biped/living/single_keyblade/riku/riku_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        RIKU_WALK = builder.nextAccessor("biped/living/single_keyblade/riku/riku_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 4f));
        RIKU_RUN = builder.nextAccessor("biped/living/single_keyblade/riku/riku_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 3f));
        AQUA_IDLE = builder.nextAccessor("biped/living/single_keyblade/aqua/aqua_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        SORA_SUMMON = builder.nextAccessor("biped/living/single_keyblade/sora/sora_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));

        SORA_AUTO1 = builder.nextAccessor("biped/combat/sora_auto1", animationAccessor -> new ComboAttackAnimation(-0.85F, 0.05F, 0.39F, 0.4F, KKCollider.KEYBLADE, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.9f));
        SORA_AUTO2 = builder.nextAccessor("biped/combat/sora_auto2", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.39F, 0.4F, KKCollider.KEYBLADE, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .9f));
        SORA_AUTO3 = builder.nextAccessor("biped/combat/sora_auto3", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.5F, 0.6F, KKCollider.KEYBLADE, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .9f));
        SORA_FINISHER1 = builder.nextAccessor("biped/combat/sora_finisher1", animationAccessor -> new ComboAttackAnimation(-0.85F, 0.05F, 0.59F, 0.6F, KKCollider.KEYBLADE, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .9f));

        INDIRECT_CAST = builder.nextAccessor("biped/combat/indirect_cast", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 3f));
        PROJECTILE_CAST = builder.nextAccessor("biped/combat/projectile_cast", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 2.8f));

        RIKU_AUTO1 = builder.nextAccessor("biped/combat/riku_attack1", animationAccessor -> new ComboAttackAnimation(-0.85F, animationAccessor, Armatures.BIPED,
                new AttackAnimation.Phase(0.0F, 0.85F, 1.30F, 1.45F, 1.45F, Armatures.BIPED.get().toolR, KKCollider.KEYBLADE),
                new AttackAnimation.Phase(1.45F, 1.52F, 1.82F, 2.70F, 2.70F, Armatures.BIPED.get().toolR, KKCollider.KEYBLADE),
                new AttackAnimation.Phase(2.70F, 2.75F, 3.45F, 3.50F, Float.MAX_VALUE, Armatures.BIPED.get().toolR, KKCollider.KEYBLADE))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 2.6f));

        KK_SHIELD_AUTO1 = builder.nextAccessor("biped/combat/kk_shield_auto_1", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED));
        KK_SHIELD_AUTO2 = builder.nextAccessor("biped/combat/kk_shield_auto_2", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED));
        KK_SHIELD_AUTO3 = builder.nextAccessor("biped/combat/kk_shield_auto_3", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED));

        AXEL_AUTO1 = builder.nextAccessor("biped/combat/chakram_auto_1", animationAccessor -> new ComboAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.get().toolR, animationAccessor, Armatures.BIPED));
        AXEL_IDLE = builder.nextAccessor("biped/living/organization/axel/axel_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        AXEL_RUN = builder.nextAccessor("biped/living/organization/axel/axel_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        XIGBAR_WALK = builder.nextAccessor("biped/living/organization/xigbar/xigbar_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        XIGBAR_IDLE = builder.nextAccessor("biped/living/organization/xigbar/xigbar_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));

        SAIX_IDLE = builder.nextAccessor("biped/living/organization/saix/saix_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        SAIX_WALK = builder.nextAccessor("biped/living/organization/saix/saix_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        SAIX_RUN = builder.nextAccessor("biped/living/organization/saix/saix_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        XEMNAS_IDLE = builder.nextAccessor("biped/living/organization/xemnas/xemnas_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        XEMNAS_WALK = builder.nextAccessor("biped/living/organization/xemnas/xemnas_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        XEMNAS_RUN = builder.nextAccessor("biped/living/organization/xemnas/xemnas_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        XEMNAS_FLY = builder.nextAccessor("biped/living/organization/xemnas/xemnas_fly", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        XALDIN_IDLE = builder.nextAccessor("biped/living/organization/xaldin/xaldin_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        XALDIN_WALK = builder.nextAccessor("biped/living/organization/xaldin/xaldin_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        XALDIN_RUN = builder.nextAccessor("biped/living/organization/xaldin/xaldin_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        DEMYX_IDLE = builder.nextAccessor("biped/living/organization/demyx/demyx_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        DEMYX_WALK = builder.nextAccessor("biped/living/organization/demyx/demyx_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        VEXEN_IDLE = builder.nextAccessor("biped/living/organization/vexen/vexen_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        VEXEN_WALK = builder.nextAccessor("biped/living/organization/vexen/vexen_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        LEXAEUS_IDLE = builder.nextAccessor("biped/living/organization/lexaeus/lexaeus_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        LEXAEUS_WALK = builder.nextAccessor("biped/living/organization/lexaeus/lexaeus_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        LEXAEUS_RUN = builder.nextAccessor("biped/living/organization/lexaeus/lexaeus_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        ZEXION_IDLE = builder.nextAccessor("biped/living/organization/zexion/zexion_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        ZEXION_WALK = builder.nextAccessor("biped/living/organization/zexion/zexion_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        ZEXION_RUN = builder.nextAccessor("biped/living/organization/zexion/zexion_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        LUXORD_IDLE = builder.nextAccessor("biped/living/organization/luxord/luxord_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        LUXORD_WALK = builder.nextAccessor("biped/living/organization/luxord/luxord_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        LUXORD_RUN = builder.nextAccessor("biped/living/organization/luxord/luxord_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        MARLUXIA_IDLE = builder.nextAccessor("biped/living/organization/marluxia/marluxia_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        MARLUXIA_WALK = builder.nextAccessor("biped/living/organization/marluxia/marluxia_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        MARLUXIA_RUN = builder.nextAccessor("biped/living/organization/marluxia/marluxia_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        LARXENE_IDLE = builder.nextAccessor("biped/living/organization/larxene/larxene_idle", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> .6f));
        LARXENE_WALK = builder.nextAccessor("biped/living/organization/larxene/larxene_walk", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));
        LARXENE_RUN = builder.nextAccessor("biped/living/organization/larxene/larxene_run", animationAccessor -> new StaticAnimation(true, animationAccessor, Armatures.BIPED));

        XEMNAS_SUMMON = builder.nextAccessor("biped/living/organization/xemnas/xemnas_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        XIGBAR_SUMMON = builder.nextAccessor("biped/living/organization/xigbar/xigbar_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        XALDIN_SUMMON = builder.nextAccessor("biped/living/organization/xaldin/xaldin_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        VEXEN_SUMMON = builder.nextAccessor("biped/living/organization/vexen/vexen_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        LEXAEUS_SUMMON = builder.nextAccessor("biped/living/organization/lexaeus/lexaeus_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        ZEXION_SUMMON = builder.nextAccessor("biped/living/organization/zexion/zexion_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        SAIX_SUMMON = builder.nextAccessor("biped/living/organization/saix/saix_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        AXEL_SUMMON = builder.nextAccessor("biped/living/organization/axel/axel_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        DEMYX_SUMMON = builder.nextAccessor("biped/living/organization/demyx/demyx_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        LUXORD_SUMMON = builder.nextAccessor("biped/living/organization/luxord/luxord_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        MARLUXIA_SUMMON = builder.nextAccessor("biped/living/organization/marluxia/marluxia_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        LARXENE_SUMMON = builder.nextAccessor("biped/living/organization/larxene/larxene_summon", animationAccessor -> new ActionAnimation(0.05F, animationAccessor, Armatures.BIPED)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (self, entitypatch, speed, prevElapsedTime, elapsedTime) -> 0.8F)
                .addEvents(AnimationEvent.InTimeEvent.create(.15f, (ep, animation, arr) -> {
                    if (ep.getOriginal().level().isClientSide)
                        if (EpicFightUtils.isPlayerSummoning(ep)) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        }
                }, AnimationEvent.Side.BOTH)));
        initSummonMap();
    }
}
