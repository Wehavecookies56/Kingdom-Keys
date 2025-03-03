package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FloorType {

    public String name;
    public int critPathLength;
    public int bonusRoomCount;
    public int branchCount;
    public int bonusRoomChance;
    public Color floorColour;
    private final Supplier<SoundEvent> music;
    public List<RoomType> roomBlacklist;
    private ResourceLocation registryName;

    public FloorType(String name, int critPathLength, int bonusRoomCount, int branchCount, int bonusRoomChance, Color floorColour, Supplier<SoundEvent> music, RoomType... roomBlackList) {
        this.name = name;
        this.critPathLength = critPathLength;
        this.bonusRoomCount = bonusRoomCount;
        this.branchCount = branchCount;
        this.bonusRoomChance = bonusRoomChance;
        this.floorColour = floorColour;
        this.music = music;
        this.roomBlacklist = Arrays.stream(roomBlackList).toList();
    }

    @Nullable
    public Supplier<SoundEvent> getMusic() {
        return music;
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
