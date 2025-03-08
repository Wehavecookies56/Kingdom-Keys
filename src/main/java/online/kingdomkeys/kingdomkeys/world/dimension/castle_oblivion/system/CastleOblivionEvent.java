package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import org.jetbrains.annotations.Nullable;

public class CastleOblivionEvent extends Event {

    Level interiorLevel;

    public CastleOblivionEvent(Level interiorLevel) {
        this.interiorLevel = interiorLevel;
    }

    public Level getInteriorLevel() {
        return interiorLevel;
    }

    public static class RoomGeneratedEvent extends CastleOblivionEvent {
        RoomData generatedRoomData;
        Room currentRoom;

        public RoomGeneratedEvent(Level level, RoomData generatedRoomData, Room currentRoom) {
            super(level);
            this.generatedRoomData = generatedRoomData;
            this.currentRoom = currentRoom;
        }

        public RoomData getGeneratedRoomData() {
            return generatedRoomData;
        }

        public Room getCurrentRoom() {
            return currentRoom;
        }
    }

    @Cancelable
    public static class PlayerChangeRoomEvent extends CastleOblivionEvent {
        @Nullable
        Room currentRoom;
        @Nullable
        Room newRoom;
        Player player;

        public PlayerChangeRoomEvent(@Nullable Room currentRoom, @Nullable Room newRoom, Player player) {
            super(player.level());
            this.currentRoom = currentRoom;
            this.newRoom = newRoom;
            this.player = player;
        }

        public @Nullable Room getCurrentRoom() {
            return currentRoom;
        }

        public @Nullable Room getNewRoom() {
            return newRoom;
        }

        public Player getPlayer() {
            return player;
        }
    }

    @Cancelable
    public static class PlayerChangeFloorEvent extends CastleOblivionEvent {
        @Nullable
        Floor currentFloor;
        @Nullable
        Floor newFloor;

        //Will be null if entering the first floor
        public @Nullable Floor getCurrentFloor() {
            return currentFloor;
        }

        public @Nullable Floor getNewFloor() {
            return newFloor;
        }

        public Player getPlayer() {
            return player;
        }

        Player player;
        public PlayerChangeFloorEvent(@Nullable Floor currentFloor, @Nullable Floor newFloor, Player player) {
            super(player.level());
            this.currentFloor = currentFloor;
            this.newFloor = newFloor;
            this.player = player;
        }

    }

}
