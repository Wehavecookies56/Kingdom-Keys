package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Mostly a copy of {@link net.minecraft.world.entity.item.PrimedTnt} with some small changes
 */
public class BlastBloxEntity extends PrimedTnt {

    public BlastBloxEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    }

    public BlastBloxEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        super(level, x, y, z, owner);
    }
}
