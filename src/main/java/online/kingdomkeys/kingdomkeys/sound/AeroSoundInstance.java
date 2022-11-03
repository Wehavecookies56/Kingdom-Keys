package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
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
   private final LivingEntity ent;
   private float pitch = 0.0F;
  
   public AeroSoundInstance(LivingEntity ent) {
      super(ModSounds.aero2.get(), SoundSource.PLAYERS);
      this.ent = ent;
      this.looping = true;
      this.delay = 0;
      this.volume = 0.0F;
      this.x = (double)((float)ent.getX());
      this.y = (double)((float)ent.getY());
      this.z = (double)((float)ent.getZ());
   }
   
	public boolean canPlaySound() {
		return true;
	}

   public boolean canStartSilent() {
      return true;
   }

   @Override
   public void tick() {
	   if(ent.isRemoved()) {
		   this.stop();
	   } else {
	      if (ModCapabilities.getGlobal(ent) != null) {
	    	  if(ModCapabilities.getGlobal(ent).getAeroTicks() <= 0) {
	    		  this.volume = 0;
		      } else {
		         this.x = (double)((float)this.ent.getX());
		         this.y = (double)((float)this.ent.getY());
		         this.z = (double)((float)this.ent.getZ());
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