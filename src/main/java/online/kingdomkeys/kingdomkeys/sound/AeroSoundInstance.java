package online.kingdomkeys.kingdomkeys.sound;

import java.util.HashMap;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

@OnlyIn(Dist.CLIENT)
public class AeroSoundInstance extends AbstractTickableSoundInstance {
   private static final float VOLUME_MIN = 0.0F;
   private static final float VOLUME_MAX = 0.7F;
   private static final float PITCH_MIN = 0.0F;
   private static final float PITCH_MAX = 1.0F;
   private static final float PITCH_DELTA = 0.0025F;
   private final Player player;
   private float pitch = 0.0F;
  
   public AeroSoundInstance(Player player) {
      super(ModSounds.aero2.get(), SoundSource.PLAYERS);
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
	      if (ModCapabilities.getGlobal(player) != null) {
	    	  if(ModCapabilities.getGlobal(player).getAeroTicks() <= 0) {
	    		  this.volume = 0;
		      } else {
		         this.x = (double)((float)this.player.getX());
		         this.y = (double)((float)this.player.getY());
		         this.z = (double)((float)this.player.getZ());
		         this.pitch = 1F;
		         this.volume = 1F;
		         /*float f = (float)this.player.getDeltaMovement().horizontalDistance();
		         if (f >= 0.01F) {
		            this.pitch = Mth.clamp(this.pitch + 0.0025F, 0.0F, 1.0F);
		            this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 0.5F), 0.0F, 0.7F);
		         } else {
		            this.pitch = 0.0F;
		            this.volume = 0.0F;
		         }*/
		      }
	      }
	   } 
   }
}