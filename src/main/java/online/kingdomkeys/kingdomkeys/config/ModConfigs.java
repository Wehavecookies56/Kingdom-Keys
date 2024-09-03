package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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

    public static List<String> magicDisplayedInCommandMenu;
    public static boolean cmHeaderTextVisible, cmClassicColors, hpShowHearts, showDriveForms, summonTogether;
    public static int cmTextXOffset, cmXScale, cmXPos, cmSelectedXOffset, cmSubXOffset, hpAlarm, hpXPos, hpYPos, hpXScale, mpXPos, mpYPos, mpXScale, dpXPos, dpYPos, dpXScale, dpYScale, playerSkinXPos, playerSkinYPos, lockOnXPos, lockOnYPos, lockOnHPScale, lockOnIconScale, lockOnIconRotation, lockOnHpPerBar, partyXPos, partyYPos, partyYDistance, focusXPos, focusYPos, focusXScale, focusYScale;

    public enum ShowType {
        SHOW, HIDE, WEAPON
    }

    public static ShowType showGuiToggle;

    public static void toggleGui() {
        int i = CLIENT.showGuiToggle.get().ordinal() + 1;
        if (i == ShowType.values().length) i = 0;
        CLIENT.showGuiToggle.set(ShowType.values()[i]);
        bakeClient();
    }

    //Command Menu
    public static void setMagicDisplayedInCommandMenu(List<String> value) {
        CLIENT.magicDisplayedInCommandMenu.set(value);
        bakeClient();
    }

    public static void setCmHeaderTextVisible(boolean value) {
        CLIENT.cmHeaderTextVisible.set(value);
        bakeClient();
    }
    
    public static void setCmClassicColors(boolean value) {
        CLIENT.cmClassicColors.set(value);
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
    
    public static void setCmSelectedXOffset(int value) {
        CLIENT.cmSelectedXOffset.set(value);
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
    
    public static void setHPAlarm(int value) {
        CLIENT.hpAlarm.set(value);
        bakeClient();
    }
    
    public static void setHPXScale(int value) {
        CLIENT.hpXScale.set(value);
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
    
    public static void setMPXScale(int value) {
        CLIENT.mpXScale.set(value);
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
    
    public static void setDpXScale(int value) {
        CLIENT.dpXScale.set(value);
        bakeClient();
    }
    
    public static void setDpYScale(int value) {
        CLIENT.dpYScale.set(value);
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
    
    public static void setLockOnIconRotation(int value) {
        CLIENT.lockOnIconRotation.set(value);
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
    
    public static void setFocusXScale(int value) {
        CLIENT.focusXScale.set(value);
        bakeClient();
    }
    
    public static void setFocusYScale(int value) {
        CLIENT.focusYScale.set(value);
        bakeClient();
    }

    public static void setShowDriveForms(boolean val) {
        CLIENT.showDriveForms.set(val);
        bakeClient();
    }

    public static void bakeClient() {
        magicDisplayedInCommandMenu = (List<String>) CLIENT.magicDisplayedInCommandMenu.get();
        cmTextXOffset = CLIENT.cmTextXOffset.get();
        cmHeaderTextVisible = CLIENT.cmHeaderTextVisible.get();
        cmClassicColors = CLIENT.cmClassicColors.get();
        cmXScale = CLIENT.cmXScale.get();
        cmXPos = CLIENT.cmXPos.get();
        cmSelectedXOffset = CLIENT.cmSelectedXOffset.get();
        cmSubXOffset = CLIENT.cmSubXOffset.get();

        hpXPos = CLIENT.hpXPos.get();
        hpYPos = CLIENT.hpYPos.get();
        hpShowHearts = CLIENT.hpShowHearts.get();
        hpAlarm = CLIENT.hpAlarm.get();
        hpXScale = CLIENT.hpXScale.get();
        
        mpXPos = CLIENT.mpXPos.get();
        mpYPos = CLIENT.mpYPos.get();
        mpXScale = CLIENT.mpXScale.get();

        dpXPos = CLIENT.dpXPos.get();
        dpYPos = CLIENT.dpYPos.get();
        dpXScale = CLIENT.dpXScale.get();
        dpYScale = CLIENT.dpYScale.get();

        playerSkinXPos = CLIENT.playerSkinXPos.get();
        playerSkinYPos = CLIENT.playerSkinYPos.get();

        lockOnXPos = CLIENT.lockOnXPos.get();
        lockOnYPos = CLIENT.lockOnYPos.get();
        lockOnHPScale = CLIENT.lockOnHPScale.get();
        lockOnIconScale = CLIENT.lockOnIconScale.get();
        lockOnIconRotation = CLIENT.lockOnIconRotation.get();
        lockOnHpPerBar = CLIENT.lockOnHpPerBar.get();

        partyXPos = CLIENT.partyXPos.get();
        partyYPos = CLIENT.partyYPos.get();
        partyYDistance = CLIENT.partyYDistance.get();

        focusXPos = CLIENT.focusXPos.get();
        focusYPos = CLIENT.focusYPos.get();
        focusXScale = CLIENT.focusXScale.get();
        focusYScale = CLIENT.focusYScale.get();

        showDriveForms = CLIENT.showDriveForms.get();
        summonTogether = CLIENT.summonTogether.get();

        showGuiToggle = CLIENT.showGuiToggle.get();
    }

    public static boolean debugConsoleOutput, bombExplodeWithfire, keybladeOpenDoors, mobLevelingUp, playerSpawnHeartless,blizzardChangeBlocks, bossDespawnIfNoTarget, respawnROD, needKeybladeForHeartless;

    public static SpawningMode heartlessSpawningMode;
    public static List<String> mobSpawnRate;

    public static int driveHeal, hpDropProbability, mpDropProbability, munnyDropProbability, driveDropProbability, focusDropProbability;

    public static double shotlockMult, critMult, drivePointsMultiplier, focusPointsMultiplier;

    public static int mobLevelStats,rodHeartlessLevelScale, rodHeartlessMaxLevel;
    public static List<String> playerSpawnHeartlessData;
    public static String linkedSavePointRecovers, savePointRecovers, warpPointRecovers;

    public static void bakeCommon() {
        heartlessSpawningMode = COMMON.heartlessSpawningMode.get();

        debugConsoleOutput = COMMON.debugConsoleOutput.get();
        bombExplodeWithfire = COMMON.bombExplodeWithFire.get();
        keybladeOpenDoors = COMMON.keybladeOpenDoors.get();
        mobSpawnRate = (List<String>) COMMON.mobSpawnRate.get();
        mobLevelingUp = COMMON.mobLevelingUp.get();

        driveHeal = COMMON.driveHeal.get();

        drivePointsMultiplier = COMMON.drivePointsMultiplier.get();
        focusPointsMultiplier = COMMON.focusPointsMultiplier.get();

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
        rodHeartlessLevelScale = COMMON.rodHeartlessLevelScale.get();
        rodHeartlessMaxLevel = COMMON.rodHeartlessMaxLevel.get();
        respawnROD = COMMON.respawnROD.get();
        bossDespawnIfNoTarget = COMMON.bossDespawnIfNoTarget.get();
    	needKeybladeForHeartless = COMMON.needKeybladeForHeartless.get();

        linkedSavePointRecovers = COMMON.linkedSavePointRecovers.get();
        savePointRecovers = COMMON.savePointRecovers.get();
        warpPointRecovers = COMMON.warpPointRecovers.get();
    }

    public static int recipeDropChance, partyRangeLimit, partyMembersLimit, shotlockMaxDist;
    public static List<Integer> statsMultiplier;
    public static List<String> driveFormXPMultiplier;
    public static double xpMultiplier, heartMultiplier, partyXPShare;
    public static boolean requireSynthTier, projectorHasShop, getExpFromShop, orgEnabled, allowBoosts, allowPartyKO, hostileMobsLevel, wayfinderOnlyParty;
    

    public static void bakeServer() {
        recipeDropChance = SERVER.recipeDropChance.get();
        partyRangeLimit = SERVER.partyRangeLimit.get();
        partyMembersLimit = SERVER.partyMembersLimit.get();
        driveFormXPMultiplier = (List<String>) SERVER.driveFormXPMultiplier.get();
        xpMultiplier = SERVER.xpMultiplier.get();
        heartMultiplier = SERVER.heartMultiplier.get();
        partyXPShare = SERVER.partyXPShare.get();
        requireSynthTier = SERVER.requireSynthTier.get();
        statsMultiplier = (List<Integer>) SERVER.statsMultiplier.get();
        projectorHasShop = SERVER.projectorHasShop.get();
        getExpFromShop = SERVER.getExpFromShop.get();
        wayfinderOnlyParty = SERVER.wayfinderParty.get();
        orgEnabled = SERVER.orgEnabled.get();
        allowBoosts = SERVER.allowBoosts.get();
        allowPartyKO = SERVER.allowPartyKO.get();
        hostileMobsLevel = SERVER.hostileMobsLevel.get();
        shotlockMaxDist = SERVER.shotlockMaxDist.get();
    }


    @SubscribeEvent
    public static void configEvent(ModConfigEvent event) {
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
