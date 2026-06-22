package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModEncounterTypes;

public interface Encounter {
    MapCodec<? extends Encounter> codec();
    EncounterType<? extends Encounter, ? extends EncounterState> type();
    Codec<Encounter> CODEC = ModEncounterTypes.REGISTRY.byNameCodec().dispatch(Encounter::type, EncounterType::codec);
}