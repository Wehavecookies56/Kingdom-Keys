package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import net.minecraft.server.level.ServerLevel;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

public interface EncounterHandler<T extends Encounter, S extends EncounterState> {

    EncounterState createState();

    void start(T encounter, S state, EncounterInstance instance, Room room, ServerLevel level);

    void tick(T encounter, S state, EncounterInstance instance, Room room, ServerLevel level);

    void end(T encounter, S state, EncounterInstance instance, Room room, ServerLevel level);
}
