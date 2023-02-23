package online.kingdomkeys.kingdomkeys.integration.epicfight;

import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Armatures;

public class KKAnimations {
    public static StaticAnimation TEST, TEST2, CHAKRAM_AUTO_1, ROXAS_AUTO_1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO_1, KK_SHIELD_AUTO_2, KK_SHIELD_AUTO_3, KH1_SORA_AUTO_1, VALOR_IDLE, VALOR_AUTO_1, VALOR_AUTO_2,
            VALOR_AUTO_3, MASTER_IDLE;



    private KKAnimations(){

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }
    private static void build() {

        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", Armatures.BIPED);
        MASTER_IDLE = new StaticAnimation(true, "biped/living/master_idle", Armatures.BIPED);

        ROXAS_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/roxas_auto_1",  Armatures.BIPED);
        ROXAS_IDLE = new StaticAnimation(true,"biped/living/roxas_idle",  Armatures.BIPED);
        ROXAS_RUN = new StaticAnimation(true,"biped/living/roxas_run",  Armatures.BIPED);

        KH1_SORA_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kh1_sora_auto_1",  Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 0.7F);

        KK_SHIELD_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null, Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_1",  Armatures.BIPED);
        KK_SHIELD_AUTO_2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_2",  Armatures.BIPED);
        KK_SHIELD_AUTO_3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/kk_shield_auto_3",  Armatures.BIPED);

        CHAKRAM_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,Armatures.BIPED.toolR,"biped/combat/chakram_auto_1",  Armatures.BIPED);

        TEST = new StaticAnimation(true,"biped/living/test",  Armatures.BIPED);
    }
}
