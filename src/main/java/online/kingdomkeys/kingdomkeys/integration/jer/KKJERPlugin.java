package online.kingdomkeys.kingdomkeys.integration.jer;

import jeresources.api.IJERAPI;
import jeresources.api.IJERPlugin;
import jeresources.api.JERPlugin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

// JER finds this by scanning for the annotation and calls receive() with its API. setup() is split out so the platform helper mixin can drive it instead on versions where that scan misses us.
@JERPlugin
public class KKJERPlugin implements IJERPlugin {
    private static boolean done;

    public static void setup(IJERAPI jerApi) {
        // Both entry points can fire on the same launch, and registering the ores twice would double up every distribution graph.
        if (done) {
            return;
        }
        done = true;

        try {
            new WorldGen(jerApi.getWorldGenRegistry()).setup();
            jerApi.getDungeonRegistry().registerChest("kingdomkeys.chests.moogle_house", ResourceKey.create(Registries.LOOT_TABLE, KingdomKeys.rl("chests/moogle_house")));
            KingdomKeys.LOGGER.info("JER integration registered");
        } catch (Throwable t) {
            KingdomKeys.LOGGER.error("JER integration failed to register", t);
        }
        //TODO entity drops
    }

    @Override
    public void receive(IJERAPI ijerapi) {
        KingdomKeys.LOGGER.info("JER called KKJERPlugin#receive");
        setup(ijerapi);
    }
}
