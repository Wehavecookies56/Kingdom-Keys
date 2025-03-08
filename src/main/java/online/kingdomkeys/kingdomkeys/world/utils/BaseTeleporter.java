package online.kingdomkeys.kingdomkeys.world.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class BaseTeleporter implements ITeleporter {

    private final double x, y, z;

    public BaseTeleporter(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BaseTeleporter(Vec3 vec3) {
        this(vec3.x, vec3.y, vec3.z);
    }

    public BaseTeleporter(double x, double y, double z) {
        this.x = x + 0.5;
        this.y = y + 0.5;
        this.z = z + 0.5;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity repositionedEntity = repositionEntity.apply(false);
        repositionedEntity.teleportTo(x, y, z);
        return repositionedEntity;
    }
}
