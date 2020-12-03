package online.kingdomkeys.kingdomkeys.client.particle;

import net.minecraft.particles.ParticleType;

public class ExpParticleType extends ParticleType<ExpParticleData> {
	  private static boolean ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER = true;
	  public ExpParticleType() {
	    super(ALWAYS_SHOW_REGARDLESS_OF_DISTANCE_FROM_PLAYER, ExpParticleData.DESERIALIZER);
	  }
	}