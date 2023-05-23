package online.kingdomkeys.kingdomkeys.client.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KingdomKeys.MODID);

    public static final RegistryObject<SoundEvent>
    		alarm = registerSound("alarm"),
    		antidrive = registerSound("antidrive"),
    		drive = registerSound("drive"),
    	    error = registerSound("error"),
            //ether
    	    //hi-potion
    	    hp_orb = registerSound("hp_orb"),
    	    itemget = registerSound("itemget"),
    	    kupo = registerSound("kupo"),
            kupoliving = registerSound("kupoliving"),
            levelup = registerSound("levelup"),
            lockon = registerSound("lockon"),
            menu_back = registerSound("menu_back"),
            menu_in = registerSound("menu_in"),
            menu_move = registerSound("menu_move"),
            menu_select = registerSound("menu_select"),
            mp_orb = registerSound("mp_orb"),
            munny = registerSound("munny"),
            potion = registerSound("potion"),
            savepoint = registerSound("savepoint"),
            savespawn = registerSound("savespawn"),
            sharpshooterbullet = registerSound("sharpshooterbullet"),
            arrowgunReload = registerSound("arrowgunreload"),
            summon = registerSound("summon"),
            unsummon = registerSound("unsummon"),
			portal = registerSound("portal"),
	        laser = registerSound("laser"),
	        fire = registerSound("fire"),
			blizzard = registerSound("blizzard"),
			cure = registerSound("cure"),
			magnet1 = registerSound("magnet1"),
			magnet2 = registerSound("magnet2"),
			aero1 = registerSound("aero1"),
			aero2 = registerSound("aero2"),
			gravity = registerSound("gravity"),
			reflect1 = registerSound("reflect1"),
	        reflect2 = registerSound("reflect2"),
	        stop = registerSound("stop"),
			playerDeath = registerSound("player_death"),
			playerDeathHardcore = registerSound("player_death_2"),
		    invincible_hit = registerSound("invincible_hit"),
			shotlock_lockon_start = registerSound("shotlock_lockon_start"),
			shotlock_lockon_idle = registerSound("shotlock_lockon_idle"),
			shotlock_lockon = registerSound("shotlock_lockon"),
			shotlock_lockon_all = registerSound("shotlock_lockon_all"),
			shotlock_shot = registerSound("shotlock_shot"),
			strike_raid = registerSound("strike_raid"),
			wisdom_shot = registerSound("wisdom_shot"),
					
			keyblade_armor = registerSound("keyblade_armor"),
			summon_armor = registerSound("summon_armor"),
			unsummon_armor = registerSound("unsummon_armor"),

            
            Record_Birth_by_Sleep_A_Link_to_the_Future = registerSound("records/birth_by_sleep_a_link_to_the_future"),
            Record_Dream_Drop_Distance_The_Next_Awakening = registerSound("records/dream_drop_distance_the_next_awakening"),
            Record_Hikari_KINGDOM_Instrumental_Version = registerSound("records/hikari_kingdom_instrumental_version"),
            Record_L_Oscurita_Dell_Ignoto = registerSound("records/l_oscurita_dell_ignoto"),
            Record_Musique_pour_la_tristesse_de_Xion = registerSound("records/musique_pour_la_tristesse_de_xion"),
            Record_No_More_Bugs_Bug_Version = registerSound("records/no_more_bugs_bug_version"),
            Record_Organization_XIII = registerSound("records/organization_xiii"),
            Record_Dearly_Beloved_UX = registerSound("records/dearly_beloved_ux"),
			Record_Passion_Instrumental = registerSound("records/passion_instrumental"),
			Record_Rage_Awakened = registerSound("records/rage_awakened"),
			Record_The_Other_Promise = registerSound("records/the_other_promise"),
			Record_13th_Struggle_Luxord = registerSound("records/13th_struggle_luxord"),
			Record_13th_Dilemma_Saix = registerSound("records/13th_dilemma_saix"),
			Record_13th_Reflection = registerSound("records/13th_reflection"),
			Record_Another_Side_Battle_Ver = registerSound("records/another_side_battle_ver"),
			Record_Cavern_Of_Remembrance_Days = registerSound("records/cavern_of_remembrance_days"),
			Record_Forgotten_Challenge_Recoded = registerSound("records/forgotten_challenge_recoded"),
			Record_Anger_Unchained = registerSound("records/anger_unchained"),
			Record_Hunter_Of_The_Dark = registerSound("records/hunter_of_the_dark"),
            Record_Destati = registerSound("records/destati"),


			Music_Dive_Into_The_Heart_Destati = registerSound("music/dive_into_the_heart_destati"),
    		Music_Lord_Of_The_Castle = registerSound("music/lord_of_the_castle");

           /*
            Music_A_Day_In_Agrabah = registerSound("music.a_day_in_agrabah"),
            Music_A_Fight_To_The_Death = registerSound("music.a_fight_to_the_death"),
            Music_A_Very_Small_Wish = registerSound("music.a_very_small_wish"),
            Music_Adventures_In_The_Savannah = registerSound("music.adventures_in_the_savannah"),
            Music_Arabian_Dream = registerSound("music.arabian_dream"),
            Music_Beneath_The_Ground = registerSound("music.beneath_the_ground"),
            Music_Critical_Drive = registerSound("music.critical_drive"),
            Music_Dark_Impetus = registerSound("music.dark_impetus"),
            Music_Dearly_Beloved_BBS = registerSound("music.dearly_beloved_bbs"),
            Music_Dearly_Beloved_CHI = registerSound("music.dearly_beloved_chi"),
            Music_Dearly_Beloved_Coded = registerSound("music.dearly_beloved_coded"),
            Music_Dearly_Beloved_CoM = registerSound("music.dearly_beloved_com"),
            Music_Dearly_Beloved_Days = registerSound("music.dearly_beloved_days"),
            Music_Dearly_Beloved_DDD = registerSound("music.dearly_beloved_ddd"),
            Music_Dearly_Beloved_KH1 = registerSound("music.dearly_beloved_kh1"),
            Music_Dearly_Beloved_KH2 = registerSound("music.dearly_beloved_kh2"),
            Music_Dearly_Beloved_ReCoM = registerSound("music.dearly_beloved_recom"),
            Music_Deep_Drive = registerSound("music.deep_drive"),
            Music_Deep_Drop = registerSound("music.deep_drop"),
            Music_Deep_Jungle = registerSound("music.deep_jungle"),
            Music_Destiny_Islands = registerSound("music.destiny_islands"),

            Music_Hand_In_Hand = registerSound("music.hand_in_hand"),
            Music_Happy_Holidays = registerSound("music.happy_holidays"),
            Music_Having_A_Wild_Time = registerSound("music.having_a_wild_time"),
            Music_Keyblade_Graveyard_Horizon = registerSound("music.keyblade_graveyard_horizon"),
            Music_Lazy_Afternoons = registerSound("music.lazy_afternoons"),
            Music_L_Impeto_Oscuro = registerSound("music.l_impeto_oscuro"),
            Music_L_Oscurita_Dell_Ignoto = registerSound("music.l_oscurita_dell_ignoto"),
            Music_Monstrous_Monstro = registerSound("music.monstrous_monstro"),
            Music_Mystic_Moon = registerSound("music.mystic_moon"),
            Music_Night_In_The_Dark_Dream = registerSound("music.night_in_the_dark_dream"),
            Music_Night_Of_Fate = registerSound("music.night_of_fate"),
            Music_Night_Of_Tragedy = registerSound("music.night_of_tragedy"),
            Music_Rage_Awakened = registerSound("music.rage_awakened"),
            Music_Risky_Romp = registerSound("music.risky_romp"),
            Music_Sacred_Distance = registerSound("music.sacred_distance"),
            Music_Sacred_Moon_Days = registerSound("music.sacred_moon_days"),
            Music_Sacred_Moon = registerSound("music.sacred_moon"),
            Music_Savannah_Pride = registerSound("music.savannah_pride"),
            Music_Sinister_Sundown = registerSound("music.sinister_sundown"),
            Music_Spooks_Of_Halloween_Town = registerSound("music.spooks_of_halloween_town"),
            Music_Tension_Rising = registerSound("music.tension_rising"),
            Music_The_Dread_Of_Night = registerSound("music.the_dread_of_night"),
            Music_The_Rustling_Forest = registerSound("music.the_rustling_forest"),
            Music_The_Secret_Whispers = registerSound("music.the_secret_whispers"),
            Music_The_Silent_Forest = registerSound("music.the_silent_forest"),
            Music_The_Underworld = registerSound("music.the_underworld"),
            Music_This_Is_Halloween = registerSound("music.this_is_halloween"),
            Music_To_Our_Surprise = registerSound("music.to_our_surprise"),
            Music_Traverse_In_Trance = registerSound("music.traverse_in_trance"),
            Music_Traverse_Town = registerSound("music.traverse_town"),
            Music_Welcome_To_Wonderland = registerSound("music.welcome_to_wonderland"),
            Music_What_A_Surprise = registerSound("music.what_a_surprise"),
            Music_What_Lies_Beneath = registerSound("music.what_lies_beneath"),
            Music_Working_Together = registerSound("music.working_together")
                    */
                    ;

    public static RegistryObject<SoundEvent> registerSound(String name) {
        final ResourceLocation soundID = new ResourceLocation(KingdomKeys.MODID, name);
        return SOUNDS.register(name, () -> new SoundEvent(soundID));
    }
}
