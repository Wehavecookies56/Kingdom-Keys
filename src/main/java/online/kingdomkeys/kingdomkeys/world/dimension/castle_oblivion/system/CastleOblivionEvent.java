package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

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
        Player player;

        public RoomGeneratedEvent(Player player, RoomData generatedRoomData, Room currentRoom) {
            super(player.level());
            this.player = player;
            this.generatedRoomData = generatedRoomData;
            this.currentRoom = currentRoom;
        }

        public RoomData getGeneratedRoomData() {
            return generatedRoomData;
        }

        public Room getCurrentRoom() {
            return currentRoom;
        }

        public Player getPlayer() {
            return player;
        }
    }

    @Cancelable
    public static class PlayerChangeRoomEvent extends CastleOblivionEvent {
        Room currentRoom;
        Room newRoom;
        Player player;

        public PlayerChangeRoomEvent(Room currentRoom, Room newRoom, Player player) {
            super(player.level());
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

    @Cancelable
    public static class PlayerChangeFloorEvent extends CastleOblivionEvent {
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
            super(player.level());
            this.currentFloor = currentFloor;
            this.newFloor = newFloor;
            this.player = player;
        }

    }

}
