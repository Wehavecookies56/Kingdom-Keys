package online.kingdomkeys.kingdomkeys.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class ModConfigs {
    public static ModConfig CLIENT_CONFIG;

    private static final ClientConfig CLIENT; //Client stuff that doesn't matter if it's changed
    private static final CommonConfig COMMON; //Stuff in both sides
    public static final ServerConfig SERVER; //Client stuff that needs to be synced from the server
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec SERVER_SPEC;

    public static ClientConfig getClientConfig(){
        return CLIENT;
    }

    public static CommonConfig getCommonConfig(){
        return COMMON;
    }

    public static ServerConfig getServerConfig(){
        return SERVER;
    }

    static {
        {
            final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            final Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
        {
            final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }

    public static List<String> magicDisplayedInCommandMenu;
    public static boolean cmHeaderTextVisible, cmClassicColors, hpShowHearts, showDriveForms, summonTogether, auto3rdPersonShip, cmChangeColor;
    public static int cmTextXOffset, cmXScale, cmXPos, cmSelectedXOffset, cmSubXOffset, hpAlarm, hpXPos, hpYPos, hpXScale, mpXPos, mpYPos, mpXScale, dpXPos, dpYPos, dpXScale, dpYScale, playerSkinXPos, playerSkinYPos, lockOnXPos, lockOnYPos, lockOnHPScale, lockOnIconScale, lockOnIconRotation, lockOnHpPerBar, partyXPos, partyYPos, partyYDistance, focusXPos, focusYPos, focusXScale, focusYScale, cmEndLWidth, cmEndRWidth, cmHeaderEndLWidth, cmHeaderEndRWidth, cmReactionEndLWidth, cmReactionEndRWidth;

    public static void setHUDData(String name, List<? extends Float> data){
        switch (name){
            case "HP" -> CLIENT.hpHUDData.set(data);
            case "MP" -> CLIENT.mpHUDData.set(data);
            case "CM" -> CLIENT.cmHUDData.set(data);
            case "Drive" -> CLIENT.driveHUDData.set(data);
            case "Focus" -> CLIENT.focusHUDData.set(data);
        }
        CLIENT.hpHUDData.save();
        CLIENT.mpHUDData.save();
        CLIENT.cmHUDData.save();
        CLIENT.driveHUDData.save();
        CLIENT.focusHUDData.save();
    }

    public static List<? extends Float> getHUDData(String name){
        return switch (name){
            case "HP" -> CLIENT.hpHUDData.get();
            case "MP" -> CLIENT.mpHUDData.get();
            case "CM" -> CLIENT.cmHUDData.get();
            case "Drive" -> CLIENT.driveHUDData.get();
            case "Focus" -> CLIENT.focusHUDData.get();
            default -> throw new IllegalStateException("Unexpected HUD value: " + name);
        };
    }

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
        CLIENT.magicDisplayedInCommandMenu.save();
        bakeClient();
    }

    public static void setCmHeaderTextVisible(boolean value) {
        CLIENT.cmHeaderTextVisible.set(value);
        CLIENT.cmHeaderTextVisible.save();
        bakeClient();
    }
    
    public static void setCmClassicColors(boolean value) {
        CLIENT.cmClassicColors.set(value);
        CLIENT.cmClassicColors.save();
        bakeClient();
    }

    public static void setCmTextXOffset(int value) {
        CLIENT.cmTextXOffset.set(value);
        CLIENT.cmTextXOffset.save();
        bakeClient();
    }

    public static void setCmXScale(int value) {
        CLIENT.cmXScale.set(value);
        CLIENT.cmXScale.save();
        bakeClient();
    }

    public static void setCmXPos(int value) {
        CLIENT.cmXPos.set(value);
        CLIENT.cmXPos.save();
        bakeClient();
    }
    
    public static void setCmSelectedXOffset(int value) {
        CLIENT.cmSelectedXOffset.set(value);
        CLIENT.cmSelectedXOffset.save();
        bakeClient();
    }

    public static void setCmSubXOffset(int value) {
        CLIENT.cmSubXOffset.set(value);
        CLIENT.cmSubXOffset.save();
        bakeClient();
    }

    //HP
    public static void setHpXPos(int value) {
        CLIENT.hpXPos.set(value);
        CLIENT.hpXPos.save();
        bakeClient();
    }

    public static void setHpYPos(int value) {
        CLIENT.hpYPos.set(value);
        CLIENT.hpYPos.save();
        bakeClient();
    }

    public static void setShowHearts(boolean value) {
        CLIENT.hpShowHearts.set(value);
        CLIENT.hpShowHearts.save();
        bakeClient();
    }
    
    public static void setHPAlarm(int value) {
        CLIENT.hpAlarm.set(value);
        CLIENT.hpAlarm.save();
        bakeClient();
    }
    
    public static void setHPXScale(int value) {
        CLIENT.hpXScale.set(value);
        CLIENT.hpXScale.save();
        bakeClient();
    }

    //MP
    public static void setMpXPos(int value) {
        CLIENT.mpXPos.set(value);
        CLIENT.mpXPos.save();
        bakeClient();
    }

    public static void setMpYPos(int value) {
        CLIENT.mpYPos.set(value);
        CLIENT.mpYPos.save();
        bakeClient();
    }
    
    public static void setMPXScale(int value) {
        CLIENT.mpXScale.set(value);
        CLIENT.mpXScale.save();
        bakeClient();
    }

    //DP
    public static void setDpXPos(int value) {
        CLIENT.dpXPos.set(value);
        CLIENT.dpXPos.save();
        bakeClient();
    }

    public static void setDpYPos(int value) {
        CLIENT.dpYPos.set(value);
        CLIENT.dpYPos.save();
        bakeClient();
    }
    
    public static void setDpXScale(int value) {
        CLIENT.dpXScale.set(value);
        CLIENT.dpXScale.save();
        bakeClient();
    }
    
    public static void setDpYScale(int value) {
        CLIENT.dpYScale.set(value);
        CLIENT.dpYScale.save();
        bakeClient();
    }

  //Player Skin
    public static void setPlayerSkinXPos(int value) {
        CLIENT.playerSkinXPos.set(value);
        CLIENT.playerSkinXPos.save();
        bakeClient();
    }

    public static void setPlayerSkinYPos(int value) {
        CLIENT.playerSkinYPos.set(value);
        CLIENT.playerSkinYPos.save();
        bakeClient();
    }

  //Lock On
    public static void setLockOnXPos(int value) {
        CLIENT.lockOnXPos.set(value);
        CLIENT.lockOnXPos.save();
        bakeClient();
    }

    public static void setLockOnYPos(int value) {
        CLIENT.lockOnYPos.set(value);
        CLIENT.lockOnYPos.save();
        bakeClient();
    }

    public static void setLockOnHPScale(int value) {
        CLIENT.lockOnHPScale.set(value);
        CLIENT.lockOnHPScale.save();
        bakeClient();
    }

    public static void setLockOnIconScale(int value) {
        CLIENT.lockOnIconScale.set(value);
        CLIENT.lockOnIconScale.save();
        bakeClient();
    }
    
    public static void setLockOnIconRotation(int value) {
        CLIENT.lockOnIconRotation.set(value);
        CLIENT.lockOnIconRotation.save();
        bakeClient();
    }

    public static void setLockOnHpPerBar(int value) {
        CLIENT.lockOnHpPerBar.set(Math.max(10, value));
        CLIENT.lockOnHpPerBar.save();
        bakeClient();
    }

    //Party
    public static void setPartyXPos(int value) {
        CLIENT.partyXPos.set(value);
        CLIENT.partyXPos.save();
        bakeClient();
    }

    public static void setPartyYPos(int value) {
        CLIENT.partyYPos.set(value);
        CLIENT.partyYPos.save();
        bakeClient();
    }

    public static void setPartyYDistance(int value) {
        CLIENT.partyYDistance.set(value);
        CLIENT.partyYDistance.save();
        bakeClient();
    }

    //Focus
    public static void setFocusXPos(int value) {
        CLIENT.focusXPos.set(value);
        CLIENT.focusXPos.save();
        bakeClient();
    }

    public static void setFocusYPos(int value) {
        CLIENT.focusYPos.set(value);
        CLIENT.focusYPos.save();
        bakeClient();
    }
    
    public static void setFocusXScale(int value) {
        CLIENT.focusXScale.set(value);
        CLIENT.focusXScale.save();
        bakeClient();
    }
    
    public static void setFocusYScale(int value) {
        CLIENT.focusYScale.set(value);
        CLIENT.focusYScale.save();
        bakeClient();
    }

    public static void setShowDriveForms(boolean val) {
        CLIENT.showDriveForms.set(val);
        CLIENT.showDriveForms.save();
        bakeClient();
    }

    public static void setCmEndLWidth(int value) {
        CLIENT.cmEndLWidth.set(value);
        CLIENT.cmEndLWidth.save();
        bakeClient();
    }

    public static void setCmEndRWidth(int value) {
        CLIENT.cmEndRWidth.set(value);
        CLIENT.cmEndRWidth.save();
        bakeClient();
    }

    public static void setCmHeaderEndLWidth(int value) {
        CLIENT.cmHeaderEndLWidth.set(value);
        CLIENT.cmHeaderEndLWidth.save();
        bakeClient();
    }

    public static void setCmHeaderEndRWidth(int value) {
        CLIENT.cmHeaderEndRWidth.set(value);
        CLIENT.cmHeaderEndRWidth.save();
        bakeClient();
    }

    public static void setCmReactionEndLWidth(int value) {
        CLIENT.cmReactionEndLWidth.set(value);
        CLIENT.cmReactionEndLWidth.save();
        bakeClient();
    }

    public static void setCmReactionEndRWidth(int value) {
        CLIENT.cmReactionEndRWidth.set(value);
        CLIENT.cmReactionEndRWidth.save();
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

        cmHeaderEndLWidth = CLIENT.cmHeaderEndLWidth.get();
        cmHeaderEndRWidth = CLIENT.cmHeaderEndRWidth.get();
        cmEndLWidth = CLIENT.cmEndLWidth.get();
        cmEndRWidth = CLIENT.cmEndRWidth.get();
        cmReactionEndLWidth = CLIENT.cmReactionEndLWidth.get();
        cmReactionEndRWidth = CLIENT.cmReactionEndRWidth.get();

        auto3rdPersonShip = CLIENT.auto3rdPersonShip.get();
        cmChangeColor = CLIENT.cmChangeColor.get();
    }

    public static boolean bombExplodeWithfire, keybladeOpenDoors, mobLevelingUp, playerSpawnHeartless,blizzardChangeBlocks, bossDespawnIfNoTarget, respawnROD, needKeybladeForHeartless, mobLevelName, allowBlocksInHangarArea;

    public static SpawningMode heartlessSpawningMode;
    public static List<String> mobSpawnRate;

    public static int driveHeal, hpDropProbability, mpDropProbability, munnyDropProbability, driveDropProbability, focusDropProbability, gummiBlocksDropPercent, recipeDropChance;

    public static double shotlockMult, critMult, drivePointsMultiplier, focusPointsMultiplier;

    public static int mobLevelStats,rodHeartlessLevelScale, rodHeartlessMaxLevel;
    public static List<String> playerSpawnHeartlessData;
    public static String savePointMaterials, linkedSavePointRecovers, savePointRecovers, warpPointRecovers;

    public static List<ResourceLocation> startingRecipes;

    public static void bakeCommon() {
        heartlessSpawningMode = COMMON.heartlessSpawningMode.get();

        bombExplodeWithfire = COMMON.bombExplodeWithFire.get();
        keybladeOpenDoors = COMMON.keybladeOpenDoors.get();
        mobSpawnRate = (List<String>) COMMON.mobSpawnRate.get();
        mobLevelingUp = COMMON.mobLevelingUp.get();
        mobLevelName = COMMON.mobLevelName.get();

        driveHeal = COMMON.driveHeal.get();

        drivePointsMultiplier = COMMON.drivePointsMultiplier.get();
        focusPointsMultiplier = COMMON.focusPointsMultiplier.get();

        playerSpawnHeartless = COMMON.playerSpawnHeartless.get();
        playerSpawnHeartlessData = (List<String>) COMMON.playerSpawnHeartlessData.get();
        shotlockMult = COMMON.shotlockMult.get();
        critMult = COMMON.critMult.get();

        recipeDropChance = COMMON.recipeDropChance.get();
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

        savePointMaterials = COMMON.savePointMaterials.get();
        linkedSavePointRecovers = COMMON.linkedSavePointRecovers.get();
        savePointRecovers = COMMON.savePointRecovers.get();
        warpPointRecovers = COMMON.warpPointRecovers.get();

        startingRecipes = ((List<String>) COMMON.startingRecipes.get()).stream().map(ResourceLocation::parse).toList();

        allowBlocksInHangarArea = COMMON.allowBlocksInHangarArea.get();
        gummiBlocksDropPercent = COMMON.gummiBlocksDropPercent.get();
    }

    @SubscribeEvent
    public static void configEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            KingdomKeys.LOGGER.info("LOAD CLIENT CONFIG");
            bakeClient();
        } else if (event.getConfig().getSpec() == COMMON_SPEC) {
            KingdomKeys.LOGGER.info("LOAD COMMON CONFIG");
            bakeCommon();
        }
    }
}
