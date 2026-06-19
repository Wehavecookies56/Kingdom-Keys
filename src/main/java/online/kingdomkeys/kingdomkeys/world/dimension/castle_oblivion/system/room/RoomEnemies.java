package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.util.StringRepresentable;

public enum RoomEnemies implements StringRepresentable {
    NONE("NONE"), S("S"), M("M"), L("L");

    final String name;

    @Override
    public String getSerializedName() {
        return name;
    }

    RoomEnemies(String name) {
        this.name = name;
    }
}
