package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class FloorType implements IForgeRegistryEntry<FloorType> {

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

    @Override
    public FloorType setRegistryName(ResourceLocation name) {
        registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<FloorType> getRegistryType() {
        return FloorType.class;
    }
}
