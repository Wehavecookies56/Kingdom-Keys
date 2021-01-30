package online.kingdomkeys.kingdomkeys.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;


/**
 * Config file for config options shared between the server and the client
 */
public class CommonConfig {

    public ForgeConfigSpec.EnumValue<SpawningMode> heartlessSpawningMode;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> mobSpawnRate;
    public ForgeConfigSpec.BooleanValue oreGen;
    public ForgeConfigSpec.BooleanValue bloxGen;

    public ForgeConfigSpec.BooleanValue debugConsoleOutput;
    public ForgeConfigSpec.ConfigValue<List<? extends Double>> limitLaserDomeMult;

    CommonConfig(final ForgeConfigSpec.Builder builder) {    	
		builder.push("general");

        oreGen = builder
                .comment("Allow Synthesis Materials ores to generate")
                .translation(KingdomKeys.MODID + ".config.ore_gen")
                .define("oreGen", true);

        bloxGen = builder
                .comment("Allow Blox to generate")
                .translation(KingdomKeys.MODID + ".config.blox_gen")
                .define("bloxGen", true);

        debugConsoleOutput = builder
                .comment("Enable debug console output")
                .translation(KingdomKeys.MODID + ".config.debug")
                .define("debugConsoleOutput", false);

        builder.pop();

        builder.push("spawning");

        heartlessSpawningMode = builder
                .comment("Heartless spawning mode: NEVER, ALWAYS, AFTER_KEYCHAIN (after the first keychain is synthesized), AFTER_DRAGON (after the Ender Dragon is defeated)")
                .translation(KingdomKeys.MODID + ".config.heartless_spawning_mode")
                .defineEnum("heartlessSpawningMode", SpawningMode.AFTER_KEYCHAIN);

        mobSpawnRate = builder
                .comment("Mob Spawn")
                .translation(KingdomKeys.MODID + ".config.mob_spawn")
                .defineList("mobSpawn", Lists.newArrayList("Moogle,2,0,1", "Pureblood,2,0,1", "Emblem,2,0,1", "Nobody,2,0,1"), o -> o instanceof String);

        builder.pop();
        
        builder.push("limits");

        /*limitLaserDomeCosts = builder
                .comment("Laser Dome Costs")
                .translation(KingdomKeys.MODID + ".config.laser_dome_costs")
                .defineList("laserDomeCosts",Lists.newArrayList(100,300,500), o -> o instanceof Integer);
        */
        limitLaserDomeMult = builder
                .comment("Laser Dome Damage Multiplier ((strength + magic) / 2 * multiplier)")
                .translation(KingdomKeys.MODID + ".config.laser_dome_mult")
                .defineList("laserDomeMult",Lists.newArrayList(0.15,0.2,0.3), o -> o instanceof Double);

        builder.pop();
    }
  
}
