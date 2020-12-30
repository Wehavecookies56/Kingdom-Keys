package online.kingdomkeys.kingdomkeys.config;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Config file for client only config options
 */
public class ClientConfig {

   // public static boolean CORSAIR_KEYBOARD_LIGHTING;
    public static ForgeConfigSpec.BooleanValue corsairKeyboardLighting;
    ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        corsairKeyboardLighting = builder
                .comment("Enable Corsair RGB keyboard lighting (Ignore if you don't have a Corsair keyboard)")
                .translation(KingdomKeys.MODID + ".config.corsair_keyboard_lighting")
                .define("corsairKeyboardLighting", true);
        builder.pop();
    }

}
