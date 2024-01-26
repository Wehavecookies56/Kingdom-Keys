package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.Size2i;

public class RoomProperties {

    private boolean lobby;
    private RoomSize size;
    private RoomCategory category;
    private RoomEnemies enemies;
    private Size2i dimensions;
    private Color colour;
    private List<RoomModifier> modifiers;
    private List<FloorType> compatibleFloors;
    private RoomStructure fixedRoom;
    private List<RoomUtils.Direction> doorDirections;

    private RoomProperties(Builder builder) {
        this.lobby = builder.lobby;
        this.size = builder.size;
        this.dimensions = builder.dimensions;
        this.modifiers = builder.modifiers;
        this.colour = builder.colour;
        this.compatibleFloors = builder.compatibleFloors;
        this.enemies = builder.enemies;
        this.fixedRoom = builder.fixedRoom;
        this.category = builder.category;
    }

    public boolean isLobby() {
        return lobby;
    }

    public RoomSize getSize() {
        return size;
    }

    public Size2i getDimensions() {
        return dimensions;
    }

    public RoomEnemies getEnemies() {
        return enemies;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public Color getColour() {
        return colour;
    }

    public List<RoomModifier> getModifiers() {
        return modifiers;
    }

    public RoomStructure getFixedRoom() {
        return fixedRoom;
    }

    public boolean isFloorCompatible(FloorType floor) {
        if (compatibleFloors.isEmpty()) {
            return true;
        } else {
            return compatibleFloors.contains(floor);
        }
    }

    public enum RoomSize {
        SPECIAL, S, M, L
    }

    public enum RoomEnemies {
        NONE, S, M, L
    }

    public enum RoomCategory {
        ENEMY, STATUS, BOUNTY, SPECIAL, ANY
    }

    public static Builder enemy(RoomSize size) {
        return new Builder(size, RoomCategory.ENEMY);
    }

    public static Builder status(RoomSize size) {
        return new Builder(size, RoomCategory.STATUS);
    }

    public static Builder bounty(RoomSize size) {
        return new Builder(size, RoomCategory.BOUNTY);
    }

    public static class Builder {
        private final RoomSize size;
        private final RoomCategory category;
        private final Size2i dimensions;
        private boolean lobby;
        private final List<RoomModifier> modifiers;
        private final List<FloorType> compatibleFloors;
        private Color colour = null;
        private RoomEnemies enemies = RoomEnemies.NONE;
        private RoomStructure fixedRoom;
        public Builder(RoomSize size, RoomCategory category, Size2i dimensions) {
            this.size = size;
            this.dimensions = dimensions;
            this.category = category;
            modifiers = new ArrayList<>();
            compatibleFloors = new ArrayList<>();
        }

        public Builder(RoomSize size, RoomCategory category) {
            this.size = size;
            this.category = category;
            switch (size) {
                case S -> this.dimensions = new Size2i(32, 32);
                case M -> this.dimensions = new Size2i(48, 48);
                case L -> this.dimensions = new Size2i(64, 64);
                default -> this.dimensions = new Size2i(0, 0);
            }
            modifiers = new ArrayList<>();
            compatibleFloors = new ArrayList<>();
        }

        public Builder isLobby() {
            this.lobby = true;
            return this;
        }

        public Builder floor(FloorType floor) {
            this.compatibleFloors.add(floor);
            return this;
        }

        public Builder floors(List<FloorType> floors) {
            this.compatibleFloors.addAll(floors);
            return this;
        }

        public Builder colour(Color colour) {
            this.colour = colour;
            return this;
        }

        public Builder modifier(RoomModifier modifier) {
            this.modifiers.add(modifier);
            return this;
        }

        public Builder modifiers(List<RoomModifier> modifiers) {
            this.modifiers.addAll(modifiers);
            return this;
        }

        public Builder enemies(RoomEnemies enemies) {
            this.enemies = enemies;
            return this;
        }

        public Builder fixed(RoomStructure structure) {
            this.fixedRoom = structure;
            return this;
        }
        public RoomProperties build() {
            return new RoomProperties(this);
        }
    }

}
