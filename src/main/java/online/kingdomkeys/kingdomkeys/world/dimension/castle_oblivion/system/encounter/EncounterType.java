package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.Tag;

public record EncounterType<T extends Encounter, S extends EncounterState>(MapCodec<T> codec, Codec<S> stateCodec, EncounterHandler<T, S> handler) {

    public EncounterInstance createInstance(RoomEncounter encounter) {
        return new EncounterInstance(encounter, this.handler.createState());
    }

    @SuppressWarnings("unchecked")
    public DataResult<Tag> encodeStart(DynamicOps<Tag> ops, EncounterState state) {
        return stateCodec.encodeStart(ops, (S) state);
    }

    public DataResult<S> decodeStart(DynamicOps<Tag> ops, Tag tag) {
        return stateCodec.parse(ops, tag);
    }
}
