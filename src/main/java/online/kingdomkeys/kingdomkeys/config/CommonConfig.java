package online.kingdomkeys.kingdomkeys.config;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.List;


/**
 * Config file for config options shared between the server and the client
 */
public class CommonConfig {

    public ModConfigSpec.EnumValue<SpawningMode> heartlessSpawningMode;
    public ModConfigSpec.ConfigValue<List<? extends String>> mobSpawnRate;

    public ModConfigSpec.IntValue rodHeartlessLevelScale;
    public ModConfigSpec.IntValue rodHeartlessMaxLevel;
    public ModConfigSpec.BooleanValue respawnROD;
    public ModConfigSpec.BooleanValue mobLevelingUp;
    public ModConfigSpec.BooleanValue mobLevelName;

    public ModConfigSpec.BooleanValue bombExplodeWithFire;
    public ModConfigSpec.BooleanValue allowBlocksInHangarArea;
    public ModConfigSpec.BooleanValue keybladeOpenDoors;

    public ModConfigSpec.IntValue driveHeal, gummiBlocksDropPercent;

    public ModConfigSpec.DoubleValue drivePointsMultiplier;
    public ModConfigSpec.DoubleValue focusPointsMultiplier;

    public ModConfigSpec.IntValue hpDropProbability;
    public ModConfigSpec.IntValue mpDropProbability;
    public ModConfigSpec.IntValue munnyDropProbability;
    public ModConfigSpec.IntValue driveDropProbability;
    public ModConfigSpec.IntValue focusDropProbability;

    public ModConfigSpec.BooleanValue blizzardChangeBlocks;
    public ModConfigSpec.BooleanValue playerSpawnHeartless;
    public ModConfigSpec.ConfigValue<List<? extends String>> playerSpawnHeartlessData;

    public ModConfigSpec.DoubleValue shotlockMult;
    public ModConfigSpec.DoubleValue critMult;

    public ModConfigSpec.IntValue mobLevelStats;

    public ModConfigSpec.BooleanValue bossDespawnIfNoTarget;
    public ModConfigSpec.BooleanValue needKeybladeForHeartless;
    public ModConfigSpec.ConfigValue<String> savePointMaterials, linkedSavePointRecovers, savePointRecovers, warpPointRecovers;

    public ModConfigSpec.ConfigValue<List<? extends String>> startingRecipes;
    
