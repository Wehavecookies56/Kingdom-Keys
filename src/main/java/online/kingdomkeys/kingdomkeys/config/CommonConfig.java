package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;


/**
 * Config file for config options shared between the server and the client
 */
public class CommonConfig {

    public ForgeConfigSpec.EnumValue<SpawningMode> heartlessSpawningMode;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> mobSpawnRate;

    public ForgeConfigSpec.IntValue rodHeartlessLevelScale;
    public ForgeConfigSpec.IntValue rodHeartlessMaxLevel;
    public ForgeConfigSpec.BooleanValue mobLevelingUp;

    public ForgeConfigSpec.BooleanValue debugConsoleOutput;
    public ForgeConfigSpec.BooleanValue bombExplodeWithFire;
    public ForgeConfigSpec.BooleanValue keybladeOpenDoors;

    public ForgeConfigSpec.IntValue driveHeal;

    public ForgeConfigSpec.DoubleValue drivePointsMultiplier;
    public ForgeConfigSpec.DoubleValue focusPointsMultiplier;

    public ForgeConfigSpec.IntValue hpDropProbability;
    public ForgeConfigSpec.IntValue mpDropProbability;
    public ForgeConfigSpec.IntValue munnyDropProbability;
    public ForgeConfigSpec.IntValue driveDropProbability;
    public ForgeConfigSpec.IntValue focusDropProbability;

    public ForgeConfigSpec.BooleanValue blizzardChangeBlocks;
    public ForgeConfigSpec.BooleanValue playerSpawnHeartless;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> playerSpawnHeartlessData;

    public ForgeConfigSpec.DoubleValue shotlockMult;
    public ForgeConfigSpec.DoubleValue critMult;

    public ForgeConfigSpec.IntValue mobLevelStats;

    public ForgeConfigSpec.BooleanValue bossDespawnIfNoTarget;
    
    CommonConfig(final ForgeConfigSpec.Builder builder) {
		builder.push("general");

        debugConsoleOutput = builder
                .comment("Enable debug console output")
                .translation(KingdomKeys.MODID + ".config.debug")
                .define("debugConsoleOutput", false);

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

        builder.pop();

        builder.push("spawning");

        heartlessSpawningMode = builder
                .comment("Heartless spawning mode: NEVER, ALWAYS, AFTER_KEYCHAIN (after the first keychain is synthesized), AFTER_DRAGON (after the Ender Dragon is defeated)")
                .translation(KingdomKeys.MODID + ".config.heartless_spawning_mode")
                .defineEnum("heartlessSpawningMode", SpawningMode.AFTER_KEYCHAIN);

        mobSpawnRate = builder
                .comment("Mob Spawn chance in percentage [type, chance] (if the chance doesn't add up to 100, enemies will not spawn)")
                .translation(KingdomKeys.MODID + ".config.mob_spawn")
                .defineList("mobSpawn", Lists.newArrayList("Pureblood,35", "Emblem,35", "Nobody,30"), o -> o instanceof String);

        playerSpawnHeartless = builder
                .comment("Allow a heartless and a nobody to spawn when a player gets killed by a heartless")
                .translation(KingdomKeys.MODID + ".config.player_spawn_heartless")
                .define("playerSpawnHeartless", true);

        mobLevelingUp = builder
                .comment("Allow heartless and nobodies to spawn with levels according to players")
                .translation(KingdomKeys.MODID + ".config.player_mob_leveling_up")
                .define("mobLevelingUp", true);
        
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
                .defineList("playerSpawnHeartlessData", Lists.newArrayList("Heartless,100,100", "Nobody,100,100"), o -> o instanceof String);

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
    }
  
}
