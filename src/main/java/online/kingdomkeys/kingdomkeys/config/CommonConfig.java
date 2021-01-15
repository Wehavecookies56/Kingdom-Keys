package online.kingdomkeys.kingdomkeys.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.minecraftforge.common.ForgeConfigSpec;
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
    
    public static List<String> mobSpawnRate; //These are not working
    
    public static ForgeConfigSpec.DoubleValue xpMultiplier;
    public static ForgeConfigSpec.DoubleValue partyXPShare;
    public static ForgeConfigSpec.DoubleValue driveXPMultiplier;
    
	public static ForgeConfigSpec.DoubleValue valorFormXPMultiplier, wisdomFormXPMultiplier, limitFormXPMultiplier, masterFormXPMultiplier, finalFormXPMultiplier;

    CommonConfig(final ForgeConfigSpec.Builder builder) {    	
    	mobSpawnRate = new ArrayList<String>();
		mobSpawnRate.add("Moogle,2,0,1");
    	mobSpawnRate.add("Pureblood,2,0,1");
    	mobSpawnRate.add("Emblem,2,0,1");
    	mobSpawnRate.add("Nobody,2,0,1");
		
    	Predicate<Object> spawnRateCheck = new Predicate<Object>() {
			@Override
			public boolean test(Object t) {
				if (!(t instanceof String)) {
					return false;
				}
				
				String str = (String) t;
				return !isNullOrEmpty(str);
			}
		};		
		
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
        
        builder.pop();
        
        builder.push("spawning");

		heartlessSpawningMode = builder
				.comment("Heartless spawning mode, AFTER_KEYCHAIN (after the first keychain is synthesized), AFTER_DRAGON (after the Ender Dragon is defeated)")
				.translation(KingdomKeys.MODID + ".config.heartless_spawning_mode")
				.defineEnum("heartlessSpawningMode", SpawningMode.AFTER_KEYCHAIN);

				builder
				.comment("Mob Spawn")
        		.translation(KingdomKeys.MODID + ".config.mob_spawn")
				.defineList("mobSpawn", mobSpawnRate, spawnRateCheck);
        
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
        
        valorFormXPMultiplier = builder
				.comment("Valor Form XP multiplier")
        		.translation(KingdomKeys.MODID + ".config.valor_xp_multiplier")
                .defineInRange("valorXPMultiplier", 1F, 0, 1000);
        
        wisdomFormXPMultiplier = builder
				.comment("Wisdom Form XP multiplier")
        		.translation(KingdomKeys.MODID + ".config.wisdom_xp_multiplier")
                .defineInRange("wisdomXPMultiplier", 1F, 0, 1000);
        
        limitFormXPMultiplier = builder
				.comment("Limit Form XP multiplier")
        		.translation(KingdomKeys.MODID + ".config.limit_xp_multiplier")
                .defineInRange("limitXPMultiplier", 1F, 0, 1000);
        
        masterFormXPMultiplier = builder
				.comment("Master Form XP multiplier")
        		.translation(KingdomKeys.MODID + ".config.master_xp_multiplier")
                .defineInRange("masterXPMultiplier", 1F, 0, 1000);
        
        finalFormXPMultiplier = builder
				.comment("Final Form XP multiplier")
        		.translation(KingdomKeys.MODID + ".config.final_xp_multiplier")
                .defineInRange("finalXPMultiplier", 1F, 0, 1000);
        		

		builder.pop();
        
    }
    
    public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty() && !str.equalsIgnoreCase("n/a"))
			return false;
		return true;
	}
  
}
