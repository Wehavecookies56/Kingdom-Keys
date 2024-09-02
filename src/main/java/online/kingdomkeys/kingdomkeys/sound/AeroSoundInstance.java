package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

@OnlyIn(Dist.CLIENT)
public class AeroSoundInstance extends AbstractTickableSoundInstance {
   private final LivingEntity ent;
  
   public AeroSoundInstance(LivingEntity ent) {
      super(ModSounds.aero2.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
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
	      if (GlobalData.get(ent) != null) {
	    	  if(GlobalData.get(ent).getAeroTicks() <= 0) {
	    		  this.stop();
		      } else {
		         this.x = (double)((float)this.ent.getX());
		         this.y = (double)((float)this.ent.getY());
		         this.z = (double)((float)this.ent.getZ());
		         this.pitch = 1F;
		         this.volume = 1F;
		      }
	      }
	   } 
   }
}