    CommonConfig(final ModConfigSpec.Builder builder) {
		builder.push("general");

        bombExplodeWithFire = builder
                .comment("Allow Bomb heartless to explode when lit on fire")
                .translation(KingdomKeys.MODID + ".config.bomb_explode_with_fire")
                .define("bombExplodeWithfire", true);

        blizzardChangeBlocks = builder
                .comment("Allow Blizzard to turn lava into obsidian and freeze water")
                .translation(KingdomKeys.MODID + ".config.blizzard_change_blocks")
                .define("blizzardChangeBlocks", true);

        keybladeOpenDoors = builder
                .comment("Allow keyblades to open iron doors with right click")
                .translation(KingdomKeys.MODID + ".config.keyblade_open_doors")
                .define("keybladeOpenDoors", true);

        driveHeal = builder
                .comment("Health % restored when using a drive form")
                .translation(KingdomKeys.MODID + ".config.drive_heal")
                .defineInRange("driveHeal",50,0,100);

        drivePointsMultiplier = builder
                .comment("Drive Points Drop Multiplier")
                .translation(KingdomKeys.MODID + ".config.drive_points_multiplier")
                .defineInRange("drivePointsMultiplier",1.0,0,100);

        focusPointsMultiplier = builder
                .comment("Focus Points Drop Multiplier")
                .translation(KingdomKeys.MODID + ".config.focus_points_multiplier")
                .defineInRange("focusPointsMultiplier",1.0,0,100);

        critMult = builder
                .comment("Critic Damage Multiplier")
                .translation(KingdomKeys.MODID + ".config.crit_mult")
                .defineInRange("critMult",1.5,0,100);
        
        needKeybladeForHeartless = builder
        		.comment("Force the player to need a Keyblade or an Organization weapon to hurt Heartless, and Nobodies")
                .translation(KingdomKeys.MODID + ".config.need_keyblade_for_heartless")
                .define("needKeybladeForHeartless", false);

        savePointMaterials = builder
                .comment("Materials used to upgrade save points (Default values: HP=kingdomkeys:orichalcum,MP=kingdomkeys:illusory_crystal,HUNGER=kingdomkeys:hungry_crystal,FOCUS=kingdomkeys:remembrance_crystal,DRIVE=kingdomkeys:evanescent_crystal,TIER=kingdomkeys:orichalcumplus)")
                .translation(KingdomKeys.MODID + ".config.save_point_materials")
                .define("savePointMaterials", "HP=kingdomkeys:orichalcum,MP=kingdomkeys:illusory_crystal,HUNGER=kingdomkeys:hungry_crystal,FOCUS=kingdomkeys:remembrance_crystal,DRIVE=kingdomkeys:evanescent_crystal,TIER=kingdomkeys:orichalcumplus", o -> o instanceof String);

        savePointRecovers = builder
                .comment("Stats restored when using a normal savepoint (Allowed values: HP,MP,HUNGER,FOCUS,DRIVE)")
                .translation(KingdomKeys.MODID + ".config.normal_save_point_restore_list")
                .define("normalSavePointRestoreList", "HP,MP", o -> o instanceof String);

        linkedSavePointRecovers = builder
                .comment("Stats restored when using a linked savepoint (Allowed values: HP,MP,HUNGER,FOCUS,DRIVE)")
                .translation(KingdomKeys.MODID + ".config.full_save_point_restore_list")
                .define("fullSavePointRestoreList", "HP,HUNGER,MP,FOCUS", o -> o instanceof String);

        warpPointRecovers = builder
                .comment("Stats restored when using a warp point (Allowed values: HP,MP,HUNGER,FOCUS,DRIVE)")
                .translation(KingdomKeys.MODID + ".config.warp_point_restore_list")
                .define("warpPointRestoreList", "HP,HUNGER,MP,FOCUS,DRIVE", o -> o instanceof String);

        allowBlocksInHangarArea = builder
                .comment("Allow the player to place a hangar in a zone where there are blocks already (probably a good idea to disable on servers)")
                .translation(KingdomKeys.MODID + ".config.allow_blocks_in_hangar_area")
                .define("allowBlocksInHangarArea", true);

        gummiBlocksDropPercent = builder
                .comment("Percentage of blocks dropped when the Gummi Ship gets destroyed")
                .translation(KingdomKeys.MODID + ".config.gummi_blocks_drop_percent")
                .defineInRange("gummiBlocksDropPercent",80,0,100);

        builder.pop();

        builder.push("spawning");

        heartlessSpawningMode = builder
                .comment("Heartless spawning mode: NEVER, ALWAYS, AFTER_KEYCHAIN (after the first keychain is synthesized), AFTER_DRAGON (after the Ender Dragon is defeated)")
                .translation(KingdomKeys.MODID + ".config.heartless_spawning_mode")
                .defineEnum("heartlessSpawningMode", SpawningMode.AFTER_KEYCHAIN);

        mobSpawnRate = builder
                .comment("Mob Spawn chance in percentage [type, chance] (if the chance doesn't add up to 100, enemies will not spawn)")
                .translation(KingdomKeys.MODID + ".config.mob_spawn")
                .defineList("mobSpawn", Lists.newArrayList("Pureblood,35", "Emblem,35", "Nobody,30"), () -> "Pureblood,35", o -> o instanceof String);

        playerSpawnHeartless = builder
                .comment("Allow a heartless and a nobody to spawn when a player gets killed by a heartless")
                .translation(KingdomKeys.MODID + ".config.player_spawn_heartless")
                .define("playerSpawnHeartless", true);

        mobLevelingUp = builder
                .comment("Allow heartless and nobodies to spawn with levels according to players")
                .translation(KingdomKeys.MODID + ".config.player_mob_leveling_up")
                .define("mobLevelingUp", true);

        mobLevelName = builder
                .comment("Add the level to the name of mobs, when this is enabled the name will not be removed from mobs that have already spawned in your world")
                .translation(KingdomKeys.MODID + ".config.mob_level_name")
                .define("mobLevelName", true);
        
        rodHeartlessLevelScale = builder
                .comment("Heartless spawning in the ROD will increase 1 level every X blocks")
                .translation(KingdomKeys.MODID + ".config.rod_heartless_level_scale")
                .defineInRange("rodHeartlessLevelScale",10,1,1000);
        
        rodHeartlessMaxLevel = builder
                .comment("Max level for heartless spawning in ROD")
                .translation(KingdomKeys.MODID + ".config.rod_heartless_max_level")
                .defineInRange("rodHeartlessMaxLevel",200,1,10000);

        playerSpawnHeartlessData = builder
                .comment("Heartless and nobody stats: name, hp (% of the player's), strength (% of the player's)")
                .translation(KingdomKeys.MODID + ".config.player_spawn_heartless_Data")
                .defineList("playerSpawnHeartlessData", Lists.newArrayList("Heartless,100,100", "Nobody,100,100"), () -> "Heartless,100,100", o -> o instanceof String);
        
        respawnROD = builder
                .comment("Force players who die in the Realm of Darkness to respawn there")
                .translation(KingdomKeys.MODID + ".config.respawn_rod")
                .define("respawnROD", false);

        mobLevelStats = builder
                .comment("Mob base stats multiplier out of 100% (default 10)")
                .translation(KingdomKeys.MODID + ".config.mob_level_stats")
                .defineInRange("mobLevelStats",10,0,100);

        bossDespawnIfNoTarget = builder
        		.comment("Make bosses despawn once his target disappears")
                .translation(KingdomKeys.MODID + ".config.boss_despawn_if_no_target")
                .define("bossDespawnIfNoTarget", true);
        
        builder.pop();

        builder.push("drops");
        hpDropProbability = builder
                .comment("HP Drops Probability")
                .translation(KingdomKeys.MODID + ".config.hp_drop_probability")
                .defineInRange("hpDropProbability",80,0,100);

        mpDropProbability = builder
                .comment("MP Drops Probability")
                .translation(KingdomKeys.MODID + ".config.mp_drop_probability")
                .defineInRange("mpDropProbability",80,0,100);

        munnyDropProbability = builder
                .comment("Munny Drops Probability")
                .translation(KingdomKeys.MODID + ".config.munny_drop_probability")
                .defineInRange("munnyDropProbability",80,0,100);

        driveDropProbability = builder
                .comment("Drive Drops Probability")
                .translation(KingdomKeys.MODID + ".config.drive_drop_probability")
                .defineInRange("driveDropProbability",80,0,100);

        focusDropProbability = builder
                .comment("Focus Drops Probability")
                .translation(KingdomKeys.MODID + ".config.focus_drop_probability")
                .defineInRange("focusDropProbability",80,0,100);

        builder.pop();

        builder.push("shotlock");

        shotlockMult = builder
                .comment("Shotlock Damage Multiplier (magic * multiplier)")
                .translation(KingdomKeys.MODID + ".config.shotlock_mult")
                .defineInRange("shotlockMult",0.4,0,100);

        builder.pop();

        builder.push("synthesis");

        startingRecipes = builder
                .comment("Synthesis recipes given to the player on first join, so changing this list will not give you recipes in worlds you've already created")
                .translation(KingdomKeys.MODID + ".config.starting_recipes")
                .defineList("startingRecipes", List.of(
                        KingdomKeys.MODID + ":" + Strings.SM_MythrilShard,
                        KingdomKeys.MODID + ":" + Strings.SM_MythrilStone,
                        KingdomKeys.MODID + ":" + Strings.SM_MythrilGem,
                        KingdomKeys.MODID + ":" + Strings.SM_MythrilCrystal,
                        KingdomKeys.MODID + ":" + Strings.potion,
                        KingdomKeys.MODID + ":" + Strings.hiPotion,
                        KingdomKeys.MODID + ":" + Strings.megaPotion,
                        KingdomKeys.MODID + ":" + Strings.ether,
                        KingdomKeys.MODID + ":" + Strings.hiEther,
                        KingdomKeys.MODID + ":" + Strings.megaEther,
                        KingdomKeys.MODID + ":" + Strings.elixir,
                        KingdomKeys.MODID + ":" + Strings.megaLixir,
                        KingdomKeys.MODID + ":" + Strings.driveRecovery,
                        KingdomKeys.MODID + ":" + Strings.hiDriveRecovery,
                        KingdomKeys.MODID + ":" + Strings.refocuser,
                        KingdomKeys.MODID + ":" + Strings.hiRefocuser,
                        KingdomKeys.MODID + ":" + Strings.powerBoost,
                        KingdomKeys.MODID + ":" + Strings.magicBoost,
                        KingdomKeys.MODID + ":" + Strings.defenseBoost,
                        KingdomKeys.MODID + ":" + Strings.apBoost
                ), o -> {
                    if (o instanceof String s) {
                        return ResourceLocation.tryParse(s) != null;
                    }
                    return false;
                });

        builder.pop();
    }
  
}
