package online.kingdomkeys.kingdomkeys.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;


/**
 * Config file for config options shared between the server and the client
 */
public class CommonConfig {

    public static ForgeConfigSpec.EnumValue<SpawningMode> heartlessSpawningMode;
    public static ForgeConfigSpec.BooleanValue oreGen;
    public static ForgeConfigSpec.BooleanValue debugConsoleOutput;
    public static ForgeConfigSpec.IntValue recipeDropChance;
    
    public static List<String> mobSpawnRate;

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
        
    }
    
    public static boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty() && !str.equalsIgnoreCase("n/a"))
			return false;
		return true;
	}
  
}
