package online.kingdomkeys.kingdomkeys.block.gummi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GummiStats(int weight, int armour, int cost, int topSpeed, int lowSpeed, int horsepower, int firepower, int fuelPerShot, int mobility) {

    public static final Codec<GummiStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("weight", 0).forGetter(GummiStats::weight),
            Codec.INT.optionalFieldOf("armor", 0).forGetter(GummiStats::armour),
            Codec.INT.optionalFieldOf("cost", 1).forGetter(GummiStats::cost),
            Codec.INT.optionalFieldOf("top_speed", 0).forGetter(GummiStats::topSpeed),
            Codec.INT.optionalFieldOf("low_speed", 0).forGetter(GummiStats::lowSpeed),
            Codec.INT.optionalFieldOf("horsepower", 0).forGetter(GummiStats::horsepower),
            Codec.INT.optionalFieldOf("firepower", 0).forGetter(GummiStats::firepower),
            Codec.INT.optionalFieldOf("fuel_per_shot", 0).forGetter(GummiStats::fuelPerShot),
            Codec.INT.optionalFieldOf("mobility", 0).forGetter(GummiStats::mobility)
    ).apply(instance, GummiStats::new));

    public static final StreamCodec<ByteBuf, GummiStats> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}
