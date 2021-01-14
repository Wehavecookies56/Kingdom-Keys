package online.kingdomkeys.kingdomkeys.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {

    private static ClientConfig CLIENT;
    private static CommonConfig COMMON;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec COMMON_SPEC;
    private static ModConfig clientConfig;
    private static ModConfig commonConfig;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }

    public static void bakeClient(final ModConfig config) {
        clientConfig = config;
    }

    public static void bakeCommon(final ModConfig config) {
        commonConfig = config;
    }

    /**
     * Method to update config value during runtime
     * @param config the config being changed
     * @param path the path of the config value
     * @param value the value to change to
     */
    public static void setValueAndSave(ModConfig config, String path, Object value) {
        config.getConfigData().set(path, value);
        config.save();
    }

    @SubscribeEvent
    public static void configEvent(ModConfig.ModConfigEvent event) {
        KingdomKeys.LOGGER.info("LOAD CONFIG");
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            bakeClient(event.getConfig());
            bakeCommon(event.getConfig());
        } else if (event.getConfig().getSpec() == COMMON_SPEC) {
            bakeCommon(event.getConfig());
        }
    }

}
