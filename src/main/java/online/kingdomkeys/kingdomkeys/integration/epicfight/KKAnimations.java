package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Models;

public class KKAnimations {
    public static StaticAnimation TEST, TEST2, CHAKRAM_AUTO_1, ROXAS_AUTO_1, ROXAS_IDLE, ROXAS_RUN,
            KK_SHIELD_AUTO_1, KK_SHIELD_AUTO_2, KK_SHIELD_AUTO_3, KH1_SORA_AUTO_1, VALOR_IDLE, VALOR_AUTO_1, VALOR_AUTO_2,
            VALOR_AUTO_3;



    private KKAnimations(){

    }

    public static void register(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }
    private static void build() {
        Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;

        VALOR_IDLE = new StaticAnimation(true, "biped/living/valor_idle", models.biped);

        ROXAS_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/roxas_auto_1",  models.biped);
        ROXAS_IDLE = new StaticAnimation(true,"biped/living/roxas_idle",  models.biped);
        ROXAS_RUN = new StaticAnimation(true,"biped/living/roxas_run",  models.biped);

        KH1_SORA_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/kh1_sora_auto_1",  models.biped)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 0.7F);

        KK_SHIELD_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/kk_shield_auto_1",  models.biped);
        KK_SHIELD_AUTO_2 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/kk_shield_auto_2",  models.biped);
        KK_SHIELD_AUTO_3 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/kk_shield_auto_3",  models.biped);

        CHAKRAM_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/chakram_auto_1",  models.biped);

        TEST = new StaticAnimation(true,"biped/living/test",  models.biped);
        TEST2 = new StaticAnimation(true,"biped/living/test2",  models.biped).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED, 2.0F);
    }
}
