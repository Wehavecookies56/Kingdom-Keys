package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.util.StringRepresentable;

public enum RoomEnemies implements StringRepresentable {
    NONE("NONE","? ? ?"), S("S","★ ☆ ☆"), M("M","★ ★ ☆"), L("L","★ ★ ★");

    final String name;
    final String stars;

    @Override
    public String getSerializedName() {
        return name;
    }

    public String getStars() {
        return stars;
    }

    RoomEnemies(String name, String stars) {
        this.name = name;
        this.stars = stars;
    }
}
