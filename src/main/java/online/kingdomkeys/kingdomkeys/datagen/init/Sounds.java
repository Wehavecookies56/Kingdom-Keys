package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

import static online.kingdomkeys.kingdomkeys.client.sound.ModSounds.*;

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
        add(revert);
        add(driveUp);
        add(error);
        add(hp_orb);
        add(itemget);
        add(kupo);
        add(kupoliving);
        add(levelup);
        add(lockon);
        add(lockoff);
        add(menu_back);
        add(menu_in);
        add(menu_move);
        add(menu_select);
        add(mp_orb);
        add(munny);
        add(potion);
        add(ether);
        //add(hiPotion);
        add(savepoint);
        add(savespawn);
        add(sharpshooterbullet);
        add(arrowgunReload);
        add(summon);
        add(unsummon);
        add(portal);
        add(laser);
        add(buy);
        add(gummiPlace);
        add(gummiRemove);
        add(guard);
        add(aerialRecovery);
        add(flowmotionLoop);
        add(koLoop);
        add(bossKill);
        add(heartlessKill);
        add(heartlessSpawn);
        add(nobodyKill);
        add(openChest);
        add(synthesisPickup);

        add(bond_of_the_blaze_hit);
        add(braveheart_hit);
        add(destinys_embrace_hit);
        add(earthshaker_hit);
        add(kingdom_key_hit);
        add(oathkeeper_hit);
        add(oblivion_hit);
        add(rainfell_hit);
        add(star_cluster_hit);
        add(way_to_dawn_hit);
        add(wayward_wind_hit);
        add(generic_hit);

        add(fire);
        add(fira);
        add(firaga);
        add(blizzard);
        add(blizzara);
        add(blizzaga);
        add(cure);
        add(cura);
        add(curaga);
        add(magnet1);
        add(magnet2);
        add(gravity);
        add(gravira);
        add(graviga);
        add(aero1);
        add(aero2);
        add(reflect1);
        add(reflect2);
        add(stop);
        add(stopra);
        add(stopga);
        add(zeroGravity);
        add(poison);
        add(firagaBurst);
        add(deepFreeze);
        add(iceBarrage);
        add(thundagaShot);
        add(zap);
        add(balloon);
        add(balloonBounce);
        add(playerCast);
        add(warpHitPlayer);
        add(spark);
        add(sparkra);
        add(sparkga);
        add(lightBeam);
        add(esuna);

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
        add(keyblade_armor, KingdomKeys.rl("keyblade_armor2"));
        add(summon_armor);
        add(unsummon_armor);
        add(wall_jump);
        add(wall_grab);
        add(air_slide);
        
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
        add(Music_Castle_Oblivion, true);
        add(Music_The_13th_Floor, true);
        add(Music_Forgotten_Challenge, true);
        add(Music_World_Map, true);
        add(Music_Daybreak_Town, true);
        add(Music_Daybreak_Town_Battle, true);
        add(Music_Destiny_Islands, true);
        add(Music_Destiny_Islands_Battle, true);
    }

    public void add(Supplier<SoundEvent> sound) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(BuiltInRegistries.SOUND_EVENT.getKey(sound.get()), SoundDefinition.SoundType.SOUND)));
    }
    
    public void add(Supplier<SoundEvent> sound, ResourceLocation sound2) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(BuiltInRegistries.SOUND_EVENT.getKey(sound.get()), SoundDefinition.SoundType.SOUND)).with(SoundDefinition.Sound.sound(sound2, SoundDefinition.SoundType.SOUND)));
    }

    public void add(Supplier<SoundEvent> sound, boolean stream) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(BuiltInRegistries.SOUND_EVENT.getKey(sound.get()), SoundDefinition.SoundType.SOUND).stream(stream)));
    }

    public void add(Supplier<SoundEvent> sound, float volume) {
        add(sound, SoundDefinition.definition().with(SoundDefinition.Sound.sound(BuiltInRegistries.SOUND_EVENT.getKey(sound.get()), SoundDefinition.SoundType.SOUND).volume(volume)));
    }
}
