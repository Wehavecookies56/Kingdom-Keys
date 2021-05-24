package online.kingdomkeys.kingdomkeys.world.utils;

import java.util.function.Function;

import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class BaseTeleporter implements ITeleporter {

    private final double x, y, z;

    public BaseTeleporter(double x, double y, double z) {
        this.x = x + 0.5;
        this.y = y + 0.5;
        this.z = z + 0.5;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity repositionedEntity = repositionEntity.apply(false);
        repositionedEntity.setPositionAndUpdate(x, y, z);
        return repositionedEntity;
    }
}
