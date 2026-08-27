package online.kingdomkeys.kingdomkeys.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record KeybladeHitParticleOptions(ResourceLocation texture, float scale) implements ParticleOptions {

    public static MapCodec<KeybladeHitParticleOptions> codec(ParticleType<KeybladeHitParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("texture").forGetter(KeybladeHitParticleOptions::texture),
                com.mojang.serialization.Codec.FLOAT.fieldOf("scale").forGetter(KeybladeHitParticleOptions::scale)
        ).apply(instance, KeybladeHitParticleOptions::new));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, KeybladeHitParticleOptions> streamCodec(ParticleType<KeybladeHitParticleOptions> type) {
        return StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, KeybladeHitParticleOptions::texture,
                ByteBufCodecs.FLOAT, KeybladeHitParticleOptions::scale,
                KeybladeHitParticleOptions::new);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.KEYBLADE_HIT.get();
    }
}
