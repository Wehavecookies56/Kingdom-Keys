package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

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
