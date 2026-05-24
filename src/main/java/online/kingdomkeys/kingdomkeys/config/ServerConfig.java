package online.kingdomkeys.kingdomkeys.config;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.List;

public class ServerConfig {

    public ModConfigSpec.IntValue partyRangeLimit, partyMembersLimit, shotlockMaxDist, wayfinderCD, wayfinderCDCall;

    public ModConfigSpec.ConfigValue<List<? extends String>> driveFormXPMultiplier;
    public ModConfigSpec.ConfigValue<List<? extends Integer>> statsMultiplier;

    public ModConfigSpec.DoubleValue xpMultiplier, magicXPMultiplier, heartMultiplier, partyXPShare;
    public ModConfigSpec.BooleanValue requireSynthTier, requireSynthTierShop, projectorHasShop, savepointGlobal, getExpFromShop, orgEnabled, allowBoosts, allowPartyKO, wayfinderParty, hostileMobsLevel, dragonLevel, gummiShipFuelSystem, softLockOnMode;

    ServerConfig(final ModConfigSpec.Builder builder) {
        builder.push("general");

        softLockOnMode = builder
                .comment("Soft lock on allows some camera movement while locked on")
                .translation(KingdomKeys.MODID + ".config.soft_lock_on_mode")
                .define("softLockOnMode", true);

        gummiShipFuelSystem = builder
                .comment("Set whether to enable Gummi Ships fuel system")
                .translation(KingdomKeys.MODID + ".config.gummi_fuel_system")
                .define("gummiShipFuelSystem", true);
        
        partyRangeLimit = builder
                .comment("Party range limit")
                .translation(KingdomKeys.MODID + ".config.party_range_limit")
                .defineInRange("partyRangeLimit", 50, 1, 150);
        
        partyMembersLimit = builder
                .comment("Party members limit")
                .translation(KingdomKeys.MODID + ".config.party_members_limit")
                .defineInRange("partyMembersLimit", 5, 1, 20);
               
        requireSynthTier = builder
                .comment("If true players will only be able to synthesise items from their tier or lower, if false they can synthesise all of them regardless of their tier")
                .translation(KingdomKeys.MODID + ".config.require_synth_tier")
                .define("requireSynthTier", false);

        requireSynthTierShop = builder
                .comment("If true players will only be able to buy items from their tier or lower, if false they can buy all of them regardless of their tier")
                .translation(KingdomKeys.MODID + ".config.require_synth_tier_shop")
                .define("requireSynthTierShop", true);
        
        projectorHasShop = builder
                .comment("If true moogle projectors will have the default shop available, if false only the moogles will")
                .translation(KingdomKeys.MODID + ".config.projector_has_shop")
                .define("projectorHasShop", false);

        savepointGlobal = builder
                .comment("If true savepoints will allow any player to mark it as global, if false only creative players will")
                .translation(KingdomKeys.MODID + ".config.savepoint_global")
                .define("savepointGlobal", false);
        
        getExpFromShop = builder
                .comment("If true both synthesis and moogle shop will give EXP for recipes, if false only synthesis")
                .translation(KingdomKeys.MODID + ".config.get_exp_from_shop")
                .define("getExpFromShop", false);
        
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
                .comment("IMPORTANT! If supplementaries is installed we recommend to change in supplementaries-client.toml \"send_chat_on_death = true\" to false, since by default it sends a - at the end and might cause issues.")
                .translation(KingdomKeys.MODID + ".config.allow_party_ko")
                .define("allowPartyKO", true);

        wayfinderCD = builder
                .comment("Cooldown (in seconds) for the Wayfinder after a successful teleport")
                .translation(KingdomKeys.MODID + ".config.wayfinder_cd")
                .defineInRange("wayfinderCD", 300,1,10000);

        wayfinderCDCall = builder
                .comment("Cooldown (in seconds) for the Wayfinder after a call")
                .translation(KingdomKeys.MODID + ".config.wayfinder_cd_call")
                .defineInRange("wayfinderCDCall", 30,1,10000);

        wayfinderParty = builder
                .comment("If true then players will only be able to use the Wayfinder with other party members, if false with anyone")
                .translation(KingdomKeys.MODID + ".config.wayfinder_party")
                .define("wayfinderParty", true);
        
        hostileMobsLevel = builder
                .comment("If true other hostile mobs will level up alongside the player level the same way heartless do")
                .translation(KingdomKeys.MODID + ".config.hostile_mobs_level")
                .define("hostileMobsLevel", true);

        dragonLevel = builder
                .comment("If true the Enderdragon will level up too")
                .translation(KingdomKeys.MODID + ".config.dragon_level")
                .define("dragonLevel", true);

        shotlockMaxDist = builder
                .comment("Shotlock max distance for locking")
                .translation(KingdomKeys.MODID + ".config.shotlock_max_dist")
                .defineInRange("shotlockMaxDist", 200, 1, 1000);

        builder.pop();

        builder.push("leveling");

        xpMultiplier = builder
                .comment("XP Multiplier")
                .translation(KingdomKeys.MODID + ".config.xp_multiplier")
                .defineInRange("xpMultiplier", 1F, 0, 1000);

        magicXPMultiplier = builder
                .comment("Magic spells XP Multiplier")
                .translation(KingdomKeys.MODID + ".config.magic_xp_multiplier")
                .defineInRange("magicXPMultiplier", 1F, 0, 1000);

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
