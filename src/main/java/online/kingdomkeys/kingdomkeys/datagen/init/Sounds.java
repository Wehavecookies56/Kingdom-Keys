package online.kingdomkeys.kingdomkeys.datagen.init;

import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Music_Dive_Into_The_Heart_Destati;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Music_Lord_Of_The_Castle;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_13th_Dilemma_Saix;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_13th_Reflection;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_13th_Struggle_Luxord;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Anger_Unchained;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Another_Side_Battle_Ver;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Birth_by_Sleep_A_Link_to_the_Future;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Cavern_Of_Remembrance_Days;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Dearly_Beloved_UX;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Destati;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Dream_Drop_Distance_The_Next_Awakening;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Forgotten_Challenge_Recoded;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Hikari_KINGDOM_Instrumental_Version;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Hunter_Of_The_Dark;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_L_Oscurita_Dell_Ignoto;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Musique_pour_la_tristesse_de_Xion;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_No_More_Bugs_Bug_Version;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Organization_XIII;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Passion_Instrumental;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_Rage_Awakened;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.Record_The_Other_Promise;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.aero1;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.aero2;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.alarm;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.antidrive;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.arrowgunReload;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.blizzard;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.cure;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.drive;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.error;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.fire;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.gravity;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.hp_orb;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.invincible_hit;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.itemget;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.kupo;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.kupoliving;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.laser;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.levelup;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.lockon;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.magnet1;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.magnet2;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.menu_back;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.menu_in;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.menu_move;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.menu_select;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.mp_orb;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.munny;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.playerDeath;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.playerDeathHardcore;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.portal;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.potion;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.reflect1;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.reflect2;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.savepoint;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.savespawn;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.sharpshooterbullet;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.shotlock_lockon;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.shotlock_lockon_all;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.shotlock_lockon_idle;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.shotlock_lockon_start;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.shotlock_shot;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.stop;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.strike_raid;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.summon;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.unsummon;
import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.wisdom_shot;

import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class Sounds extends SoundDefinitionsProvider {

    public Sounds(DataGenerator generator, ExistingFileHelper helper) {
        super(generator.getPackOutput(), KingdomKeys.MODID, helper);
    }

    @Override
    public void registerSounds() {
        //Sound effects
        add(alarm);
        add(antidrive);
        add(drive);
        add(error);
        add(hp_orb);
        add(itemget);
        add(kupo);
        add(kupoliving);
        add(levelup);
        add(lockon);
        add(menu_back);
        add(menu_in);
        add(menu_move);
        add(menu_select);
        add(mp_orb);
        add(munny);
        add(potion);
        add(savepoint);
        add(savespawn);
        add(sharpshooterbullet);
        add(arrowgunReload);
        add(summon);
        add(unsummon);
        add(portal);
        add(laser);
        add(fire);
        add(blizzard);
        add(cure);
        add(magnet1);
        add(magnet2);
        add(gravity);
        add(aero1);
        add(aero2);
        add(reflect1);
        add(reflect2);
        add(stop);
        add(playerDeath);
        add(playerDeathHardcore);
        add(invincible_hit);
        add(shotlock_lockon_start);
        add(shotlock_lockon_idle);
        add(shotlock_lockon);
        add(shotlock_lockon_all);
        add(shotlock_shot);
        add(strike_raid);
        add(wisdom_shot);
        
        //Music
        //Records
        add(Record_Birth_by_Sleep_A_Link_to_the_Future, true);
        add(Record_Dream_Drop_Distance_The_Next_Awakening, true);
        add(Record_Hikari_KINGDOM_Instrumental_Version, true);
        add(Record_L_Oscurita_Dell_Ignoto, true);
        add(Record_Musique_pour_la_tristesse_de_Xion, true);
        add(Record_No_More_Bugs_Bug_Version, true);
        add(Record_Organization_XIII, true);
        add(Record_Dearly_Beloved_UX, true);
        add(Record_Passion_Instrumental, true);
        add(Record_Rage_Awakened, true);
        add(Record_The_Other_Promise, true);
        add(Record_13th_Struggle_Luxord, true);
        add(Record_13th_Dilemma_Saix, true);
        add(Record_13th_Reflection, true);
        add(Record_Another_Side_Battle_Ver, true);
        add(Record_Cavern_Of_Remembrance_Days, true);
        add(Record_Forgotten_Challenge_Recoded, true);
        add(Record_Anger_Unchained, true);
        add(Record_Hunter_Of_The_Dark, true);
        add(Record_Destati, true);
        
        //BGM
        add(Music_Dive_Into_The_Heart_Destati, true);
        add(Music_Lord_Of_The_Castle, true);
    }

    public void add(Supplier<SoundEvent> sound) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(ForgeRegistries.SOUND_EVENTS.getKey(sound.get()), SoundDefinition.SoundType.SOUND)));
    }

    public void add(Supplier<SoundEvent> sound, boolean stream) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(ForgeRegistries.SOUND_EVENTS.getKey(sound.get()), SoundDefinition.SoundType.SOUND).stream(stream)));
    }

    public void add(Supplier<SoundEvent> sound, float volume) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(ForgeRegistries.SOUND_EVENTS.getKey(sound.get()), SoundDefinition.SoundType.SOUND).volume(volume)));
    }
}
