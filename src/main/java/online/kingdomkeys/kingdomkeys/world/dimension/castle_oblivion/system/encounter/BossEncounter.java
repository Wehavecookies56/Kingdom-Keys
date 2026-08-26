package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import com.mojang.serialization.MapCodec;

public class BossEncounter implements Encounter {

    @Override
    public MapCodec<? extends Encounter> codec() {
        return null;
    }

    @Override
    public EncounterType<? extends Encounter, ? extends EncounterState> type() {
        return null;
    }


}
