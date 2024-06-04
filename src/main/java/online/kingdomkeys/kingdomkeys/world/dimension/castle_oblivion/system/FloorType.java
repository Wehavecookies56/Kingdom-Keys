package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class FloorType {

    public String name;
    public int critPathLength;
    public int bonusRoomCount;
    public int branchCount;
    public int bonusRoomChance;
    public Color floorColour;
    public List<RoomType> roomBlacklist;
    private ResourceLocation registryName;

    public FloorType(String name, int critPathLength, int bonusRoomCount, int branchCount, int bonusRoomChance, Color floorColour, RoomType... roomBlackList) {
        this.name = name;
        this.critPathLength = critPathLength;
        this.bonusRoomCount = bonusRoomCount;
        this.branchCount = branchCount;
        this.bonusRoomChance = bonusRoomChance;
        this.floorColour = floorColour;
        this.roomBlacklist = Arrays.stream(roomBlackList).toList();
    }

    public FloorType setRegistryName(ResourceLocation name) {
        registryName = name;
        return this;
    }

    @Nullable
    public ResourceLocation getRegistryName() {
        return registryName;
    }

}
