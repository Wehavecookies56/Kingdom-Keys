package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.world.features.ModFeatures;


/**
 * Config file for config options shared between the server and the client
 */
public class CommonConfig {

    public ForgeConfigSpec.EnumValue<SpawningMode> heartlessSpawningMode;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> mobSpawnRate;

    public ForgeConfigSpec.BooleanValue mobLevelingUp;

    public ForgeConfigSpec.BooleanValue oreGen;
    public ForgeConfigSpec.BooleanValue bloxGen;

    public ForgeConfigSpec.ConfigValue<String> twilightOreNetherGen;
    public ForgeConfigSpec.ConfigValue<String> wellspringOreNetherGen;
    public ForgeConfigSpec.ConfigValue<String> writhingOreNetherGen;
    public ForgeConfigSpec.ConfigValue<String> blazingOreNetherGen;
    public ForgeConfigSpec.ConfigValue<String> writhingOreEndGen;
    public ForgeConfigSpec.ConfigValue<String> pulsingOreEndGen;
    public ForgeConfigSpec.ConfigValue<String> betwixtOreGen;
    public ForgeConfigSpec.ConfigValue<String> sinisterOreGen;
    public ForgeConfigSpec.ConfigValue<String> stormyOreGen;
    public ForgeConfigSpec.ConfigValue<String> writhingOreGen;
    public ForgeConfigSpec.ConfigValue<String> hungryOreGen;
    public ForgeConfigSpec.ConfigValue<String> lightningOreGen;
    public ForgeConfigSpec.ConfigValue<String> lucidOreGen;
    public ForgeConfigSpec.ConfigValue<String> remembranceOreGen;
    public ForgeConfigSpec.ConfigValue<String> soothingOreGen;
    public ForgeConfigSpec.ConfigValue<String> tranquilityOreGen;
    public ForgeConfigSpec.ConfigValue<String> twilightOreGen;
    public ForgeConfigSpec.ConfigValue<String> wellspringOreGen;
    public ForgeConfigSpec.ConfigValue<String> blazingOreWarmGen;
    public ForgeConfigSpec.ConfigValue<String> frostOreColdGen;
    public ForgeConfigSpec.ConfigValue<String> pulsingOreColdGen;
    public ForgeConfigSpec.ConfigValue<String> frostOreColderGen;
    public ForgeConfigSpec.ConfigValue<String> pulsingOreWetGen;
	public ForgeConfigSpec.ConfigValue<String> stormyOreWetGen;
	public ForgeConfigSpec.ConfigValue<String> blazingOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> betwixtOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> frostOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> pulsingOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> sinisterOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> soothingOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> stormyOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> twilightOreDeepslateGen;
	public ForgeConfigSpec.ConfigValue<String> writhingOreDeepslateGen;

    public ForgeConfigSpec.ConfigValue<String> bloxClusterEndGen;
    public ForgeConfigSpec.ConfigValue<String> prizeBloxClusterEndGen;
    public ForgeConfigSpec.ConfigValue<String> bloxClusterGen;
    public ForgeConfigSpec.ConfigValue<String> prizeBloxClusterGen;

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

        builder.push("worldgen");

        oreGen = builder
                .comment("Allow Synthesis Materials ores to generate")
                .translation(KingdomKeys.MODID + ".config.ore_gen")
                .define("oreGen", true);

        bloxGen = builder
                .comment("Allow Blox to generate")
                .translation(KingdomKeys.MODID + ".config.blox_gen")
                .define("bloxGen", true);

        builder.push("overworld");

        bloxClusterGen = builder
                .comment("Blox Cluster Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.blox_cluster_gen")
                .define("bloxClusterGen", ModFeatures.BLOX_CLUSTER_CONFIG.toConfigString());

        prizeBloxClusterGen = builder
                .comment("Prize Blox Cluster Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.prize_blox_cluster_gen")
                .define("prizeBloxClusterGen", ModFeatures.PRIZE_BLOX_CLUSTER_CONFIG.toConfigString());

