package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.util.StringRepresentable;

public enum Quarter implements StringRepresentable {
    TOP("top"), BOTTOM("bottom"), LEFT("left"), RIGHT("right");

    final String name;

    Quarter(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public Quarter opposite() {
        return switch (this) {
            case TOP -> BOTTOM;
            case BOTTOM -> TOP;
            case RIGHT -> LEFT;
            case LEFT -> RIGHT;
        };
    }
}
