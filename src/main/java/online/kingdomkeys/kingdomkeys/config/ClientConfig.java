package online.kingdomkeys.kingdomkeys.config;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Config file for client only config options
 */
public class ClientConfig {

    public ForgeConfigSpec.BooleanValue corsairKeyboardLighting;

    public ForgeConfigSpec.IntValue cmTextXOffset;
    public ForgeConfigSpec.BooleanValue cmHeaderTextVisible;
    public ForgeConfigSpec.IntValue cmXScale, cmXPos, cmSubXOffset;


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
    }

}
