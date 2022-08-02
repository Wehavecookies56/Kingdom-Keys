package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.gameasset.Models;

public class KKAnimations {
    public static StaticAnimation CHAKRAM_AUTO_1;

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(KingdomKeys.MODID, KKAnimations::build);

    }
    private static void build() {
        Models<?> models = FMLEnvironment.dist == Dist.CLIENT ? ClientModels.LOGICAL_CLIENT : Models.LOGICAL_SERVER;
        CHAKRAM_AUTO_1 = new BasicAttackAnimation(0.16F, 0.05F, 0.16F, 0.7F, null,"Tool_R","biped/combat/chakram_auto_1",  models.biped);

    }
}
