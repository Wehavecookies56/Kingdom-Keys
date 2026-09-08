package online.kingdomkeys.kingdomkeys.encounter;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifierType;

import java.util.List;
import java.util.Optional;

public interface EncounterContext {
    Optional<EncounterInstance> getEncounter();

    List<BlockPos> getSpawnPoints();
    default List<Room.TreasurePoint> getTreasurePoints() {
        return List.of();
    }

    <T extends RoomModifier> List<T> getModifiers(RoomModifierType<?> type);

    List<Player> getParticipants(ServerLevel level);

    default Optional<Room> getRoom() {
        return Optional.empty();
    }

    int getBaseLevel();

    void onSpawn(LivingEntity entity);
}
