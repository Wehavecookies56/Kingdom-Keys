package online.kingdomkeys.kingdomkeys.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSavePointScreen;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

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
    }

}
