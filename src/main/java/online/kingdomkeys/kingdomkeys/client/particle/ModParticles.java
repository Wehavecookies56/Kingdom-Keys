package online.kingdomkeys.kingdomkeys.client.particle;

import java.util.function.Supplier;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, KingdomKeys.MODID);

	public static final RegistryObject<ParticleType<?>> TYPE_EXP = createParticle("exp_particle", () -> new SimpleParticleType(true));

	private static <T extends ParticleOptions, M extends ParticleType<T>>RegistryObject<ParticleType<?>> createParticle(String name, Supplier<M> type) {
		RegistryObject<ParticleType<?>> particleType = PARTICLES.register(name, type);
		return particleType;
	}

}
