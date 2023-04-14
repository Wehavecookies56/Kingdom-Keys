package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class KKAnimations {
    public static StaticAnimation TEST, TEST2, CHAKRAM_AUTO_1, ROXAS_AUTO_1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO_1, KK_SHIELD_AUTO_2, KK_SHIELD_AUTO_3, KH1_SORA_AUTO_1, VALOR_IDLE, VALOR_AUTO_1, VALOR_AUTO_2,
            VALOR_AUTO_3, MASTER_IDLE, WISDOM_IDLE, WISDOM_RUN;



    private KKAnimations(){

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }
    private static void build() {

        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", Armatures.BIPED);
        WISDOM_IDLE = new StaticAnimation(true, "biped/living/wisdom_idle", Armatures.BIPED);
        WISDOM_RUN = new StaticAnimation(true, "biped/living/wisdom_run", Armatures.BIPED)
                .addEvents(AnimationProperty.StaticAnimationProperty.EVENTS, AnimationEvent.TimeStampedEvent
                        .create(.0f, (ep, arr) -> {
                            double x;
                            double y;
                            double z;
                            if(ep.getTarget() != null)
                            {
                                LivingEntity target = ep.getTarget();
                                x = target.getX();
                                y = target.getY();
                                z = target.getZ();
                            }
                            else
                            {
                                LivingEntity player = ep.getOriginal();
                                Vec3 lookVector = player.getLookAngle();
                                x = player.getX() + lookVector.x * 5;
                                y = player.getY();
                                z = player.getZ() + lookVector.z * 5;
                            }
                            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,
                                    ep.getOriginal().getLevel());
                            lightningBolt.setPos(x, y, z);
                            ep.getOriginal().getLevel().addFreshEntity(lightningBolt);
                }, AnimationEvent.Side.BOTH));



        MASTER_IDLE = new StaticAnimation(true, "biped/living/master_idle", Armatures.BIPED);



        ROXAS_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/roxas_auto_1",  Armatures.BIPED);
        ROXAS_IDLE = new StaticAnimation(true,"biped/living/roxas_idle",  Armatures.BIPED);
        ROXAS_RUN = new StaticAnimation(true,"biped/living/roxas_run",  Armatures.BIPED);

        KH1_SORA_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kh1_sora_auto_1",  Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 1.1F);

        KK_SHIELD_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_1",  Armatures.BIPED);
        KK_SHIELD_AUTO_2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_2",  Armatures.BIPED);
        KK_SHIELD_AUTO_3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_3",  Armatures.BIPED);

        CHAKRAM_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/chakram_auto_1",  Armatures.BIPED);

        TEST = new StaticAnimation(true,"biped/living/test",  Armatures.BIPED);
    }
}
