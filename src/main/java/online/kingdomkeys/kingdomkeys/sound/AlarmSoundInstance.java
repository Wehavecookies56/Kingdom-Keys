package online.kingdomkeys.kingdomkeys.sound;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.util.Utils;

@OnlyIn(Dist.CLIENT)
public class AlarmSoundInstance extends AbstractTickableSoundInstance {
   private static final float VOLUME_MIN = 0.0F;
   private static final float VOLUME_MAX = 0.7F;
   private static final float PITCH_MIN = 0.0F;
   private static final float PITCH_MAX = 1.0F;
   private static final float PITCH_DELTA = 0.0025F;
   private final Player player;
   private float pitch = 0.0F;
  
   public AlarmSoundInstance(Player player) {
      super(ModSounds.alarm.get(), SoundSource.PLAYERS);
      this.player = player;
      this.looping = true;
      this.delay = 0;
      this.volume = 0.0F;
      this.x = (double)((float)player.getX());
      this.y = (double)((float)player.getY());
      this.z = (double)((float)player.getZ());
   }
	
	public boolean canPlaySound() {
		return true;
	}

   public boolean canStartSilent() {
      return true;
   }

   @Override
   public void tick() {
	   if(player.isRemoved()) {
		   this.stop();
	   } else {
	      if (!Utils.isPlayerLowHP(player)) {
	         this.volume = 0;
	      } else {
	         this.x = (double)((float)this.player.getX());
	         this.y = (double)((float)this.player.getY());
	         this.z = (double)((float)this.player.getZ());
	         this.pitch = 1F;
	         this.volume = ModConfigs.hpAlarm ? 0.5F : 0F;	
	      }
	   }	   
   }
}