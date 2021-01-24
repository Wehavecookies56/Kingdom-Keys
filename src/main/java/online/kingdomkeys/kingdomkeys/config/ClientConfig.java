package online.kingdomkeys.kingdomkeys.config;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Config file for client only config options
 */
public class ClientConfig {

public ForgeConfigSpec.BooleanValue corsairKeyboardLighting;
    
    public ForgeConfigSpec.BooleanValue cmHeaderTextVisible;
    public ForgeConfigSpec.IntValue cmTextXOffset, cmXScale, cmXPos, cmSubXOffset;
    
    public ForgeConfigSpec.BooleanValue hpShowHearts;
    public ForgeConfigSpec.IntValue hpXPos, hpYPos;
    
    public ForgeConfigSpec.IntValue mpXPos, mpYPos;
    
    public ForgeConfigSpec.IntValue dpXPos, dpYPos;
    
    public ForgeConfigSpec.IntValue playerSkinXPos, playerSkinYPos;
    
    public ForgeConfigSpec.IntValue lockOnXPos, lockOnYPos;
    
    public ForgeConfigSpec.IntValue partyXPos, partyYPos, partyYOffset;


    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");

        corsairKeyboardLighting = builder
                .comment("Enable Corsair RGB keyboard lighting (Ignore if you don't have a Corsair keyboard)")
                .translation(KingdomKeys.MODID + ".config.corsair_keyboard_lighting")
                .define("corsairKeyboardLighting", true);

        builder.pop();

        builder.push("gui");
        
        builder.push("command_menu");
        
        cmTextXOffset = builder
                .comment("Command Menu Text X Offset")
                .translation(KingdomKeys.MODID + ".config.cm_text_x_offset")
                .defineInRange("cmTextXOffset", 0, -1000, 1000);
        
        cmHeaderTextVisible = builder
                .comment("Command Menu Header Text Visibility")
                .translation(KingdomKeys.MODID + ".config.cm_header_text_visibility")
                .define("cmHeaderTextVisibility", true);
        
        cmXScale = builder
                .comment("Command Menu X Scale %")
                .translation(KingdomKeys.MODID + ".config.cm_x_scale")
                .defineInRange("cmXScale", 100, -1000, 1000);
        
        cmXPos = builder
                .comment("Command Menu X Pos")
                .translation(KingdomKeys.MODID + ".config.cm_x_scale")
                .defineInRange("cmXPos", 0, -1000, 1000);
        
        cmSubXOffset = builder
                .comment("Command Menu Submenu X Offset %")
                .translation(KingdomKeys.MODID + ".config.cm_sub_x_offset")
                .defineInRange("cmSubXOffset", 100, -1000, 1000);
        
        builder.pop();
        
        builder.push("hp_bar");
        
        hpXPos = builder
                .comment("Health Bar X Pos")
                .translation(KingdomKeys.MODID + ".config.hp_x_pos")
                .defineInRange("hpXPos", 0, -1000, 1000);
        
        hpYPos = builder
                .comment("Health Bar Y Pos")
                .translation(KingdomKeys.MODID + ".config.hp_y_pos")
                .defineInRange("hpYPos", 0, -1000, 1000);
        
        hpShowHearts = builder
        		.comment("Show Hearts")
                .translation(KingdomKeys.MODID + ".config.hp_hearts")
                .define("hpShowHearts", true);
        
        builder.pop();
        
        builder.push("mp_bar");
        
        mpXPos = builder
                .comment("Magic Bar X Pos")
                .translation(KingdomKeys.MODID + ".config.mp_x_pos")
                .defineInRange("mpXPos", 0, -1000, 1000);
        
        mpYPos = builder
                .comment("Magic Bar Y Pos")
                .translation(KingdomKeys.MODID + ".config.mp_y_pos")
                .defineInRange("mpYPos", 0, -1000, 1000);
        
        builder.pop();
        
        builder.push("dp_bar");
        
        dpXPos = builder
                .comment("Drive Bar X Pos")
                .translation(KingdomKeys.MODID + ".config.dp_x_pos")
                .defineInRange("dpXPos", 0, -1000, 1000);
        
        dpYPos = builder
                .comment("Drive Bar Y Pos")
                .translation(KingdomKeys.MODID + ".config.dp_y_pos")
                .defineInRange("dpYPos", 0, -1000, 1000);
        
        builder.pop();
        
        builder.push("player_skin");
        
        playerSkinXPos = builder
                .comment("Player Skin X Pos")
                .translation(KingdomKeys.MODID + ".config.player_skin_x_pos")
                .defineInRange("playerSkinXPos", 0, -1000, 1000);
        
        playerSkinYPos = builder
                .comment("Player Skin Y Pos")
                .translation(KingdomKeys.MODID + ".config.player_skin_y_pos")
                .defineInRange("playerSkinYPos", 0, -1000, 1000);
        
        builder.pop();
        
        builder.push("lock_on");
        
        lockOnXPos = builder
                .comment("Lock On HP X Pos")
                .translation(KingdomKeys.MODID + ".config.lock_on_x_pos")
                .defineInRange("lockOnXPos", 0, -1000, 1000);
        
        lockOnYPos = builder
                .comment("Lock On HP Y Pos")
                .translation(KingdomKeys.MODID + ".config.lock_on_y_pos")
                .defineInRange("lockOnYPos", 0, -1000, 1000);
        
        builder.pop();
        
        builder.push("party");
        
        partyXPos = builder
                .comment("Party HUD X Pos")
                .translation(KingdomKeys.MODID + ".config.party_x_pos")
                .defineInRange("partyXPos", 0, -1000, 1000);
        
        partyYPos = builder
                .comment("Party HUD Y Pos")
                .translation(KingdomKeys.MODID + ".config.party_y_pos")
                .defineInRange("partyYPos", 0, -1000, 1000);
        
        partyYOffset = builder
                .comment("Party HUD Y Offset")
                .translation(KingdomKeys.MODID + ".config.party_y_offset")
                .defineInRange("partyYOffset", 100, -1000, 1000);
        
        builder.pop();
    
    builder.pop();
    }

}
