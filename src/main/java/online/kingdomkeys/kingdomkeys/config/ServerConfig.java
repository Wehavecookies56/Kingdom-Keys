package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ServerConfig {

    public ForgeConfigSpec.IntValue recipeDropChance;

    public ForgeConfigSpec.IntValue partyRangeLimit;

    public ForgeConfigSpec.ConfigValue<List<? extends String>> driveFormXPMultiplier;
    public ForgeConfigSpec.ConfigValue<List<? extends Integer>> statsMultiplier;

    public ForgeConfigSpec.DoubleValue xpMultiplier;
    public ForgeConfigSpec.DoubleValue heartMultiplier;
    public ForgeConfigSpec.DoubleValue partyXPShare;
   // public ForgeConfigSpec.IntValue magicUsesTimer;
    
    public ForgeConfigSpec.BooleanValue requireSynthTier;
    
    public ForgeConfigSpec.IntValue limitLaserCircleCost;
    public ForgeConfigSpec.IntValue limitLaserDomeCost;
    public ForgeConfigSpec.IntValue limitArrowRainCost;

    ServerConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");

        recipeDropChance = builder
                .comment("Recipe drop chance")
                .translation(KingdomKeys.MODID + ".config.recipe_drop_chance")
                .defineInRange("recipeDropChance", 2, 0, 100);

        partyRangeLimit = builder
                .comment("Party range limit")
                .translation(KingdomKeys.MODID + ".config.party_range_limit")
                .defineInRange("partyRangeLimit", 50, 1, 150);
        
        /*magicUsesTimer = builder
                .comment("Magic uses timer in ticks (1 second = 20 ticks) 0 = magic uses won't decrease, 1 = disable grand magic. Used for grand magic so bigger number the longer it will take to substract an use so it will be easier to activate it")
                .translation(KingdomKeys.MODID + ".config.magic_uses_timer")
                .defineInRange("magicUsesTimer", 100, 0, 1200);*/
        
        requireSynthTier = builder
                .comment("If true players will only be able to synthesis items from their tier or lower, if false they can synthesise all regardless of their tier")
                .translation(KingdomKeys.MODID + ".config.require_synth_tier")
                .define("requireSynthTier", false);


        builder.pop();

        builder.push("leveling");

        xpMultiplier = builder
                .comment("XP Multiplier")
                .translation(KingdomKeys.MODID + ".config.xp_multiplier")
                .defineInRange("xpMultiplier", 1F, 0, 1000);

        heartMultiplier = builder
                .comment("Hearts Multiplier")
                .translation(KingdomKeys.MODID + ".config.heart_multiplier")
                .defineInRange("heartMultiplier", 1F, 0, 1000);
        
        partyXPShare = builder
              //  .comment("XP Share in party (value used here will be used if the party size is 2, will be downscaled the bigger the party is)")
        		.comment("XP Share in party (killer gets 100%, the rest of party members the % specified here)")
                .translation(KingdomKeys.MODID + ".config.party_xp_share")
                .defineInRange("partyXPShare", 0F, 0, 100);

        driveFormXPMultiplier = builder
                .comment("Drive Form XP Multiplier")
                .translation(KingdomKeys.MODID + ".config.drive_form_xp_multiplier")
                .defineList("driveFormXPMultiplier", Lists.newArrayList("Valor,1", "Wisdom,1", "Limit,1", "Master,1", "Final,1"), o -> o instanceof String);

        statsMultiplier = builder
                .comment("Strength, Magic and Defense multiplier in % for players")
                .translation(KingdomKeys.MODID + ".config.stats_multiplier")
                .defineList("statsMultiplier", Lists.newArrayList(100, 100, 100), o -> o instanceof Integer);

        builder.pop();
        
        builder.push("limits");

        limitLaserCircleCost = builder
                .comment("Laser Circle Cost")
                .translation(KingdomKeys.MODID + ".config.laser_circle_cost")
                .defineInRange("laserCircleCost",100,0,1000);
        
        limitLaserDomeCost = builder
                .comment("Laser Dome Cost")
                .translation(KingdomKeys.MODID + ".config.laser_dome_cost")
                .defineInRange("laserDomeCost",400,0,1000);
        
        limitArrowRainCost = builder
                .comment("Laser Dome Cost")
                .translation(KingdomKeys.MODID + ".config.arrow_rain_cost")
                .defineInRange("arrowRainCost",300,0,1000);

        builder.pop();
    }

}
