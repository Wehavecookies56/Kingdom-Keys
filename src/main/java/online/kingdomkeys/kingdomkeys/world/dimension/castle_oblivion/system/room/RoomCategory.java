package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room;

import net.minecraft.util.StringRepresentable;

public enum RoomCategory implements StringRepresentable {
    ENEMY("ENEMY"), STATUS("STATUS"), BOUNTY("BOUNTY"), ENCOUNTER("ENCOUNTER"), SPECIAL("SPECIAL"), ANY("ANY");

    final String name;

    @Override
    public String getSerializedName() {
        return name;
    }

    public String getTranslationKey() {
        return "co.category."+this.name.toLowerCase();
    }

    RoomCategory(String name) {
        this.name = name;
    }
}
