package online.kingdomkeys.kingdomkeys.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.HUDElement;

import java.util.List;

/**
 * Config file for client only config options
 */
public class ClientConfig {
	public ModConfigSpec.ConfigValue<List<? extends Float>> hpHUDData, mpHUDData, cmHUDData, rcHUDData, driveHUDData, focusHUDData, partyHUDData, lockOnHUDData, portraitHUDData, munnyExpHUDData, levelUpHUDData, driveLevelHUDData, minimapHUDData, roomNameHUDData;

    public ModConfigSpec.BooleanValue cmHeaderTextVisible, cmClassicColors, snapChatToCommandMenu, auto3rdPersonShip, cmChangeColor, customFont;
    public ModConfigSpec.IntValue cmTextXOffset, cmSelectedXOffset, cmSubXOffset, cmEndLWidth, cmEndRWidth, cmHeaderEndLWidth, cmHeaderEndRWidth, cmReactionEndLWidth, cmReactionEndRWidth;
    
    public ModConfigSpec.BooleanValue hpShowHearts;
    public ModConfigSpec.IntValue hpAlarm;


    public ModConfigSpec.IntValue lockOnIconScale, lockOnIconRotation, lockOnHpPerBar;
    
    public ModConfigSpec.IntValue partyYDistance;

    public ModConfigSpec.BooleanValue showDriveForms, summonTogether;

	public ModConfigSpec.EnumValue<ModConfigs.ShowType> showGuiToggle;

	public ModConfigSpec.ConfigValue<List<? extends Integer>> hiddenMagic;

	public ModConfigSpec.BooleanValue shoulderSurfingDecoupled;

	public ModConfigSpec.BooleanValue seasonalEvents;

