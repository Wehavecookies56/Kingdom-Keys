package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum LineDisplay implements StringRepresentable {
    OFF("off"), ODD("odd"), EVEN("even");

    final String name;

    LineDisplay(String name) {
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

    public Component getDisplayName() {
        return Component.translatable("kingdomkeys.gummi.hangar.area." + this.name);
    }

    public LineDisplay next() {
        return switch (this) {
            case OFF -> ODD;
            case ODD -> EVEN;
            case EVEN -> OFF;
        };
    }

}
