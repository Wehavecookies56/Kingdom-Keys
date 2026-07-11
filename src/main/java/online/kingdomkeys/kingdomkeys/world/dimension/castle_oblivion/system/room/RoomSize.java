package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.util.StringRepresentable;

public enum RoomSize implements StringRepresentable {
    SPECIAL("SPECIAL","? ? ?"), S("S","★ ☆ ☆"), M("M","★ ★ ☆"), L("L","★ ★ ★");

    final String name;
    final String stars;

    @Override
    public String getSerializedName() {
        return name;
    }

    public String getStars() {
        return stars;
    }

    RoomSize(String name, String stars) {
        this.name = name;
        this.stars = stars;
    }
}