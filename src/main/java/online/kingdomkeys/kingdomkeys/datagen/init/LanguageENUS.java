package online.kingdomkeys.kingdomkeys.datagen.init;

import static online.kingdomkeys.kingdomkeys.KingdomKeys.MODID;
import static online.kingdomkeys.kingdomkeys.lib.Strings.*;

import net.minecraft.data.DataGenerator;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.datagen.provider.KKLanguageProvider;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;

public class LanguageENUS extends KKLanguageProvider {

    public LanguageENUS(DataGenerator gen) {
        super(gen, "en_us");
    }

    @Override
    protected void addTranslations() {
    	/**GUIS**/
    	//Advancements
    	addAdvancement("root","Welcome to Kingdom Keys!", "Install Kingdom Keys");
    	addAdvancement("to_soa","Is this... my heart?!", "Deep dive into your heart");
    	addAdvancement("choice","A sword, shield and staff?", "Make a choice");
    	addAdvancement("obtain_recipe","I better take this to a Moogle", "Obtain a recipe");
    	addAdvancement("obtain_projector","A sacrifice had to be made", "Obtain a moogle projector");
    	addAdvancement("summon_keyblade","Am I the choosen one?", "Summon your keyblade");
    	addAdvancement("upgrade_keyblade","It can be even stronger", "Upgrade your keyblade through the use of the Keyblade Forge");
    	addAdvancement("levelup1","Woah!", "Level up");
    	addAdvancement("levelup50","Halfway there", "Reach level 50");
    	addAdvancement("levelup100","I'm at the top!", "Reach level 100");
    	addAdvancement("obtain_drive","Do I need a license?", "Obtain your first Drive Form orb");
    	addAdvancement("obtain_keychain","These moogles know how to work", "Obtain your first keychain");
    	addAdvancement("obtain_kiblade","One key to rule them all!", "Obtain the legendary \u03c7-Blade");
    	addAdvancement("obtain_magic","Like a Wizard!", "Obtain your first Magic Spell");
    	addAdvancement("obtain_org","Nobody wore this", "Obtain the organization robes");
    	addAdvancement("to_rod","Darkness within darkness awaits you", "Get sucked into the Realm of Darkness");
    	//addAdvancement("castle_oblivion_tp","How did I get here?", "Reach the unknown land of Castle Oblivion");

    	        
        //Containers
        add("container.magical_chest", "Magical Chest");
        add("container.pedestal", "Pedestal");

        //Menu
        add(Gui_Menu_Back, "Back");
        add(Gui_Menu_Back + ".desc", "Go back to the previous menu.");
        add(Gui_Menu_Accept, "Accept");
        add(Gui_Menu_Main_Title, "Menu");
        
        add(Gui_Menu_Main_Button_Items, "Items");
        add(Gui_Menu_Main_Button_Items + ".desc", "Access to your equipables (weapon, potions...) and your inventory.");
        add(Gui_Menu_Main_Button_Abilities, "Abilities");
        add(Gui_Menu_Main_Button_Abilities + ".desc", "Equip or unequip your abilities.");
        add(Gui_Menu_Main_Button_Customize, "Customize");
        add(Gui_Menu_Main_Button_Customize + ".desc", "Customize the magic shortcuts");
        add(Gui_Menu_Main_Button_Party, "Party");
        add(Gui_Menu_Main_Button_Party + ".desc", "Create and manage your party.");
        add(Gui_Menu_Main_Button_Status, "Status");
        add(Gui_Menu_Main_Button_Status + ".desc", "Check your stats.");
        add(Gui_Menu_Main_Button_Journal, "Journal");
        add(Gui_Menu_Main_Button_Journal + ".desc", "");
        add(Gui_Menu_Main_Button_Config, "Config");
        add(Gui_Menu_Main_Button_Config + ".desc", "Configure various graphical aspects of the HUD.");
        add(Gui_Menu_Main_Button_Style, "Combat Style");
        add(Gui_Menu_Main_Button_Style + ".desc", "Set your combat style with Epic Fight");
        
        add(Gui_Menu_Main_Synthesis_Tier, "Synthesis Tier");
        add(Gui_Menu_Main_Munny, "Munny");
        add(Gui_Menu_Main_Hearts, "Hearts");
        add(Gui_Menu_Main_Time, "World Time");
        add(Gui_Menu_Main_Time_Spent, "Global Time");
        add(Gui_Menu_Items, "Items");
        add(Gui_Menu_Items_Equipment, "Equipment");
        add(Gui_Menu_Items_Equipment + ".desc", "Equip your weapon and various equipables (not yet available).");
        add(Gui_Menu_Items_Stock, "Stock");
        add(Gui_Menu_Items_Stock + ".desc", "Check your inventory.");
        add(Gui_Menu_Items_Equipment_Weapon, "Weapon");
        add(Gui_Menu_Items_Equipment_Weapon_Keyblades, "Keyblades");
        add(Gui_Menu_Items_Equipment_Shotlock, "Shotlock");
        add(Gui_Menu_Items_Equipment_Accessories, "Accessories");
        add(Gui_Menu_Items_Equipment_Armor, "Armor");
        add(Gui_Menu_Items_Equipment_Pauldron, "Pauldron");
        add(Gui_Menu_Items_Equipment_Items, "Items");
        add(Gui_Menu_Customize, "Customize");
        add(Gui_Menu_Customize + ".shortcuts", "Shortcuts");
        add(Gui_Menu_Customize + ".shortcut", "Shortcut");
        add(Gui_Menu_Customize_Magic, "Magics");
        add(Gui_Menu_Customize_Magic_Hidden, "Hidden");
        add(Gui_Menu_Customize_Magic_Active, "Command Menu");
        add(Gui_Menu_Customize + ".unequip", "Unequip");
        add(Gui_Menu_Party, "Party");
        add(Gui_Menu_Party_Create, "Create party");
        add(Gui_Menu_Party_Create + ".desc", "Create a new party.");
        add(Gui_Menu_Party_Create_Name, "Name");
        add(Gui_Menu_Party_Create_Accessibility, "Accessibility and limit");
        add(Gui_Menu_Party_Create_Accessibility_Public, "Public");
        add(Gui_Menu_Party_Create_Accessibility_Private, "Private");
        add(Gui_Menu_Party_Join, "Join party");
        add(Gui_Menu_Party_Join + ".desc", "Join an already created party.");
        add(Gui_Menu_Party_Leader_Invite, "Invite");
        add(Gui_Menu_Party_Leader_Settings, "Settings");
        add(Gui_Menu_Party_Leader_Promote, "Promote");
        add(Gui_Menu_Party_Leader_Kick, "Kick");
        add(Gui_Menu_Party_Leader_Disband, "Disband");
        add(Gui_Menu_Party_Member_Leave, "Leave");
        add(Gui_Menu_Status, "Status");
        add(Gui_Menu_Status_Level, "Level");
        add(Gui_Menu_Status_TotalExp, "Experience");
        add(Gui_Menu_Status_NextLevel, "Next LV");
        add(Gui_Menu_Status_HP, "HP");
        add(Gui_Menu_Status_MP, "MP");
        add(Gui_Menu_Status_AP, "AP");
        add(Gui_Menu_Status_DriveGauge, "Drive Gauge");
        add(Gui_Menu_Status_Strength, "Strength");
        add(Gui_Menu_Status_Magic, "Magic");
        add(Gui_Menu_Status_Defense, "Defense");
        add(Gui_Menu_Status_FireRes, "Fire Resistance");
        add(Gui_Menu_Status_BlizzardRes, "Blizzard Resistance");
        add(Gui_Menu_Status_ThunderRes, "Thunder Resistance");
        add(Gui_Menu_Status_LightRes, "Light Resistance");
        add(Gui_Menu_Status_DarkRes, "Dark Resistance");
        add(Gui_Menu_Status_FireResShort, "Fire Res.");
        add(Gui_Menu_Status_BlizzardResShort, "Blizzard Res.");
        add(Gui_Menu_Status_ThunderResShort, "Thunder Res.");
        add(Gui_Menu_Status_LightResShort, "Light Res.");
        add(Gui_Menu_Status_DarkResShort, "Dark Res.");
        add(Gui_Menu_Status_FormLevel, "Form Level");
        add(Gui_Menu_Status_FormGauge, "Form Gauge");
        add(Gui_Menu_Status_Abilities, "Abilities");
        add(Gui_Menu_Status_Ability, "Ability");
        
        add(Gui_Menu_Config, "Config");
        add(Gui_Menu_Config + ".bg", "Background");
        add(Gui_Menu_Config + ".command_menu", "Command Menu");
        add(Gui_Menu_Config + ".hp", "HP Bar");
        add(Gui_Menu_Config + ".mp", "MP Bar");
        add(Gui_Menu_Config + ".dp", "DP Bar");
        add(Gui_Menu_Config + ".player_skin", "Player Skin");
        add(Gui_Menu_Config + ".lock_on_hp", "Lock On");
        add(Gui_Menu_Config + ".party", "Party");
        add(Gui_Menu_Config + ".focus", "Focus Bar");
        add(Gui_Menu_Config + ".classic_colors", "Classic colors");
        add(Gui_Menu_Config + ".x_scale", "X Scale");
        add(Gui_Menu_Config + ".y_scale", "Y Scale");
        add(Gui_Menu_Config + ".x_pos", "X Position");
        add(Gui_Menu_Config + ".selected_x_pos", "Selected X Offset");
        add(Gui_Menu_Config + ".y_pos", "Y Position");
        add(Gui_Menu_Config + ".y_dist", "Y Distance");
        add(Gui_Menu_Config + ".sub_x_offset", "Submenu X Offset");
        add(Gui_Menu_Config + ".header_title", "Header Title");
        add(Gui_Menu_Config + ".text_x_offset", "Text X Offset");
        add(Gui_Menu_Config + ".hp_scale", "HP Bar Scale");
        add(Gui_Menu_Config + ".icon_scale", "Lock On Icon Scale");
        add(Gui_Menu_Config + ".icon_rotation", "Lock On Icon Rotation Speed");
        add(Gui_Menu_Config + ".hp_per_bar", "HP Per Bar");
        add(Gui_Menu_Config + ".show_hearts", "Show hearts on HUD");
        add(Gui_Menu_Config + ".hp_alarm", "Low HP Alarm volume");
        add(Gui_Menu_Config + ".impexp", "Import/Export");
        add(Gui_Menu_Config + ".impexp.import", "Import");
        add(Gui_Menu_Config + ".impexp.export", "Export to clipboard");
        add(Gui_Menu_Config + ".notif_color", "Notification");
        add(Gui_Menu_Config + ".armor.red", "Armor red");
        add(Gui_Menu_Config + ".armor.green", "Armor green");
        add(Gui_Menu_Config + ".armor.blue", "Armor blue");
        add(Gui_Menu_Config + ".armor.glint", "Armor glint");
        add(Gui_Menu_Journal, "Journal");
        add(Gui_Menu_Style, "Combat Style");
        add(Gui_Menu_Style + ".single", "Single");
        add(Gui_Menu_Style + ".dual", "Dual");
        add(Gui_Menu_Style + ".sora", "Sora");
        add(Gui_Menu_Style + ".roxas", "Roxas");
        add(Gui_Menu_Style + ".riku", "Riku");
        add(Gui_Menu_Style + ".terra", "Terra");
        add(Gui_Menu_Style + ".aqua", "Aqua");
        add(Gui_Menu_Style + ".ventus", "Ventus");
        add(Gui_Menu_Style + ".kh2roxasdual", "Roxas (KH2)");
        add(Gui_Menu_Style + ".daysroxasdual", "Roxas (358/2 Days)");

        //Synthesis
        add(Gui_Synthesis, "Item Workshop");
        add(Gui_Synthesis_Exp, "Exp");
        add(Gui_Synthesis_Synthesise, "Synthesise Items");
        add(Gui_Synthesis_Synthesise_Title, "Synthesis");
        add(Gui_Synthesis_Synthesise_Create, "Create");
        add(Gui_Synthesis_Forge_Upgrade, "Upgrade");
        add(Gui_Synthesis_Forge, "Keyblade Forge");
        add(Gui_Synthesis_Forge_Title, "Forge");
        add(Gui_Synthesis_Materials, "Material List");
        add(Gui_Synthesis_Materials_Deposit, "Deposit");
        add(Gui_Synthesis_Materials_Take, "Take");
        add(Gui_Shop, "Shop");
        add(Gui_Shop_Page, "Page:");
        add(Gui_Shop_NoSpace, "Not enough space");
        add(Gui_Shop_Tier, "Tier:");
        add(Gui_Shop_Main_Title, "Shop");
        add(Gui_Shop_Buy_Cost, "Cost:");
        add(Gui_Synthesis_Moogle_Name, "%s's Moogle Shop");

        //Command Menu
        add(Gui_CommandMenu_Command, "COMMAND");
        add(Gui_CommandMenu_Attack, "Attack");
        add(Gui_CommandMenu_Portal, "Portal");
        add(Gui_CommandMenu_Magic, "Magic");
        add(Gui_CommandMenu_Items, "Items");
        add(Gui_CommandMenu_Drive, "Drive");
        add(Gui_CommandMenu_Drive_Revert, "Revert");
        add(Gui_CommandMenu_Limit, "Limit");
        add(Gui_CommandMenu_Target,"Target");
        add(Gui_CommandMenu_Portals_Title, "Portals");
        add(Gui_CommandMenu_Magic_Title, "Magic");
        add(Gui_CommandMenu_Items_Title, "Items");
        add(Gui_CommandMenu_Drive_Title, "Forms");
        add(Gui_CommandMenu_Limit_Title, "Limits");

        add("kingdomkeys.helmet", "Helmet");
        add("kingdomkeys.chestplate", "Chestplate");
        add("kingdomkeys.leggings", "Leggings");
        add("kingdomkeys.boots", "Boots");
        
        //Synthesis Bag
        add("gui.synthesisbag.upgrade", "Upgrade size");
        add("gui.synthesisbag.munny", "Munny");
        add("gui.synthesisbag.notenoughmunny", "Not enough munny");
        
        add("gui.statboost.increased","Increased %s, now it's %s");
        add("gui.statboost.tooltip", "Increases %s by 1");
        		
        add("gui.magicspell.unlock","Unlocks %s");
        add("gui.magicspell.maxed", "%s is already maxed");
        		
        add("gui.driveformorb.tooltip", "Upgrades %s Form");
    	add("gui.driveformorb.upgrade", "%s Form has obtained %s exp");


        //Proof of Heart
        add("gui.proofofheart.desc", "Use this to leave Organization XIII");
        add("gui.proofofheart.desc2", "You won't be able to use it if you're wearing the Organization XIII robes");
        add("gui.proofofheart.notinorg", "You are not in Organization XIII");
        add("gui.proofofheart.leftorg", "You have left Organization XIII");
        add("gui.proofofheart.unequip", "First unequip your Organization XIII armor");

        //Organization XIII
        add("gui.org.line1", "By donning the Dark Robe you are now a member of Organization XIII.");
        add("gui.org.line2", "Choose a member of Organization XIII you align with.");
        add("gui.org.line3", "Your choice will determine the weapon you start with.");
        add("gui.org.line4", "You wish to align with %1$s?");
        add("gui.org.line5", "It will cost to change this after you have made your choice.");
        add("gui.org.ok", "Ok");
        add("gui.org.select", "Select");
        add("gui.org.cancel", "Cancel");
        add("gui.org.confirm", "Confirm");

        //Save Point
        add(Gui_Save_Creation_Title, "Name Save Point");
        add(Gui_Save_Creation_Prompt, "Enter a name for this Save Point");
        add(Gui_Save_Creation_Global, "Set globally visible");
        add(Gui_Save_Creation_Global_Desc, "Makes this save point accessible to all players");
        add(Gui_Save_Creation_Accept, "Save");

        add(Gui_Save_Main_CurrentPosition, "You are Here");
        add(Gui_Save_Main_Sort, "Sort:");
        add(Gui_Save_Main_Rename, "Rename");
        add(Gui_Save_Main_Retake, "Retake");

        add(Gui_Save_Sorting_ByRecent, "Recent");
        add(Gui_Save_Sorting_ByName, "Name");
        add(Gui_Save_Sorting_ByDimension, "Dimension");
        add(Gui_Save_Sorting_ByOwner, "Owner");
        add(Gui_Save_Sorting_Ascending, "Ascending");
        add(Gui_Save_Sorting_Descending, "Descending");

        //K.O. Screen
        add(Gui_KO_Die, "Give Up");
        add(Gui_KO_Quit, "Exit");

        //Level up messages
        add(Stats_LevelUp_Str, "Strength increased!");
        add(Stats_LevelUp_Def, "Defense increased!");
        add(Stats_LevelUp_Magic, "Magic increased!");
        add(Stats_LevelUp_HP, "Maximum HP increased!");
        add(Stats_LevelUp_MP, "Maximum MP increased!");
        add(Stats_LevelUp_AP, "Maximum AP increased!");
        add(Stats_LevelUp_FormGauge, "Form Gauge Powered Up!");
        add(Stats_LevelUp_MaxAccessories, "Gained accessory slot!");
        add(Stats_LevelUp_MaxArmors,"Gained armor slot!");
        add(Stats_LevelNext, "Next LV");
        add(Stats_MunnyGet, "Munny Get!");

        /**Blocks**/
        //Blox
        addBlock(ModBlocks.normalBlox, "Normal Blox");
        addBlock(ModBlocks.hardBlox, "Hard Blox");
        addBlock(ModBlocks.metalBlox, "Metal Blox");
        addBlock(ModBlocks.dangerBlox, "Danger Blox");
        addBlock(ModBlocks.bounceBlox, "Bounce Blox");
        addBlock(ModBlocks.blastBlox, "Blast Blox");
        addBlock(ModBlocks.ghostBlox, "Ghost Blox");
        addBlock(ModBlocks.prizeBlox, "Prize Blox");
        addBlock(ModBlocks.rarePrizeBlox, "Rare Prize Blox");
        addBlock(ModBlocks.magnetBlox, "Magnet Blox");
        addBlock(ModBlocks.pairBlox, "Pair Blox");

        //Ores
        addBlock(ModBlocks.blazingOre, "Blazing Ore");
        addBlock(ModBlocks.blazingOreN, "Nether Blazing Ore");
        addBlock(ModBlocks.blazingOreD, "Deepslate Blazing Ore");
        addBlock(ModBlocks.soothingOre, "Soothing Ore");
        addBlock(ModBlocks.soothingOreD, "Deepslate Soothing Ore");
        addBlock(ModBlocks.writhingOre, "Writhing Ore");
        addBlock(ModBlocks.writhingOreN, "Nether Writhing Ore");
        addBlock(ModBlocks.writhingOreE, "End Writhing Ore");
        addBlock(ModBlocks.writhingOreD, "Deepslate Writhing Ore");
        addBlock(ModBlocks.betwixtOre, "Betwixt Ore");
        addBlock(ModBlocks.betwixtOreD, "Deepslate Betwixt Ore");
        addBlock(ModBlocks.betwixtOreE, "End Betwixt Ore");
        addBlock(ModBlocks.wellspringOre, "Wellspring Ore");
        addBlock(ModBlocks.wellspringOreN, "Nether Wellspring Ore");
        addBlock(ModBlocks.frostOre, "Frost Ore");
        addBlock(ModBlocks.frostOreD, "Deepslate Frost Ore");
        addBlock(ModBlocks.lucidOre, "Lucid Ore");
        addBlock(ModBlocks.lightningOre, "Lightning Ore");
        addBlock(ModBlocks.pulsingOre, "Pulsing Ore");
        addBlock(ModBlocks.pulsingOreD, "Deepslate Pulsing Ore");
        addBlock(ModBlocks.pulsingOreE, "End Pulsing Ore");
        addBlock(ModBlocks.remembranceOre, "Remembrance Ore");
        addBlock(ModBlocks.hungryOre, "Hungry Ore");
        addBlock(ModBlocks.sinisterOre, "Sinister Ore");
        addBlock(ModBlocks.sinisterOreD, "Deepslate Sinister Ore");
        addBlock(ModBlocks.stormyOre, "Stormy Ore");
        addBlock(ModBlocks.stormyOreD, "Deepslate Stormy Ore");
        addBlock(ModBlocks.tranquilityOre, "Tranquility Ore");
        addBlock(ModBlocks.twilightOre, "Twilight Ore");
        addBlock(ModBlocks.twilightOreD, "Deepslate Twilight Ore");
        addBlock(ModBlocks.twilightOreN, "Nether Twilight Ore");

        //Other
        addBlock(ModBlocks.mosaic_stained_glass, "Mosaic Stained Glass");
        addBlock(ModBlocks.orgPortal, "Organization Portal");
        addBlock(ModBlocks.moogleProjector, "Moogle Projector");
        addBlock(ModBlocks.station_of_awakening_core, "Station of Awakening Platform Core");
        addBlock(ModBlocks.magicalChest, "Magical Chest");
        addBlock(ModBlocks.pedestal, "Pedestal");
        addBlock(ModBlocks.savepoint, "Save Point");
        add("block." + MODID + ".linked_savepoint", "Linked Save Point");
        add("block." + MODID + ".warp_point", "Warp Point");
        addBlock(ModBlocks.soADoor, "Mysterious Door");
        addBlock(ModBlocks.gummiEditor, "Gummi Editor");
        addBlock(ModBlocks.sorCore, "Station of Sorrow Core");
        addBlock(ModBlocks.dataPortal, "Data Portal");
        addBlock(ModBlocks.airstepTarget, "Airstep point");

        add("savepoint.healing","healing");
        add("savepoint.magic","magic restoration");
        add("savepoint.feed","feeding");
        add("savepoint.focus","focus restoration");
        add("savepoint.drive","drive restoration");
        add("savepoint.upgrade","Savepoint %s speed is now at %s%%");
        add("savepoint.maxed","Savepoint %s speed is already maxed");
        add("savepoint.upgrade_type","Savepoint upgraded to {0}");
        add("savepoint.max_upgrade","Max upgrade reached");

        //Castle Oblivion
        addBlock(ModBlocks.cardDoor, "Card Door");
        addBlock(ModBlocks.structureWall, "Structure Wall");
        // addBlock(ModBlocks.castleOblivionWall, "Oblivion Block");
        // addBlock(ModBlocks.castleOblivionWallChisled, "Chisled Oblivion Block");
        // addBlock(ModBlocks.castleOblivionWall2, "Dark Oblivion Block");
        // addBlock(ModBlocks.castleOblivionWall3, "Darker Oblivion Block");
        // addBlock(ModBlocks.castleOblivionPillar, "Oblivion Pillar");
        // addBlock(ModBlocks.castleOblivionStairs, "Oblivion Stairs");
        // addBlock(ModBlocks.castleOblivionSlab, "Oblivion Slab");
        
        //Realm of Darkness
        addBlock(ModBlocks.rodCrackedStone, "Dark Cracked Stone");
        addBlock(ModBlocks.rodSand, "Dark Sand");
        addBlock(ModBlocks.rodStone, "Dark Stone");
        

        /**KK stuff**/
        //Abilities
        addAbilityWithDesc(ModAbilities.AUTO_VALOR, "Auto Valor", "Automatically sets the Reaction Command to Valor in a pinch, if Valor Form is usable.");
        addAbilityWithDesc(ModAbilities.AUTO_WISDOM, "Auto Wisdom", "Automatically sets the Reaction Command to Wisdom in a pinch, if Wisdom Form is usable.");
        addAbilityWithDesc(ModAbilities.AUTO_LIMIT, "Auto Limit", "Automatically sets the Reaction Command to Limit in a pinch, if Limit Form is usable.");
        addAbilityWithDesc(ModAbilities.AUTO_MASTER, "Auto Master", "Automatically sets the Reaction Command to Master in a pinch, if Master Form is usable.");
        addAbilityWithDesc(ModAbilities.AUTO_FINAL, "Auto Final", "Automatically sets the Reaction Command to Final in a pinch, if Final Form is usable.");
        addAbilityWithDesc(ModAbilities.STRIKE_RAID, "Strike Raid", "Right click while crouching to throw your keyblade, uses 10MP.");

        addGrowthAbility(ModAbilities.HIGH_JUMP, "High Jump LV.1", "High Jump LV.2", "High Jump LV.3", "High Jump MAX");
        addAbilityDesc(ModAbilities.HIGH_JUMP, "Now you can jump really high!");
        addGrowthAbility(ModAbilities.QUICK_RUN, "Quick Run LV.1", "Quick Run LV.2", "Quick Run LV.3", "Quick Run MAX");
        addAbilityDesc(ModAbilities.QUICK_RUN, "If you press the action button while running you will sprint.");
        addGrowthAbility(ModAbilities.DODGE_ROLL, "Dodge Roll LV.1", "Dodge Roll LV.2", "Dodge Roll LV.3", "Dodge Roll MAX");
        addAbilityDesc(ModAbilities.DODGE_ROLL, "If you press the action button while walking you will dodge roll. ");
        addGrowthAbility(ModAbilities.AERIAL_DODGE, "Aerial Dodge LV.1", "Aerial Dodge LV.2", "Aerial Dodge LV.3", "Aerial Dodge MAX");
        addAbilityDesc(ModAbilities.AERIAL_DODGE, "In the air you can press jump again to double jump.");
        addGrowthAbility(ModAbilities.GLIDE, "Glide LV.1", "Glide LV.2", "Glide LV.3", "Glide MAX");
        addAbilityDesc(ModAbilities.GLIDE, "In the air, hold the jump key to glide.");

        addAbilityWithDesc(ModAbilities.SCAN, "Scan", "Inspect the target's current HP.");
        addAbilityWithDesc(ModAbilities.ZERO_EXP, "Zero EXP", "Disables the gaining of experience.");
        addAbilityWithDesc(ModAbilities.MP_HASTE, "MP Haste", "Increases MP restoration speed after MP is fully consumed.");
        addAbilityWithDesc(ModAbilities.MP_HASTERA, "MP Hastera", "Increases MP restoration speed even more after MP is fully consumed.");
        addAbilityWithDesc(ModAbilities.MP_HASTEGA, "MP Hastega", "Highly Increases MP restoration speed after MP is fully consumed.");
        addAbilityWithDesc(ModAbilities.MP_RAGE,  "MP Rage", "Restores MP relative to the amount of damage taken.");
        addAbilityWithDesc(ModAbilities.DAMAGE_CONTROL, "Damage Control", "Halve the damage you take when your HP is at 25% or below. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.DAMAGE_DRIVE, "Damage Drive", "Restores the Drive Gauge every time damage is taken. The amount restored to the Drive Gauge is relative to the damage received.");
        addAbilityWithDesc(ModAbilities.DRIVE_BOOST, "Drive Boost", "Allows greater restoration of the Drive Gauge during MP Charge.");
        addAbilityWithDesc(ModAbilities.FORM_BOOST, "Form Boost", "Increases the duration of each Drive Form.");
        addAbilityWithDesc(ModAbilities.FIRE_BOOST, "Fire Boost", "Increases damage done with fire-based attacks.");
        addAbilityWithDesc(ModAbilities.BLIZZARD_BOOST, "Blizzard Boost", "Increases damage done with blizzard-based attacks.");
        addAbilityWithDesc(ModAbilities.WATER_BOOST, "Water Boost", "Increases damage done with water-based attacks.");
        addAbilityWithDesc(ModAbilities.THUNDER_BOOST, "Thunder Boost", "Increases damage done with thunder-based attacks.");
        addAbilityWithDesc(ModAbilities.TREASURE_MAGNET, "Treasure Magnet", "Automatically draw in and collect nearby prizes. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.EXPERIENCE_BOOST, "Experience Boost", "Increases experience gained by 100% when you are at half health or less.");
        addAbilityWithDesc(ModAbilities.SECOND_CHANCE, "Second Chance", "Ensures 1 HP remains after taking massive damage.");
        addAbilityWithDesc(ModAbilities.LIGHT_AND_DARKNESS, "Light & Darkness", "Has a secret effect");
        addAbilityWithDesc(ModAbilities.SYNCH_BLADE, "Synch Blade", "Equips a weapon in each hand. The ability of the left hand weapon becomes available as well.");
        addAbilityWithDesc(ModAbilities.MP_SAFETY, "MP Safety", "Disable recharging when MP runs out using shortcuts. Except if it's Cure");
        addAbilityWithDesc(ModAbilities.DRIVE_CONVERTER, "Drive Converter", "Increment all Drive Point prizes value");
        addAbilityWithDesc(ModAbilities.FOCUS_CONVERTER, "Focus Converter", "Increment all Focus prizes value.");
        addAbilityWithDesc(ModAbilities.FULL_MP_BLAST, "Full MP Blast", "When your MP is full, increase the power of your first magical ability by 50%. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.WIZARDS_RUSE, "Wizard's Ruse", "Potentially recover HP proportionate to the MP you should expend. Stack the ability to increase the odds.");
        addAbilityWithDesc(ModAbilities.EXTRA_CAST, "Extra Cast", "Allows the use of one last spell before running out of MP.");
        addAbilityWithDesc(ModAbilities.MP_THRIFT, "MP Thrift", "Decrease MP cost by 20%. Stack the ability to increaase the effect.");
        addAbilityWithDesc(ModAbilities.CRITICAL_BOOST, "Critical Boost", "Increases damage dealt by critical hits by 10%. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.JACKPOT, "Jackpot", "Increment the values for HP, MP and Munny prizes. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.LUCKY_LUCKY, "Lucky Strike", "Brings luck, fortune and looting to the wearer, increasing the drop rate of items. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.ITEM_BOOST, "Item Boost", "Increases effect done with healing items on the field by 50%. Equip more to increase the effect.");
        addAbilityWithDesc(ModAbilities.FIRAZA, "Firaza", "Allows the user to get the Firaza reaction command.");
        addAbilityWithDesc(ModAbilities.BLIZZAZA, "Blizzaza", "Allows the user to get the Blizzaza reaction command.");
        addAbilityWithDesc(ModAbilities.WATERZA, "Waterza", "Allows the user to get the Waterza reaction command.");
        addAbilityWithDesc(ModAbilities.THUNDAZA, "Thundaza", "Allows the user to get the Thundaza reaction command.");
        addAbilityWithDesc(ModAbilities.CURAZA, "Curaza", "Allows the user to get the Curaza reaction command.");
        addAbilityWithDesc(ModAbilities.GRAND_MAGIC_HASTE, "Grand Magic Haste", "Gives the user a higher chance to cast an upgraded magic. Stack the ability to increase the chance.");
        addAbilityWithDesc(ModAbilities.BERSERK_CHARGE, "Berserk Charge", "Gives the user +2 Strength when in MP recharge. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.LEAF_BRACER, "Leaf Bracer", "Casting Cure on yourself will continue even when attacked.");
        addAbilityWithDesc(ModAbilities.HP_GAIN, "HP Gain", "Restores a bit of HP when a shotlock impacts. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.ENDLESS_MAGIC, "Endless Magic", "Allows the user to reduce the cooldown between magic casts. Stack the ability to increase the effect.");
        addAbilityWithDesc(ModAbilities.DARK_DOMINATION, "Dark Domination", "Allows the user to control Antiform at will.");
        addAbilityWithDesc(ModAbilities.MAGIC_LOCK_ON, "Magic Lock-On", "Allows the user to use some magics at the locked entity's position.");
        addAbilityWithDesc(ModAbilities.COMBO_PLUS, "Combo Plus", "Increases maximum combo by 1 when on the ground. Equip more to enable more combos");
        addAbilityWithDesc(ModAbilities.NEGATIVE_COMBO, "Negative Combo", "Decreases maximum combo on the ground and in midair by 1. Equip more to increase the effect.");
        addAbilityWithDesc(ModAbilities.FINISHING_PLUS, "Finishing Plus", "Unleash successive finishing moves after combos.");
        addAbilityWithDesc(ModAbilities.PROTECT, "Protect", "Absorbs 10% of the damage taken.");
        addAbilityWithDesc(ModAbilities.PROTECTRA, "Protectra", "Absorbs 20% of the damage taken.");
        addAbilityWithDesc(ModAbilities.PROTECTGA, "Protectga", "Absorbs 40% of the damage taken.");


        //Limits
        addLimit(ModLimits.LASER_CIRCLE, "Laser Circle");
        addLimit(ModLimits.LASER_DOME, "Laser Dome");
        addLimit(ModLimits.ARROW_RAIN, "Arrow Rain");
        addLimit(ModLimits.SLOW_THUNDER_TRAIL, "Slow Thunder Trail");
        addLimit(ModLimits.FAST_THUNDER_TRAIL, "Fast Thunder Trail");

        //Shotlocks
        addShotlock(ModShotlocks.RAGNAROK, "Ragnarok");
        addShotlock(ModShotlocks.DARK_VOLLEY, "Dark Volley");
        addShotlock(ModShotlocks.PRISM_RAIN, "Prism Rain");
        addShotlock(ModShotlocks.SONIC_SHADOW, "Sonic Shadow");
        addShotlock(ModShotlocks.ULTIMA_CANNON, "Ultima Cannon");

        //Magic
        addMagic(ModMagic.FIRE, "Fire", "Fira", "Firaga", "Firaza");
        addMagic(ModMagic.BLIZZARD, "Blizzard", "Blizzara", "Blizzaga", "Blizzaza");
        addMagic(ModMagic.WATER, "Water", "Watera", "Waterga", "Waterza");
        addMagic(ModMagic.THUNDER, "Thunder", "Thundara", "Thundaga", "Thundaza");
        addMagic(ModMagic.CURE, "Cure", "Cura", "Curaga", "Curaza");
        addMagic(ModMagic.AERO, "Aero", "Aerora", "Aeroga");
        addMagic(ModMagic.MAGNET, "Magnet", "Magnera", "Magnega");
        addMagic(ModMagic.REFLECT, "Reflect", "Reflera", "Reflega");
        addMagic(ModMagic.GRAVITY, "Gravity", "Gravira", "Graviga");
        addMagic(ModMagic.STOP, "Stop", "Stopra", "Stopga");

        //Drive Forms
        addDriveForm(ModDriveForms.VALOR, "Valor");
        addDriveForm(ModDriveForms.WISDOM, "Wisdom");
        addDriveForm(ModDriveForms.LIMIT, "Limit");
        addDriveForm(ModDriveForms.MASTER, "Master");
        addDriveForm(ModDriveForms.FINAL, "Final");
        addDriveForm(ModDriveForms.ANTI, "Antiform");
        
        //Reaction Commands
        addReactionCommand(ModReactionCommands.AUTO_VALOR, "Auto Valor");
        addReactionCommand(ModReactionCommands.AUTO_WISDOM, "Auto Wisdom");
        addReactionCommand(ModReactionCommands.AUTO_LIMIT, "Auto Limit");
        addReactionCommand(ModReactionCommands.AUTO_MASTER, "Auto Master");
        addReactionCommand(ModReactionCommands.AUTO_FINAL, "Auto Final");
        addReactionCommand(ModReactionCommands.SAVE, "Save");

        /**Items**/
        //Cards
        add("item.mapcard.prefix", "(%s) %s");
        addItem(ModItems.tranquilDarkness, "Tranquil Darkness");
        addItem(ModItems.teemingDarkness, "Teeming Darkness");
        addItem(ModItems.feebleDarkness, "Feeble Darkness");
        addItem(ModItems.almightyDarkness, "Almighty Darkness");
        addItem(ModItems.sleepingDarkness, "Sleeping Darkness");
        addItem(ModItems.loomingDarkness, "Looming Darkness");
        addItem(ModItems.bottomlessDarkness, "Bottomless Darkness");
        addItem(ModItems.martialWaking, "Martial Waking");
        addItem(ModItems.sorcerousWaking, "Sorcerous Waking");
        addItem(ModItems.alchemicWaking, "Alchemic Waking");
        addItem(ModItems.stagnantSpace, "Stagnant Space");
        addItem(ModItems.weightlessSpace, "Weightless Space");
        addItem(ModItems.calmBounty, "Calm Bounty");
        addItem(ModItems.guardedTrove, "Guarded Trove");
        addItem(ModItems.falseBounty, "False Bounty");
        addItem(ModItems.momentsReprieve, "Moment's Reprieve");
        addItem(ModItems.minglingWorlds, "Mingling Worlds");
        addItem(ModItems.moogleRoom, "Moogle Room");
        addItem(ModItems.prosperousRepository, "Prosperous Repository");
        addItem(ModItems.treacherousRepository, "Treacherous Repository");
        addItem(ModItems.reposefulGrove, "Reposeful Grove");

        //Materials
        addItem(ModItems.blazing_shard, "Blazing Shard");
        addItem(ModItems.blazing_stone, "Blazing Stone");
        addItem(ModItems.blazing_gem, "Blazing Gem");
        addItem(ModItems.blazing_crystal, "Blazing Crystal");

        addItem(ModItems.soothing_shard, "Soothing Shard");
        addItem(ModItems.soothing_stone, "Soothing Stone");
        addItem(ModItems.soothing_gem, "Soothing Gem");
        addItem(ModItems.soothing_crystal, "Soothing Crystal");

        addItem(ModItems.writhing_shard, "Writhing Shard");
        addItem(ModItems.writhing_stone, "Writhing Stone");
        addItem(ModItems.writhing_gem, "Writhing Gem");
        addItem(ModItems.writhing_crystal, "Writhing Crystal");

        addItem(ModItems.betwixt_shard, "Betwixt Shard");
        addItem(ModItems.betwixt_stone, "Betwixt Stone");
        addItem(ModItems.betwixt_gem, "Betwixt Gem");
        addItem(ModItems.betwixt_crystal, "Betwixt Crystal");

        addItem(ModItems.wellspring_shard, "Wellspring Shard");
        addItem(ModItems.wellspring_stone, "Wellspring Stone");
        addItem(ModItems.wellspring_gem, "Wellspring Gem");
        addItem(ModItems.wellspring_crystal, "Wellspring Crystal");

        addItem(ModItems.frost_shard, "Frost Shard");
        addItem(ModItems.frost_stone, "Frost Stone");
        addItem(ModItems.frost_gem, "Frost Gem");
        addItem(ModItems.frost_crystal, "Frost Crystal");

        addItem(ModItems.lightning_shard, "Lightning Shard");
        addItem(ModItems.lightning_stone, "Lightning Stone");
        addItem(ModItems.lightning_gem, "Lightning Gem");
        addItem(ModItems.lightning_crystal, "Lightning Crystal");

        addItem(ModItems.lucid_shard, "Lucid Shard");
        addItem(ModItems.lucid_stone, "Lucid Stone");
        addItem(ModItems.lucid_gem, "Lucid Gem");
        addItem(ModItems.lucid_crystal, "Lucid Crystal");

        addItem(ModItems.hungry_shard, "Hungry Shard");
        addItem(ModItems.hungry_stone, "Hungry Stone");
        addItem(ModItems.hungry_gem, "Hungry Gem");
        addItem(ModItems.hungry_crystal, "Hungry Crystal");

        addItem(ModItems.twilight_shard, "Twilight Shard");
        addItem(ModItems.twilight_stone, "Twilight Stone");
        addItem(ModItems.twilight_gem, "Twilight Gem");
        addItem(ModItems.twilight_crystal, "Twilight Crystal");

        addItem(ModItems.mythril_shard, "Mythril Shard");
        addItem(ModItems.mythril_stone, "Mythril Stone");
        addItem(ModItems.mythril_gem, "Mythril Gem");
        addItem(ModItems.mythril_crystal, "Mythril Crystal");

        addItem(ModItems.tranquility_shard, "Tranquility Shard");
        addItem(ModItems.tranquility_stone, "Tranquility Stone");
        addItem(ModItems.tranquility_gem, "Tranquility Gem");
        addItem(ModItems.tranquility_crystal, "Tranquility Crystal");

        addItem(ModItems.sinister_shard, "Sinister Shard");
        addItem(ModItems.sinister_stone, "Sinister Stone");
        addItem(ModItems.sinister_gem, "Sinister Gem");
        addItem(ModItems.sinister_crystal, "Sinister Crystal");

        addItem(ModItems.stormy_shard, "Stormy Shard");
        addItem(ModItems.stormy_stone, "Stormy Stone");
        addItem(ModItems.stormy_gem, "Stormy Gem");
        addItem(ModItems.stormy_crystal, "Stormy Crystal");

        addItem(ModItems.remembrance_shard, "Remembrance Shard");
        addItem(ModItems.remembrance_stone, "Remembrance Stone");
        addItem(ModItems.remembrance_gem, "Remembrance Gem");
        addItem(ModItems.remembrance_crystal, "Remembrance Crystal");

        addItem(ModItems.pulsing_shard, "Pulsing Shard");
        addItem(ModItems.pulsing_stone, "Pulsing Stone");
        addItem(ModItems.pulsing_gem, "Pulsing Gem");
        addItem(ModItems.pulsing_crystal, "Pulsing Crystal");

        addItem(ModItems.orichalcum, "Orichalcum");
        addItem(ModItems.orichalcumplus, "Orichalcum+");
        addItem(ModItems.lost_illusion, "Lost Illusion");
        addItem(ModItems.manifest_illusion, "Manifest Illusion");

        addItem(ModItems.fluorite, "Fluorite");
        addItem(ModItems.damascus, "Damascus");
        addItem(ModItems.adamantite, "Adamantite");
        addItem(ModItems.electrum, "Electrum");
        addItem(ModItems.evanescent_crystal, "Evanescent Crystal");
        addItem(ModItems.illusory_crystal, "Illusory Crystal");

        //Keyblades
        addItem(ModItems.abaddonPlasma, "Abaddon Plasma");
        addItem(ModItems.abyssalTide, "Abyssal Tide");
        addItem(ModItems.acedsKeyblade, "Aced's Keyblade");
        addItem(ModItems.adventRed, "Advent Red");
        addItem(ModItems.allForOne, "All For One");
        addItem(ModItems.astralBlast, "Astral Blast");
        addItem(ModItems.aubade, "Aubade");
        addItem(ModItems.avasKeyblade, "Ava's Keyblade");
        addItem(ModItems.bondOfFlame, "Bond Of Flame");
        addItem(ModItems.bondOfTheBlaze, "Bond of the Blaze");
        addItem(ModItems.braveheart, "Braveheart");
        addItem(ModItems.brightcrest, "Brightcrest");
        addItem(ModItems.chaosRipper, "Chaos Ripper");
        addItem(ModItems.circleOfLife, "Circle Of Life");
        addItem(ModItems.classicTone, "Classic Tone");
        addItem(ModItems.counterpoint, "Counterpoint");
        addItem(ModItems.crabclaw, "Crabclaw");
        addItem(ModItems.crownOfGuilt, "Crown Of Guilt");
        addItem(ModItems.crystalSnow, "Crystal Snow");
        addItem(ModItems.darkerThanDark, "Darker Than Dark");
        addItem(ModItems.darkgnaw, "Darkgnaw");
        addItem(ModItems.dawnTillDusk, "Dawn Till Dusk");
        addItem(ModItems.deadOfNight, "Dead of Night");
        addItem(ModItems.decisivePumpkin, "Decisive Pumpkin");
        addItem(ModItems.destinysEmbrace, "Destiny's Embrace");
        addItem(ModItems.diamondDust, "Diamond Dust");
        addItem(ModItems.divewing, "Divewing");
        addItem(ModItems.divineRose, "Divine Rose");
        addItem(ModItems.dualDisc, "Dual Disc");
        addItem(ModItems.earthshaker, "Earthshaker");
        addItem(ModItems.elementalEncoder, "Elemental Encoder");
        addItem(ModItems.endOfPain, "End Of Pain");
        addItem(ModItems.endsOfTheEarth, "Ends Of The Earth");
        addItem(ModItems.everAfter, "Ever After");
        addItem(ModItems.fairyHarp, "Fairy Harp");
        addItem(ModItems.fairyStars, "Fairy Stars");
        addItem(ModItems.fatalCrest, "Fatal Crest");
        addItem(ModItems.favoriteDeputy, "Favorite Deputy");
        addItem(ModItems.fenrir, "Fenrir");
        addItem(ModItems.ferrisGear, "Ferris Gear");
        addItem(ModItems.followTheWind, "Follow the Wind");
        addItem(ModItems.frolicFlame, "Frolic Flame");
        addItem(ModItems.glimpseOfDarkness, "Glimpse Of Darkness");
        addItem(ModItems.grandChef, "Grand Chef");
        addItem(ModItems.guardianBell, "Guardian Bell");
        addItem(ModItems.guardianSoul, "Guardian Soul");
        addItem(ModItems.gulasKeyblade, "Gula's Keyblade");
        addItem(ModItems.gullWing, "Gull Wing");
        addItem(ModItems.happyGear, "Happy Gear");
        addItem(ModItems.herosCrest, "Hero's Crest");
        addItem(ModItems.herosOrigin, "Hero's Origin");
        addItem(ModItems.hiddenDragon, "Hidden Dragon");
        addItem(ModItems.hunnySpout, "Hunny Spout");
        addItem(ModItems.hyperdrive, "Hyperdrive");
        addItem(ModItems.incompleteKiblade, "Incomplete \u03c7-Blade");
        addItem(ModItems.invisKeyblade, "Invi's Keyblade");
        addItem(ModItems.irasKeyblade, "Ira's Keyblade");
        addItem(ModItems.jungleKing, "Jungle King");
        addItem(ModItems.keybladeOfPeoplesHearts, "Keyblade Of People's Hearts");
        addItem(ModItems.kiblade, "\u03c7-Blade");
        addItem(ModItems.kingdomKey, "Kingdom Key");
        addItem(ModItems.kingdomKeyD, "Kingdom Key D");
        addItem(ModItems.knockoutPunch, "Knockout Punch");
        addItem(ModItems.ladyLuck, "Lady Luck");
        addItem(ModItems.leviathan, "Leviathan");
        addItem(ModItems.lionheart, "Lionheart");
        addItem(ModItems.lostMemory, "Lost Memory");
        addItem(ModItems.lunarEclipse, "Lunar Eclipse");
        addItem(ModItems.markOfAHero, "Mark Of A Hero");
        addItem(ModItems.mastersDefender, "Master's Defender");
        addItem(ModItems.maverickFlare, "Maverick Flare");
        addItem(ModItems.metalChocobo, "Metal Chocobo");
        addItem(ModItems.midnightBlue, "Midnight Blue");
        addItem(ModItems.midnightRoar, "Midnight Roar");
        addItem(ModItems.mirageSplit, "Mirage Split");
        addItem(ModItems.missingAche, "Missing Ache");
        addItem(ModItems.monochrome, "Monochrome");
        addItem(ModItems.moogleOGlory, "Moogle O' Glory");
        addItem(ModItems.mysteriousAbyss, "Mysterious Abyss");
        addItem(ModItems.nanoGear, "Nano Gear");
        addItem(ModItems.nightmaresEnd, "Nightmare's End");
        addItem(ModItems.nightmaresEndAndMirageSplit, "Combined Keyblade");
        addItem(ModItems.noName, "The Gazing Eye");
        addItem(ModItems.noNameBBS, "No Name");
        addItem(ModItems.oathkeeper, "Oathkeeper");
        addItem(ModItems.oblivion, "Oblivion");
        addItem(ModItems.oceansRage, "Ocean's Rage");
        addItem(ModItems.olympia, "Olympia");
        addItem(ModItems.omegaWeapon, "Omega Weapon");
        addItem(ModItems.ominousBlight, "Ominous Blight");
        addItem(ModItems.oneWingedAngel, "One Winged Angel");
        addItem(ModItems.painOfSolitude, "Pain Of Solitude");
        addItem(ModItems.phantomGreen, "Phantom Green");
        addItem(ModItems.photonDebugger, "Photon Debugger");
        addItem(ModItems.pixiePetal, "Pixie Petal");
        addItem(ModItems.pumpkinhead, "Pumpkinhead");
        addItem(ModItems.rainfell, "Rainfell");
        addItem(ModItems.rejectionOfFate, "Rejection Of Fate");
        addItem(ModItems.royalRadiance, "Royal Radiance");
        addItem(ModItems.rumblingRose, "Rumbling Rose");
        addItem(ModItems.shootingStar, "Shooting Star");
        addItem(ModItems.signOfInnocence, "Sign Of Innocence");
        addItem(ModItems.silentDirge, "Silent Dirge");
        addItem(ModItems.skullNoise, "Skull Noise");
        addItem(ModItems.sleepingLion, "Sleeping Lion");
        addItem(ModItems.soulEater, "Soul Eater");
        addItem(ModItems.spellbinder, "Spellbinder");
        addItem(ModItems.starCluster, "Star Cluster");
        addItem(ModItems.starSeeker, "Star Seeker");
        addItem(ModItems.starlight, "Starlight");
        addItem(ModItems.stormfall, "Stormfall");
        addItem(ModItems.strokeOfMidnight, "Stroke Of Midnight");
        addItem(ModItems.sweetDreams, "Sweet Dreams");
        addItem(ModItems.sweetMemories, "Sweet Memories");
        addItem(ModItems.sweetstack, "Sweetstack");
        addItem(ModItems.threeWishes, "Three Wishes");
        addItem(ModItems.totalEclipse, "Total Eclipse");
        addItem(ModItems.treasureTrove, "Treasure Trove");
        addItem(ModItems.trueLightsFlight, "True Light's Flight");
        addItem(ModItems.twilightBlaze, "Twilight Blaze");
        addItem(ModItems.twoBecomeOne, "Two Become One");
        addItem(ModItems.ultimaWeaponBBS, "Ultima Weapon (BBS)");
        addItem(ModItems.ultimaWeaponDDD, "Ultima Weapon (DDD)");
        addItem(ModItems.ultimaWeaponKH1, "Ultima Weapon (KH1)");
        addItem(ModItems.ultimaWeaponKH2, "Ultima Weapon (KH2)");
        addItem(ModItems.ultimaWeaponKH3, "Ultima Weapon (KH3)");
        addItem(ModItems.umbrella, "Umbrella");
        addItem(ModItems.unbound, "Unbound");
        addItem(ModItems.victoryLine, "Victory Line");
        addItem(ModItems.voidGear, "Void Gear");
        addItem(ModItems.voidGearRemnant, "Void Gear Remnant");
        addItem(ModItems.waytotheDawn, "Way to the Dawn");
        addItem(ModItems.waywardWind, "Wayward Wind");
        addItem(ModItems.wheelOfFate, "Wheel of Fate");
        addItem(ModItems.winnersProof, "Winner's Proof");
        addItem(ModItems.wishingLamp, "Wishing Lamp");
        addItem(ModItems.wishingStar, "Wishing Star");
        addItem(ModItems.woodenKeyblade, "Wooden Keyblade");
        addItem(ModItems.woodenStick, "Wooden Stick");
        addItem(ModItems.youngXehanortsKeyblade, "Young Xehanort's Keyblade");
        addItem(ModItems.zeroOne, "Zero/One");
        addItem(ModItems.dreamSword, "Dream Sword");
        addItem(ModItems.dreamStaff, "Dream Staff");
        addItem(ModItems.dreamShield, "Dream Shield");
        addItem(ModItems.struggleSword, "Struggle Sword");
        addItem(ModItems.struggleWand, "Struggle Wand");
        addItem(ModItems.struggleHammer, "Struggle Hammer");
        addItem(ModItems.k111, "K111");
        addItem(ModItems.retribution, "Retribution");

        //Keychains
        addItem(ModItems.abaddonPlasmaChain, "Abaddon Plasma Chain");
        addItem(ModItems.abyssalTideChain, "Abyssal Tide Chain");
        addItem(ModItems.acedsKeybladeChain, "Aced's Keyblade Chain");
        addItem(ModItems.adventRedChain, "Advent Red Chain");
        addItem(ModItems.allForOneChain, "All For One Chain");
        addItem(ModItems.astralBlastChain, "Astral Blast Chain");
        addItem(ModItems.aubadeChain, "Aubade Chain");
        addItem(ModItems.avasKeybladeChain, "Ava's Keyblade Chain");
        addItem(ModItems.bondOfFlameChain, "Bond Of Flame Chain");
        addItem(ModItems.bondOfTheBlazeChain, "Bond of the Blaze Chain");
        addItem(ModItems.braveheartChain, "Braveheart Chain");
        addItem(ModItems.brightcrestChain, "Brightcrest Chain");
        addItem(ModItems.chaosRipperChain, "Chaos Ripper Chain");
        addItem(ModItems.circleOfLifeChain, "Circle Of Life Chain");
        addItem(ModItems.classicToneChain, "Classic Tone Chain");
        addItem(ModItems.counterpointChain, "Counterpoint Chain");
        addItem(ModItems.crabclawChain, "Crabclaw Chain");
        addItem(ModItems.crownOfGuiltChain, "Crown Of Guilt Chain");
        addItem(ModItems.crystalSnowChain, "Crystal Snow Chain");
        addItem(ModItems.darkerThanDarkChain, "Darker Than Dark Chain");
        addItem(ModItems.darkgnawChain, "Darkgnaw Chain");
        addItem(ModItems.dawnTillDuskChain, "Dawn Till Dusk Chain");
        addItem(ModItems.deadOfNightChain, "Dead of Night Chain");
        addItem(ModItems.decisivePumpkinChain, "Decisive Pumpkin Chain");
        addItem(ModItems.destinysEmbraceChain, "Destiny's Embrace Chain");
        addItem(ModItems.diamondDustChain, "Diamond Dust Chain");
        addItem(ModItems.divewingChain, "Divewing Chain");
        addItem(ModItems.divineRoseChain, "Divine Rose Chain");
        addItem(ModItems.dualDiscChain, "Dual Disc Chain");
        addItem(ModItems.earthshakerChain, "Earthshaker Chain");
        addItem(ModItems.elementalEncoderChain, "Elemental Encoder Chain");
        addItem(ModItems.endOfPainChain, "End Of Pain Chain");
        addItem(ModItems.endsOfTheEarthChain, "Ends Of The Earth Chain");
        addItem(ModItems.everAfterChain, "Ever After Chain");
        addItem(ModItems.fairyHarpChain, "Fairy Harp Chain");
        addItem(ModItems.fairyStarsChain, "Fairy Stars Chain");
        addItem(ModItems.fatalCrestChain, "Fatal Crest Chain");
        addItem(ModItems.favoriteDeputyChain, "Favorite Deputy Chain");
        addItem(ModItems.fenrirChain, "Fenrir Chain");
        addItem(ModItems.ferrisGearChain, "Ferris Gear Chain");
        addItem(ModItems.followTheWindChain, "Follow The Wind Chain");
        addItem(ModItems.frolicFlameChain, "Frolic Flame Chain");
        addItem(ModItems.glimpseOfDarknessChain, "Glimpse Of Darkness Chain");
        addItem(ModItems.grandChefChain, "Grand Chef Chain");
        addItem(ModItems.guardianBellChain, "Guardian Bell Chain");
        addItem(ModItems.guardianSoulChain, "Guardian Soul Chain");
        addItem(ModItems.gulasKeybladeChain, "Gula's Keyblade Chain");
        addItem(ModItems.gullWingChain, "Gull Wing Chain");
        addItem(ModItems.happyGearChain, "Happy Gear Chain");
        addItem(ModItems.herosCrestChain, "Hero's Crest Chain");
        addItem(ModItems.herosOriginChain, "Hero's Origin Chain");
        addItem(ModItems.hiddenDragonChain, "Hidden Dragon Chain");
        addItem(ModItems.hunnySpoutChain, "Hunny Spout Chain");
        addItem(ModItems.hyperdriveChain, "Hyperdrive Chain");
        addItem(ModItems.incompleteKibladeChain, "Incomplete \u03c7-Blade Chain");
        addItem(ModItems.invisKeybladeChain, "Invi's Keyblade Chain");
        addItem(ModItems.irasKeybladeChain, "Ira's Keyblade Chain");
        addItem(ModItems.jungleKingChain, "Jungle King Chain");
        addItem(ModItems.keybladeOfPeoplesHeartsChain, "Keyblade Of People's Hearts Chain");
        addItem(ModItems.kibladeChain, "\u03c7-Blade Chain");
        addItem(ModItems.kingdomKeyChain, "Kingdom Key Chain");
        addItem(ModItems.kingdomKeyDChain, "Kingdom Key D Chain");
        addItem(ModItems.knockoutPunchChain, "Knockout Punch Chain");
        addItem(ModItems.ladyLuckChain, "Lady Luck Chain");
        addItem(ModItems.leviathanChain, "Leviathan Chain");
        addItem(ModItems.lionheartChain, "Lionheart Chain");
        addItem(ModItems.lostMemoryChain, "Lost Memory Chain");
        addItem(ModItems.lunarEclipseChain, "Lunar Eclipse Chain");
        addItem(ModItems.markOfAHeroChain, "Mark Of A Hero Chain");
        addItem(ModItems.mastersDefenderChain, "Master's Defender Chain");
        addItem(ModItems.maverickFlareChain, "Maverick Flare Chain");
        addItem(ModItems.metalChocoboChain, "Metal Chocobo Chain");
        addItem(ModItems.midnightBlueChain, "Midnight Blue Chain");
        addItem(ModItems.midnightRoarChain, "Midnight Roar Chain");
        addItem(ModItems.mirageSplitChain, "Mirage Split Chain");
        addItem(ModItems.missingAcheChain, "Missing Ache Chain");
        addItem(ModItems.monochromeChain, "Monochrome Chain");
        addItem(ModItems.moogleOGloryChain, "Moogle O' Glory Chain");
        addItem(ModItems.mysteriousAbyssChain, "Mysterious Abyss Chain");
        addItem(ModItems.nanoGearChain, "Nano Gear Chain");
        addItem(ModItems.nightmaresEndChain, "Nightmare's End Chain");
        addItem(ModItems.nightmaresEndAndMirageSplitChain, "Combined Keyblade Chain");
        addItem(ModItems.noNameChain, "The Gazing Eye Chain");
        addItem(ModItems.noNameBBSChain, "No Name Chain");
        addItem(ModItems.oathkeeperChain, "Oathkeeper Chain");
        addItem(ModItems.oblivionChain, "Oblivion Chain");
        addItem(ModItems.oceansRageChain, "Ocean's Rage Chain");
        addItem(ModItems.olympiaChain, "Olympia Chain");
        addItem(ModItems.omegaWeaponChain, "Omega Weapon Chain");
        addItem(ModItems.ominousBlightChain, "Ominous Blight Chain");
        addItem(ModItems.oneWingedAngelChain, "One Winged Angel Chain");
        addItem(ModItems.painOfSolitudeChain, "Pain Of Solitude Chain");
        addItem(ModItems.phantomGreenChain, "Phantom Green Chain");
        addItem(ModItems.photonDebuggerChain, "Photon Debugger Chain");
        addItem(ModItems.pixiePetalChain, "Pixie Petal Chain");
        addItem(ModItems.pumpkinheadChain, "Pumpkinhead Chain");
        addItem(ModItems.rainfellChain, "Rainfell Chain");
        addItem(ModItems.rejectionOfFateChain, "Rejection Of Fate Chain");
        addItem(ModItems.royalRadianceChain, "Royal Radiance Chain");
        addItem(ModItems.rumblingRoseChain, "Rumbling Rose Chain");
        addItem(ModItems.shootingStarChain, "Shooting Star Chain");
        addItem(ModItems.signOfInnocenceChain, "Sign Of Innocence Chain");
        addItem(ModItems.silentDirgeChain, "Silent Dirge Chain");
        addItem(ModItems.skullNoiseChain, "Skull Noise Chain");
        addItem(ModItems.sleepingLionChain, "Sleeping Lion Chain");
        addItem(ModItems.soulEaterChain, "Soul Eater Chain");
        addItem(ModItems.spellbinderChain, "Spellbinder Chain");
        addItem(ModItems.starClusterChain, "Star Cluster Chain");
        addItem(ModItems.starSeekerChain, "Star Seeker Chain");
        addItem(ModItems.starlightChain, "Starlight Chain");
        addItem(ModItems.stormfallChain, "Stormfall Chain");
        addItem(ModItems.strokeOfMidnightChain, "Stroke Of Midnight Chain");
        addItem(ModItems.sweetDreamsChain, "Sweet Dreams Chain");
        addItem(ModItems.sweetMemoriesChain, "Sweet Memories Chain");
        addItem(ModItems.sweetstackChain, "Sweetstack Chain");
        addItem(ModItems.threeWishesChain, "Three Wishes Chain");
        addItem(ModItems.totalEclipseChain, "Total Eclipse Chain");
        addItem(ModItems.treasureTroveChain, "Treasure Trove Chain");
        addItem(ModItems.trueLightsFlightChain, "True Light's Flight Chain");
        addItem(ModItems.twilightBlazeChain, "Twilight Blaze Chain");
        addItem(ModItems.twoBecomeOneChain, "Two Become One Chain");
        addItem(ModItems.ultimaWeaponBBSChain, "Ultima Weapon (BBS) Chain");
        addItem(ModItems.ultimaWeaponDDDChain, "Ultima Weapon (DDD) Chain");
        addItem(ModItems.ultimaWeaponKH1Chain, "Ultima Weapon (KH1) Chain");
        addItem(ModItems.ultimaWeaponKH2Chain, "Ultima Weapon (KH2) Chain");
        addItem(ModItems.ultimaWeaponKH3Chain, "Ultima Weapon (KH3) Chain");
        addItem(ModItems.umbrellaChain, "Umbrella Chain");
        addItem(ModItems.unboundChain, "Unbound Chain");
        addItem(ModItems.victoryLineChain, "Victory Line Chain");
        addItem(ModItems.voidGearChain, "Void Gear Chain");
        addItem(ModItems.voidGearRemnantChain, "Void Gear Remnant Chain");
        addItem(ModItems.waytotheDawnChain, "Way to the Dawn Chain");
        addItem(ModItems.waywardWindChain, "Wayward Wind Chain");
        addItem(ModItems.wheelOfFateChain, "Wheel of Fate Chain");
        addItem(ModItems.winnersProofChain, "Winner's Proof Chain");
        addItem(ModItems.wishingLampChain, "Wishing Lamp Chain");
        addItem(ModItems.wishingStarChain, "Wishing Star Chain");
        addItem(ModItems.youngXehanortsKeybladeChain, "Young Xehanort's Keyblade Chain");
        addItem(ModItems.zeroOneChain, "Zero/One Chain");
        addItem(ModItems.k111c, "K111c");
        addItem(ModItems.retributionChain, "Retribution Chain");


        //Keyblade Descriptions
        add("item." + MODID + "." + Strings.abaddonPlasma + ".desc", "A weapon that lets you string together faster, incredibly long ground combos.");
        add("item." + MODID + "." + Strings.abyssalTide + ".desc", "A weapon that performs very well in midair. Excellent for taking on fliers.");
        add("item." + MODID + "." + Strings.acedsKeyblade + ".desc", "The Keyblade owned by Ursus' Foreteller.");
        add("item." + MODID + "." + Strings.adventRed + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.allForOne + ".desc", "A Keyblade that triggers fewer Reality Shifts, but compensates with a boost in Magic and more frequent critical hits.");
        add("item." + MODID + "." + Strings.astralBlast + ".desc", "A weapon that lets you string together longer ground and aerial combos.");
        add("item." + MODID + "." + Strings.aubade + ".desc", "A weapon that draws forth its wielder's personality.");
        add("item." + MODID + "." + Strings.avasKeyblade + ".desc", "The Keyblade owned by Vulpeus' Foreteller.");
        add("item." + MODID + "." + Strings.bondOfFlame + ".desc", "Enhances magic to increase damage dealt by fire-based attacks.");
        add("item." + MODID + "." + Strings.bondOfTheBlaze + ".desc", "The Keyblade wielded by Lea.");
        add("item." + MODID + "." + Strings.braveheart + ".desc", "Riku's Keyblade after the Way to the Dawn was broken.");
        add("item." + MODID + "." + Strings.brightcrest + ".desc", "A Keyblade with long reach that provides an outstanding boost in Magic. It also makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.chaosRipper + ".desc", "A Keyblade with long reach that does little for your Magic, but provides an outstanding boost in Strength.");
        add("item." + MODID + "." + Strings.circleOfLife + ".desc", "Has great strength, increasing MP restoration speed after MP is consumed.");
        add("item." + MODID + "." + Strings.classicTone + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.counterpoint + ".desc", "A Keyblade with long reach that provides an extra boost in Magic and makes it easier to trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.crabclaw + ".desc", "Raises max MP by 1, and enhances magic and summon power. Also deals good physical damage.");
        add("item." + MODID + "." + Strings.crownOfGuilt + ".desc", "A weapon that boosts your Magic to give it incredible power.");
        add("item." + MODID + "." + Strings.crystalSnow + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.darkerThanDark + ".desc", "A weapon that offers high Magic and combo reach.");
        add("item." + MODID + "." + Strings.darkgnaw + ".desc", "A Keyblade that makes up for its poor reach and low critical hit ratio by providing an extra boost in Strength.");
        add("item." + MODID + "." + Strings.dawnTillDusk + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.deadOfNight + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.decisivePumpkin + ".desc", "The greater number of combos landed, the more damage is dealt, leading to a strong finishing move!");
        add("item." + MODID + "." + Strings.destinysEmbrace + ".desc", "A Keyblade that makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.diamondDust + ".desc", "Greatly enhances magic and summon power. Raises max MP by 3.");
        add("item." + MODID + "." + Strings.divewing + ".desc", "A Keyblade with long reach that provides an extra boost in Magic and makes it easier to trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.divineRose + ".desc", "A powerful weapon that is difficult to deflect. Capable of dealing a string of critical blows.");
        add("item." + MODID + "." + Strings.dualDisc + ".desc", "A Keyblade that provides an extra boost in Strength and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.earthshaker + ".desc", "The Keyblade Terra started out with. What it lacks in reach it makes up for with a slight boost in Strength.");
        add("item." + MODID + "." + Strings.elementalEncoder + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.endOfPain + ".desc", "A Keyblade with high magical power and critical hit rate, but reduces the occurrence of Reality Shift.");
        add("item." + MODID + "." + Strings.endsOfTheEarth + ".desc", "A well-balanced Keyblade that provides an extra boost to all your stats.");
        add("item." + MODID + "." + Strings.everAfter + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.fairyHarp + ".desc", "Raises max MP by 1, and enhances magic and summon power. Sometimes deals powerful critical blows.");
        add("item." + MODID + "." + Strings.fairyStars + ".desc", "A Keyblade that provides a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.fatalCrest + ".desc", "Increases strength during MP Charge and allows unlimited chaining of combos.");
        add("item." + MODID + "." + Strings.favoriteDeputy + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.fenrir + ".desc", "Has great range and strength, but maximum ground and midair combos are decreased by 1.");
        add("item." + MODID + "." + Strings.ferrisGear + ".desc", "A Keyblade that provides an extra boost in Strength and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.followTheWind + ".desc", "Draws in nearby orbs.");
        add("item." + MODID + "." + Strings.frolicFlame + ".desc", "A well-balanced Keyblade that provides an extra boost to all of your stats.");
        add("item." + MODID + "." + Strings.glimpseOfDarkness + ".desc", "A weapon that possesses very high Strength. Effective against tough enemies.");
        add("item." + MODID + "." + Strings.grandChef + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.guardianBell + ".desc", "A Keyblade with long reach that provides an extra boost in Magic.");
        add("item." + MODID + "." + Strings.guardianSoul + ".desc", "Has great strength, increasing the amount of damage dealt from Reaction Commands.");
        add("item." + MODID + "." + Strings.gulasKeyblade + ".desc", "The Keyblade owned by Leopardos' Foreteller.");
        add("item." + MODID + "." + Strings.gullWing + ".desc", "Greatly increases the amount of experience gained when defeating an enemy at a critical moment.");
        add("item." + MODID + "." + Strings.happyGear + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.herosCrest + ".desc", "Increases the damage of the finishing move in the air relative to the number of hits in the combo.");
        add("item." + MODID + "." + Strings.herosOrigin + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.hiddenDragon + ".desc", "Restores MP relative to the amount of damage taken.");
        add("item." + MODID + "." + Strings.hunnySpout + ".desc", "A well-balanced Keyblade.");
        add("item." + MODID + "." + Strings.hyperdrive + ".desc", "A Keyblade with above-average reach that provides a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.incompleteKiblade + ".desc", "An incomplete form of the legendary Keyblade, the \u03c7-blade.");
        add("item." + MODID + "." + Strings.invisKeyblade + ".desc", "The Keyblade owned by Anguis' Foreteller.");
        add("item." + MODID + "." + Strings.irasKeyblade + ".desc", "The Keyblade owned by Unicornis' Foreteller.");
        add("item." + MODID + "." + Strings.jungleKing + ".desc", "Has a long reach, but seldom deals critical blows.");
        add("item." + MODID + "." + Strings.keybladeOfPeoplesHearts + ".desc", "A keyblade with the ability to unlock a person's heart, releasing the darkness within.");
        add("item." + MODID + "." + Strings.kiblade + ".desc", "A legendary weapon, the original Keyblade which all other are imperfectly modeled after.");
        add("item." + MODID + "." + Strings.kingdomKey + ".desc", "The key chain attached draws out the Keyblade's true form and power.");
        add("item." + MODID + "." + Strings.kingdomKeyD + ".desc", "A Keyblade which mirrors the Kingdom Key from the Realm of Darkness.");
        add("item." + MODID + "." + Strings.knockoutPunch + ".desc", "A Keyblade that lands fewer critical hits, but compensates with a Strength boost and more frequent Reality Shifts.");
        add("item." + MODID + "." + Strings.ladyLuck + ".desc", "Raises max MP by 2, and significantly enhances magic and summon power. Also inflicts good physical damage.");
        add("item." + MODID + "." + Strings.leviathan + ".desc", "A weapon that performs extremely well in midair. Outstanding for taking on fliers.");
        add("item." + MODID + "." + Strings.lionheart + ".desc", "Raises max MP by 1, and enhances magic and summon power. Also deals great physical damage.");
        add("item." + MODID + "." + Strings.lostMemory + ".desc", "A Keyblade with long reach that makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.lunarEclipse + ".desc", "A weapon that boosts versatility by greatly boosting both Strength and Magic.");
        add("item." + MODID + "." + Strings.markOfAHero + ".desc", "A Keyblade that provides an extra boost in Strength and deals higher damage when you land a critical hit.");
        add("item." + MODID + "." + Strings.mastersDefender + ".desc", "Master Eraqus's Keyblade. All of its stats are high.");
        add("item." + MODID + "." + Strings.maverickFlare + ".desc", "A weapon that offers high Strength and ground combo speed.");
        add("item." + MODID + "." + Strings.metalChocobo + ".desc", "Possesses incredible power and reach, but reduces max MP by 1. Rarely deals critical blows.");
        add("item." + MODID + "." + Strings.midnightBlue + ".desc", "A Keyblade imbued with wondrous power.");
        add("item." + MODID + "." + Strings.midnightRoar + ".desc", "A weapon that possesses high Strength. Useful against tough enemies.");
        add("item." + MODID + "." + Strings.mirageSplit + ".desc", "A Keyblade formed from a Reality Shift in The World That Never Was.");
        add("item." + MODID + "." + Strings.missingAche + ".desc", "A weapon that lets you string together faster, longer ground combos.");
        add("item." + MODID + "." + Strings.monochrome + ".desc", "Increases the effect of restoration items used on the field.");
        add("item." + MODID + "." + Strings.moogleOGlory + ".desc", "Kupo.");
        add("item." + MODID + "." + Strings.mysteriousAbyss + ".desc", "Enhances magic to increase damage dealt by blizzard-based attacks.");
        add("item." + MODID + "." + Strings.nanoGear + ".desc", "A well-balanced Keyblade.");
        add("item." + MODID + "." + Strings.nightmaresEnd + ".desc", "A Keyblade formed from a Reality Shift in The World That Never Was.");
        add("item." + MODID + "." + Strings.nightmaresEndAndMirageSplit + ".desc", "A Keyblade formed by combining both the Mirage Split and Nightmare's End.");
        add("item." + MODID + "." + Strings.noName + ".desc", "The Keyblade that Luxu received from the Master of Masters, containing his very own eye.");
        add("item." + MODID + "." + Strings.noNameBBS + ".desc", "A Keyblade with long reach that provides an outstanding boost in Magic and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.oathkeeper + ".desc", "Enhances magic and increases the duration of a Drive Form.");
        add("item." + MODID + "." + Strings.oblivion + ".desc", "Has great strength, and allows the Drive Gauge to restore quickly during MP Charge.");
        add("item." + MODID + "." + Strings.oceansRage + ".desc", "A Keyblade that lands fewer critical hits, but compensates with a boost in Magic and more frequent Reality Shifts.");
        add("item." + MODID + "." + Strings.olympia + ".desc", "A powerful weapon that is difficult to deflect. Capable of inflicting mighty critical blows.");
        add("item." + MODID + "." + Strings.omegaWeapon + ".desc", "A formidable weapon with exceptional capabilities.");
        add("item." + MODID + "." + Strings.ominousBlight + ".desc", "A weapon that lets you string together faster, much longer ground combos.");
        add("item." + MODID + "." + Strings.oneWingedAngel + ".desc", "Raises max MP by 1, and enhances magic and summon power. Also deals great physical damage.");
        add("item." + MODID + "." + Strings.painOfSolitude + ".desc", "A weapon that boosts your Magic to give it more power.");
        add("item." + MODID + "." + Strings.phantomGreen + ".desc", "A Keyblade imbued with wondrous power.");
        add("item." + MODID + "." + Strings.photonDebugger + ".desc", "Increases damage done by thunder-based attacks.");
        add("item." + MODID + "." + Strings.pixiePetal + ".desc", "A Keyblade that makes up for its poor reach with an extra boost in Magic. It also makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.pumpkinhead + ".desc", "Has a long reach and the ability to deal a string of critical blows.");
        add("item." + MODID + "." + Strings.rainfell + ".desc", "The Keyblade Aqua started out with. What it lacks in reach it makes up for with a balanced boost to Strength and Magic.");
        add("item." + MODID + "." + Strings.rejectionOfFate + ".desc", "A weapon that enables your attacks to reach a wide area and deal immense damage.");
        add("item." + MODID + "." + Strings.royalRadiance + ".desc", "A Keyblade with long reach that makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.rumblingRose + ".desc", "Has great strength, allowing finishing combo moves to be unleashed successively.");
        add("item." + MODID + "." + Strings.shootingStar + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.signOfInnocence + ".desc", "A weapon that boosts your Magic to give it a lot more power.");
        add("item." + MODID + "." + Strings.silentDirge + ".desc", "A weapon that provides versatility by boosting both Strength and Magic.");
        add("item." + MODID + "." + Strings.skullNoise + ".desc", "A Keyblade that provides a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.sleepingLion + ".desc", "Well-balanced with strength and magic, increasing maximum ground-based combos by 1.");
        add("item." + MODID + "." + Strings.soulEater + ".desc", "A sword that swims with darkness. Possesses high Strength.");
        add("item." + MODID + "." + Strings.spellbinder + ".desc", "Raises max MP by 2, and significantly enhances magic and summon power.");
        add("item." + MODID + "." + Strings.starCluster + ".desc", "Mickey's Keyblade, also known as Kingdom Key W.");
        add("item." + MODID + "." + Strings.starSeeker + ".desc", "Increases maximum combo by 1 when in midair.");
        add("item." + MODID + "." + Strings.starlight + ".desc", "A basic Keyblade which is associated with the force of Light.");
        add("item." + MODID + "." + Strings.stormfall + ".desc", "A well-balanced Keyblade that provides an extra boost to all your stats.");
        add("item." + MODID + "." + Strings.strokeOfMidnight + ".desc", "A Keyblade that makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.sweetDreams + ".desc", "A Keyblade with long reach that provides an extra boost in Strength and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.sweetMemories + ".desc", "Although it does not enhance attack strength, it increases the drop rate of items.");
        add("item." + MODID + "." + Strings.sweetstack + ".desc", "A Keyblade that provides an extra boost in Strength and ensures every strike is a critical hit.");
        add("item." + MODID + "." + Strings.threeWishes + ".desc", "A powerful weapon that is difficult to deflect.");
        add("item." + MODID + "." + Strings.totalEclipse + ".desc", "A weapon that possesses extreme Strength. Devastates tough enemies.");
        add("item." + MODID + "." + Strings.treasureTrove + ".desc", "A Keyblade that makes up for its poor reach with a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.trueLightsFlight + ".desc", "A weapon that enables your attacks to reach a wide area and deal heavy damage.");
        add("item." + MODID + "." + Strings.twilightBlaze + ".desc", "A weapon that boasts superior Strength and ground combo speed.");
        add("item." + MODID + "." + Strings.twoBecomeOne + ".desc", "A weapon of great strength and magic that has a special effect.");
        add("item." + MODID + "." + Strings.ultimaWeaponBBS + ".desc", "The most powerful of Keyblades.");
        add("item." + MODID + "." + Strings.ultimaWeaponDDD + ".desc", "An outstanding Keyblade that boosts all stats, and makes it easy to both land critical hits and trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.ultimaWeaponKH1 + ".desc", "The ultimate Keyblade. Raises max MP by 2, and possesses maximum power and attributes.");
        add("item." + MODID + "." + Strings.ultimaWeaponKH2 + ".desc", "The Keyblade above all others, holding all power and will increase MP restoration rate, once all MP has been consumed.");
        add("item." + MODID + "." + Strings.ultimaWeaponKH3 + ".desc", "The supreme Keyblade.");
        add("item." + MODID + "." + Strings.umbrella + ".desc", "This looks awfully familiar...");
        add("item." + MODID + "." + Strings.unbound + ".desc", "Keyblade perfection. It boosts all stats, while making it easy to land critical hits and even easier to trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.victoryLine + ".desc", "A Keyblade with above-average reach that makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.voidGear + ".desc", "A Keyblade with long reach that provides an outstanding boost in Strength and deals higher damage when you land a critical hit.");
        add("item." + MODID + "." + Strings.voidGearRemnant + ".desc", "A Keyblade with long reach that provides an outstanding boost in Strength and deals higher damage when you land a critical hit.");
        add("item." + MODID + "." + Strings.wayToTheDawn + ".desc", "Deals various attacks.");
        add("item." + MODID + "." + Strings.waywardWind + ".desc", "The Keyblade Ventus started out with. What it lacks in reach it makes up for with a slight boost in Strength.");
        add("item." + MODID + "." + Strings.wheelOfFate + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.winnersProof + ".desc", "Has high strength and hold's an excellent magic power. When the enemies are defeated, experience points are not gained.");
        add("item." + MODID + "." + Strings.wishingLamp + ".desc", "Wishes come true by increasing the drop rate of munny, HP and MP orbs.");
        add("item." + MODID + "." + Strings.wishingStar + ".desc", "Has a short reach, but always finishes up a combo attack with a powerful critical blow.");
        add("item." + MODID + "." + Strings.youngXehanortsKeyblade + ".desc", "The Keyblade weilded by Young Xehanort.");
        add("item." + MODID + "." + Strings.zeroOne + ".desc", "A Keyblade newly wrought within the datascape. Its powers render all opponents helpless.");
        add("item." + MODID + "." + Strings.k111 + ".desc", "A keyblade from a long forgotten age, it seems to resemble something familiar...");
        add("item." + MODID + "." + Strings.retribution + ".desc", "A carefully forged Keyblade, with the intention to refine combat.\nSubmitted by TopazTK & Televo.");

        //Organization Weapons
        addItem(ModItems.malice, "Malice");
        addItem(ModItems.sanction, "Sanction");
        addItem(ModItems.overlord, "Overlord");
        addItem(ModItems.veneration, "Veneration");
        addItem(ModItems.autocracy, "Autocracy");
        addItem(ModItems.conquest, "Conquest");
        addItem(ModItems.terminus, "Terminus");
        addItem(ModItems.judgement, "Judgement");
        addItem(ModItems.discipline, "Discipline");
        addItem(ModItems.aristocracy, "Aristocracy");
        addItem(ModItems.superiority, "Superiority");
        addItem(ModItems.aggression, "Aggression");
        addItem(ModItems.fury, "Fury");
        addItem(ModItems.despair, "Despair");
        addItem(ModItems.triumph, "Triumph");
        addItem(ModItems.ruination, "Ruination");
        addItem(ModItems.domination, "Domination");
        addItem(ModItems.annihilation, "Annihilation");
        addItem(ModItems.tyrant, "Tyrant");
        addItem(ModItems.magnificence, "Magnificence");
        addItem(ModItems.infinity, "Infinity");
        addItem(ModItems.interdiction, "Interdiction");
        addItem(ModItems.roundFan, "Round Fan");
        addItem(ModItems.absolute, "Absolute");

        addItem(ModItems.standalone, "Standalone");
        addItem(ModItems.killerbee, "Killerbee");
        addItem(ModItems.stingray, "Stingray");
        addItem(ModItems.counterweight, "Counterweight");
        addItem(ModItems.precision, "Precision");
        addItem(ModItems.dualHead, "Dual Head");
        addItem(ModItems.bahamut, "Bahamut");
        addItem(ModItems.gullwing, "Gullwing");
        addItem(ModItems.blueFrame, "Blue Frame");
        addItem(ModItems.starShell, "Star Shell");
        addItem(ModItems.sunrise, "Sunrise");
        addItem(ModItems.ignition, "Ignition");
        addItem(ModItems.armstrong, "Armstrong");
        addItem(ModItems.hardBoiledHeat, "Hard Boiled Heat");
        addItem(ModItems.diabloEye, "Diablo Eye");
        addItem(ModItems.doubleTap, "Double Tap");
        addItem(ModItems.stardust, "Stardust");
        addItem(ModItems.energyMuzzle, "Energy Muzzle");
        addItem(ModItems.crimeAndPunishment, "Crime And Punishment");
        addItem(ModItems.cupidsArrow, "Cupids Arrow");
        addItem(ModItems.finalWeapon, "Final Weapon");
        addItem(ModItems.sharpshooter, "Sharpshooter");
        addItem(ModItems.dryer, "Dryer");
        addItem(ModItems.trumpet, "Trumpet");

        addItem(ModItems.zephyr, "Zephyr");
        addItem(ModItems.moonglade, "Moonglade");
        addItem(ModItems.aer, "Aer");
        addItem(ModItems.nescience, "Nescience");
        addItem(ModItems.brume, "Brume");
        addItem(ModItems.asura, "Asura");
        addItem(ModItems.crux, "Crux");
        addItem(ModItems.paladin, "Paladin");
        addItem(ModItems.fellking, "Fellking");
        addItem(ModItems.nightcloud, "Nightcloud");
        addItem(ModItems.shimmer, "Shimmer");
        addItem(ModItems.vortex, "Vortex");
        addItem(ModItems.scission, "Scission");
        addItem(ModItems.heavenfall, "Heavenfall");
        addItem(ModItems.aether, "Aether");
        addItem(ModItems.mazzaroth, "Mazzaroth");
        addItem(ModItems.hegemon, "Hegemon");
        addItem(ModItems.foxfire, "Foxfire");
        addItem(ModItems.yaksha, "Yaksha");
        addItem(ModItems.cynosura, "Cynosura");
        addItem(ModItems.dragonreign, "Dragonreign");
        addItem(ModItems.lindworm, "Lindworm");
        addItem(ModItems.broom, "Broom");
        addItem(ModItems.wyvern, "Wyvern");

        addItem(ModItems.testerZero, "Tester Zero");
        addItem(ModItems.productOne, "Product One");
        addItem(ModItems.deepFreeze, "Deep Freeze");
        addItem(ModItems.cryoliteShield, "Cryolite Shield");
        addItem(ModItems.falseTheory, "False Theory");
        addItem(ModItems.glacier, "Glacier");
        addItem(ModItems.absoluteZero, "Absolute Zero");
        addItem(ModItems.gunz, "Gunz");
        addItem(ModItems.mindel, "Mindel");
        addItem(ModItems.snowslide, "Snowslide");
        addItem(ModItems.iceberg, "Iceberg");
        addItem(ModItems.inquisition, "Inquisition");
        addItem(ModItems.scrutiny, "Scrutiny");
        addItem(ModItems.empiricism, "Empiricism");
        addItem(ModItems.edification, "Edification");
        addItem(ModItems.contrivance, "Contrivance");
        addItem(ModItems.wurm, "Wurm");
        addItem(ModItems.subzero, "Subzero");
        addItem(ModItems.coldBlood, "Cold Blood");
        addItem(ModItems.diamondShield, "Diamond Shield");
        addItem(ModItems.aegis, "Aegis");
        addItem(ModItems.frozenPride, "Frozen Pride");
        addItem(ModItems.potLid, "Pot Lid");
        addItem(ModItems.snowman, "Snowman");

        addItem(ModItems.reticence, "Reticence");
        addItem(ModItems.goliath, "Goliath");
        addItem(ModItems.copperRed, "Copper Red");
        addItem(ModItems.daybreak, "Daybreak");
        addItem(ModItems.colossus, "Colossus");
        addItem(ModItems.ursaMajor, "Ursa Major");
        addItem(ModItems.megacosm, "Megacosm");
        addItem(ModItems.terrene, "Terrene");
        addItem(ModItems.fuligin, "Fuligin");
        addItem(ModItems.hardWinter, "Hard Winter");
        addItem(ModItems.firefly, "Firefly");
        addItem(ModItems.harbinger, "Harbinger");
        addItem(ModItems.redwood, "Redwood");
        addItem(ModItems.sequoia, "Sequoia");
        addItem(ModItems.ironBlack, "Iron Black");
        addItem(ModItems.earthshine, "Earthshine");
        addItem(ModItems.octiron, "Octiron");
        addItem(ModItems.hyperion, "Hyperion");
        addItem(ModItems.clarity, "Clarity");
        addItem(ModItems.oneThousandAndOneNights, "One Thousand And One Nights");
        addItem(ModItems.cardinalVirtue, "Cardinal Virtue");
        addItem(ModItems.skysplitter, "Skysplitter");
        addItem(ModItems.bleepBloopBop, "Bleep Bloop Bop");
        addItem(ModItems.monolith, "Monolith");

        addItem(ModItems.blackPrimer, "Black Primer");
        addItem(ModItems.whiteTome, "White Tome");
        addItem(ModItems.illicitResearch, "Illicit Research");
        addItem(ModItems.buriedSecrets, "Buried Secrets");
        addItem(ModItems.arcaneCompendium, "Arcane Compendium");
        addItem(ModItems.dissentersNotes, "Dissenters Notes");
        addItem(ModItems.nefariousCodex, "Nefarious Codex");
        addItem(ModItems.mysticAlbum, "Mystic Album");
        addItem(ModItems.cursedManual, "Cursed Manual");
        addItem(ModItems.tabooText, "Taboo Text");
        addItem(ModItems.eldritchEsoterica, "Eldritch Esoterica");
        addItem(ModItems.freakishBestiary, "Freakish Bestiary");
        addItem(ModItems.madmansVita, "Madmans Vita");
        addItem(ModItems.untitledWritings, "Untitled Writings");
        addItem(ModItems.abandonedDogma, "Abandoned Dogma");
        addItem(ModItems.atlasOfOmens, "Atlas Of Omens");
        addItem(ModItems.revoltingScrapbook, "Revolting Scrapbook");
        addItem(ModItems.lostHeterodoxy, "Lost Heterodoxy");
        addItem(ModItems.otherworldlyTales, "Otherworldly Tales");
        addItem(ModItems.indescribableLore, "Indescribable Lore");
        addItem(ModItems.radicalTreatise, "Radical Treatise");
        addItem(ModItems.bookOfRetribution, "Book Of Retribution");
        addItem(ModItems.midnightSnack, "Midnight Snack");
        addItem(ModItems.dearDiary, "Dear Diary");

        addItem(ModItems.newMoon, "New Moon");
        addItem(ModItems.werewolf, "Werewolf");
        addItem(ModItems.artemis, "Artemis");
        addItem(ModItems.luminary, "Luminary");
        addItem(ModItems.selene, "Selene");
        addItem(ModItems.moonrise, "Moonrise");
        addItem(ModItems.astrologia, "Astrologia");
        addItem(ModItems.crater, "Crater");
        addItem(ModItems.lunarPhase, "Lunar Phase");
        addItem(ModItems.crescent, "Crescent");
        addItem(ModItems.gibbous, "Gibbous");
        addItem(ModItems.berserker, "Berserker");
        addItem(ModItems.twilight, "Twilight");
        addItem(ModItems.queenOfTheNight, "Queen Of The Night");
        addItem(ModItems.balsamicMoon, "Balsamic Moon");
        addItem(ModItems.orbit, "Orbit");
        addItem(ModItems.lightYear, "Light Year");
        addItem(ModItems.kingOfTheNight, "King Of The Night");
        addItem(ModItems.moonset, "Moonset");
        addItem(ModItems.horoscope, "Horoscope");
        addItem(ModItems.dichotomy, "Dichotomy");
        addItem(ModItems.lunatic, "Lunatic");
        addItem(ModItems.justDesserts, "Just Desserts");
        addItem(ModItems.bunnymoon, "Bunnymoon");

        addItem(ModItems.ashes, "Ashes");
        addItem(ModItems.doldrums, "Doldrums");
        addItem(ModItems.delayedAction, "Delayed Action");
        addItem(ModItems.diveBombers, "Dive Bombers");
        addItem(ModItems.combustion, "Combustion");
        addItem(ModItems.moulinRouge, "Moulin Rouge");
        addItem(ModItems.blazeOfGlory, "Blaze Of Glory");
        addItem(ModItems.prometheus, "Prometheus");
        addItem(ModItems.ifrit, "Ifrit");
        addItem(ModItems.magmaOcean, "Magma Ocean");
        addItem(ModItems.volcanics, "Volcanics");
        addItem(ModItems.inferno, "Inferno");
        addItem(ModItems.sizzlingEdge, "Sizzling Edge");
        addItem(ModItems.corona, "Corona");
        addItem(ModItems.ferrisWheel, "Ferris Wheel");
        addItem(ModItems.burnout, "Burnout");
        addItem(ModItems.omegaTrinity, "Omega Trinity");
        addItem(ModItems.outbreak, "Outbreak");
        addItem(ModItems.doubleEdge, "Double Edge");
        addItem(ModItems.wildfire, "Wildfire");
        addItem(ModItems.prominence, "Prominence");
        addItem(ModItems.eternalFlames, "Eternal Flames");
        addItem(ModItems.pizzaCut, "Pizza Cut");
        addItem(ModItems.conformers, "Conformers");

        addItem(ModItems.basicModel, "Basic Model");
        addItem(ModItems.tuneUp, "Tune Up");
        addItem(ModItems.quartet, "Quartet");
        addItem(ModItems.quintet, "Quintet");
        addItem(ModItems.overture, "Overture");
        addItem(ModItems.oldHand, "Old Hand");
        addItem(ModItems.daCapo, "Da Capo");
        addItem(ModItems.powerChord, "Power Chord");
        addItem(ModItems.fermata, "Fermata");
        addItem(ModItems.interlude, "Interlude");
        addItem(ModItems.serenade, "Serenade");
        addItem(ModItems.songbird, "Songbird");
        addItem(ModItems.riseToFame, "Rise To Fame");
        addItem(ModItems.rockStar, "Rock Star");
        addItem(ModItems.eightFinger, "Eight Finger");
        addItem(ModItems.concerto, "Concerto");
        addItem(ModItems.harmonics, "Harmonics");
        addItem(ModItems.millionBucks, "Million Bucks");
        addItem(ModItems.fortissimo, "Fortissimo");
        addItem(ModItems.upToEleven, "Up To Eleven");
        addItem(ModItems.sanctuary, "Sanctuary");
        addItem(ModItems.arpeggio, "Arpeggio");
        addItem(ModItems.princeOfAwesome, "Prince Of Awesome");
        addItem(ModItems.afterSchool, "After School");

        addItem(ModItems.theFool, "The Fool");
        addItem(ModItems.theMagician, "The Magician");
        addItem(ModItems.theStar, "The Star");
        addItem(ModItems.theMoon, "The Moon");
        addItem(ModItems.justice, "Justice");
        addItem(ModItems.theHierophant, "The Hierophant");
        addItem(ModItems.theWorld, "The World");
        addItem(ModItems.temperance, "Temperance");
        addItem(ModItems.theHighPriestess, "The High Priestess");
        addItem(ModItems.theTower, "The Tower");
        addItem(ModItems.theHangedMan, "The Hanged Man");
        addItem(ModItems.death, "Death");
        addItem(ModItems.theHermit, "The Hermit");
        addItem(ModItems.strength, "Strength");
        addItem(ModItems.theLovers, "The Lovers");
        addItem(ModItems.theChariot, "The Chariot");
        addItem(ModItems.theSun, "The Sun");
        addItem(ModItems.theDevil, "The Devil");
        addItem(ModItems.theEmpress, "The Empress");
        addItem(ModItems.theEmperor, "The Emperor");
        addItem(ModItems.theJoker, "The Joker");
        addItem(ModItems.fairGame, "Fair Game");
        addItem(ModItems.finestFantasy13, "Finest Fantasy 13");
        addItem(ModItems.highRollersSecret, "High Rollers Secret");

        addItem(ModItems.fickleErica, "Fickle Erica");
        addItem(ModItems.jiltedAnemone, "Jilted Anemone");
        addItem(ModItems.proudAmaryllis, "Proud Amaryllis");
        addItem(ModItems.madSafflower, "Mad Safflower");
        addItem(ModItems.poorMelissa, "Poor Melissa");
        addItem(ModItems.tragicAllium, "Tragic Allium");
        addItem(ModItems.mournfulCineria, "Mournful Cineria");
        addItem(ModItems.pseudoSilene, "Pseudo Silene");
        addItem(ModItems.faithlessDigitalis, "Faithless Digitalis");
        addItem(ModItems.grimMuscari, "Grim Muscari");
        addItem(ModItems.docileVallota, "Docile Vallota");
        addItem(ModItems.quietBelladonna, "Quiet Belladonna");
        addItem(ModItems.partingIpheion, "Parting Ipheion");
        addItem(ModItems.loftyGerbera, "Lofty Gerbera");
        addItem(ModItems.gallantAchillea, "Gallant Achillea");
        addItem(ModItems.noblePeony, "Noble Peony");
        addItem(ModItems.fearsomeAnise, "Fearsome Anise");
        addItem(ModItems.vindictiveThistle, "Vindictive Thistle");
        addItem(ModItems.fairHelianthus, "Fair Helianthus");
        addItem(ModItems.solemnMagnolia, "Solemn Magnolia");
        addItem(ModItems.hallowedLotus, "Hallowed Lotus");
        addItem(ModItems.gracefulDahlia, "Graceful Dahlia");
        addItem(ModItems.stirringLadle, "Stirring Ladle");
        addItem(ModItems.daintyBellflowers, "Dainty Bellflowers");

        addItem(ModItems.trancheuse, "Trancheuse");
        addItem(ModItems.orage, "Orage");
        addItem(ModItems.tourbillon, "Tourbillon");
        addItem(ModItems.tempete, "Tempete");
        addItem(ModItems.carmin, "Carmin");
        addItem(ModItems.meteore, "Meteore");
        addItem(ModItems.etoile, "Etoile");
        addItem(ModItems.irregulier, "Irregulier");
        addItem(ModItems.dissonance, "Dissonance");
        addItem(ModItems.eruption, "Eruption");
        addItem(ModItems.soleilCouchant, "Soleil Couchant");
        addItem(ModItems.indigo, "Indigo");
        addItem(ModItems.vague, "Vague");
        addItem(ModItems.deluge, "Deluge");
        addItem(ModItems.rafale, "Rafale");
        addItem(ModItems.typhon, "Typhon");
        addItem(ModItems.extirpeur, "Extirpeur");
        addItem(ModItems.croixDuSud, "Croix Du Sud");
        addItem(ModItems.lumineuse, "Lumineuse");
        addItem(ModItems.clairDeLune, "Clair De Lune");
        addItem(ModItems.volDeNuit, "Vol De Nuit");
        addItem(ModItems.foudre, "Foudre");
        addItem(ModItems.demoiselle, "Demoiselle");
        addItem(ModItems.ampoule, "Ampoule");

        //Rings
        addItem(ModItems.abilityRing, "Ability Ring");
        addItem(ModItems.aquamarineRing, "Aquamarine Ring");
        addItem(ModItems.cosmicArts, "Cosmic Arts");
        addItem(ModItems.fullBloom, "Full Bloom");
        addItem(ModItems.fullBloomPlus, "Full Bloom+");
        addItem(ModItems.shadowArchive, "Shadow Archive");
        addItem(ModItems.shadowArchivePlus, "Shadow Archive+");
        addItem(ModItems.drawRing, "Draw Ring");
        addItem(ModItems.executiveRing, "Executive Ring");
        addItem(ModItems.starCharm, "Star Charm");
        addItem(ModItems.luckyRing, "Lucky Ring");
        
        addItem(ModItems.fireBangle, "Fire Bangle");
        addItem(ModItems.blizzardArmlet, "Blizzard Armlet");
        addItem(ModItems.thunderTrinket, "Thunder Trinket");
        addItem(ModItems.petiteRibbon, "Petite Ribbon");
        addItem(ModItems.ribbon, "Ribbon");
        addItem(ModItems.grandRibbon, "Grand Ribbon");

        addItem(ModItems.abasChain, "Abas Chain");
        addItem(ModItems.acrisius, "Acrisius");
        addItem(ModItems.acrisiusPlus, "Acrisius+");
        addItem(ModItems.aegisChain, "Aegis Chain");
        addItem(ModItems.blizzaraArmlet, "Blizzara Armlet");
        addItem(ModItems.blizzagaArmlet, "Blizzaga Armlet");
        addItem(ModItems.blizzagunArmlet, "Blizzagun Armlet");
        addItem(ModItems.powerBand, "Power Band");
        addItem(ModItems.busterBand, "Buster Band");
        addItem(ModItems.championBelt, "Champion Belt");
        addItem(ModItems.chaosAnklet, "Chaos Anklet");
        addItem(ModItems.cosmicBelt, "Cosmic Belt");
        addItem(ModItems.cosmicChain, "Cosmic Chain");
        addItem(ModItems.darkAnklet, "Dark Anklet");
        addItem(ModItems.divineBandanna, "Divine Bandanna");
        addItem(ModItems.elvenBandanna, "Elven Bandanna");
        addItem(ModItems.firaBangle, "Fira Bangle");
        addItem(ModItems.firagaBangle, "Firaga Bangle");
        addItem(ModItems.firagunBangle, "Firagun Bangle");
        addItem(ModItems.protectBelt, "Protect Belt");
        addItem(ModItems.gaiaBelt, "Gaia Belt");
        //addItem(ModItems.lightDefTest, "Light Res. Test");
        addItem(ModItems.midnightAnklet, "Midnight Anklet");
        addItem(ModItems.shadowAnklet, "Shadow Anklet");
        addItem(ModItems.shockCharm, "Shock Charm");
        addItem(ModItems.shockCharmPlus, "Shock Charm+");
        addItem(ModItems.thundaraTrinket, "Thundara Trinket");
        addItem(ModItems.thundagaTrinket, "Thundaga Trinket");
        addItem(ModItems.thundagunTrinket, "Thundagun Trinket");

        addItem(ModItems.engineersRing, "Engineer's Ring");
        addItem(ModItems.techniciansRing , "Technician's Ring");
        addItem(ModItems.skillRing , "Skill Ring");
        addItem(ModItems.skillfulRing , "Skillful Ring");
        addItem(ModItems.expertsRing , "Expert's Ring");
        addItem(ModItems.mastersRing , "Master's Ring");
        addItem(ModItems.cosmicRing , "Cosmic Ring");
        addItem(ModItems.sardonyxRing , "Sardonyx Ring");
        addItem(ModItems.goldRing , "Gold Ring");
        addItem(ModItems.garnetRing , "Garnet Ring");
        addItem(ModItems.diamondRing , "Diamond Ring");
        addItem(ModItems.silverRing , "Silver Ring");
        addItem(ModItems.tourmalineRing , "Tourmaline Ring");
        addItem(ModItems.platinumRing , "Platinum Ring");
        addItem(ModItems.mythrilRing , "Mythril Ring");
        addItem(ModItems.orichalcumRing , "Orichalcum Ring");
        addItem(ModItems.medal , "Medal");
        addItem(ModItems.soldierEarring , "Soldier Earring");
        addItem(ModItems.mageEarring , "Mage Earring");
        addItem(ModItems.moonAmulet , "Moon Amulet");
        addItem(ModItems.slayerEarring , "Slayer Earring");
        addItem(ModItems.fencerEarring , "Fencer Earring");

        //Spawn Eggs
        addItem(ModEntities.MOOGLE_EGG, "Moogle Spawn Egg");
        addItem(ModEntities.SHADOW_EGG, "Shadow Spawn Egg");
        addItem(ModEntities.MEGA_SHADOW_EGG, "Mega-Shadow Spawn Egg");
        addItem(ModEntities.GIGA_SHADOW_EGG, "Gigas Shadow Spawn Egg");
        addItem(ModEntities.DARKBALL_EGG, "Darkball Spawn Egg");
        addItem(ModEntities.SHADOW_GLOB_EGG, "Shadow Glob Spawn Egg");

        addItem(ModEntities.MINUTE_BOMB_EGG, "Minute Bomb Spawn Egg");
        addItem(ModEntities.SKATER_BOMB_EGG, "Skater Bomb Spawn Egg");
        addItem(ModEntities.STORM_BOMB_EGG, "Storm Bomb Spawn Egg");
        addItem(ModEntities.DETONATOR_EGG, "Detonator Spawn Egg");

        addItem(ModEntities.RED_NOCTURNE_EGG, "Red Nocturne Spawn Egg");
        addItem(ModEntities.BLUE_RHAPSODY_EGG, "Blue Rhapsody Spawn Egg");
        addItem(ModEntities.YELLOW_OPERA_EGG, "Yellow Opera Spawn Egg");
        addItem(ModEntities.GREEN_REQUIEM_EGG, "Green Requiem Spawn Egg");
        addItem(ModEntities.EMERALD_BLUES_EGG, "Emerald Blues Spawn Egg");
        addItem(ModEntities.LARGE_BODY_EGG, "Large Body Spawn Egg");
        addItem(ModEntities.DIRE_PLANT_EGG, "Dire Plant Spawn Egg");
        addItem(ModEntities.SOLDIER_EGG, "Soldier Spawn Egg");
        addItem(ModEntities.WHITE_MUSHROOM_EGG, "White Mushroom Spawn Egg");
        addItem(ModEntities.BLACK_FUNGUS_EGG, "Black Fungus Spawn Egg");

        addItem(ModEntities.NOBODY_CREEPER_EGG, "Creeper (Nobody) Spawn Egg");
        addItem(ModEntities.DUSK_EGG, "Dusk Spawn Egg");
        addItem(ModEntities.ASSASSIN_EGG, "Assassin Spawn Egg");
        addItem(ModEntities.DRAGOON_EGG, "Dragoon Spawn Egg");
        addItem(ModEntities.MARLUXIA_EGG, "Marluxia Spawn Egg");
        

        //Armour
        add("gui.summonarmor.notenoughspace", "You don't have enough room in your inventory");
        
        addItem(ModItems.organizationRobe_Helmet, "Organization Hood");
        addItem(ModItems.organizationRobe_Chestplate, "Organization Coat");
        addItem(ModItems.organizationRobe_Leggings, "Organization Leggings");
        addItem(ModItems.organizationRobe_Boots, "Organization Boots");
        
        addItem(ModItems.terra_Helmet, "Terra Helmet");
        addItem(ModItems.terra_Chestplate, "Terra Chestplate");
        addItem(ModItems.terra_Leggings, "Terra Leggings");
        addItem(ModItems.terra_Boots, "Terra Boots");
        addItem(ModItems.terra_Shoulder, "Terra's Pauldron");

        addItem(ModItems.aqua_Helmet, "Aqua Helmet");
        addItem(ModItems.aqua_Chestplate, "Aqua Chestplate");
        addItem(ModItems.aqua_Leggings, "Aqua Leggings");
        addItem(ModItems.aqua_Boots, "Aqua Boots");
        addItem(ModItems.aqua_Shoulder, "Aqua's Pauldron");

        addItem(ModItems.ventus_Helmet, "Ventus Helmet");
        addItem(ModItems.ventus_Chestplate, "Ventus Chestplate");
        addItem(ModItems.ventus_Leggings, "Ventus Leggings");
        addItem(ModItems.ventus_Boots, "Ventus Boots");
        addItem(ModItems.ventus_Shoulder, "Ventus' Pauldron");

        addItem(ModItems.nightmareVentus_Helmet, "Nightmare Ventus Helmet");
        addItem(ModItems.nightmareVentus_Chestplate, "Nightmare Ventus Chestplate");
        addItem(ModItems.nightmareVentus_Leggings, "Nightmare Ventus Leggings");
        addItem(ModItems.nightmareVentus_Boots, "Nightmare Ventus Boots");
        addItem(ModItems.nightmareVentus_Shoulder, "Nightmare Ventus' Pauldron");

        addItem(ModItems.eraqus_Helmet, "Eraqus Helmet");
        addItem(ModItems.eraqus_Chestplate, "Eraqus Chestplate");
        addItem(ModItems.eraqus_Leggings, "Eraqus Leggings");
        addItem(ModItems.eraqus_Boots, "Eraqus Boots");
        addItem(ModItems.eraqus_Shoulder, "Eraqus' Pauldron");

        addItem(ModItems.xehanort_Helmet, "Xehanort Helmet");
        addItem(ModItems.xehanort_Chestplate, "Xehanort Chestplate");
        addItem(ModItems.xehanort_Leggings, "Xehanort Leggings");
        addItem(ModItems.xehanort_Boots, "Xehanort Boots");
        addItem(ModItems.xehanort_Shoulder, "Xehanort' Pauldron");

        addItem(ModItems.ux_Helmet, "UX Armor Helmet");
        addItem(ModItems.ux_Chestplate, "UX Armor Chestplate");
        addItem(ModItems.ux_Leggings, "UX Armor Leggings");
        addItem(ModItems.ux_Boots, "UX Armor Boots");
        addItem(ModItems.ux_Shoulder, "UX Armor's Pauldron");

        addItem(ModItems.vanitas_Helmet, "Vanitas Helmet");
        addItem(ModItems.vanitas_Chestplate, "Vanitas Chestplate");
        addItem(ModItems.vanitas_Leggings, "Vanitas Leggings");
        addItem(ModItems.vanitas_Boots, "Vanitas Boots");
        
        addItem(ModItems.vanitas_Remnant_Helmet, "Vanitas Remnant Helmet");
        addItem(ModItems.vanitas_Remnant_Chestplate, "Vanitas Remnant Chestplate");
        addItem(ModItems.vanitas_Remnant_Leggings, "Vanitas Remnant Leggings");
        addItem(ModItems.vanitas_Remnant_Boots, "Vanitas Remnant Boots");

        addItem(ModItems.antiCoat_Helmet, "Anticoat Hood");
        addItem(ModItems.antiCoat_Chestplate, "Anticoat Coat");
        addItem(ModItems.antiCoat_Leggings, "Anticoat Leggings");
        addItem(ModItems.antiCoat_Boots, "Anticoat Boots");

        addItem(ModItems.xemnas_Helmet, "Xemnas Hood");
        addItem(ModItems.xemnas_Chestplate, "Xemnas Coat");
        addItem(ModItems.xemnas_Leggings, "Xemnas Leggings");
        addItem(ModItems.xemnas_Boots, "Xemnas Boots");

        addItem(ModItems.dark_Riku_Chestplate, "Dark Riku Chestplate");
        addItem(ModItems.dark_Riku_Leggings, "Dark Riku Leggings");
        addItem(ModItems.dark_Riku_Boots, "Dark Riku Boots");

        addItem(ModItems.aced_Helmet, "Aced Hood");
        addItem(ModItems.aced_Chestplate, "Aced Coat");
        addItem(ModItems.aced_Leggings, "Aced Leggings");
        addItem(ModItems.aced_Boots, "Aced Boots");

        addItem(ModItems.ava_Helmet, "Ava Hood");
        addItem(ModItems.ava_Chestplate, "Ava Coat");
        addItem(ModItems.ava_Leggings, "Ava Leggings");
        addItem(ModItems.ava_Boots, "Ava Boots");

        addItem(ModItems.gula_Helmet, "Gula Hood");
        addItem(ModItems.gula_Chestplate, "Gula Coat");
        addItem(ModItems.gula_Leggings, "Gula Leggings");
        addItem(ModItems.gula_Boots, "Gula Boots");

        addItem(ModItems.invi_Helmet, "Invi Hood");
        addItem(ModItems.invi_Chestplate, "Invi Coat");
        addItem(ModItems.invi_Leggings, "Invi Leggings");
        addItem(ModItems.invi_Boots, "Invi Boots");

        addItem(ModItems.ira_Helmet, "Ira Hood");
        addItem(ModItems.ira_Chestplate, "Ira Coat");
        addItem(ModItems.ira_Leggings, "Ira Leggings");
        addItem(ModItems.ira_Boots, "Ira Boots");

        //Discs
        add("disc.duration.desc", "Duration");
        add("disc.durationunits.desc", "(mins:secs)");
        addMusicDisc(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future, "Yoko Shimomura & Kaoru Wada - Birth by Sleep -A Link to the Future-");
        addMusicDisc(ModItems.disc_Dream_Drop_Distance_The_Next_Awakening, "Yoko Shimomura & Kaoru Wada - Dream Drop Distance -The Next Awakening-");
        addMusicDisc(ModItems.disc_Hikari_KINGDOM_Instrumental_Version, "Yoko Shimomura & Kaoru Wada - Hikari -KINGDOM Instrumental Version-");
        addMusicDisc(ModItems.disc_L_Oscurita_Dell_Ignoto, "Yoko Shimomura - L'Oscurita Dell'Ignoto");
        addMusicDisc(ModItems.disc_Musique_pour_la_tristesse_de_Xion, "Yoko Shimomura - Musique pour la tristesse de Xion");
        addMusicDisc(ModItems.disc_No_More_Bugs_Bug_Version, "Yoko Shimomura & Hirosato Noda - No More Bugs!! -Bug Version-");
        addMusicDisc(ModItems.disc_Organization_XIII, "Yoko Shimomura - Organization XIII");
        addMusicDisc(ModItems.disc_Dearly_Beloved_UX, "Yoko Shimomura - Dearly Beloved -Union \u03c7 Credits Version-");
        addMusicDisc(ModItems.disc_Passion_Instrumental, "Yoko Shimomura - Passion -Kingdom Orchestra Instrumental Version-");
        addMusicDisc(ModItems.disc_Rage_Awakened, "Yoko Shimomura - Rage Awakened");
        addMusicDisc(ModItems.disc_The_Other_Promise, "Yoko Shimomura - The Other Promise");
        addMusicDisc(ModItems.disc_13th_Struggle_Luxord, "Yuko Komiyama - 13th Struggle -Luxord-");
        addMusicDisc(ModItems.disc_13th_Dilemma_Saix, "Yuki Kishida - 13th Dilemma -Saix-");
        addMusicDisc(ModItems.disc_13th_Reflection, "Yoko Shimomura - 13th Reflection");
        addMusicDisc(ModItems.disc_Another_Side_Battle_Ver, "Yoko Shimomura - Another Side -Battle Ver-");
        addMusicDisc(ModItems.disc_Cavern_Of_Remembrance_Days, "Yoko Shimomura - Cavern of Remembrance -Days Version-");
        addMusicDisc(ModItems.disc_Forgotten_Challenge_Recoded, "Yoko Shimomura - Forgotten Challenge -Re:Coded Version-");
        addMusicDisc(ModItems.disc_Anger_Unchained, "Takeharu Ishimoto - Anger Unchained");
        addMusicDisc(ModItems.disc_Hunter_Of_The_Dark, "Yoko Shimomura - Hunter of the Dark");
        addMusicDisc(ModItems.disc_Destati, "Yoko Shimomura - Destati");        

        //Command Menu Items
        addItem(ModItems.potion, "Potion");
        addItem(ModItems.hiPotion, "Hi-Potion");
        addItem(ModItems.megaPotion, "Mega-Potion");
        addItem(ModItems.ether, "Ether");
        addItem(ModItems.hiEther, "Hi-Ether");
        addItem(ModItems.megaEther, "Mega-Ether");
        addItem(ModItems.elixir, "Elixir");
        addItem(ModItems.megaLixir, "Megalixir");
        addItem(ModItems.driveRecovery, "Drive Recovery");
        addItem(ModItems.hiDriveRecovery, "High Drive Recovery");
        addItem(ModItems.refocuser, "Refocuser");
        addItem(ModItems.hiRefocuser, "Hi-Refocuser");
        addItem(ModItems.apBoost, "AP Boost");
        addItem(ModItems.powerBoost, "Power Boost");
        addItem(ModItems.magicBoost, "Magic Boost");
        addItem(ModItems.defenseBoost, "Defense Boost");

        add("potion.desc.hp", "\u00A7aHP\u00A7r");
        add("potion.desc.mp", "\u00A79MP\u00A7r");
        add("potion.desc.hpmp", "\u00A7aHP\u00A7r and \u00A79MP\u00A7r");
        add("potion.desc.drive", "\u00A7eDrive\u00A7r");
        add("potion.desc.focus", "\u00A76Focus\u00A7r");
        add("potion.desc.beginning", "Will restore %s%s %s ");
        add("potion.desc.toall", "to all your party members in range");
        add("potion.desc.toone", "to the chosen party member");

        //Orbs
        addItem(ModItems.fireSpell, "Fire Spell");
        addItem(ModItems.blizzardSpell, "Blizzard Spell");
        addItem(ModItems.waterSpell, "Water Spell");
        addItem(ModItems.thunderSpell, "Thunder Spell");
        addItem(ModItems.cureSpell, "Cure Spell");
        addItem(ModItems.aeroSpell, "Aero Spell");
        addItem(ModItems.magnetSpell, "Magnet Spell");
        addItem(ModItems.reflectSpell, "Reflect Spell");
        addItem(ModItems.gravitySpell, "Gravity Spell");
        addItem(ModItems.stopSpell, "Stop Spell");

        addItem(ModItems.valorOrb, "Valor Form Orb");
        addItem(ModItems.wisdomOrb, "Wisdom Form Orb");
        addItem(ModItems.limitOrb, "Limit Form Orb");
        addItem(ModItems.masterOrb, "Master Form Orb");
        addItem(ModItems.finalOrb, "Final Form Orb");

        //Other
        addItem(ModItems.recipe, "Recipe");
        addItem(ModItems.recipeD, "Tier D Recipe");
        addItem(ModItems.recipeC, "Tier C Recipe");
        addItem(ModItems.recipeB, "Tier B Recipe");
        addItem(ModItems.recipeA, "Tier A Recipe");
        addItem(ModItems.recipeS, "Tier S Recipe");
        addItem(ModItems.recipeSS, "Tier SS Recipe");
        addItem(ModItems.recipeSSS, "Tier SSS Recipe");
        addItem(ModItems.iceCream, "Sea Salt Ice Cream");
        addItem(ModItems.winnerStick, "Winner Stick");
        addItem(ModItems.synthesisBag, "Synthesis Bag");
        addItem(ModItems.proofOfHeart, "Proof of Heart");
        addItem(ModItems.wayfinder, "Wayfinder");

        add("patchouli.kingdomkeys.journal.name", "Jiminy's Journal");
        add("patchouli.kingdomkeys.journal.desc", "Thank Naminé.");
        add("patchouli.kingdomkeys.journal.hello", "Welcome, Keyblade wielder to the $(thing)Kingdom Keys$() Mod.$(p)In this book, you'll find all info you need to get started, as well as helpful tips.");

        /**Entities**/
        addEntityType(ModEntities.TYPE_BLAST_BLOX,"Primed Blast Blox");
        addEntityType(ModEntities.TYPE_PAIR_BLOX, "Pair Blox");

        addEntityType(ModEntities.TYPE_BLIZZARD, "Blizzard");
        addEntityType(ModEntities.TYPE_FIRE, "Fire");
        addEntityType(ModEntities.TYPE_THUNDER, "Thunder");
        addEntityType(ModEntities.TYPE_THUNDERBOLT, "Thunderbolt");
        addEntityType(ModEntities.TYPE_GRAVITY, "Gravity");
        addEntityType(ModEntities.TYPE_MAGNET, "Magnet");
        addEntityType(ModEntities.TYPE_WATER, "Water");
        addEntityType(ModEntities.TYPE_KK_THROWABLE, "Chakram");
        addEntityType(ModEntities.TYPE_ORG_PORTAL, "Organization Portal");
        addEntityType(ModEntities.TYPE_HPORB, "HP Orb");
        addEntityType(ModEntities.TYPE_MPORB, "MP Orb");
        addEntityType(ModEntities.TYPE_DRIVEORB, "DP Orb");
        addEntityType(ModEntities.TYPE_MUNNY, "Munny");
        
        addEntityType(ModEntities.TYPE_SPAWNING_ORB, "Spawning Orb");

        addEntityType(ModEntities.TYPE_MOOGLE, "Moogle");
        addEntityType(ModEntities.TYPE_SHADOW, "Shadow");
        addEntityType(ModEntities.TYPE_MEGA_SHADOW, "Mega-Shadow");
        addEntityType(ModEntities.TYPE_GIGA_SHADOW, "Gigas Shadow");
        addEntityType(ModEntities.TYPE_DARKBALL, "Darkball");
        addEntityType(ModEntities.TYPE_SHADOW_GLOB, "Shadow Glob");

        addEntityType(ModEntities.TYPE_MINUTE_BOMB, "Minute Bomb");
        addEntityType(ModEntities.TYPE_SKATER_BOMB, "Skater Bomb");
        addEntityType(ModEntities.TYPE_STORM_BOMB, "Storm Bomb");
        addEntityType(ModEntities.TYPE_DETONATOR, "Detonator");

        addEntityType(ModEntities.TYPE_RED_NOCTURNE, "Red Nocturne");
        addEntityType(ModEntities.TYPE_BLUE_RHAPSODY, "Blue Rhapsody");
        addEntityType(ModEntities.TYPE_YELLOW_OPERA, "Yellow Opera");
        addEntityType(ModEntities.TYPE_GREEN_REQUIEM, "Green Requiem");
        addEntityType(ModEntities.TYPE_EMERALD_BLUES, "Emerald Blues");
        addEntityType(ModEntities.TYPE_LARGE_BODY, "Large Body");
        addEntityType(ModEntities.TYPE_DIRE_PLANT, "Dire Plant");
        addEntityType(ModEntities.TYPE_SOLDIER, "Soldier");
        addEntityType(ModEntities.TYPE_WHITE_MUSHROOM, "White Mushroom");
        addEntityType(ModEntities.TYPE_BLACK_FUNGUS, "Black Fungus");

        addEntityType(ModEntities.TYPE_NOBODY_CREEPER, "Creeper (Nobody)");
        addEntityType(ModEntities.TYPE_DUSK, "Dusk");
        addEntityType(ModEntities.TYPE_ASSASSIN, "Assassin");
        addEntityType(ModEntities.TYPE_DRAGOON, "Dragoon");
        addEntityType(ModEntities.TYPE_MARLUXIA, "Marluxia");

        /**Biomes**/
        add("biome.kingdomkeys.dive_to_the_heart", "Dive to the Heart");
        add("biome.kingdomkeys.realm_of_darkness", "Realm of Darkness");
        add("biome.kingdomkeys.station_of_sorrow", "Station of Sorrow");
        add("biome.kingdomkeys.castle_oblivion", "Castle Oblivion");
        add("biome.kingdomkeys.castle_oblivion_interior", "Castle Oblivion Interior");

        /**JEI**/
        add("jei.category.kingdomkeys.synthesis", "Item Synthesis");
        add("jei.category.kingdomkeys.keyblade_summon", "Keyblade Summoning");
        add("jei.category.kingdomkeys.synthesis.locked", "Recipe not unlocked");
        add("jei.category.kingdomkeys.synthesis.unlocked", "Recipe unlocked");
        add("jei.category.kingdomkeys.keyblade_summon.info", "View info for how-to");
        add("jei.info.kingdomkeys.moogle_projector", "Obtained from Moogles when killed by an Anvil. Used for Item Synthesis and upgrading Keyblades via the Keyblade Forge and depositing materials used for Synthesis. Moogles also serve the same purpose as this.");
        add("jei.info.kingdomkeys.organization_weapons", "As an Organization member you can unlock weapons within the equipment menu by spending hearts gained from kills, you will earn 2x hearts from using a weapon from your chosen member. Summon the weapons using the summon key.");
        add("jei.info.kingdomkeys.organization_robes", "Wear the full Organization set to join and select a member to start with, no matter who you choose you can unlock every member's weapons however it requires unlocking the adjacent member's weapon first.");
        add("jei.info.kingdomkeys.proof_of_heart", "Obtained from defeating the Ender Dragon, use this to leave the Organization.");
        add("jei.info.kingdomkeys.keychains", "Keychains can be used to summon the associated Keyblade by equipping the keychain through the Kingdom Keys menu, use the summon key to summon the Keyblade.");
        add("jei.info.kingdomkeys.recipes", "Dropped from mobs and found in Moogle house chests in villages. Use these to unlock recipes for Item Synthesis.");
        add("jei.info.kingdomkeys.ghost_blox", "Apply a redstone signal to toggle the visibility of the Ghost Blox and all adjacent Ghost Blox, while in the invisible state they have no collision.");
        add("jei.info.kingdomkeys.danger_blox", "Similar to a Cactus but deals more damage, causes damage on contact and also when hit. Unlike a Cactus it has no placement limits or growth. Wear boots to avoid damage while walking on them.");
        add("jei.info.kingdomkeys.blast_blox", "TNT-like with more destructive power, triggers on contact with anything but a feather in your hand.");
        add("jei.info.kingdomkeys.bounce_blox", "Entities that step on this block will bounce, sneak to land on the block without bouncing.");
        add("jei.info.kingdomkeys.magnet_blox", "Pulls or pushes entities in the direction its facing. Apply a redstone signal to activate, right click to change the range and sneak-right click with your fist to toggle attract and repel mode.");
        add("jei.info.kingdomkeys.spell_orb", "Use to unlock the specified spell. Once unlocked magic can be used with the Command Menu as long as you have enough MP, until level 5 you will have 0 MP. Dropped from breaking Prize Blox.");
        add("jei.info.kingdomkeys.valor_orb", "Use to unlock Valor Form. Valor has a 2nd Keyblade slot. Activation requires 3 bars of the Drive Gauge. Dropped from breaking Rare Prize Blox.");
        add("jei.info.kingdomkeys.wisdom_orb", "Use to unlock Wisdom Form. Activation requires 3 bars of the Drive Gauge. Dropped from breaking Rare Prize Blox.");
        add("jei.info.kingdomkeys.limit_orb", "Use to unlock Limit Form. Activation requires 4 bars of the Drive Gauge. Dropped from breaking Rare Prize Blox.");
        add("jei.info.kingdomkeys.master_orb", "Use to unlock Master Form. Master has a 2nd Keyblade slot. Activation requires 4 bars of the Drive Gauge. Dropped from breaking Rare Prize Blox.");
        add("jei.info.kingdomkeys.final_orb", "Use to unlock Final Form. Final has a 2nd Keyblade slot. Activation requires 5 bars of the Drive Gauge. Dropped from breaking Rare Prize Blox.");

        /**Others**/
        //Messages
        add("message.magnet_blox.attract", "Attract Mode");
        add("message.magnet_blox.repel", "Repel Mode");
        add("message.magnet_blox.range", "Range is now: %s");
        add("message.form_unlocked", "Unlocked %s form");
        add("message.chest.lock", "Use a keyblade to lock this chest");
        add("message.chest.can_be_locked", "Can be locked with a keyblade");
        add("message.chest.locked", "This chest is locked");
        add("message.chest.keyblade_set", "Your keyblade has been set to unlock this chest");
        add("message.chest.unlocked", "Chest has been unlocked");
        add("message.kingdomkeys.gui_toggle", "GUI display set to: %s");
        add("message.wayfinder.your_wayfinder","This is your Wayfinder, hand it over to someone else");
        add("message.wayfinder.in_your_party","in your party");
        add("message.wayfinder.not_in_party","You are not in a party");
        add("message.wayfinder.player_not_in_party","Player %s is not in your party");
        add("message.wayfinder.player_not_online","Player %s is not online");
        add("message.wayfinder.owner","Owner: %s");
        add("message.wayfinder.cooldown","Cooldown: %s%%");
        add("message.unlocked","Unlocked %s");
        add("message.magic_upgrade","%s has been upgraded to %s");
        add("message.magic_max_level","%s is already at the max level");
        add("message.recipe.no_more_to_learn","No more recipes to learn");
        add("message.recipe.cant_learn_yet","You can't learn that recipe yet");
        add("message.recipe.already_learnt", "Recipe for %s already learnt");
        add("message.recipe.learnt", "Learnt recipe for %s");

        //Station of Awakening
        add("soa.menu.1", "Before you can open the menu.");
        add("soa.menu.2", "You must make a choice.");
        add("soa.menu.ok", "Ok.");
        add("soa.menu.cancel", "Cancel.");
        add("soa.warrior.1", "The power of the warrior.");
        add("soa.warrior.2", "Invincible courage.");
        add("soa.warrior.3", "A sword of terrible destruction.");
        add("soa.guardian.1", "The power of the guardian.");
        add("soa.guardian.2", "Kindness to aid friends.");
        add("soa.guardian.3", "A shield to repell all.");
        add("soa.mystic.1", "The power of the mystic.");
        add("soa.mystic.2", "Inner strength.");
        add("soa.mystic.3", "A staff of wonder and ruin.");
        add("soa.choice.confirm", "Is this the power you seek?");
        add("soa.sacrifice.confirm", "You give up this power?");
        add("soa.ok", "Yes.");
        add("soa.cancel", "No.");
        add("soa.confirm.cancel", "Maybe not.");
        add("soa.title",  "Station of Awakening");
        add("soa.subtitle", "Dive to the Heart");
        add("soa.choice.intro.1", "Power sleeps within you.");
        add("soa.choice.intro.2", "If you give it form...");
        add("soa.choice.intro.3", "It will give you strength.");
        add("soa.choice.intro.4", "Choose well.");
        add("soa.sacrifice.intro.1", "Your path is set.");
        add("soa.sacrifice.intro.2", "Now, what will you give up in exchange?");
        add("soa.reset.intro.1", "Choose carefully.");
        add("soa.reset.intro.2", "What form will your power take?");
        add("soa.confirm.1", "You've chosen the power");
        add("soa.confirm.warrior", "of the Warrior.");
        add("soa.confirm.guardian", "of the Guardian.");
        add("soa.confirm.mystic", "of the Mystic.");
        add("soa.confirm.3", "You've given the power");
        add("soa.confirm.5", "Is this the form you choose?");

        //Heartless intro
        add(Strings.HeartlessIntro1,"This world has been connected");
        add(Strings.HeartlessIntro2,"Tied to the darkness...");
        add(Strings.HeartlessIntro3,"Soon to be completely eclipsed");
        
        //Controls
        add("key.categories.kingdomkeys", "Kingdom Keys");
        add(InputHandler.Keybinds.ACTION, "Action key");
        add(InputHandler.Keybinds.BACK, "Command menu back");
        add(InputHandler.Keybinds.ENTER, "Command menu enter");
        add(InputHandler.Keybinds.SCROLL_ACTIVATOR, "Command menu mouse controller key");
        add(InputHandler.Keybinds.SCROLL_UP, "Command menu up");
        add(InputHandler.Keybinds.SCROLL_DOWN, "Command menu down");
        add(InputHandler.Keybinds.SUMMON_KEYBLADE, "Summon weapon");
        add(InputHandler.Keybinds.SUMMON_ARMOR, "Summon armor");
        add(InputHandler.Keybinds.REACTION_COMMAND, "Reaction Command");
        add(InputHandler.Keybinds.LOCK_ON, "Lock-on");
        add(InputHandler.Keybinds.OPENMENU, "Open Menu");
        add(InputHandler.Keybinds.SHOW_GUI, "Toggle HUD");

        //Groups
        add("itemGroup.kingdomkeys_misc", "Kingdom Keys: Misc");
        add("itemGroup.kingdomkeys_equipables", "Kingdom Keys: Equipables");
        add("itemGroup.kingdomkeys_keyblades", "Kingdom Keys: Keyblades");
        add("itemGroup.kingdomkeys_org_weapons", "Kingdom Keys: Organization");

        //Death Messages
        add("keybladedamage.death", "%s was slain by %s");

    }
}
