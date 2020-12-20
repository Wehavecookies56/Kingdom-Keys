package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IMultiPartEntity {

    World getWorld();

    boolean attackEntityFromPart(PartEntity part, DamageSource source, float damage);

}
