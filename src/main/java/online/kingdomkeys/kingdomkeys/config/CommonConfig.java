package online.kingdomkeys.kingdomkeys.config;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


/**
 * Config file for config options shared between the server and the client
 */
public class CommonConfig {

    public static ForgeConfigSpec.IntValue heartlessSpawningMode;
    public static ForgeConfigSpec.BooleanValue oreGen;
    public static ForgeConfigSpec.BooleanValue debugConsoleOutput;

    CommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        
        heartlessSpawningMode = builder
                .comment("Heartless spawning mode (0 = never, 1 = always, 2 = after the first keychain is synthesised, 3 = after the dragon is defeated)")
                .translation(KingdomKeys.MODID + ".config.heartless_spawning_mode")
                .defineInRange("heartlessSpawningMode", 2, 0, 3);
        
        oreGen = builder
        		.comment("Allow Synthesis Materials ores to generate")
                .translation(KingdomKeys.MODID + ".config.ore_gen")
                .define("oreGen", true);

        debugConsoleOutput = builder
                .comment("Enable debug console output")
                .translation(KingdomKeys.MODID + ".config.debug")
                .define("debugConsoleOutput", false);

        //Moogle spawning
        
        builder.pop();
        
    }
  
}
