package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomEncounters;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

public class EncounterInstance {
    private final RoomEncounter encounter;
    private EncounterState state;
    private long activeTicks;
    private boolean isComplete;

    public EncounterInstance(RoomEncounter encounter, EncounterState state) {
        this.encounter = encounter;
        this.state = state;
    }

    public RoomEncounter getEncounter() {
        return encounter;
    }

    public EncounterState getState() {
        return state;
    }

    public <T extends EncounterState> T getState(Class<T> clazz) {
        return clazz.cast(state);
    }

    public void setComplete() {
        this.isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setState(EncounterState state) {
        this.state = state;
    }

    public long getActiveTicks() {
        return activeTicks;
    }

    public void start(Room room, ServerLevel level) {
        room.setDoorLocks(level, true);
        encounter.getHandler().start(encounter.getEncounter(), getEncounter().getHandler().createState(), this, room, level);
    }

    public void tick(Room room, ServerLevel level) {
        encounter.getHandler().tick(encounter.getEncounter(), state, this, room, level);
        if (isComplete) {
            end(room, level);
        }
        activeTicks++;
    }

    public void end(Room room, ServerLevel level) {
        room.setDoorLocks(level, false);
        encounter.getHandler().end(encounter.getEncounter(), state, this, room, level);
        Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
            player.sendSystemMessage(Component.literal("ENCOUNTER COMPLETE"));
            getEncounter().getRewards().forEach(player::addItem);
        });
    }

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putString("room_encounter", encounter.getRegistryName().toString());
        EncounterType<?, ?> type = encounter.encounter.type();
        tag.put("state", type.encodeStart(NbtOps.INSTANCE, state).getOrThrow());
        tag.putLong("active_ticks", activeTicks);
        tag.putBoolean("complete", isComplete);
        return tag;
    }

    public EncounterInstance(CompoundTag tag) {
        encounter = ModRoomEncounters.registry.get().getValue(ResourceLocation.parse(tag.getString("room_encounter")));
        state = encounter.encounter.type().decodeStart(NbtOps.INSTANCE, tag.getCompound("state")).getOrThrow();
        activeTicks = tag.getLong("active_ticks");
        isComplete = tag.getBoolean("complete");
    }
}
