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

    public static ForgeConfigSpec.EnumValue<SpawningMode> heartlessSpawningMode;
    public static ForgeConfigSpec.BooleanValue oreGen;
    public static ForgeConfigSpec.BooleanValue debugConsoleOutput;
    public static ForgeConfigSpec.IntValue recipeDropChance;
    
    public static ForgeConfigSpec.IntValue partyRangeLimit;

    
    public static ConfigValue<List<? extends String>> mobSpawnRate;
    public static ConfigValue<List<? extends String>> driveFormXPMultiplier;
    
    public static ForgeConfigSpec.DoubleValue xpMultiplier;
    public static ForgeConfigSpec.DoubleValue partyXPShare;
    public static ForgeConfigSpec.DoubleValue driveXPMultiplier;
    
	//public static ForgeConfigSpec.DoubleValue valorFormXPMultiplier, wisdomFormXPMultiplier, limitFormXPMultiplier, masterFormXPMultiplier, finalFormXPMultiplier;

    CommonConfig(final ForgeConfigSpec.Builder builder) {    	
		builder.push("general");
        
        oreGen = builder
        		.comment("Allow Synthesis Materials ores to generate")
                .translation(KingdomKeys.MODID + ".config.ore_gen")
                .define("oreGen", true);

        debugConsoleOutput = builder
                .comment("Enable debug console output")
                .translation(KingdomKeys.MODID + ".config.debug")
                .define("debugConsoleOutput", false);
        
        recipeDropChance = builder
                .comment("Recipe drop chance")
                .translation(KingdomKeys.MODID + ".config.recipe_drop_chance")
                .defineInRange("recipeDropChance", 2, 0, 100);
        
        partyRangeLimit = builder
                .comment("Party range limit")
                .translation(KingdomKeys.MODID + ".config.party_range_limit")
                .defineInRange("partyRangeLimit", 50, 1, 150);
        
        builder.pop();
        
        builder.push("spawning");

		heartlessSpawningMode = builder
				.comment("Heartless spawning mode, AFTER_KEYCHAIN (after the first keychain is synthesized), AFTER_DRAGON (after the Ender Dragon is defeated)")
				.translation(KingdomKeys.MODID + ".config.heartless_spawning_mode")
				.defineEnum("heartlessSpawningMode", SpawningMode.AFTER_KEYCHAIN);

		mobSpawnRate = builder
				.comment("Mob Spawn")
        		.translation(KingdomKeys.MODID + ".config.mob_spawn")
                .defineList("mobSpawn", Lists.newArrayList("Moogle,2,0,1", "Pureblood,2,0,1", "Pureblood,2,0,1", "Nobody,2,0,1"), o -> o instanceof String);		
        
        builder.pop();
        
        builder.push("leveling");
        
        xpMultiplier = builder
                .comment("XP Multiplier")
                .translation(KingdomKeys.MODID + ".config.xp_multiplier")
                .defineInRange("xpMultiplier", 1F, 0, 1000);

        partyXPShare = builder
                .comment("XP Share in party (value used here will be used if the party size is 2, will be downscaled the bigger the party is)")
                .translation(KingdomKeys.MODID + ".config.party_xp_share")
                .defineInRange("partyXPShare", 0F, 0, 100);
        
        driveFormXPMultiplier = builder
				.comment("Drive Form XP Multiplier")
        		.translation(KingdomKeys.MODID + ".config.drive_form_xp_multiplier")
                .defineList("driveFormXPMultiplier", Lists.newArrayList("Valor,1", "Wisdom,1", "Limit,1", "Master,1", "Final,1"), o -> o instanceof String);
        
		builder.pop();
        
    }
    
    public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty() && !str.equalsIgnoreCase("n/a"))
			return false;
		return true;
	}
  
}
