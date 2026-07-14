package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomEncounters;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

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
        if (!isComplete) {
            Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
                CastleOblivionData.InteriorData.get(level).ifPresent(interiorData -> {
                    interiorData.setDirty();
                    interiorData.sendToClient(player);
                });
                PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(CastleOblivionData.InteriorData.get(level).get(), level), (ServerPlayer) player);
            });
            room.setDoorLocks(level, true);
            encounter.getHandler().start(encounter.getEncounter(), getEncounter().getHandler().createState(), this, room, level);
        }
    }

    public void tick(Room room, ServerLevel level) {
        if (!isComplete) {
            encounter.getHandler().tick(encounter.getEncounter(), state, this, room, level);
            activeTicks++;
        } else {
            end(room, level);
        }
    }

    public void end(Room room, ServerLevel level) {
        room.setDoorLocks(level, false);
        encounter.getHandler().end(encounter.getEncounter(), state, this, room, level);
        Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
            List<Utils.Title> message = List.of(
                    new Utils.Title("co.encounter.end", "")
            );
            PacketHandler.sendTo(new SCShowMessagesPacket(message), (ServerPlayer) player);
            //player.sendSystemMessage(Component.translatable("co.encounter.end"));
            getEncounter().getRewards().forEach(player::addItem);
            CastleOblivionData.InteriorData.get(level).ifPresent(interiorData -> {
                interiorData.setDirty();
                interiorData.sendToClient(player);
            });
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
        encounter = ModRoomEncounters.registry.get().getValue(KingdomKeys.rl(tag.getString("room_encounter")));
        state = encounter.encounter.type().decodeStart(NbtOps.INSTANCE, tag.getCompound("state")).getOrThrow();
        activeTicks = tag.getLong("active_ticks");
        isComplete = tag.getBoolean("complete");
    }
}
