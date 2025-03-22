package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;

public class CastleOblivionEvent extends Event {

    ServerLevel interiorLevel;

    public CastleOblivionEvent(ServerLevel interiorLevel) {
        this.interiorLevel = interiorLevel;
    }

    public ServerLevel getInteriorLevel() {
        return interiorLevel;
    }

    public static class RoomGeneratedEvent extends CastleOblivionEvent {
        RoomData generatedRoomData;
        Room currentRoom;

        public RoomGeneratedEvent(ServerLevel level, RoomData generatedRoomData, Room currentRoom) {
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

    public static class PlayerChangeRoomEvent extends CastleOblivionEvent implements ICancellableEvent {
        Room currentRoom;
        Room newRoom;
        Player player;

        public PlayerChangeRoomEvent(Room currentRoom, Room newRoom, Player player) {
            super((ServerLevel) player.level());
            this.currentRoom = currentRoom;
            this.newRoom = newRoom;
            this.player = player;
        }

        public Room getCurrentRoom() {
            return currentRoom;
        }

        public Room getNewRoom() {
            return newRoom;
        }

        public Player getPlayer() {
            return player;
        }
    }

    public static class PlayerChangeFloorEvent extends CastleOblivionEvent implements ICancellableEvent {
        Floor currentFloor;
        Floor newFloor;

        public Floor getCurrentFloor() {
            return currentFloor;
        }

        public Floor getNewFloor() {
            return newFloor;
        }

        public Player getPlayer() {
            return player;
        }

        Player player;
        public PlayerChangeFloorEvent(Floor currentFloor, Floor newFloor, Player player) {
            super((ServerLevel) player.level());
            this.currentFloor = currentFloor;
            this.newFloor = newFloor;
            this.player = player;
        }

    }

}
