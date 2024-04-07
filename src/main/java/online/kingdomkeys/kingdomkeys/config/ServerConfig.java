package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ServerConfig {

    public ForgeConfigSpec.IntValue recipeDropChance;

    public ForgeConfigSpec.IntValue partyRangeLimit;
    public ForgeConfigSpec.IntValue partyMembersLimit;

    public ForgeConfigSpec.ConfigValue<List<? extends String>> driveFormXPMultiplier;
    public ForgeConfigSpec.ConfigValue<List<? extends Integer>> statsMultiplier;

    public ForgeConfigSpec.DoubleValue xpMultiplier;
    public ForgeConfigSpec.DoubleValue heartMultiplier;
    public ForgeConfigSpec.DoubleValue partyXPShare;
   // public ForgeConfigSpec.IntValue magicUsesTimer;
    
    public ForgeConfigSpec.BooleanValue requireSynthTier;
        
    public ForgeConfigSpec.BooleanValue projectorHasShop;
    public ForgeConfigSpec.BooleanValue orgEnabled;
    public ForgeConfigSpec.BooleanValue allowBoosts, allowPartyKO, hostileMobsLevel;

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
        
        partyMembersLimit = builder
                .comment("Party members limit")
                .translation(KingdomKeys.MODID + ".config.party_members_limit")
                .defineInRange("partyMembersLimit", 5, 1, 20);
               
        requireSynthTier = builder
                .comment("If true players will only be able to synthesis items from their tier or lower, if false they can synthesise all regardless of their tier")
                .translation(KingdomKeys.MODID + ".config.require_synth_tier")
                .define("requireSynthTier", false);
        
        projectorHasShop = builder
                .comment("If true moogle projectors will have the default shop available, if false only the moogles will")
                .translation(KingdomKeys.MODID + ".config.projector_has_shop")
                .define("projectorHasShop", false);
        
        orgEnabled = builder
                .comment("If true the organization system will be enabled, if false will be disabled")
                .translation(KingdomKeys.MODID + ".config.org_enabled")
                .define("orgEnabled", true);
        
        allowBoosts = builder
                .comment("If true then boosts like Power Boost, Magic Boost and Defense Boost will be enabled, if false they won't add stats")
                .translation(KingdomKeys.MODID + ".config.allow_boosts")
                .define("allowBoosts", true);
        
        allowPartyKO = builder
                .comment("If true then when a player in a party (with more party members online) dies, they will be put in a KO state allowing to cast cure or potions to be revived")
                .translation(KingdomKeys.MODID + ".config.allow_party_ko")
                .define("allowPartyKO", true);
        
        hostileMobsLevel = builder
                .comment("If true other hostile mobs will level up alongside the player level the same way heartless do")
                .translation(KingdomKeys.MODID + ".config.hostile_mobs_level")
                .define("hostileMobsLevel", true);

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
        
    }

}
