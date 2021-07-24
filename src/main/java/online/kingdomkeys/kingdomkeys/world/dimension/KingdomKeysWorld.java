package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.world.utils.WorldLoader;
//Here for when we add worlds back
public class KingdomKeysWorld {

    ResourceLocation worldFile;
    Level world;
    int xOffset, yOffset, zOffset;
    WorldLoader loader;

    public KingdomKeysWorld(ResourceLocation worldFile, Level world) {
        this.worldFile = worldFile;
        this.world = world;
        this.xOffset = 0;
        this.yOffset = 60;
        this.zOffset = 0;
        loader = new WorldLoader();
    }

    public KingdomKeysWorld(ResourceLocation worldFile, Level world, int xOffset, int yOffset, int zOffset) {
        this.worldFile = worldFile;
        this.world = world;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        loader = new WorldLoader();
    }

    public void generate() {

    }

    public BlockPos getSpawn() {
        return null;
    }

}
