package online.kingdomkeys.kingdomkeys.config;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Config file for client only config options
 */
public class ClientConfig {

   // public static boolean CORSAIR_KEYBOARD_LIGHTING;
    public ForgeConfigSpec.BooleanValue corsairKeyboardLighting;
    
    public ForgeConfigSpec.IntValue cmTextXOffset;
    public ForgeConfigSpec.BooleanValue cmHeaderTextVisible;
    
    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        
        corsairKeyboardLighting = builder
                .comment("Enable Corsair RGB keyboard lighting (Ignore if you don't have a Corsair keyboard)")
                .translation(KingdomKeys.MODID + ".config.corsair_keyboard_lighting")
                .define("corsairKeyboardLighting", true);
        	
        builder.pop();
        
        builder.push("gui");
        
        cmTextXOffset = builder
                .comment("Command Menu Text X Offset")
                .translation(KingdomKeys.MODID + ".config.cm_text_x_offset")
                .defineInRange("cmTextXOffset", 0, -1000, 1000);
        
        cmHeaderTextVisible = builder
                .comment("Command Menu Header Text Visibility")
                .translation(KingdomKeys.MODID + ".config.cm_header_text_visibility")
                .define("cmHeaderTextVisibility", true);
        
        builder.pop();
    }

}