        betwixtOreGen = builder
                .comment("Betwixt Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.betwixt_ore_gen")
                .define("betwixtOreGen", ModFeatures.BETWIXT_ORE_CONFIG.toConfigString());

        sinisterOreGen = builder
                .comment("Sinister Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.sinister_ore_gen")
                .define("sinisterOreGen", ModFeatures.SINISTER_ORE_CONFIG.toConfigString());

        stormyOreGen = builder
                .comment("Stormy Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.stormy_ore_gen")
                .define("stormyOreGen", ModFeatures.STORMY_ORE_CONFIG.toConfigString());

        writhingOreGen = builder
                .comment("Writhing Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.writhing_ore_gen")
                .define("writhingOreGen", ModFeatures.WRITHING_ORE_CONFIG.toConfigString());

        hungryOreGen = builder
                .comment("Hungry Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.hungry_ore_gen")
                .define("hungryOreGen", ModFeatures.HUNGRY_ORE_CONFIG.toConfigString());

        lightningOreGen = builder
                .comment("Lightning Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.lightning_ore_gen")
                .define("lightningOreGen", ModFeatures.LIGHTNING_ORE_CONFIG.toConfigString());

        lucidOreGen = builder
                .comment("Lucid Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.lucid_ore_gen")
                .define("lucidOreGen", ModFeatures.LUCID_ORE_CONFIG.toConfigString());

        remembranceOreGen = builder
                .comment("Remembrance Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.remembrance_ore_gen")
                .define("remembranceOreGen", ModFeatures.REMEMBRANCE_ORE_CONFIG.toConfigString());

        soothingOreGen = builder
                .comment("Soothing Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.soothing_ore_gen")
                .define("soothingOreGen", ModFeatures.SOOTHING_ORE_CONFIG.toConfigString());

        tranquilityOreGen = builder
                .comment("Tranquility Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.tranquility_ore_gen")
                .define("tranquilityOreGen", ModFeatures.TRANQUILITY_ORE_CONFIG.toConfigString());

        twilightOreGen = builder
                .comment("Twilight Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.twilight_ore_gen")
                .define("twilightOreGen", ModFeatures.TWILIGHT_ORE_CONFIG.toConfigString());

        wellspringOreGen = builder
                .comment("Wellspring Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.wellspring_ore_gen")
                .define("wellspringOreGen", ModFeatures.WELLSPRING_ORE_CONFIG.toConfigString());

        blazingOreWarmGen = builder
                .comment("Blazing Ore Generation (Warm biomes): [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.blazing_ore_warm_gen")
                .define("blazingOreWarmGen", ModFeatures.BLAZING_ORE_WARM_CONFIG.toConfigString());

        frostOreColdGen = builder
                .comment("Frost Ore Generation (Cold biomes): [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.frost_ore_cold_gen")
                .define("frostOreColdGen", ModFeatures.FROST_ORE_COLD_CONFIG.toConfigString());

        pulsingOreColdGen = builder
                .comment("Pulsing Ore Generation (Cold biomes): [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.pulsing_ore_cold_gen")
                .define("pulsingOreColdGen", ModFeatures.PULSING_ORE_COLD_CONFIG.toConfigString());

        frostOreColderGen = builder
                .comment("Frost Ore Generation (Very cold biomes): [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.frost_ore_colder_gen")
                .define("frostOreColderGen", ModFeatures.FROST_ORE_COLDER_CONFIG.toConfigString());

        pulsingOreWetGen = builder
                .comment("Pulsing Ore Generation (Wet biomes): [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.pulsing_ore_wet_gen")
                .define("pulsingOreWetGen", ModFeatures.PULSING_ORE_WET_CONFIG.toConfigString());

        stormyOreWetGen = builder
                .comment("Stormy Ore Generation (Wet biomes): [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.stormy_ore_wet_gen")
                .define("stormyOreWetGen", ModFeatures.STORMY_ORE_WET_CONFIG.toConfigString());
        
