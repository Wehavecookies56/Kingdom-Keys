package online.kingdomkeys.kingdomkeys.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Config file for client only config options
 */
public class ClientConfig {

    public ForgeConfigSpec.BooleanValue corsairKeyboardLighting;
    
    public ForgeConfigSpec.BooleanValue cmHeaderTextVisible, cmClassicColors;
    public ForgeConfigSpec.IntValue cmTextXOffset, cmXScale, cmXPos, cmSelectedXOffset, cmSubXOffset;
    
    public ForgeConfigSpec.BooleanValue hpShowHearts;
    public ForgeConfigSpec.IntValue hpAlarm;
    
    public ForgeConfigSpec.IntValue hpXPos, hpYPos, hpXScale;
    
    public ForgeConfigSpec.IntValue mpXPos, mpYPos, mpXScale;
    
    public ForgeConfigSpec.IntValue dpXPos, dpYPos, dpXScale, dpYScale;
    
    public ForgeConfigSpec.IntValue playerSkinXPos, playerSkinYPos;
    
    public ForgeConfigSpec.IntValue lockOnXPos, lockOnYPos, lockOnHPScale, lockOnIconScale, lockOnIconRotation, lockOnHpPerBar;
    
    public ForgeConfigSpec.IntValue partyXPos, partyYPos, partyYDistance;
    
    public ForgeConfigSpec.IntValue focusXPos, focusYPos, focusXScale, focusYScale;
    
    public ForgeConfigSpec.BooleanValue showDriveForms, summonTogether;

	public ForgeConfigSpec.EnumValue<ModConfigs.ShowType> showGuiToggle;

	public ForgeConfigSpec.ConfigValue<List<? extends String>> magicDisplayedInCommandMenu;

	ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        	summonTogether = builder
                .comment("Summon both Keyblade and Armor with Summon Keyblade key")
                .translation(KingdomKeys.MODID + ".config.summon_together")
                .define("summonTogether", false);
        builder.push("gui");

			showGuiToggle = builder
				.comment("Toggle HUD visibility, weapon option will show only while holding a Keyblade or Organization weapon")
				.translation(KingdomKeys.MODID + ".config.show_gui_toggle")
				.defineEnum("showGuiToggle", ModConfigs.ShowType.SHOW);

			showDriveForms = builder
                .comment("Drive Forms Visibility")
                .translation(KingdomKeys.MODID + ".config.show_drive_forms")
                .define("showDriveForms", true);
        
	        builder.push("command_menu");

			magicDisplayedInCommandMenu = builder
					.comment("The Magic to display in the Magic menu within the Command Menu")
					.translation(KingdomKeys.MODID + ".config.cm_magic_display")
					.defineList("magicDisplayedInCommandMenu", Lists.newArrayList("kingdomkeys:magic_fire", "kingdomkeys:magic_blizzard", "kingdomkeys:magic_water", "kingdomkeys:magic_thunder", "kingdomkeys:magic_cure", "kingdomkeys:magic_aero", "kingdomkeys:magic_magnet", "kingdomkeys:magic_reflect", "kingdomkeys:magic_gravity", "kingdomkeys:magic_stop"), o -> o instanceof String);
	        
	        cmTextXOffset = builder
	                .comment("Command Menu Text X Offset")
	                .translation(KingdomKeys.MODID + ".config.cm_text_x_offset")
	                .defineInRange("cmTextXOffset", 0, -1000, 1000);
	        
	        cmHeaderTextVisible = builder
	                .comment("Command Menu Header Text Visibility")
	                .translation(KingdomKeys.MODID + ".config.cm_header_text_visibility")
	                .define("cmHeaderTextVisibility", true);
	        
	        cmClassicColors = builder
	                .comment("Command Menu classic color scheme")
	                .translation(KingdomKeys.MODID + ".config.cm_classic_colors")
	                .define("cmClassicColors", false);
	        
	        cmXScale = builder
	                .comment("Command Menu X Scale %")
	                .translation(KingdomKeys.MODID + ".config.cm_x_scale")
	                .defineInRange("cmXScale", 100, -1000, 1000);
	        
