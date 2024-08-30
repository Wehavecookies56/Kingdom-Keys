package online.kingdomkeys.kingdomkeys.util;

import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Function8;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSavePointScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncMoogleNames;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

public class StreamCodecs {

    public static final StreamCodec<FriendlyByteBuf, LinkedHashMap<String, int[]>> KNOWN_MAGIC = new StreamCodec<>() {
        @Override
        public LinkedHashMap<String, int[]> decode(FriendlyByteBuf buffer) {
            LinkedHashMap<String, int[]> map = new LinkedHashMap<>();
            CompoundTag tag = buffer.readNbt();
            Iterator<String> iterator = tag.getAllKeys().iterator();
            while (iterator.hasNext()) {
                String magicName = iterator.next();
                map.put(magicName, tag.getIntArray(magicName));
            }
            return map;
        }

        @Override
        public void encode(FriendlyByteBuf buffer, LinkedHashMap<String, int[]> value) {
            CompoundTag magic = new CompoundTag();
            Iterator<Map.Entry<String, int[]>> magicsIt = value.entrySet().iterator();
            while (magicsIt.hasNext()) {
                Map.Entry<String, int[]> pair = magicsIt.next();
                magic.putIntArray(pair.getKey().toString(), pair.getValue());
            }
            buffer.writeNbt(magic);
        }
    };

    public static final StreamCodec<FriendlyByteBuf, Map<UUID, Pair<SavePointStorage.SavePoint, Instant>>> SAVE_POINTS = new StreamCodec<>() {
        @Override
        public Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> decode(FriendlyByteBuf pBuffer) {
            return SCOpenSavePointScreen.readSavePoints(pBuffer);
        }

        @Override
        public void encode(FriendlyByteBuf buf, Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints) {
            buf.writeInt(savePoints.size());
            savePoints.values().forEach(savePoint -> {
                buf.writeNbt(savePoint.getFirst().serializeNBT());
                buf.writeLong(savePoint.getSecond().getEpochSecond());
                buf.writeInt(savePoint.getSecond().getNano());
            });
        }
    };

    public static final StreamCodec<FriendlyByteBuf, Map<ResourceLocation, List<String>>> MOOGLE_NAMES = new StreamCodec<>() {
        @Override
        public Map<ResourceLocation, List<String>> decode(FriendlyByteBuf buffer) {
            Map<ResourceLocation, List<String>> registry = new HashMap<>();
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                int valueSize = buffer.readInt();
                ResourceLocation location = ResourceLocation.parse(buffer.readUtf());
                List<String> list = new ArrayList<>();
                for (int j = 0; j < valueSize; j++) {
                    list.add(buffer.readUtf());
                }
                registry.put(location, list);
            }
            return registry;
        }

        @Override
        public void encode(FriendlyByteBuf buffer, Map<ResourceLocation, List<String>> names) {
            List<ResourceLocation> keys = names.keySet().stream().toList();
            List<List<String>> values = names.values().stream().toList();
            int size = names.size();
            buffer.writeInt(size);
            for(int i = 0; i < size; i++) {
                int valueSize = values.get(i).size();
                buffer.writeInt(valueSize);
                buffer.writeUtf(keys.get(i).toString());
                for (int j = 0; j < valueSize; j++) {
                    buffer.writeUtf(values.get(i).get(j));
                }
            }
        }
    };

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> p_331335_) {
        return new StreamCodec<>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
            }
        };
    }

}