        blazingOreDeepslateGen = builder
                .comment("Blazing Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.blazing_ore_wet_gen")
                .define("blazingOreDeepslateGen", ModFeatures.BLAZING_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        betwixtOreDeepslateGen = builder
                .comment("Betwixt Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.betwixt_ore_wet_gen")
                .define("betwixtOreDeepslateGen", ModFeatures.BETWIXT_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        frostOreDeepslateGen = builder
                .comment("Frost Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.frost_ore_wet_gen")
                .define("frostOreDeepslateGen", ModFeatures.FROST_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        pulsingOreDeepslateGen = builder
                .comment("Pulsing Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.pulsing_ore_wet_gen")
                .define("pulsingOreDeepslateGen", ModFeatures.PULSING_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        sinisterOreDeepslateGen = builder
                .comment("Sinister Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.sinister_ore_wet_gen")
                .define("sinisterOreDeepslateGen", ModFeatures.SINISTER_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        soothingOreDeepslateGen = builder
                .comment("Soothing Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.soothing_ore_wet_gen")
                .define("soothingOreDeepslateGen", ModFeatures.SOOTHING_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        stormyOreDeepslateGen = builder
                .comment("Stormy Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.stormy_ore_wet_gen")
                .define("stormyOreDeepslateGen", ModFeatures.STORMY_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        twilightOreDeepslateGen = builder
                .comment("Twilight Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.twilight_ore_wet_gen")
                .define("twilightOreDeepslateGen", ModFeatures.TWILIGHT_ORE_DEEPSLATE_CONFIG.toConfigString());
        
        writhingOreDeepslateGen = builder
                .comment("Writhing Ore Deepslate Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.writhing_ore_wet_gen")
                .define("writhingOreDeepslateGen", ModFeatures.WRITHING_ORE_DEEPSLATE_CONFIG.toConfigString());

        builder.pop();

        builder.push("nether");

        twilightOreNetherGen = builder
                .comment("Twilight Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.twilight_ore_nether_gen")
                .define("twilightOreNetherGen", ModFeatures.TWILIGHT_ORE_NETHER_CONFIG.toConfigString());

        wellspringOreNetherGen = builder
                .comment("Wellspring Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.wellspring_ore_nether_gen")
                .define("wellspringOreNetherGen", ModFeatures.WELLSPRING_ORE_NETHER_CONFIG.toConfigString());

        writhingOreNetherGen = builder
                .comment("Writhing Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.writhing_ore_nether_gen")
                .define("writhingOreNetherGen", ModFeatures.WRITHING_ORE_NETHER_CONFIG.toConfigString());

        blazingOreNetherGen = builder
                .comment("Blazing Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.blazing_ore_nether_gen")
                .define("blazingOreNetherGen", ModFeatures.BLAZING_ORE_NETHER_CONFIG.toConfigString());

        builder.pop();

        builder.push("end");

        writhingOreEndGen = builder
                .comment("Writhing Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.writhing_ore_end_gen")
                .define("writhingOreEndGen", ModFeatures.WRITHING_ORE_END_CONFIG.toConfigString());

        pulsingOreEndGen = builder
                .comment("Pulsing Ore Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.pulsing_ore_end_gen")
                .define("pulsingOreEndGen", ModFeatures.PULSING_ORE_END_CONFIG.toConfigString());

        bloxClusterEndGen = builder
                .comment("Blox Cluster Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.blox_cluster_end_gen")
                .define("bloxClusterEndGen", ModFeatures.BLOX_CLUSTER_END_CONFIG.toConfigString());

        prizeBloxClusterEndGen = builder
                .comment("Prize Blox Cluster Generation: [enabled, vein size, minimum height, maximum height, count]")
                .translation(KingdomKeys.MODID + "config.prize_blox_cluster_end_gen")
                .define("prizeBloxClusterEndGen", ModFeatures.PRIZE_BLOX_CLUSTER_END_CONFIG.toConfigString());

        builder.pop();

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
