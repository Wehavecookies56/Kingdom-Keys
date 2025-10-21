package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.util.StringRepresentable;

public enum Corner implements StringRepresentable {
    CORNER1("1"), CORNER2("2"), CORNER3("3"), CORNER4("4");

    final String name;

    Corner(String name) {
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

    public Corner opposite() {
        return switch (this) {
            case CORNER1 -> CORNER3;
            case CORNER2 -> CORNER4;
            case CORNER3 -> CORNER1;
            case CORNER4 -> CORNER2;
        };
    }

    public Corner next() {
        return switch (this) {
            case CORNER1 -> CORNER2;
            case CORNER2 -> CORNER3;
            case CORNER3 -> CORNER4;
            case CORNER4 -> CORNER1;
        };
    }

    public Corner prev() {
        return switch (this) {
            case CORNER1 -> CORNER4;
            case CORNER2 -> CORNER1;
            case CORNER3 -> CORNER2;
            case CORNER4 -> CORNER3;
        };
    }

}
