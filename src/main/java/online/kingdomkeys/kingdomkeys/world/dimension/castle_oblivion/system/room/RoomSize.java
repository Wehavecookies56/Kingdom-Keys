package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.util.StringRepresentable;

public enum RoomSize implements StringRepresentable {
    SPECIAL("SPECIAL"), S("S"), M("M"), L("L");

    final String name;

    @Override
    public String getSerializedName() {
        return name;
    }

    RoomSize(String name) {
        this.name = name;
    }
}