	ClientConfig(final ModConfigSpec.Builder builder) {
		summonTogether = builder
                .comment("Summon both Keyblade and Armor with Summon Keyblade key")
                .translation(KingdomKeys.MODID + ".config.summon_together")
                .define("summonTogether", false);

		auto3rdPersonShip = builder
				.comment("Automatically change to 3rd person when riding a gummi ship")
				.translation(KingdomKeys.MODID + ".config.auto_third_person_ship")
				.define("auto3rdPersonShip", true);

		seasonalEvents = builder
				.comment("Enable fun cosmetic seasonal events (disable if you hate fun, no judgement)")
				.translation(KingdomKeys.MODID + ".config.seasonal_events")
				.define("seasonalEvents", true);

		builder.push("hud_data");
		cmHUDData = builder
				.comment("Command Menu HUD Data")
				.translation(KingdomKeys.MODID + ".config.cm_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("cmHUDData", () -> HUDElement.getDefaultValues("CM"), o -> o instanceof Number);
		rcHUDData = builder
				.comment("Reaction Commands HUD Data")
				.translation(KingdomKeys.MODID + ".config.rc_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("rcHUDData", () -> HUDElement.getDefaultValues("RC"), o -> o instanceof Number);
		hpHUDData = builder
				.comment("Health Bar HUD Data")
				.translation(KingdomKeys.MODID + ".config.hp_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("hpHUDData", () -> HUDElement.getDefaultValues("HP"), o -> o instanceof Number);
		mpHUDData = builder
				.comment("Magic Bar HUD Data")
				.translation(KingdomKeys.MODID + ".config.mp_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("mpHUDData", () -> HUDElement.getDefaultValues("MP"), o -> o instanceof Number);
		driveHUDData = builder
				.comment("Drive Bar HUD Data")
				.translation(KingdomKeys.MODID + ".config.drive_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("driveHUDData", () -> HUDElement.getDefaultValues("Drive"), o -> o instanceof Number);
		portraitHUDData = builder
				.comment("Portrait HUD Data")
				.translation(KingdomKeys.MODID + ".config.portrait_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("portraitHUDData", () -> HUDElement.getDefaultValues("Portrait"), o -> o instanceof Number);
		lockOnHUDData = builder
				.comment("Lock On HUD Data")
				.translation(KingdomKeys.MODID + ".config.lock_on_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("lockOnHUDData", () -> HUDElement.getDefaultValues("LockOn"), o -> o instanceof Number);
		partyHUDData = builder
				.comment("Party HUD Data")
				.translation(KingdomKeys.MODID + ".config.party_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("partyHUDData", () -> HUDElement.getDefaultValues("Party"), o -> o instanceof Number);
		focusHUDData = builder
				.comment("Focus Bar HUD Data")
				.translation(KingdomKeys.MODID + ".config.focus_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("focusHUDData", () -> HUDElement.getDefaultValues("Focus"), o -> o instanceof Number);
		munnyExpHUDData = builder
				.comment("Munny get and Exp. for next level HUD Data")
				.translation(KingdomKeys.MODID + ".config.munny_exp_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("munnyExpHUDData", () -> HUDElement.getDefaultValues("MunnyExp"), o -> o instanceof Number);
		levelUpHUDData = builder
				.comment("Level up notification HUD Data")
				.translation(KingdomKeys.MODID + ".config.levelup_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("levelUpHUDData", () -> HUDElement.getDefaultValues("LevelUp"), o -> o instanceof Number);
		driveLevelHUDData = builder
				.comment("Drive form level up HUD Data")
				.translation(KingdomKeys.MODID + ".config.drivelevel_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("driveLevelHUDData", () -> HUDElement.getDefaultValues("DriveLevel"), o -> o instanceof Number);
		minimapHUDData = builder
				.comment("Castle Oblivion Minimap HUD Data")
				.translation(KingdomKeys.MODID + ".config.minimap_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("minimapHUDData", () -> HUDElement.getDefaultValues("Minimap"), o -> o instanceof Number);
		roomNameHUDData = builder
				.comment("Castle Oblivion Room Name HUD Data")
				.translation(KingdomKeys.MODID + ".config.roomname_hud_data")//X,Y,Width ,Height ,xScale, yScale,rotation,anchor (ordinal)
				.defineList("roomnameHUDData", () -> HUDElement.getDefaultValues("RoomName"), o -> o instanceof Number);
		builder.pop();

        builder.push("gui");
			showGuiToggle = builder
				.comment("Toggle HUD visibility, weapon option will show only while holding a Keyblade or Organization weapon")
				.translation(KingdomKeys.MODID + ".config.show_gui_toggle")
				.defineEnum("showGuiToggle", ModConfigs.ShowType.SHOW);

			customFont = builder
				.comment("Enable the custom font")
				.translation(KingdomKeys.MODID + ".config.custom_font")
				.define("customFont", true);

			showDriveForms = builder
                .comment("Drive Forms Visibility")
                .translation(KingdomKeys.MODID + ".config.show_drive_forms")
                .define("showDriveForms", true);

		builder.push("command_menu");
			cmChangeColor = builder
                    .comment("Allow the Command Menu to change colors based on nearby enemies")
                    .translation(KingdomKeys.MODID + ".config.cm_change_color")
                    .define("cmChangeColor", true);

			hiddenMagic = builder
					.comment("Magic to hide from the Command Menu")
					.translation(KingdomKeys.MODID + ".config.cm_hidden_magic")
					.defineList("hiddenMagic", () -> List.of(),obj -> obj instanceof Integer);
	        
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
	        
	        snapChatToCommandMenu = builder
	                .comment("Push the chat log above the Command Menu so the two do not overlap")
	                .translation(KingdomKeys.MODID + ".config.snap_chat_to_command_menu")
	                .define("snapChatToCommandMenu", true);
	        
	        cmSelectedXOffset = builder
	                .comment("Command Menu Selected X Offset")
	                .translation(KingdomKeys.MODID + ".config.cm_selected_x_offset")
	                .defineInRange("cmSelectedXOffset", 5, -1000, 1000);
	        
	        cmSubXOffset = builder
	                .comment("Command Menu Submenu X Offset %")
	                .translation(KingdomKeys.MODID + ".config.cm_sub_x_offset")
	                .defineInRange("cmSubXOffset", 100, -1000, 1000);

			cmEndLWidth = builder
					.comment("Command Menu Element Left End Segment Width")
					.translation(KingdomKeys.MODID + ".config.cm_end_l_width")
					.defineInRange("cmEndLWidth", 10, 0, 256);

			cmEndRWidth = builder
				.comment("Command Menu Element Right End Segment Width")
				.translation(KingdomKeys.MODID + ".config.cm_end_r_width")
				.defineInRange("cmEndRWidth", 10, 0, 256);

			cmHeaderEndLWidth = builder
				.comment("Command Menu Header Left End Segment Width")
				.translation(KingdomKeys.MODID + ".config.cm_header_end_l_width")
				.defineInRange("cmHeaderEndLWidth", 10, 0, 256);

			cmHeaderEndRWidth = builder
				.comment("Command Menu Header Right End Segment Width")
				.translation(KingdomKeys.MODID + ".config.cm_header_end_r_width")
				.defineInRange("cmHeaderEndRWidth", 15, 0, 256);

			cmReactionEndLWidth = builder
				.comment("Reaction command left end segment width")
				.translation(KingdomKeys.MODID + ".config.cm_reaction_end_l_width")
				.defineInRange("cmReactionEndLWidth", 10, 0, 256);

			cmReactionEndRWidth = builder
				.comment("Reaction command right end segment width")
				.translation(KingdomKeys.MODID + ".config.cm_reaction_end_r_width")
				.defineInRange("cmReactionEndRWidth", 10, 0, 256);
			builder.pop();
	        
	        builder.push("hp_bar");
	        hpShowHearts = builder
	        		.comment("Show Hearts")
	                .translation(KingdomKeys.MODID + ".config.hp_hearts")
	                .define("hpShowHearts", true);
	        
	        hpAlarm = builder
	        		.comment("Low HP Alarm Volume")
	                .translation(KingdomKeys.MODID + ".config.hp_alarm")
	                .defineInRange("hpAlarmVolume", 10, 0, 10);
	        builder.pop();
	        
	        builder.push("lock_on");
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
			if (KingdomKeys.shoulderSurfingLoaded) {
				shoulderSurfingDecoupled = builder
						.comment("Shoulder Surfing mod: Decoupled camera while not locked on")
						.translation(KingdomKeys.MODID + ".config.shoulder_surfing_decoupled")
						.define("shoulderSurfingDecoupled", true);
			}
	        builder.pop();
	        
	        builder.push("party");
	        partyYDistance = builder
	                .comment("Party HUD Y Offset")
	                .translation(KingdomKeys.MODID + ".config.party_y_offset")
	                .defineInRange("partyYOffset", 70, -1000, 1000);
	        builder.pop();
    }

}
