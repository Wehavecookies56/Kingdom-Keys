package online.kingdomkeys.kingdomkeys.client.particle;

import java.util.function.Supplier;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, KingdomKeys.MODID);

	public static final RegistryObject<ParticleType<?>> TYPE_EXP = createParticle("exp_particle", () -> new BasicParticleType(true));

	private static <T extends IParticleData, M extends ParticleType<T>>RegistryObject<ParticleType<?>> createParticle(String name, Supplier<M> type) {
		RegistryObject<ParticleType<?>> particleType = PARTICLES.register(name, type);
		return particleType;
	}

}
