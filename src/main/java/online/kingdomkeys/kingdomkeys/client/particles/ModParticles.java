package online.kingdomkeys.kingdomkeys.client.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, KingdomKeys.MODID);

    public static final net.minecraft.resources.ResourceLocation GENERIC_HIT_TEXTURE = KingdomKeys.rl("textures/particle/generic_hit.png");
    public static final net.minecraft.resources.ResourceLocation LEAF_HIT_TEXTURE = KingdomKeys.rl("textures/particle/leaf_hit.png");

    public static final DeferredHolder<ParticleType<?>, ParticleType<KeybladeHitParticleOptions>> KEYBLADE_HIT =
            PARTICLES.register("keyblade_hit", () -> new ParticleType<KeybladeHitParticleOptions>(false) {
                @Override
                public MapCodec<KeybladeHitParticleOptions> codec() {
                    return KeybladeHitParticleOptions.codec(this);
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, KeybladeHitParticleOptions> streamCodec() {
                    return KeybladeHitParticleOptions.streamCodec(this);
                }
            });
}
