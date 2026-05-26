package online.kingdomkeys.kingdomkeys.config;

import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class ModConfigs {
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

    public static List<? extends Integer> hiddenMagic;
    public static boolean cmHeaderTextVisible, cmClassicColors, hpShowHearts, showDriveForms, summonTogether, auto3rdPersonShip, cmChangeColor, customFont, shoulderSurfingDecoupled, seasonalEvents;
    public static int cmTextXOffset, cmSelectedXOffset, cmSubXOffset, hpAlarm, lockOnIconScale, lockOnIconRotation, lockOnHpPerBar, partyYDistance, cmEndLWidth, cmEndRWidth, cmHeaderEndLWidth, cmHeaderEndRWidth, cmReactionEndLWidth, cmReactionEndRWidth;

    public static void setHUDData(String name, List<? extends Float> data){
        switch (name){
            case "HP" -> {
                CLIENT.hpHUDData.set(data);
                CLIENT.hpHUDData.save();
            }
            case "MP" -> {
                CLIENT.mpHUDData.set(data);
                CLIENT.mpHUDData.save();
            }
            case "CM" -> {
                CLIENT.cmHUDData.set(data);
                CLIENT.cmHUDData.save();
            }
            case "RC" -> {
                CLIENT.rcHUDData.set(data);
                CLIENT.rcHUDData.save();
            }
            case "Drive" -> {
                CLIENT.driveHUDData.set(data);
                CLIENT.driveHUDData.save();
            }
            case "Focus" -> {
                CLIENT.focusHUDData.set(data);
                CLIENT.focusHUDData.save();
            }
            case "Portrait" -> {
                CLIENT.portraitHUDData.set(data);
                CLIENT.portraitHUDData.save();
            }
            case "Party" -> {
                CLIENT.partyHUDData.set(data);
                CLIENT.partyHUDData.save();
            }
            case "LockOn" -> {
                CLIENT.lockOnHUDData.set(data);
                CLIENT.lockOnHUDData.save();
            }
            case "MunnyExp" -> {
                CLIENT.munnyExpHUDData.set(data);
                CLIENT.munnyExpHUDData.save();
            }
            case "LevelUp" -> {
                CLIENT.levelUpHUDData.set(data);
                CLIENT.levelUpHUDData.save();
            }
            case "DriveLevel" -> {
                CLIENT.driveLevelHUDData.set(data);
                CLIENT.driveLevelHUDData.save();
            }
            case "Minimap" -> {
                CLIENT.minimapHUDData.set(data);
                CLIENT.minimapHUDData.save();
            }
        }
    }

    public static List<? extends Float> getHUDData(String name){
        return switch (name){
            case "HP" -> CLIENT.hpHUDData.get();
            case "MP" -> CLIENT.mpHUDData.get();
            case "CM" -> CLIENT.cmHUDData.get();
            case "RC" -> CLIENT.rcHUDData.get();
            case "Drive" -> CLIENT.driveHUDData.get();
            case "Focus" -> CLIENT.focusHUDData.get();
            case "Party" -> CLIENT.partyHUDData.get();
            case "LockOn" -> CLIENT.lockOnHUDData.get();
            case "Portrait" -> CLIENT.portraitHUDData.get();
            case "MunnyExp" -> CLIENT.munnyExpHUDData.get();
            case "LevelUp" -> CLIENT.levelUpHUDData.get();
            case "DriveLevel" -> CLIENT.driveLevelHUDData.get();
            case "Minimap" -> CLIENT.minimapHUDData.get();
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
    //Font
    public static void setCustomFont(boolean customFont) {
        CLIENT.customFont.set(customFont);
        CLIENT.customFont.save();
        bakeClient();
    }
    //Command Menu
    public static void setHiddenMagic(List<Integer> value) {
        CLIENT.hiddenMagic.set(value);
        CLIENT.hiddenMagic.save();
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

  //Lock On
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
    public static void setPartyYDistance(int value) {
        CLIENT.partyYDistance.set(value);
        CLIENT.partyYDistance.save();
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
        customFont = CLIENT.customFont.get();
        if(customFont) {
            ClientUtils.KK_Font_EXP = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "kk_font_exp"));
            ClientUtils.KK_Font_MENU = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "kk_font_menu"));
        } else {
            ClientUtils.KK_Font_EXP = Style.EMPTY;
            ClientUtils.KK_Font_MENU = Style.EMPTY;
        }

        hiddenMagic = (List<Integer>) CLIENT.hiddenMagic.get();

        cmTextXOffset = CLIENT.cmTextXOffset.get();
        cmHeaderTextVisible = CLIENT.cmHeaderTextVisible.get();
        cmClassicColors = CLIENT.cmClassicColors.get();
        cmSelectedXOffset = CLIENT.cmSelectedXOffset.get();
        cmSubXOffset = CLIENT.cmSubXOffset.get();

        hpShowHearts = CLIENT.hpShowHearts.get();
        hpAlarm = CLIENT.hpAlarm.get();

        lockOnIconScale = CLIENT.lockOnIconScale.get();
        lockOnIconRotation = CLIENT.lockOnIconRotation.get();
        lockOnHpPerBar = CLIENT.lockOnHpPerBar.get();

        partyYDistance = CLIENT.partyYDistance.get();

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
        if (KingdomKeys.shoulderSurfingLoaded) {
            shoulderSurfingDecoupled = CLIENT.shoulderSurfingDecoupled.get();
        }
        seasonalEvents = CLIENT.seasonalEvents.get();
        hiddenMagic = CLIENT.hiddenMagic.get();
    }

    public static boolean bombExplodeWithfire, keybladeOpenDoors, mobLevelingUp, playerSpawnHeartless,blizzardChangeBlocks, bossDespawnIfNoTarget, respawnROD, needKeybladeForHeartless, mobLevelName, allowBlocksInHangarArea, hideOrgNames;

    public static SpawningMode heartlessSpawningMode;
    public static List<String> mobSpawnRate;

    public static int driveHeal, hpDropProbability, mpDropProbability, munnyDropProbability, driveDropProbability, focusDropProbability, gummiBlocksDropPercent, recipeDropChance;

    public static double shotlockMult, critMult, drivePointsMultiplier, focusPointsMultiplier, fuelConsumeFactor;

    public static int mobLevelStats,rodHeartlessLevelScale, rodHeartlessMaxLevel;
    public static List<String> playerSpawnHeartlessData;

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

        startingRecipes = ((List<String>) COMMON.startingRecipes.get()).stream().map(ResourceLocation::parse).toList();

        allowBlocksInHangarArea = COMMON.allowBlocksInHangarArea.get();
        gummiBlocksDropPercent = COMMON.gummiBlocksDropPercent.get();

        hideOrgNames = COMMON.hideOrgNames.get();

        fuelConsumeFactor = COMMON.fuelConsumeFactor.get();
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
