package online.kingdomkeys.kingdomkeys.integration.jer;

import jeresources.api.IJERAPI;
import jeresources.api.JERPlugin;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class KKJERPlugin  {

    @JERPlugin
    public static IJERAPI jerApi;

    public static void setup() {
        new WorldGen(jerApi.getWorldGenRegistry()).setup();
        jerApi.getDungeonRegistry().registerChest("kingdomkeys.chests.moogle_house", new ResourceLocation(KingdomKeys.MODID, "chests/moogle_house"));
        //TODO entity drops
    }


}
