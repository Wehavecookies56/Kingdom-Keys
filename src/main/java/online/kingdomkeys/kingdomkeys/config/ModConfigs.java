package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {

    private static ClientConfig CLIENT;
    private static CommonConfig COMMON;
    private static ServerConfig SERVER;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;

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
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }

    public static boolean corsairKeyboardLighting;
    public static int cmTextXOffset;
    public static boolean cmHeaderTextVisible;
    public static int cmXScale, cmXPos, cmSubXOffset;

    public static void setCmHeaderTextVisible(boolean value) {
        CLIENT.cmHeaderTextVisible.set(value);
        bakeClient();
    }

    public static void setCmTextXOffset(int value) {
        CLIENT.cmTextXOffset.set(value);
        bakeClient();
    }
    
    public static void setCmXScale(int value) {
        CLIENT.cmXScale.set(value);
        bakeClient();
    }
    
    public static void setCmXPos(int value) {
        CLIENT.cmXPos.set(value);
        bakeClient();
    }
    
    public static void setCmSubXOffset(int value) {
        CLIENT.cmSubXOffset.set(value);
        bakeClient();
    }

    public static void bakeClient() {
        corsairKeyboardLighting = CLIENT.corsairKeyboardLighting.get();
        cmTextXOffset = CLIENT.cmTextXOffset.get();
        cmHeaderTextVisible = CLIENT.cmHeaderTextVisible.get();
        cmXScale = CLIENT.cmXScale.get();
        cmXPos = CLIENT.cmXPos.get();
        cmSubXOffset = CLIENT.cmSubXOffset.get();
    }

    public static boolean oreGen;
    public static boolean bloxGen;
    public static boolean debugConsoleOutput;

    public static SpawningMode heartlessSpawningMode;
    public static List<String> mobSpawnRate;

    public static void bakeCommon() {
        heartlessSpawningMode = COMMON.heartlessSpawningMode.get();
        oreGen = COMMON.oreGen.get();
        bloxGen = COMMON.bloxGen.get();
        debugConsoleOutput = COMMON.debugConsoleOutput.get();
        mobSpawnRate = (List<String>) COMMON.mobSpawnRate.get();
    }

    public static int recipeDropChance;
    public static int partyRangeLimit;
    public static List<String> driveFormXPMultiplier;
    public static double xpMultiplier;
    public static double partyXPShare;

    public static void bakeServer() {
        recipeDropChance = SERVER.recipeDropChance.get();
        partyRangeLimit = SERVER.partyRangeLimit.get();
        driveFormXPMultiplier = (List<String>) SERVER.driveFormXPMultiplier.get();
        xpMultiplier = SERVER.xpMultiplier.get();
        partyXPShare = SERVER.partyXPShare.get();
    }


    @SubscribeEvent
    public static void configEvent(ModConfig.ModConfigEvent event) {
        KingdomKeys.LOGGER.info("LOAD CONFIG");
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            bakeClient();
        } else if (event.getConfig().getSpec() == COMMON_SPEC) {
            bakeCommon();
        } else if (event.getConfig().getSpec() == SERVER_SPEC) {
            bakeServer();
        }
    }

}
