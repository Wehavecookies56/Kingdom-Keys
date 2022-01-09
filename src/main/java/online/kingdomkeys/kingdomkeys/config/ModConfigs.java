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

    private static ClientConfig CLIENT; //Client stuff that doesn't matter if it's changed
    private static CommonConfig COMMON; //Stuff in both sides
    private static ServerConfig SERVER; //Client stuff that needs to be synced from the server
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
    public static boolean cmHeaderTextVisible;
    public static int cmTextXOffset, cmXScale, cmXPos, cmSubXOffset;

    public static boolean hpShowHearts;
    public static int hpXPos, hpYPos;
    
    public static int mpXPos, mpYPos;
    
    public static int dpXPos, dpYPos;
    
    public static int playerSkinXPos, playerSkinYPos;
    
    public static int lockOnXPos, lockOnYPos, lockOnHPScale, lockOnIconScale, lockOnHpPerBar;
    
    public static int partyXPos, partyYPos, partyYDistance;
    
    public static int focusXPos, focusYPos;

	public static boolean showDriveForms;
	
    
    //Command Menu
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
    
    //HP
    public static void setHpXPos(int value) {
        CLIENT.hpXPos.set(value);
        bakeClient();
    }
    
    public static void setHpYPos(int value) {
        CLIENT.hpYPos.set(value);
        bakeClient();
    }
    
    public static void setShowHearts(boolean value) {
        CLIENT.hpShowHearts.set(value);
        bakeClient();
    }
    
    //MP
    public static void setMpXPos(int value) {
        CLIENT.mpXPos.set(value);
        bakeClient();
    }
    
    public static void setMpYPos(int value) {
        CLIENT.mpYPos.set(value);
        bakeClient();
    }
    
    //DP
    public static void setDpXPos(int value) {
        CLIENT.dpXPos.set(value);
        bakeClient();
    }
    
    public static void setDpYPos(int value) {
        CLIENT.dpYPos.set(value);
        bakeClient();
    }
    
  //Player Skin
    public static void setPlayerSkinXPos(int value) {
        CLIENT.playerSkinXPos.set(value);
        bakeClient();
    }
    
    public static void setPlayerSkinYPos(int value) {
        CLIENT.playerSkinYPos.set(value);
        bakeClient();
    }
    
  //Lock On
    public static void setLockOnXPos(int value) {
        CLIENT.lockOnXPos.set(value);
        bakeClient();
    }

    public static void setLockOnYPos(int value) {
        CLIENT.lockOnYPos.set(value);
        bakeClient();
    }    

    public static void setLockOnHPScale(int value) {
        CLIENT.lockOnHPScale.set(value);
        bakeClient();
    }    

    public static void setLockOnIconScale(int value) {
        CLIENT.lockOnIconScale.set(value);
        bakeClient();
    }
    
    public static void setLockOnHpPerBar(int value) {
        CLIENT.lockOnHpPerBar.set(Math.max(10, value));
        bakeClient();
    }
    
    //Party
    public static void setPartyXPos(int value) {
        CLIENT.partyXPos.set(value);
        bakeClient();
    }
    
    public static void setPartyYPos(int value) {
        CLIENT.partyYPos.set(value);
        bakeClient();
    }
    
    public static void setPartyYDistance(int value) {
        CLIENT.partyYDistance.set(value);
        bakeClient();
    }

    //Focus
    public static void setFocusXPos(int value) {
        CLIENT.focusXPos.set(value);
        bakeClient();
    }
    
    public static void setFocusYPos(int value) {
        CLIENT.focusYPos.set(value);
        bakeClient();
    }
    
    public static void setShowDriveForms(boolean val) {
        CLIENT.showDriveForms.set(val);
        bakeClient();
    }
    
    public static void bakeClient() {
        corsairKeyboardLighting = CLIENT.corsairKeyboardLighting.get();
        cmTextXOffset = CLIENT.cmTextXOffset.get();
        cmHeaderTextVisible = CLIENT.cmHeaderTextVisible.get();
        cmXScale = CLIENT.cmXScale.get();
        cmXPos = CLIENT.cmXPos.get();
        cmSubXOffset = CLIENT.cmSubXOffset.get();
        
        hpXPos = CLIENT.hpXPos.get();
        hpYPos = CLIENT.hpYPos.get();
        hpShowHearts = CLIENT.hpShowHearts.get();
        
        mpXPos = CLIENT.mpXPos.get();
        mpYPos = CLIENT.mpYPos.get();
        
        dpXPos = CLIENT.dpXPos.get();
        dpYPos = CLIENT.dpYPos.get();
        
        playerSkinXPos = CLIENT.playerSkinXPos.get();
        playerSkinYPos = CLIENT.playerSkinYPos.get();
        
        lockOnXPos = CLIENT.lockOnXPos.get();
        lockOnYPos = CLIENT.lockOnYPos.get();
        lockOnHPScale = CLIENT.lockOnHPScale.get();
        lockOnIconScale = CLIENT.lockOnIconScale.get();
        lockOnHpPerBar = CLIENT.lockOnHpPerBar.get();

        partyXPos = CLIENT.partyXPos.get();
        partyYPos = CLIENT.partyYPos.get();
        partyYDistance = CLIENT.partyYDistance.get();
        
        focusXPos = CLIENT.focusXPos.get();
        focusYPos = CLIENT.focusYPos.get();
        
        showDriveForms = CLIENT.showDriveForms.get();
    }

    public static boolean oreGen;
    public static boolean bloxGen;
    public static boolean debugConsoleOutput;
    public static boolean bombExplodeWithfire;
    public static boolean keybladeOpenDoors;

    public static SpawningMode heartlessSpawningMode;
    public static List<String> moogleSpawnRate;
    public static List<String> mobSpawnRate;
    public static boolean mobLevelingUp;
    
    public static int driveHeal;

    public static double drivePointsMultiplier;
    public static double focusPointsMultiplier;
    
    public static int hpDropProbability;
    public static int mpDropProbability;
    public static int munnyDropProbability;
    public static int driveDropProbability;
    public static int focusDropProbability;

    public static double limitLaserCircleMult;
    public static double limitLaserDomeMult;
    public static double limitArrowRainMult;
    public static double shotlockMult;
    public static double critMult;
    
    public static boolean playerSpawnHeartless;
    public static boolean blizzardChangeBlocks;
    public static int mobLevelStats;
    public static List<String> playerSpawnHeartlessData;

    public static void bakeCommon() {
        heartlessSpawningMode = COMMON.heartlessSpawningMode.get();
        oreGen = COMMON.oreGen.get();
        bloxGen = COMMON.bloxGen.get();
        debugConsoleOutput = COMMON.debugConsoleOutput.get();
        bombExplodeWithfire = COMMON.bombExplodeWithFire.get();
        keybladeOpenDoors = COMMON.keybladeOpenDoors.get();
        moogleSpawnRate = (List<String>) COMMON.moogleSpawnRate.get();
        mobSpawnRate = (List<String>) COMMON.mobSpawnRate.get();
        mobLevelingUp = COMMON.mobLevelingUp.get();
        
        driveHeal = COMMON.driveHeal.get();
        
        drivePointsMultiplier = COMMON.drivePointsMultiplier.get();
        focusPointsMultiplier = COMMON.focusPointsMultiplier.get();

        limitLaserCircleMult = COMMON.limitLaserCircleMult.get();
        limitLaserDomeMult = COMMON.limitLaserDomeMult.get();
        limitArrowRainMult = COMMON.limitArrowRainMult.get();
        playerSpawnHeartless = COMMON.playerSpawnHeartless.get();
        playerSpawnHeartlessData = (List<String>) COMMON.playerSpawnHeartlessData.get();
        shotlockMult = COMMON.shotlockMult.get();
        critMult = COMMON.critMult.get();
        
        hpDropProbability = COMMON.hpDropProbability.get();
        mpDropProbability = COMMON.mpDropProbability.get();
        munnyDropProbability = COMMON.munnyDropProbability.get();
        driveDropProbability = COMMON.driveDropProbability.get();
        focusDropProbability = COMMON.focusDropProbability.get();
        blizzardChangeBlocks = COMMON.blizzardChangeBlocks.get();
        
        mobLevelStats = COMMON.mobLevelStats.get();
        
    }

    public static int recipeDropChance;
    public static int partyRangeLimit;
    public static List<String> driveFormXPMultiplier;
    public static double xpMultiplier;
    public static double heartMultiplier;
    public static double partyXPShare;
    public static int magicUsesTimer;
    
    public static int limitLaserCircleCost;
    public static int limitLaserDomeCost;
    public static int limitArrowRainCost;
    public static List<Integer> statsMultiplier;

    public static void bakeServer() {
        recipeDropChance = SERVER.recipeDropChance.get();
        partyRangeLimit = SERVER.partyRangeLimit.get();
        driveFormXPMultiplier = (List<String>) SERVER.driveFormXPMultiplier.get();
        xpMultiplier = SERVER.xpMultiplier.get();
        heartMultiplier = SERVER.heartMultiplier.get();
        partyXPShare = SERVER.partyXPShare.get();
        magicUsesTimer = SERVER.magicUsesTimer.get();
        
        limitLaserCircleCost = SERVER.limitLaserCircleCost.get();
        limitLaserDomeCost = SERVER.limitLaserDomeCost.get();
        limitArrowRainCost = SERVER.limitArrowRainCost.get();
        statsMultiplier = (List<Integer>) SERVER.statsMultiplier.get();
    }


    @SubscribeEvent
    public static void configEvent(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            KingdomKeys.LOGGER.info("LOAD CLIENT CONFIG");
            bakeClient();
        } else if (event.getConfig().getSpec() == COMMON_SPEC) {
            KingdomKeys.LOGGER.info("LOAD COMMON CONFIG");
            bakeCommon();
        } else if (event.getConfig().getSpec() == SERVER_SPEC) {
            KingdomKeys.LOGGER.info("LOAD SERVER CONFIG");
            bakeServer();
        }
    }

}
