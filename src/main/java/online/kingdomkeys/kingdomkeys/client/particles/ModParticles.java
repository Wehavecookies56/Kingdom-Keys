package online.kingdomkeys.kingdomkeys.client.particles;

import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, KingdomKeys.MODID);

	public static final RegistryObject<ParticleType<?>> expParticle = createParticle("exp_particle");

	private static RegistryObject<ParticleType<?>> createParticle(String name) {
		RegistryObject<ParticleType<?>> thing = PARTICLES.register(name, () -> new ExpParticleType());
		return thing;
	}

}
