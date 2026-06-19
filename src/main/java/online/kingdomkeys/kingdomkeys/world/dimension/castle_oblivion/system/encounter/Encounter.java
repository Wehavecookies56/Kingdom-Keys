package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

public class Encounter extends JsonRegistryObject {

    EncounterState state = EncounterState.NOT_STARTED;

    public Encounter(CompoundTag tag) {
    }

    public Encounter(JsonElement element) {
    }

    public void deserializeNBT(CompoundTag tag) {
        state = EncounterState.values()[tag.getInt("state")];
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("state", state.ordinal());
        return tag;
    }

    public void start(ServerLevel level, Room room) {
        room.setDoorLocks(level, true);
        state = EncounterState.IN_PROGRESS;
    }

    public void tick(ServerLevel level, Room room) {

    }

    public void end(ServerLevel level, Room room) {
        room.setDoorLocks(level, false);
        state = EncounterState.COMPLETE;
    }

}