	        cmXPos = builder
	                .comment("Command Menu X Pos")
	                .translation(KingdomKeys.MODID + ".config.cm_x_pos")
	                .defineInRange("cmXPos", 0, -1000, 1000);
	        
	        cmSelectedXOffset = builder
	                .comment("Command Menu Selected X Offset")
	                .translation(KingdomKeys.MODID + ".config.cm_selected_x_offset")
	                .defineInRange("cmSelectedXOffset", 5, -1000, 1000);
	        
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
	        
	        hpAlarm = builder
	        		.comment("Low HP Alarm Volume")
	                .translation(KingdomKeys.MODID + ".config.hp_alarm")
	                .defineInRange("hpAlarmVolume", 10, 0, 10);
	        
	        hpXScale = builder
	                .comment("Health Bar X Scale")
	                .translation(KingdomKeys.MODID + ".config.hp_x_scale")
	                .defineInRange("hpXScale", 100, -1000, 1000);
	        
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
	        
	        mpXScale = builder
	                .comment("Magic Bar X Scale")
	                .translation(KingdomKeys.MODID + ".config.mp_x_scale")
	                .defineInRange("mpXScale", 100, -1000, 1000);
	        
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
	        
	        dpXScale = builder
	                .comment("Drive Bar X Scale")
	                .translation(KingdomKeys.MODID + ".config.dp_x_scale")
	                .defineInRange("dpXScale", 100, -1000, 1000);
	        
	        dpYScale = builder
	                .comment("Drive Bar Y Scale")
	                .translation(KingdomKeys.MODID + ".config.dp_y_scale")
	                .defineInRange("dpYScale", 100, -1000, 1000);
	        
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
	        
	        lockOnHPScale = builder
	                .comment("Lock On HP Bar Scale")
	                .translation(KingdomKeys.MODID + ".config.lock_on_hp_scale")
	                .defineInRange("lockOnHPScale", 70, -1000, 1000);
	        
	        lockOnIconScale = builder
	                .comment("Lock On Icon Scale")
	                .translation(KingdomKeys.MODID + ".config.lock_on_icon_scale")
	                .defineInRange("lockOnIconScale", 75, -1000, 1000);
	        
	        lockOnIconRotation = builder
	                .comment("Lock On Icon Rotation Speed")
	                .translation(KingdomKeys.MODID + ".config.lock_on_icon_rotation")
	                .defineInRange("lockOnIconRotation", 16, -1000, 1000);
	        
	        lockOnHpPerBar = builder
	                .comment("Lock On HP per bar")
	                .translation(KingdomKeys.MODID + ".config.lock_on_hp_per_bar")
	                .defineInRange("lockOnHpPerBar", 40, 10, 100);
	        
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
	        
	        partyYDistance = builder
	                .comment("Party HUD Y Offset")
	                .translation(KingdomKeys.MODID + ".config.party_y_offset")
	                .defineInRange("partyYOffset", 60, -1000, 1000);
	        
	        builder.pop();
	        
	        builder.push("focus");
	        
	        focusXPos = builder
	                .comment("Focus HUD X Pos")
	                .translation(KingdomKeys.MODID + ".config.focus_x_pos")
	                .defineInRange("focusXPos", 0, -1000, 1000);
	        
	        focusYPos = builder
	                .comment("Focus HUD Y Pos")
	                .translation(KingdomKeys.MODID + ".config.focus_y_pos")
	                .defineInRange("focusYPos", 0, -1000, 1000);
	        
	        focusXScale = builder
	                .comment("Focus Bar X Scale")
	                .translation(KingdomKeys.MODID + ".config.focus_x_scale")
	                .defineInRange("focusXScale", 100, -1000, 1000);
	        
	        focusYScale = builder
	                .comment("Focus Bar Y Scale")
	                .translation(KingdomKeys.MODID + ".config.focus_y_scale")
	                .defineInRange("focusYScale", 100, -1000, 1000);
	        
	        builder.pop();
        
        builder.pop();
    }

}
