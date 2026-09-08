package online.kingdomkeys.kingdomkeys.encounter;

import net.minecraft.server.level.ServerLevel;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

public interface EncounterHandler<T extends Encounter, S extends Encounter.State> {

    Encounter.State createState();

    void start(T encounter, S state, EncounterInstance instance, EncounterContext context, ServerLevel level);

    void tick(T encounter, S state, EncounterInstance instance, EncounterContext context, ServerLevel level);

    void end(T encounter, S state, EncounterInstance instance, EncounterContext context, ServerLevel level);
}
