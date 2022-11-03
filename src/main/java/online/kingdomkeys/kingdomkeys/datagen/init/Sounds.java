package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.*;

public class Sounds extends SoundDefinitionsProvider {

    public Sounds(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, KingdomKeys.MODID, helper);
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
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(sound.get().getRegistryName(), SoundDefinition.SoundType.SOUND)));
    }

    public void add(Supplier<SoundEvent> sound, boolean stream) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(sound.get().getRegistryName(), SoundDefinition.SoundType.SOUND).stream(stream)));
    }

    public void add(Supplier<SoundEvent> sound, float volume) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(sound.get().getRegistryName(), SoundDefinition.SoundType.SOUND).volume(volume)));
    }
}
