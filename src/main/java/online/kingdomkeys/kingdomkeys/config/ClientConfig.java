package online.kingdomkeys.kingdomkeys.config;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Config file for client only config options
 */
public class ClientConfig {

   // public static boolean CORSAIR_KEYBOARD_LIGHTING;
    public ForgeConfigSpec.BooleanValue corsairKeyboardLighting;
    
    public ForgeConfigSpec.BooleanValue cmHeaderTextVisible;
    public ForgeConfigSpec.IntValue cmTextXOffset, cmXScale, cmXPos, cmSubXOffset;
    
    public ForgeConfigSpec.BooleanValue hpShowHearts;
    public ForgeConfigSpec.IntValue hpXPos, hpYPos;
    
    public ForgeConfigSpec.IntValue mpXPos, mpYPos;
    
    
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
        
        builder.pop();
    }

}
